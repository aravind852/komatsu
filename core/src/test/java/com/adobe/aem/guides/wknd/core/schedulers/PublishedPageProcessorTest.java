///*
// *  Copyright 2018 Adobe Systems Incorporated
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package com.adobe.aem.guides.wknd.core.schedulers;
//
//import org.apache.sling.api.resource.*;
//import org.apache.sling.commons.scheduler.Scheduler;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.jcr.Session;
//import java.util.*;
//
//import static org.mockito.Mockito.*;
//
//public class PublishedPageProcessorTest {
//
//    @Mock private ResourceResolverFactory resolverFactory;
//    @Mock private Scheduler scheduler;
//    @Mock private ResourceResolver resolver;
//    @Mock private Resource resource;
//    @Mock private Resource content;
//    @Mock private ModifiableValueMap valueMap;
//    @Mock private Session session;
//
//    @InjectMocks private PublishedPageProcessor processor;
//
//    @BeforeEach
//    public void setup() throws Exception {
//        MockitoAnnotations.openMocks(this);
//        when(resolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resolver);
//        when(resolver.findResources(anyString(), anyString())).thenReturn(Collections.singletonList(resource).iterator());
//        when(resource.adaptTo(ReplicationStatus.class)).thenReturn(() -> true, () -> true);
//        when(resource.getChild("jcr:content")).thenReturn(content);
//        when(content.adaptTo(ModifiableValueMap.class)).thenReturn(valueMap);
//        when(resolver.adaptTo(Session.class)).thenReturn(session);
//    }
//
//    @Test
//    public void testRunInAuthorMode() throws Exception {
//        System.setProperty("run.modes", "author");
//        processor.run();
//        verify(valueMap, atLeastOnce()).put(eq("processedDate"), any());
//        verify(session).save();
//    }
//
//    @Test
//    public void testRunInPublishModeSkipsExecution() {
//        System.setProperty("run.modes", "publish");
//        processor.run();
//        verify(session, never()).save();
//    }
//}