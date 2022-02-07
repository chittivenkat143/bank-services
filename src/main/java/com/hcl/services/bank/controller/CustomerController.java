package com.hcl.services.bank.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.domain.dto.CustomerDto;
import com.hcl.services.bank.domain.dto.CustomerRequestDTO;
import com.hcl.services.bank.domain.dto.projection.CustomerView;
import com.hcl.services.bank.domain.dto.projection.CustomerViewOpenProj;
import com.hcl.services.bank.exception.BaseException;
import com.hcl.services.bank.service.ICustomerService;
import com.hcl.services.bank.utils.AppUtils;
import com.hcl.services.bank.utils.MapperHelper;

@RestController
@RequestMapping("/bank/customers")
public class CustomerController {
	private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private ICustomerService customerService;

	@PostMapping("/saveorupdate")
	public BaseResponse saveOrUpdateCustomer(@Valid @RequestBody CustomerRequestDTO customerDto, BindingResult errors) {
		logger.info("CC:saveOrUpdateCustomer");
		if (errors.hasErrors()) {
			logger.error("CC:errors:\t" + AppUtils.getInstance().getBindingResultToStrings(errors));
			return new BaseResponse(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Customer customer = customerService.saveOrUpdateCustomer(customerDto);
		return new BaseResponse(customer, HttpStatus.CREATED);
	}

	@GetMapping("/customer/{customerId}")
	public BaseResponse getCustomerById(@PathVariable("customerId") Long customerId) {
		logger.info("CC:getCustomerById=\t" + customerId);
		Customer customer = customerService.getCustomerById(customerId);
		if (customer != null) {
			logger.info("CC:getCustomerById:Found");
			return new BaseResponse(customer, HttpStatus.OK);
		} else {
			logger.info("CC:getCustomerById:Not Found:\t" + customerId);
			throw new BaseException("Customer not Found");
		}
	}
	
	@GetMapping("/mobile/{mobileNo}")
	public BaseResponse getCustomersByCustomerMobile(@PathVariable String mobileNo) {
		CustomerView customerDto = customerService.getCustomerByCustomerMobile(mobileNo);
		return new BaseResponse(customerDto, HttpStatus.OK);
	}

	@GetMapping("/type/{customerType}")
	public BaseResponse getCustomersByCustomerType(@PathVariable Integer customerType) {
		List<CustomerView> customerDto = customerService.getCustomersByCustomerType(customerType);
		return new BaseResponse(customerDto, HttpStatus.OK);
	}
	
	@GetMapping("/email/{email}")
	public BaseResponse getCustomersByEmail(@PathVariable String email) {
		CustomerViewOpenProj customerDto = customerService.getCustomerByEmail(email);
		return new BaseResponse(customerDto, HttpStatus.OK);
	}

}
