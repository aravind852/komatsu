package com.adobe.aem.guides.wknd.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Search Page Titles under /content/wknd",
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/bin/searchtitles"
        }
)
public class FindPageTitlesServlet extends SlingAllMethodsServlet {

    private static final String ROOT_PATH = "/content/wknd";

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
    	JsonArray results = new JsonArray();
        String queryText = request.getParameter("searchTerm");
        JsonObject obj1 = new JsonObject();
        obj1.addProperty("query", queryText);
        results.add(obj1);
        if (queryText == null || queryText.trim().isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing query parameter 'q'\"}");
            return;
        }

        ResourceResolver resolver = request.getResourceResolver();
        String jcrSql2 = "SELECT * FROM [cq:PageContent] AS page " +
                "WHERE ISDESCENDANTNODE(page, '" + ROOT_PATH + "') " +
                "AND (CONTAINS(page.[jcr:title], '" + queryText + "') " +
                "OR CONTAINS(page.[jcr:description], '" + queryText + "'))";
        
        Iterator<Resource> resultResources = resolver.findResources(jcrSql2, Query.JCR_SQL2);

       
        while (resultResources.hasNext()) {
            Resource contentRes = resultResources.next();
            Resource page = contentRes.getParent(); // Get parent cq:Page
            obj1.addProperty("page", page.getPath());
            results.add(obj1);
            if (page != null) {
                String title = contentRes.getValueMap().get("jcr:title", "");
                String description = contentRes.getValueMap().get("jcr:description", "");
                String lastModified = contentRes.getValueMap().get("cq:lastModified", "");
             
                JsonObject obj = new JsonObject();
                obj.addProperty("title", title);
                obj.addProperty("path", page.getPath());
                
                obj.addProperty("description", description);
                obj.addProperty("lastModified", lastModified);
                
                results.add(obj);
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(results.toString());
    }
}