package com.emadmessiha.eventviewer.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.model.EventsSource;
import com.emadmessiha.eventviewer.model.PagedEventResults;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class EventViewerServiceImpl implements IEventViewerService {

    Logger logger = LoggerFactory.getLogger(EventViewerServiceImpl.class);

    private final String EVENTS = "events";

    @Value("${spring.data.mongodb.uri}")
    private String mongodbUri;

    @Autowired
    ResourceLoader resourceLoader;

    private MongoClient getMongoClient(){
        return new MongoClient(new MongoClientURI(mongodbUri));
    }

    private MongoCollection<Document> getEventsCollection(MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase(EVENTS);
        return db.getCollection(EVENTS);
    }

    private BasicDBObject getFieldsForSearchResults(){
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 1);
        fields.put("event_date", 1);
        fields.put("event_type", 1);
        fields.put("event_summary", 1);
        fields.put("event_size", 1);
        return fields;    
    }

    private Date getEndDateByNumberOfDays(Date startDate, Integer numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);      
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
        return calendar.getTime();
    }

    private List<EventItem> convertDocumentsToEventItems(Iterable<Document> docs) {
        List<EventItem> items = new ArrayList<>();
        for (Document doc : docs) {
            items.add(new EventItem(doc));
        }
        return items;
    }

    private Integer getTotalPages(Integer totalResults, Integer pageSize) {
        if (totalResults % pageSize == 0) {
            return totalResults / pageSize;
        } else {
            return totalResults / pageSize + 1;
        }
    }

    public PagedEventResults getPagedResultsByQuery(MongoCollection<Document> collection, BasicDBObject query, Integer pageSize, Integer pageNumber) {
        PagedEventResults pagedResults = new PagedEventResults();

        FindIterable<Document> results = collection.find(query)
                .projection(getFieldsForSearchResults())
                .skip(pageSize * (pageNumber - 1)).limit(pageSize);
        Integer numResults = Math.toIntExact(collection.countDocuments(query));
            
        pagedResults.setTotalResults(numResults);
        pagedResults.setPage(pageNumber);
        pagedResults.setPageSize(pageSize);
        pagedResults.setTotalPages(getTotalPages(numResults, pageSize));
        pagedResults.setData(convertDocumentsToEventItems(results));
        return pagedResults;
    }

    public Boolean isValidSearchRequest(Integer numberOfDays, Integer pageSize, Integer pageNumber) {
        if (numberOfDays > 90) {
            // 3 month, which is a quarter
            throw new IllegalArgumentException("Number of days duration cannot exceed 90 days");
        }
        if (pageSize == null || pageSize < 0) {
            throw new IllegalArgumentException("Page size cannot be empty or less than zero");
        }
        if (pageNumber == null || pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be empty or less than zero");
        }
        return true;
    }

    @Override
    public PagedEventResults searchEvents(Date startDate, Integer numberOfDays, Integer pageSize, Integer pageNumber) {
        if (isValidSearchRequest(numberOfDays, pageSize, pageNumber)) {
            MongoClient mongoClient = getMongoClient();
            MongoCollection<Document> collection = getEventsCollection(mongoClient);
            
            Date endDate = getEndDateByNumberOfDays(startDate, numberOfDays);

            BasicDBObject query = new BasicDBObject("event_date", new BasicDBObject("$gte", startDate).append("$lt", endDate));
            PagedEventResults items = getPagedResultsByQuery(collection, query, pageSize, pageNumber);
            mongoClient.close();
            return items;
        } else {
            return new PagedEventResults();
        }
    }

    @Override
    public EventItem getEventDetails(String objectId) {
        MongoClient mongoClient = getMongoClient();
        MongoCollection<Document> collection = getEventsCollection(mongoClient);
        
        Document returnedDoc = collection.find(new BasicDBObject("_id", new ObjectId(objectId))).first();
        mongoClient.close();

        if (returnedDoc == null) {
            return null;
        }

        return new EventItem(returnedDoc);
    }

    @Override
    public Exception loadData(EventsSource source) {
        try {
            String filePath = "data.json";
            String filePathZipped = "data.zip";
            BufferedInputStream inputStream = new BufferedInputStream(source.url().openStream());
            FileOutputStream fileOS = new FileOutputStream(source.getIsZipped() ? filePathZipped : filePath);
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            fileOS.close();
            Exception unzipResult = null;
            if (source.getIsZipped()) {
                unzipResult = unzipDataFile(filePathZipped);
            }
            if (unzipResult == null) {
                return loadDataFromJsonFile(filePath);
            } else {
                return unzipResult;
            }
        } catch (Exception e) {
            return e;
        }
    }

    public Exception unzipDataFile(String fileZip) {
        try {
            File destDir = new File(fileZip.replace(".zip", ".json"));
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }

    public Exception loadDataFromJsonFile(String filePath) {
        MongoClient mongoClient = getMongoClient();
        MongoCollection<Document> collection = getEventsCollection(mongoClient);
        Gson gson = new Gson();
        try {
            File initialFile = new File(filePath);
            InputStream targetStream = new FileInputStream(initialFile);
            JsonReader reader = new JsonReader(new InputStreamReader(targetStream, "UTF-8"));
            reader.beginArray();
            collection.drop();
            while (reader.hasNext()) {
                EventItem event = gson.fromJson(reader, EventItem.class);
                collection.insertOne(event.toBSONDocument());
            }
            reader.endArray();
            reader.close();
        } catch (Exception ex) {
            return ex;
        }
        mongoClient.close();
        return null;
    }
}