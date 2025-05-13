package com.adobe.aem.guides.wknd.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,adapters = AssignementComponent.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AssignementComponentImpl implements AssignementComponent{

	@ValueMapValue
	String text;
	@ValueMapValue
	String mobilenumber;
	@ValueMapValue
	String drivinglicense;
	@ValueMapValue
	String gender;
	@ValueMapValue
	String dob;
	@ValueMapValue
	String linkedinid;
	
	@Override
	public String getNameData() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public String getMobileNumber() {
		// TODO Auto-generated method stub
		return mobilenumber;
	}

	@Override
	public String getDrivingLicense() {
		// TODO Auto-generated method stub
		return drivinglicense;
	}

	@Override
	public String getGender() {
		// TODO Auto-generated method stub
		return gender;
	}

	@Override
	public String getDOB() {
		// TODO Auto-generated method stub
		return dob;
	}

	@Override
	public String getLinkedInID() {
		// TODO Auto-generated method stub
		return linkedinid;
	}
	
}
