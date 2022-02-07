package com.hcl.services.bank.domain.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequestDTO {

	@NotNull(message = "Required Account number to debit")
	private String accountNumberDebit;

	@NotNull(message = "Required Account number to credit")
	private String accountNumberCredit;

	@DecimalMin("1.0")
	private Double transactionAmount;

}
