package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

@FunctionalInterface
public interface TransferState {
	TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber);
}
