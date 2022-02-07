package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

public class InitiateState implements TransferState {

	private static InitiateState initiateState;

	public static InitiateState getInstance() {
		if (initiateState == null) {
			initiateState = new InitiateState();
			return initiateState;
		}
		return initiateState;
	}

	private InitiateState() {
	}

	@Override
	public TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber) {
		transferContext.setHasIssues(hasIssues);
		System.out.println(!hasIssues ? "Initiating Transfer Money" : "Initiating Transfer Money Failed");
		transferContext.setCurrentState(hasIssues ? IssueState.getInstance() : ProgressState.getInstance());
		return transferContext;
	}
}
