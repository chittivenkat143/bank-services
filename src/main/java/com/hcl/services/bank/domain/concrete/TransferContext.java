package com.hcl.services.bank.domain.concrete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.Transaction.State;
import com.hcl.services.bank.repo.AccountRepository;
import com.hcl.services.bank.repo.TransactionRepository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class TransferContext {
	private TransferState currentState;
	private boolean hasIssues;

	private Transaction transaction;
	private Account accountDebit;
	private Account accountCredit;
	private String txnNumber;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountRepository accountRepo;

	public TransferContext(TransferState transferState, Transaction transaction, Account accountDebit,
			Account accountCredit, String txnNumber) {
		this.currentState = transferState;
		if (currentState == null) {
			currentState = InitiateState.getInstance();
		}
		this.transaction = transaction;
		this.accountDebit = accountDebit;
		this.accountCredit = accountCredit;
		this.txnNumber = txnNumber;
	}

	public TransferContext updateState(boolean hasIssues) {
		this.hasIssues = hasIssues;
		currentState.updateState(this, hasIssues, transaction, accountDebit, accountCredit, txnNumber);
		switch (transaction.getTransactionstate()) {
		case INITIATE:
			changeTxnState(hasIssues ? State.FAILED : State.INITIATE);
			break;
		case PROGRESS:
			changeTxnState(hasIssues ? State.FAILED : State.PROGRESS);
			break;
		case TRANSFERRING:
			changeTxnState(hasIssues ? State.FAILED : State.TRANSFERRING);
			break;
		case COMPLETE:
			changeTxnState(hasIssues ? State.FAILED : State.COMPLETE);
			break;
		default:
			break;
		}
		return this;
	}

	private void changeTxnState(State state) {
		transaction.setTransactionstate(state);
		transactionRepo.save(transaction);
	}
}
