//
//package com.adobe.aem.guides.wknd.core.servlets;
//
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.resource.ValueMap;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.Collections;
//
//import static org.mockito.Mockito.*;
//
//public class PageAuthorServletTest {
//
//    private PageAuthorServlet servlet;
//    private SlingHttpServletRequest request;
//    private SlingHttpServletResponse response;
//    private ResourceResolver resolver;
//    private Resource page, content, userRes;
//    private ValueMap pageProps, userProps;
//
//    @BeforeEach
//    public void setUp() {
//        servlet = new PageAuthorServlet();
//        request = mock(SlingHttpServletRequest.class);
//        response = mock(SlingHttpServletResponse.class);
//        resolver = mock(ResourceResolver.class);
//        page = mock(Resource.class);
//        content = mock(Resource.class);
//        userRes = mock(Resource.class);
//        pageProps = mock(ValueMap.class);
//        userProps = mock(ValueMap.class);
//
//        when(request.getResourceResolver()).thenReturn(resolver);
//        when(request.getParameter("path")).thenReturn("/content/test");
//        when(request.getRequestPathInfo()).thenReturn(() -> "json");
//        when(resolver.getResource("/content/test")).thenReturn(page);
//        when(page.getChild("jcr:content")).thenReturn(content);
//        when(content.getValueMap()).thenReturn(pageProps);
//        when(pageProps.get("cq:lastModifiedBy", "")).thenReturn("author1");
//        when(resolver.getResource("/home/users/author1")).thenReturn(userRes);
//        when(userRes.getValueMap()).thenReturn(userProps);
//        when(userProps.get("profile/givenName", "Unknown")).thenReturn("John");
//        when(userProps.get("profile/familyName", "Author")).thenReturn("Doe");
//        when(page.listChildren()).thenReturn(Collections.emptyIterator());
//    }
//
//    @Test
//    public void testDoGetWithValidJsonOutput() throws Exception {
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        servlet.doGet(request, response);
//
//        writer.flush();
//        String output = stringWriter.toString();
//        assert output.contains("John");
//        assert output.contains("Doe");
//        assert output.contains("modifiedPages");
//    }
//}