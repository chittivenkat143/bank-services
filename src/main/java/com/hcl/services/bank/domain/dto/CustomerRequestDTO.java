package com.hcl.services.bank.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerRequestDTO {
	private Long customerId;

	@NotBlank(message = "Provide Customer Name")
	@Size(min = 10, max = 32, message = "Customer Name must be between 10 and 32 characters long")
	private String customerName;

	@Size(min = 10)
	@NotBlank(message = "Provide mobile number")
	private String customerMobile;

	@Email(message = "Please provide valid email")
	@NotBlank(message = "Provide email id")
	private String customerEmail;

	@NotBlank(message = "Provide username")
	@Size(min = 10, max = 15, message = "Username must be between 15 and 35 characters long")
	private String customerUsername;

	@NotBlank(message = "Provide password")
	private String customerPassword;

	@NotBlank(message = "Provide address")
	@Size(min = 15, max = 35, message = "Address must be between 15 and 35 characters long")
	private String customerAddress;

	private Integer customerType;// General(0), Wealth(1)
	private Integer customerStatus;// Active(0) , Inactive(1), Closed(2), Hold(3)
}
