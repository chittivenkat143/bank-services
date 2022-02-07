package com.hcl.services.bank.domain.dto.projection;

import java.util.Date;

import com.hcl.services.bank.domain.Transaction.State;
import com.hcl.services.bank.domain.Transaction.TxnType;

public interface TransactionView {
	public Long getTransactionId();
	public String getTransactionNumber();
	public Date getTransactionOn();
	public Double getTransactionAmount();
	public TxnType getTransactionType();
	public AccountView getTransactionAccountId();
	public String getTransactionAccountNumber();
	public State getTransactionstate();
}
