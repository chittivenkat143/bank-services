package com.hcl.services.bank.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction.State;
import com.hcl.services.bank.domain.Transaction.TxnType;

public class TransactionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long transactionId;
	private String transactionNumber;
	private Date transactionOn;
	private Double transactionAmount;
	private TxnType transactionType;
	private Account transactionAccountId;
	private String transactionAccountNumber;
	private State transactionstate;
	
	public TransactionDto(Long transactionId, String transactionNumber, Date transactionOn, Double transactionAmount,
			TxnType transactionType, Account transactionAccountId, String transactionAccountNumber,
			State transactionstate) {
		this.transactionId = transactionId;
		this.transactionNumber = transactionNumber;
		this.transactionOn = transactionOn;
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.transactionAccountId = transactionAccountId;
		this.transactionAccountNumber = transactionAccountNumber;
		this.transactionstate = transactionstate;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Date getTransactionOn() {
		return transactionOn;
	}

	public void setTransactionOn(Date transactionOn) {
		this.transactionOn = transactionOn;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public TxnType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TxnType transactionType) {
		this.transactionType = transactionType;
	}

	public Account getTransactionAccountId() {
		return transactionAccountId;
	}

	public void setTransactionAccountId(Account transactionAccountId) {
		this.transactionAccountId = transactionAccountId;
	}

	public String getTransactionAccountNumber() {
		return transactionAccountNumber;
	}

	public void setTransactionAccountNumber(String transactionAccountNumber) {
		this.transactionAccountNumber = transactionAccountNumber;
	}

	public State getTransactionstate() {
		return transactionstate;
	}

	public void setTransactionstate(State transactionstate) {
		this.transactionstate = transactionstate;
	}
	
}
