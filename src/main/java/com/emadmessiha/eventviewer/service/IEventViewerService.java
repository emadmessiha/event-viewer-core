package com.emadmessiha.eventviewer.service;

import java.util.Date;
import java.util.List;

import com.emadmessiha.eventviewer.model.EventItem;

public interface IEventViewerService {

    List<EventItem> searchEvents(Date startDate, Integer numberOfDays);

    EventItem getEventDetails(String objectId);
}