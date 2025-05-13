package com.adobe.aem.guides.wknd.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultModel {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<Map<String, String>> results = new ArrayList<>();

    public List<Map<String, String>> getResults() {
        return results;
    }

    @PostConstruct
    protected void init() {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        String rootPath = "/content/wknd/us/en"; // Adjust to your content root if different
        String query = "SELECT * FROM [cq:PageContent] AS page " +
                "WHERE ISDESCENDANTNODE(page, '" + rootPath + "') " +
                "AND (CONTAINS(page.[jcr:title], '" + searchTerm + "') " +
                "OR CONTAINS(page.[jcr:description], '" + searchTerm + "'))";

        Iterator<Resource> pages = resourceResolver.findResources(query, "JCR-SQL2");

        while (pages.hasNext()) {
            Resource page = pages.next();
            Map<String, String> item = new HashMap<>();
            item.put("title", page.getValueMap().get("jcr:title", ""));
            item.put("description", page.getValueMap().get("jcr:description", ""));
            item.put("lastModified", page.getValueMap().get("cq:lastModified", "").toString());

            // Optional image handling
            Resource imageResource = page.getChild("image");
            String image = (imageResource != null) ? imageResource.getValueMap().get("fileReference", "") : "";
            item.put("image", image);

            results.add(item);
        }
    }
}
