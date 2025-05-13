/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.servlets;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_PATHS + "=/bin/page-author-info"
        })
public class PageAuthorServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getParameter("path");
        if (path == null || path.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("Missing 'path' parameter");
            return;
        }

        String ext = request.getRequestPathInfo().getExtension();
        ResourceResolver resolver = request.getResourceResolver();
        Resource page = resolver.getResource(path);

        if (page == null || page.getChild("jcr:content") == null) {
            response.setStatus(404);
            response.getWriter().write("Page not found or missing jcr:content");
            return;
        }

        Resource content = page.getChild("jcr:content");
        ValueMap properties = content.getValueMap();
        String lastModifiedBy = properties.get("cq:lastModifiedBy", "");
        String firstName = "Unknown";
        String lastName = "Author";

        Resource userRes = resolver.getResource("/home/users/" + lastModifiedBy);
        if (userRes != null) {
            ValueMap userProps = userRes.getValueMap();
            firstName = userProps.get("profile/givenName", "Unknown");
            lastName = userProps.get("profile/familyName", "Author");
        }

        JSONArray children = new JSONArray();
        Iterator<Resource> childPages = page.listChildren();
        while (childPages.hasNext()) {
            Resource child = childPages.next();
            Resource childContent = child.getChild("jcr:content");
            if (childContent != null) {
                ValueMap childProps = childContent.getValueMap();
                if (lastModifiedBy.equals(childProps.get("cq:lastModifiedBy", ""))) {
                    children.put(child.getPath());
                }
            }
        }

        if ("xml".equals(ext)) {
            response.setContentType("application/xml");
            response.getWriter().write(
                    "<?xml version='1.0' encoding='UTF-8'?>\n<author>" +
                    "<firstName>" + firstName + "</firstName>" +
                    "<lastName>" + lastName + "</lastName>" +
                    "<pages>" + children.toString() + "</pages>" +
                    "</author>");
        } else {
            response.setContentType("application/json");
            JSONObject json = new JSONObject();
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("modifiedPages", children);
            response.getWriter().write(json.toString());
        }
    }
}