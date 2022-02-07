package com.hcl.services.bank.domain.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDto {
	private Long accountId;
	private String accountNumber;
	private Date accountCreatedOn;
	private AccountTypeDto accountCode;
	private CustomerDto accountCustomerId;
	private Double accountBalance;
	private List<TransactionDto> transactions;
}
