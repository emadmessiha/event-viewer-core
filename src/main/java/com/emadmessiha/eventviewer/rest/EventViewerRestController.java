package com.emadmessiha.eventviewer.rest;

import java.util.Date;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.model.PagedEventResults;
import com.emadmessiha.eventviewer.service.IEventViewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/events")
public class EventViewerRestController {

    @Autowired
    IEventViewerService eventsService;
 
    @CrossOrigin
    @GetMapping("/search/{startDate}/{numberOfDays}")
    public PagedEventResults searchEvents(
        @PathVariable("startDate") @DateTimeFormat(iso = ISO.DATE) Date startDate, 
        @PathVariable("numberOfDays") Integer numberOfDays,
        @RequestParam Integer page,
        @RequestParam Integer pageSize) {
            return eventsService.searchEvents(startDate, numberOfDays, pageSize, page);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public EventItem getEventDetails(
        @PathVariable("id") String id) {
            return eventsService.getEventDetails(id);
    }

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        return eventsService.importFile(file) ? 
            new ResponseEntity<>(HttpStatus.CREATED) : 
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}