package com.adobe.aem.guides.wknd.core.servlets;

import com.google.gson.JsonArray;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.jcr.query.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FindPageTitleServletTest {

	@InjectMocks
    private FindPageTitlesServlet servlet;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resourceResolver;

    @BeforeEach
    void setUp() {
    	//servlet = new FindPageTitleServlet();
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resourceResolver = mock(ResourceResolver.class);
    }

    @Test
    void testValidSearchQueryReturnsResults() throws Exception {
        // Arrange
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("q")).thenReturn("adventure");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(response.getWriter()).thenReturn(writer);

        // Mock Resource with jcr:title
        Resource contentResource = mock(Resource.class);
        Resource parentPage = mock(Resource.class);

        when(contentResource.getParent()).thenReturn(parentPage);
        when(parentPage.getPath()).thenReturn("/content/wknd/us/en/adventure");

        when(contentResource.getValueMap()).thenReturn(new org.apache.sling.api.wrappers.ValueMapDecorator(
                Collections.singletonMap("jcr:title", "Adventure Page")
        ));

        Iterator<Resource> iterator = Collections.singletonList(contentResource).iterator();
        when(resourceResolver.findResources(anyString(), eq(Query.JCR_SQL2))).thenReturn(iterator);

        // Act
        servlet.doGet(request, response);

        // Assert
        writer.flush(); // Flush the writer to populate stringWriter
        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.contains("Adventure Page"));
        assertTrue(jsonOutput.contains("/content/wknd/us/en/adventure"));
    }

    @Test
    void testMissingQueryParameterReturnsBadRequest() throws Exception {
        // Arrange
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getParameter("q")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("{\"error\": \"Missing query parameter 'q'\"}");
    }

    @Test
    void testEmptyResultsReturnsEmptyArray() throws Exception {
        // Arrange
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("q")).thenReturn("nonexistent");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(response.getWriter()).thenReturn(writer);

        when(resourceResolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(Collections.emptyIterator());

        // Act
        servlet.doGet(request, response);

        // Assert
        writer.flush();
        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.equals("[]"));
    }
}