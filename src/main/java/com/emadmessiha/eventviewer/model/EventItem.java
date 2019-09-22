package com.emadmessiha.eventviewer.model;

import java.util.Date;

import org.bson.Document;

import lombok.Data;

@Data
public class EventItem {
    private String object_id;
    private Date event_date;
    private String event_type;
    private String event_summary;
    private Float event_size;
    private String event_details;

    public EventItem() {
        this.object_id = "";
    }

    public EventItem(Document bsonDocument) {
        this.object_id = String.valueOf(bsonDocument.get("_id"));
        this.event_date = (Date) bsonDocument.get("event_date");
        this.event_type = String.valueOf(bsonDocument.get("event_type"));
        this.event_summary = String.valueOf(bsonDocument.get("event_summary"));
        this.event_size = Float.valueOf(String.valueOf(bsonDocument.get("event_size")));
        this.event_details = String.valueOf(bsonDocument.get("event_details"));
    }

    public Document toBSONDocument() {
        return new Document("event_date", this.event_date)
            .append("event_type", this.event_type)
            .append("event_summary", this.event_summary)
            .append("event_size", this.event_size)
            .append("event_details", this.event_details);
    }
}