package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchResultModelTest {

    private SlingHttpServletRequest request;
    private ResourceResolver resourceResolver;

    @BeforeEach
    public void setUp() {
        request = Mockito.mock(SlingHttpServletRequest.class);
        resourceResolver = Mockito.mock(ResourceResolver.class);
    }

    @Test
    public void testInitWithValidSearchTerm() {
        // Arrange
        Mockito.when(request.getParameter("searchTerm")).thenReturn("WKND");

        Resource mockPage = Mockito.mock(Resource.class);
        ValueMap valueMap = Mockito.mock(ValueMap.class);

        Mockito.when(valueMap.get("jcr:title", "")).thenReturn("WKND Adventures");
        Mockito.when(valueMap.get("jcr:description", "")).thenReturn("Exciting outdoor trips");
        Mockito.when(valueMap.get("cq:lastModified", "")).thenReturn("2024-01-01");

        Mockito.when(mockPage.getValueMap()).thenReturn(valueMap);

        // Image mock
        Resource imageResource = Mockito.mock(Resource.class);
        ValueMap imageValueMap = Mockito.mock(ValueMap.class);
        Mockito.when(imageValueMap.get("fileReference", "")).thenReturn("/content/dam/image.jpg");
        Mockito.when(imageResource.getValueMap()).thenReturn(imageValueMap);
        Mockito.when(mockPage.getChild("image")).thenReturn(imageResource);

        Iterator<Resource> mockIterator = Collections.singletonList(mockPage).iterator();
        Mockito.when(resourceResolver.findResources(Mockito.anyString(), Mockito.eq("JCR-SQL2")))
               .thenReturn(mockIterator);

        // Inject into model manually
        SearchResultModel model = new SearchResultModel();
        inject(model, request, resourceResolver);

        // Act
        model.init();

        // Assert
        List<Map<String, String>> results = model.getResults();
        assertEquals(1, results.size());

        Map<String, String> result = results.get(0);
        assertEquals("WKND Adventures", result.get("title"));
        assertEquals("Exciting outdoor trips", result.get("description"));
        assertEquals("2024-01-01", result.get("lastModified"));
        assertEquals("/content/dam/image.jpg", result.get("image"));
    }

    @Test
    public void testInitWithEmptySearchTerm() {
        // Arrange
        Mockito.when(request.getParameter("searchTerm")).thenReturn("");

        SearchResultModel model = new SearchResultModel();
        inject(model, request, resourceResolver);

        // Act
        model.init();

        // Assert
        assertTrue(model.getResults().isEmpty());
    }

    private void inject(SearchResultModel model, SlingHttpServletRequest req, ResourceResolver resolver) {
        // Reflection injection since @SlingObject and @Self aren’t auto-populated in unit tests
        try {
        	Field reqField = SearchResultModel.class.getDeclaredField("request");
            reqField.setAccessible(true);
            reqField.set(model, req);

            Field resolverField = SearchResultModel.class.getDeclaredField("resourceResolver");
            resolverField.setAccessible(true);
            resolverField.set(model, resolver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mocks", e);
        }
    }
}