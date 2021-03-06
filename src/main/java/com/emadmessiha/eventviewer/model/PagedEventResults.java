package com.emadmessiha.eventviewer.model;

import java.util.List;

import lombok.Data;

@Data
public class PagedEventResults {
    private Integer page;
    private Integer pageSize;
    private Integer totalResults;
    private Integer totalPages;
    private List<EventItem> data;
}