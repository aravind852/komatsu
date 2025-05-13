package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class, 
	adapters = DetailsComponent.class, 
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)


public class DetailsComponentImpl implements DetailsComponent{
	@Optional
@ValueMapValue
String text;
	@Override
	public String getTextData() {
		
		return text;
	}

}
