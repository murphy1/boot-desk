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

    private String searchQuery;

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

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
