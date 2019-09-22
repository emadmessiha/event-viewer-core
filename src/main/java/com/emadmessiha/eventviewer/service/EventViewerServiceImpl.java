package com.emadmessiha.eventviewer.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.emadmessiha.eventviewer.model.EventItem;

import org.springframework.stereotype.Service;

@Service
public class EventViewerServiceImpl implements IEventViewerService {

    @Override
    public List<EventItem> searchEvents(Date startDate, Integer numberOfDays) {
        if (numberOfDays > 90) {
            // 3 month, which is a quarter
            throw new IllegalArgumentException("Number of days duration cannot exceed 90 days");
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);            
            calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
            Date endDate = calendar.getTime();
            List<EventItem> items = new ArrayList<>();
            EventItem item = new EventItem();
            items.add(item);
            return items;
        }
    }

    @Override
    public EventItem getEventDetails(String objectId) {
        return new EventItem();
    }
    
}