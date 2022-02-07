package com.hcl.services.bank.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountTypeDto {
	private Long accountCode;
	private String accountName;
	private String accountDescription;
}
