package com.hcl.services.bank.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.CustomerRequestDTO;
import com.hcl.services.bank.domain.dto.projection.CustomerView;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.repo.CustomerRepository;
import com.hcl.services.bank.utils.MapperHelper;

@ExtendWith({ MockitoExtension.class })
class CustomerServiceTest {

	@Mock
	CustomerRepository repository;

	@InjectMocks
	CustomerService customerService;

	@Mock
	MapperHelper mapper;

	Customer customer;
	CustomerRequestDTO customerDto;
	CustomerView customerView;

	/**
	 * SpelAwareProxyProjectionFactory is a ProxyProjectionFactory help to add support to use Value
	 * Interface won't hold any value. It will help to hold values by setters 
	 */
	private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

	@BeforeEach
	void setUp() throws Exception {
		customerDto = new CustomerRequestDTO();
		customerDto.setCustomerName("Vishitha");
		customerDto.setCustomerMobile("9568568556");
		customerDto.setCustomerEmail("vishitha@gmail.com");
		customerDto.setCustomerUsername("vishitha");
		customerDto.setCustomerPassword("password123");
		customerDto.setCustomerType(0);
		customerDto.setCustomerStatus(0);

		customer = new Customer();
		// customer.setCustomerId(10001l);
		customer.setCustomerName("Vishitha");
		customer.setCustomerMobile("9568568556");
		customer.setCustomerEmail("vishitha@gmail.com");
		customer.setCustomerUsername("vishitha");
		customer.setCustomerPassword("password123");
		customer.setCustomerType(0);
		customer.setCustomerStatus(0);

		customerView = factory.createProjection(CustomerView.class);
		customerView.setCustomerId(10001l);
		customerView.setCustomerName("Vishitha");
		customerView.setCustomerMobile("9568568556");
		customerView.setCustomerEmail("vishitha@gmail.com");
	}

	@Test
	@DisplayName("Save Customer: Positive")
	final void testSaveCustomer_PC() {
		when(mapper.toCustomerEntity(any(CustomerRequestDTO.class))).thenReturn(customer);
		when(repository.save(any(Customer.class))).thenAnswer(c -> {
			Customer cus = c.getArgument(0);
			cus.setCustomerId(10001l);
			return cus;
		});

		Customer customerRe = customerService.saveOrUpdateCustomer(customerDto);
		assertAll("customer", () -> assertEquals(10001l, customerRe.getCustomerId()),
				() -> assertEquals("Vishitha", customerRe.getCustomerName()),
				() -> assertEquals("9568568556", customerRe.getCustomerMobile()),
				() -> assertEquals("vishitha@gmail.com", customerRe.getCustomerEmail()),
				() -> assertEquals("vishitha", customerRe.getCustomerUsername()),
				() -> assertEquals("password123", customerRe.getCustomerPassword()),
				() -> assertEquals(0, customerRe.getCustomerType()),
				() -> assertEquals(0, customerRe.getCustomerStatus()));

	}
	
	@Test
	@DisplayName("Update Customer: Positive")
	final void testUpdateCustomer_PC() {
		customer.setCustomerId(10001l);
		when(mapper.toCustomerEntity(any(CustomerRequestDTO.class))).thenReturn(customer);
		when(repository.save(any(Customer.class))).thenReturn(customer);
		
		Customer customerRe = customerService.saveOrUpdateCustomer(customerDto);
		assertAll("customer", () -> assertEquals(10001l, customerRe.getCustomerId()),
				() -> assertEquals("Vishitha", customerRe.getCustomerName()),
				() -> assertEquals("9568568556", customerRe.getCustomerMobile()),
				() -> assertEquals("vishitha@gmail.com", customerRe.getCustomerEmail()),
				() -> assertEquals("vishitha", customerRe.getCustomerUsername()),
				() -> assertEquals("password123", customerRe.getCustomerPassword()),
				() -> assertEquals(0, customerRe.getCustomerType()),
				() -> assertEquals(0, customerRe.getCustomerStatus()));
		
	}

	@Test
	@DisplayName("Get Customer By Id: Positive")
	final void testGetCustomerById_PC() {
		customer.setCustomerId(10001l);
		when(repository.findById(anyLong())).thenReturn(Optional.of(customer));
		Customer customerRe = customerService.getCustomerById(10001l);
		assertAll("customer", () -> assertEquals(10001l, customerRe.getCustomerId()),
				() -> assertEquals("Vishitha", customerRe.getCustomerName()),
				() -> assertEquals("9568568556", customerRe.getCustomerMobile()),
				() -> assertEquals("vishitha@gmail.com", customerRe.getCustomerEmail()),
				() -> assertEquals("vishitha", customerRe.getCustomerUsername()),
				() -> assertEquals("password123", customerRe.getCustomerPassword()),
				() -> assertEquals(0, customerRe.getCustomerType()),
				() -> assertEquals(0, customerRe.getCustomerStatus()));
	}

	@Test
	@DisplayName("Get Customer By Id: Negative")
	final void testGetCustomerById_NC() {
		when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException("Customer Not Found"));
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->customerService.getCustomerById(10001l));
		assertEquals("Customer Not Found", exception.getMessage());
	}

	@Test
	@DisplayName("Get Customer By Mobile: Positive")
	final void testGetCustomerByCustomerMobile_PC() {
		when(repository.findCustomerByCustomerMobile(anyString())).thenReturn(Optional.of(customerView));
		CustomerView customerRe = customerService.getCustomerByCustomerMobile("9568568556");
		assertAll("customer", () -> assertEquals(10001l, customerRe.getCustomerId()),
				() -> assertEquals("Vishitha", customerRe.getCustomerName()),
				() -> assertEquals("9568568556", customerRe.getCustomerMobile()),
				() -> assertEquals("vishitha@gmail.com", customerRe.getCustomerEmail()));
	}
	
	@Test
	@DisplayName("Get Customer By Mobile: Negative")
	final void testGetCustomerByCustomerMobile_NC() {
		when(repository.findCustomerByCustomerMobile(anyString())).thenThrow(new ResourceNotFoundException("Customer Not Found"));
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->customerService.getCustomerByCustomerMobile("9568568556"));
		assertEquals("Customer Not Found", exception.getMessage());
	}

}
