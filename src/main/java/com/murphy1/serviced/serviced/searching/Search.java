package com.murphy1.serviced.serviced.searching;

import org.springframework.stereotype.Component;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Component
public class Search {

    @Enumerated(EnumType.ORDINAL)
    private SearchType searchType;

    @Enumerated(EnumType.ORDINAL)
    private SearchObject searchObject;

    @Enumerated(EnumType.ORDINAL)
    private SearchObject searchObject1;

    private String searchQuery;

    private String searchQuery1;

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public SearchObject getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(SearchObject searchObject) {
        this.searchObject = searchObject;
    }

    public SearchObject getSearchObject1() {
        return searchObject1;
    }

    public void setSearchObject1(SearchObject searchObject1) {
        this.searchObject1 = searchObject1;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery1() {
        return searchQuery1;
    }

    public void setSearchQuery1(String searchQuery1) {
        this.searchQuery1 = searchQuery1;
    }
}
