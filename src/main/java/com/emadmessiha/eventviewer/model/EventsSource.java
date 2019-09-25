package com.emadmessiha.eventviewer.model;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.Data;

@Data
public class EventsSource {
    private String httpFullURL;
    private Boolean isZipped;

    public URL url() {
        try {
            return new URL(this.httpFullURL);
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}