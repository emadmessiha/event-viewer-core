package com.emadmessiha.eventviewer.service;

import java.util.Date;

import com.emadmessiha.eventviewer.model.EventItem;
import com.emadmessiha.eventviewer.model.PagedEventResults;

public interface IEventViewerService {

    PagedEventResults searchEvents(Date startDate, Integer numberOfDays, Integer pageSize, Integer pageNumber);

    EventItem getEventDetails(String objectId);

    Boolean reloadSeedData();
}