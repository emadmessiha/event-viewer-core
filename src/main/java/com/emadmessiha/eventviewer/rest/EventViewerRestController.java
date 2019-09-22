package com.emadmessiha.eventviewer.rest;

import java.util.Date;
import java.util.List;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.service.IEventViewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventViewerRestController {

    @Autowired
    IEventViewerService eventsService;
 
    @GetMapping("/search/{startDate}/{numberOfDays}")
    public List<EventItem> searchEvents(
        @PathVariable("startDate") @DateTimeFormat(iso = ISO.DATE) Date startDate, 
        @PathVariable("numberOfDays") Integer numberOfDays) {
            return eventsService.searchEvents(startDate, numberOfDays);
    }

    @GetMapping("/{id}")
    public EventItem getEventDetails(
        @PathVariable("id") String id) {
            return eventsService.getEventDetails(id);
    }
}