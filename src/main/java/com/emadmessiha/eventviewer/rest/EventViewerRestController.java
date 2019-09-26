package com.emadmessiha.eventviewer.rest;

import java.util.Date;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.model.EventsSource;
import com.emadmessiha.eventviewer.model.PagedEventResults;
import com.emadmessiha.eventviewer.service.IEventViewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventViewerRestController {

    @Autowired
    IEventViewerService eventsService;
 
    @GetMapping("/search/{startDate}/{numberOfDays}")
    public PagedEventResults searchEvents(
        @PathVariable("startDate") @DateTimeFormat(iso = ISO.DATE) Date startDate, 
        @PathVariable("numberOfDays") Integer numberOfDays,
        @RequestParam Integer page,
        @RequestParam Integer pageSize) {
            return eventsService.searchEvents(startDate, numberOfDays, pageSize, page);
    }

    @GetMapping("/{id}")
    public EventItem getEventDetails(
        @PathVariable("id") String id) {
            return eventsService.getEventDetails(id);
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importData(@RequestBody EventsSource dataSource) {
        Exception loadException = eventsService.loadData(dataSource);
        if (loadException == null) { 
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        } else {
            loadException.printStackTrace();
            return new ResponseEntity<>(loadException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}