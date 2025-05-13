package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Published Page Processor Config")
public @interface PublishedPageProcessorConfig {

    @AttributeDefinition(
            name = "Scheduler Cron Expression",
            description = "CRON expression to run every 2 minutes"
    )
    String scheduler_expression() default "0 0/2 * * * ?";

    @AttributeDefinition(
            name = "Run on Author Only",
            description = "Set to true to restrict execution to Author environment"
    )
    boolean authorEnvironmentOnly() default true;
}
