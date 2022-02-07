package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

public class ProgressState implements TransferState {

	private static ProgressState progressState;

	public static ProgressState getInstance() {
		if (progressState == null) {
			progressState = new ProgressState();
			return progressState;
		}
		return progressState;
	}

	private ProgressState() {
	}

	@Override
	public TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber) {
		System.out.println(!hasIssues ? "In progress Transfer Money" : "In progress Transfer Money Failed");
		transferContext.setHasIssues(false);
		transferContext.setCurrentState(hasIssues ? IssueState.getInstance() : TransferringState.getInstance());
		return transferContext;
	}
}
