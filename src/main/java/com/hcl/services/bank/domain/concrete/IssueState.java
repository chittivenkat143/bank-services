package com.hcl.services.bank.domain.concrete;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;

public class IssueState implements TransferState {

	private static IssueState issueState;

	public static IssueState getInstance() {
		if (issueState == null) {
			issueState = new IssueState();
			return issueState;
		}
		return issueState;
	}

	private IssueState() {
	}

	@Override
	public TransferContext updateState(TransferContext transferContext, boolean hasIssues, Transaction transaction,
			Account debitAcc, Account creditAcc, String txnNumber) {
		System.out.println("Issue on Transfer Money");
		return transferContext;
	}
}
