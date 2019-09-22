package com.emadmessiha.eventviewer.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.emadmessiha.eventviewer.model.EventItem;
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
import org.springframework.stereotype.Service;

@Service
public class EventViewerServiceImpl implements IEventViewerService {

    Logger logger = LoggerFactory.getLogger(EventViewerServiceImpl.class);

    private final String EVENTS = "events";

    private MongoClient getMongoClient(){
        return new MongoClient(new MongoClientURI("mongodb://root:root@localhost"));
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

    @Override
    public List<EventItem> searchEvents(Date startDate, Integer numberOfDays) {
        if (numberOfDays > 90) {
            // 3 month, which is a quarter
            throw new IllegalArgumentException("Number of days duration cannot exceed 90 days");
        } else {
            MongoClient mongoClient = getMongoClient();
            MongoCollection<Document> collection = getEventsCollection(mongoClient);
            
            Date endDate = getEndDateByNumberOfDays(startDate, numberOfDays);

            BasicDBObject query = new BasicDBObject("event_date", new BasicDBObject("$gte", startDate).append("$lt", endDate));
            FindIterable<Document> results = collection.find(query).projection(getFieldsForSearchResults());
            
            List<EventItem> items = convertDocumentsToEventItems(results);
            mongoClient.close();
            return items;
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
}