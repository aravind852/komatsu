package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = {

		Constants.SERVICE_DESCRIPTION + "=Servlet which gives provided paths",

		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/api/search" })

@SuppressWarnings({ "CQRules:CQBP-75" })

public class Exercise2 extends SlingAllMethodsServlet {

	@Override

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)

			throws ServletException, IOException {
		String path = request.getParameter("path"); // e.g., /content/mysite/home
        if (path == null || path.isEmpty()) {
            response.getWriter().write("Page path is missing.");
            return;
        }
 
        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource(path);
 
        String title = "h";
        if (resource == null) {
            response.getWriter().write("Resource not found at path: " + path);
            return;
        }
        try {
            Node pageNode = resource.adaptTo(Node.class);
            if (pageNode != null && pageNode.hasNode("jcr:content")) {
                Node contentNode = pageNode.getNode("jcr:content");
                 title = contentNode.hasProperty("cq:lastModifiedBy")
                        ? contentNode.getProperty("cq:lastModifiedBy").getString()
                        : "(No title set)";
                response.getWriter().write("Title: " + title);
            } else {
                response.getWriter().write("jcr:content node not found.");
                response.getWriter().write( "workin");
            }
        } catch (RepositoryException e) {
            throw new ServletException("Error accessing JCR node", e);
        }
    
		
	}
}


