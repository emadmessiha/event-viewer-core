package com.emadmessiha.eventviewer.service;

import java.util.Date;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.model.EventsSource;
import com.emadmessiha.eventviewer.model.PagedEventResults;

import org.springframework.web.multipart.MultipartFile;

public interface IEventViewerService {

    PagedEventResults searchEvents(Date startDate, Integer numberOfDays, Integer pageSize, Integer pageNumber);

    EventItem getEventDetails(String objectId);

    Exception loadData(EventsSource source);
    Boolean importFile(MultipartFile jsonFile);
}