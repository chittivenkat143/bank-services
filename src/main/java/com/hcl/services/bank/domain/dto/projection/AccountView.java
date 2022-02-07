package com.hcl.services.bank.domain.dto.projection;

import java.util.Date;
import java.util.List;

import com.hcl.services.bank.domain.AccountType;
import com.hcl.services.bank.domain.Customer;

public interface AccountView {
	public Long getAccountId();
	public String getAccountNumber();
	public Date getAccountCreatedOn();
	public AccountType getAccountType();
	public Customer getAccountCustomerId();
	public Double getAccountBalance();
	public List<TransactionView> getTransactions();
}
