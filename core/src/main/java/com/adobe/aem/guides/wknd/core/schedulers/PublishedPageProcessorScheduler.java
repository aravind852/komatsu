package com.adobe.aem.guides.wknd.core.schedulers;

import com.adobe.aem.guides.wknd.core.config.PublishedPageProcessorConfig;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.replication.ReplicationStatus;

import org.apache.sling.api.resource.*;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = PublishedPageProcessorConfig.class)
public class PublishedPageProcessorScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PublishedPageProcessorScheduler.class);

    @Reference
    private SlingSettingsService slingSettingsService;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private boolean isAuthor;

    private PublishedPageProcessorConfig config;

    @Activate
    @Modified
    protected void activate(PublishedPageProcessorConfig config) {
        this.config = config;
        Set<String> runModes = slingSettingsService.getRunModes();
        isAuthor = runModes.contains("author");
        LOG.info("PublishedPageProcessorScheduler activated. Run mode: {}", isAuthor ? "Author" : "Publish");
    }

    @Override
    public void run() {
        if (config.authorEnvironmentOnly() && !isAuthor) {
            LOG.info("Skipping execution - Not running on Author environment.");
            return;
        }

        LOG.info("Running PublishedPageProcessorScheduler...");

        try (ResourceResolver resolver = getServiceResourceResolver()) {
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager == null) {
                LOG.warn("PageManager is null");
                return;
            }

            Page rootPage = pageManager.getPage("/content");
            if (rootPage == null) {
                LOG.warn("Root /content page not found");
                return;
            }

            Iterator<Page> pageIterator = rootPage.listChildren();
            while (pageIterator.hasNext()) {
                Page page = pageIterator.next();

                // Check if page is published using ReplicationStatus
                ReplicationStatus status = page.adaptTo(ReplicationStatus.class);
                if (status != null && status.isActivated()) {
                    Node node = page.adaptTo(Node.class);
                    if (node != null && !node.hasProperty("processedDate")) {
                        Calendar now = Calendar.getInstance();
                        node.setProperty("processedDate", now);
                        resolver.commit();
                        LOG.info("Set processedDate on published page: {}", page.getPath());
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("Error while processing published pages", e);
        }
    }

    private ResourceResolver getServiceResourceResolver() throws LoginException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "komatsu"); // using your system user
        return resolverFactory.getServiceResourceResolver(params);
    }
}
