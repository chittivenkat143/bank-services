package com.hcl.services.bank.domain.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionResponseDTO {
	private String txnNumber;
	private String txnState;
	private AccountDto debitAccount;
	private AccountDto creditAccount;
	private Double amount;
	private List<TransactionDto> transactions;
}
