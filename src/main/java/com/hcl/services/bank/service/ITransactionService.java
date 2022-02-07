package com.hcl.services.bank.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.TransactionDto;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionRequestDateDto;
import com.hcl.services.bank.domain.dto.TransactionResponseDTO;

public interface ITransactionService {
	public List<Transaction> getTransactionByAccountId(Long accountId);

	public List<Transaction> getTransactionByTransactionNumber(String transactionNumber);
	
	public TransactionResponseDTO buildTransaction(TransactionRequestDTO transactionDto);

	public List<TransactionDto> getTransactionsBetweenDates(TransactionRequestDateDto transactionDto);
}
