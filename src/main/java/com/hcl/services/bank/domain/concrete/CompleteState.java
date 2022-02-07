package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

public class CompleteState implements TransferState {

	private static CompleteState completeState;

	public static CompleteState getInstance() {
		if (completeState == null) {
			completeState = new CompleteState();
			return completeState;
		}
		return completeState;
	}

	private CompleteState() {
	}

	@Override
	public TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber) {
		transferContext.setHasIssues(hasIssues);
		System.out.println(!hasIssues ? "Transfer Money Complete" : "Transfer Money Complete Failed");
		return transferContext;
	}

}
