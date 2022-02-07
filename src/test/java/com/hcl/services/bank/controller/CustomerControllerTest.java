package com.hcl.services.bank.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.domain.dto.CustomerRequestDTO;
import com.hcl.services.bank.domain.dto.projection.CustomerView;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.service.impl.CustomerService;

@ExtendWith({MockitoExtension.class})
class CustomerControllerTest {
	
	@Mock
	private CustomerService customerService;
	
	@InjectMocks
	private CustomerController customerController;

	Customer customer;
	CustomerRequestDTO customerDto;
	
	@Mock
	BindingResult errors;
	
	@Mock
	private List<CustomerView> customers = new ArrayList<>();
	
	@BeforeEach
	void setUp() throws Exception {		
		customerDto = new CustomerRequestDTO();
		customerDto.setCustomerName("Pushpa Raj");
		customerDto.setCustomerMobile("9876543210");
		customerDto.setCustomerEmail("pushparaj@gmail.com");
		customerDto.setCustomerUsername("pushparaj");
		customerDto.setCustomerPassword("pushparaj123");
		customerDto.setCustomerType(0);
		customerDto.setCustomerStatus(0);
		
		customer = new Customer();
		customer.setCustomerId(10001l);
		customer.setCustomerName("Vishitha");
		customer.setCustomerMobile("9568568556");
		customer.setCustomerEmail("vishitha@gmail.com");
		customer.setCustomerUsername("vishitha");
		customer.setCustomerPassword("password123");
		customer.setCustomerType(0);
		customer.setCustomerStatus(0);
	}
	
	

	@Test
	@DisplayName("Save Customer: Positive")
	void testSaveOrUpdateCustomer_PC() {
		when(customerService.saveOrUpdateCustomer(customerDto)).thenReturn(customer);
		BaseResponse baseResponse = customerController.saveOrUpdateCustomer(customerDto, errors);
		assertEquals(HttpStatus.CREATED, baseResponse.getStatus());
	}
	
	@Test
	@DisplayName("Save Customer: Negative")
	void testSaveOrUpdateCustomer_NC() {
		when(errors.hasErrors()).thenReturn(true);
		BaseResponse baseResponse = customerController.saveOrUpdateCustomer(customerDto, errors);				
		assertEquals(HttpStatus.BAD_REQUEST, baseResponse.getStatus());
		verify(errors, times(1)).hasErrors();
		assertTrue(errors.hasErrors());
	}


	@Test
	@DisplayName("Get Customer By Id: Positive")
	void testGetCustomerById_PC() {
		when(customerService.getCustomerById(any(Long.class))).thenReturn(customer);
		BaseResponse customerBR = customerController.getCustomerById(10001l);
		Customer customerRe = (Customer) customerBR.getResponse();
		assertAll("customer", 
					() -> assertEquals(10001l, customerRe.getCustomerId()),
					() -> assertEquals("Vishitha", customerRe.getCustomerName()),
					() -> assertEquals("9568568556", customerRe.getCustomerMobile()),
					() -> assertEquals("vishitha@gmail.com", customerRe.getCustomerEmail()),
					() -> assertEquals("vishitha", customerRe.getCustomerUsername()),
					() -> assertEquals("password123", customerRe.getCustomerPassword()),
					() -> assertEquals(0, customerRe.getCustomerType()),					
					() -> assertEquals(0, customerRe.getCustomerStatus())				
				);
	}
	
	@Test
	@DisplayName("Get Customer By Id: Negative")
	void testGetCustomerById_NC() {
		when(customerService.getCustomerById(any(Long.class))).thenThrow(new ResourceNotFoundException("Customer Not Found"));
		RuntimeException exception = assertThrows(ResourceNotFoundException.class, ()->customerController.getCustomerById(10111l));
		assertEquals("Customer Not Found", exception.getMessage());
	}

	@Test
	@DisplayName("Get Customers By Type: Positive")
	void testGetCustomersByCustomerType_PS() {
		when(customerService.getCustomersByCustomerType(0)).thenReturn(customers);
		BaseResponse customersBR = customerController.getCustomersByCustomerType(0);
		@SuppressWarnings("unchecked")
		List<CustomerView> customersNew = (List<CustomerView>) customersBR.getResponse();
		assertEquals(0, customersNew.size());
	}

}
