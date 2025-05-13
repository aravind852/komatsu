package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = SlingHttpServletRequest.class, 
		adapters = KFComponent.class,
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class KFComponentImpl implements KFComponent{
	
	@ValueMapValue
	String title;
	@ValueMapValue
	String subTitle;
	@ValueMapValue
	String description;
	@ValueMapValue
	String image;
	@ValueMapValue
	String ctaButton;
	@ValueMapValue
	String ctaLink;
	

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getSubTitle() {
		// TODO Auto-generated method stub
		return subTitle;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public String getCTAButton() {
		// TODO Auto-generated method stub
		return ctaButton;
	}

	@Override
	public String getCTALink() {
		// TODO Auto-generated method stub
		return ctaLink;
	}

}
