package com.hcl.services.bank.service.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.CustomerRequestDTO;
import com.hcl.services.bank.domain.dto.projection.CustomerView;
import com.hcl.services.bank.domain.dto.projection.CustomerViewOpenProj;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.repo.CustomerRepository;
import com.hcl.services.bank.service.ICustomerService;
import com.hcl.services.bank.utils.MapperHelper;

@Service
public class CustomerService implements ICustomerService {
	private static Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private MapperHelper mapper;

	@Override
	public Customer saveOrUpdateCustomer(CustomerRequestDTO customerDto) {
		logger.info("CS:saveOrUpdateCustomer");
		Customer customer = mapper.toCustomerEntity(customerDto);
		customer = repository.save(customer);
		return customer;
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		logger.info("CS:getCustomerById:\t" + customerId);
		return repository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not Found ID:" + customerId));
	}
	
	@Override
	public List<Customer> getCustomersByType(Integer customerType){
		logger.info("CS:getCustomersByType:\t" + customerType);
		return Collections.emptyList();//repository.findByCustomerType(customerType);
	}

	@Override
	public CustomerView getCustomerByCustomerMobile(String mobileNo) {
		logger.info("CS:getCustomerDtoByCustomerMobile:\t" + mobileNo);
		return repository.findCustomerByCustomerMobile(mobileNo)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not Found By Mobile:" + mobileNo));
	}
	
	@Override
	public CustomerViewOpenProj getCustomerByEmail(String email) {
		logger.info("CS:getCustomerByEmail:\t" + email);
		return repository.findByCustomerEmail(email);
	}

	@Override
	public List<CustomerView> getCustomersByCustomerType(Integer customerType) {
		logger.info("CS:getCustomersByCustomerType:\t" + customerType);
		return repository.findByCustomerType(customerType);
	}

}
