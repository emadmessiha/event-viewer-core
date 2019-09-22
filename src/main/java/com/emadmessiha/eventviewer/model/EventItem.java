package com.emadmessiha.eventviewer.model;

import java.util.Date;

import lombok.Data;

@Data
public class EventItem {
    private Date event_date;
    private String event_type;
    private String event_summary;
    private Float event_size;
    private String event_details;
}