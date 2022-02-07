package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

public class TransferringState implements TransferState {

	private static TransferringState transferringState;

	public static TransferringState getInstance() {
		if (transferringState == null) {
			transferringState = new TransferringState();
			return transferringState;
		}
		return transferringState;
	}

	private TransferringState() {
	}

	@Override
	public TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber) {
		transferContext.setHasIssues(hasIssues);
		System.out.println(!hasIssues ? "Transferring Money" : "Transferring Money Failed");
		transferContext.setCurrentState(hasIssues ? IssueState.getInstance() : CompleteState.getInstance());
		return transferContext;
	}
}
