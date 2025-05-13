package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchFormModelTest {

    private SlingHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        request = Mockito.mock(SlingHttpServletRequest.class);
    }

    @Test
    public void testSearchTermIsInitializedFromRequestParameter() {
        // Arrange
        Mockito.when(request.getParameter("searchTerm")).thenReturn("mock-query");

        // Act
        SearchFormModel model = new SearchFormModel(request);
        model.init(); // Manually call PostConstruct

        // Assert
        assertEquals("mock-query", model.getSearchTerm());
    }

    @Test
    public void testSearchTermIsNullWhenParameterIsMissing() {
        // Arrange
        Mockito.when(request.getParameter("searchTerm")).thenReturn(null);

        // Act
        SearchFormModel model = new SearchFormModel(request);
        model.init(); // Manually call PostConstruct

        // Assert
        assertEquals(null, model.getSearchTerm());
    }
}