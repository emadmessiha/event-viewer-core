package com.emadmessiha.eventviewer.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventViewerWebController {
 
    @GetMapping("/")
    public String homePage(Model model) {
        return "home";
    }
}