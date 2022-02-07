package com.hcl.services.bank.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRequestDTO {

	private Long accountId;

	//@NotNull(message = "Provide account number")
	//@Size(min = 10)
	private String accountNumber;

	@NotNull(message = "Provide Account Code")
	private Integer accountCode;

	@NotNull(message = "Provide customer Id")
	// @Size(min = 10)
	private Integer accountCustomerId;

	private Double accountBalance;
}
