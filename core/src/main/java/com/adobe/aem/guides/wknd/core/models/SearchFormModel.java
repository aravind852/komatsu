package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchFormModel {

    private final SlingHttpServletRequest request;

    private String searchTerm;

    public SearchFormModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    protected void init() {
        searchTerm = request.getParameter("searchTerm");
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
