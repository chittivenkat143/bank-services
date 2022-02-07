package com.hcl.services.bank.domain.dto.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CustomerViewOpenProj {
	
	@Value("#{target.customerUsername + ' ' + target.customerPassword}")
    String getFullNameAndPassword();
	
	@Value("#{target.customerMobile}")
	public String getCustomerMobile();
	
	@Value("#{target.customerEmail}")
	public String getCustomerEmail();
	
	@Value("#{target.customerType}")
	public Integer getCustomerType();
	
	@Value("#{target.customerStatus}")
	public Integer getCustomerStatus();

}
