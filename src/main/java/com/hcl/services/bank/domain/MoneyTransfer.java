package com.hcl.services.bank.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyTransfer {

	private Account accountCredit;
	private Account accountDebit;
	private Double amount;

}
