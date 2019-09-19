package com.emadmessiha.eventviewer.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventViewerRestController {
 
    @GetMapping("")
    public List<Object> listEvents() {
        // TODO: implement this method to return the data
        return new ArrayList<>();
    }
}