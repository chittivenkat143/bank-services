package com.hcl.services.bank.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.concrete.TransferContext;
import com.hcl.services.bank.domain.dto.TransactionDto;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionRequestDateDto;
import com.hcl.services.bank.domain.dto.TransactionResponseDTO;
import com.hcl.services.bank.exception.InsufficientBalanceException;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.repo.AccountRepository;
import com.hcl.services.bank.repo.TransactionRepository;
import com.hcl.services.bank.service.ITransactionService;
import com.hcl.services.bank.utils.MapperHelper;

@Service
public class TransactionService implements ITransactionService {
	private static Logger logger = LoggerFactory.getLogger(TransactionService.class);
	

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private MapperHelper mapper;
	
	public TransactionService(TransactionRepository transactionRepo, AccountRepository accountRepo,
			MapperHelper mapper) {
		this.transactionRepo = transactionRepo;
		this.accountRepo = accountRepo;
		this.mapper = mapper;
	}

	@Transactional
	@Override
	public TransactionResponseDTO buildTransaction(TransactionRequestDTO transactionDto) {
		logger.info("TSImpl:buildTransaction");
		TransactionResponseDTO responseDto = new TransactionResponseDTO();
		try {
			Optional<Account> accountDebit = accountRepo.findByAccountNumber(transactionDto.getAccountNumberDebit());
			Optional<Account> accountCredit = accountRepo.findByAccountNumber(transactionDto.getAccountNumberCredit());
			String transactionNumber = UUID.randomUUID().toString();
			
			if (!isBothAccountsAreActive(accountDebit, accountCredit)) {
				logger.error("Both Accounts must available for transaction");
				responseDto.setTxnNumber(transactionNumber);
				responseDto.setTxnState(Transaction.State.FAILED.name());
				throw new ResourceNotFoundException("Account not found");
			}
			
			if (!validateCreditLimit(transactionDto, accountDebit)) {
				logger.error("Amount not avaliable for transaction");
				responseDto.setTxnState(Transaction.State.FAILED.name());
				throw new InsufficientBalanceException("Insufficient balance for the transaction");
			}
			
			logger.info("Both Accounts are available And Transaction state is " + Transaction.State.INITIATE.toString());
			
			responseDto.setTxnNumber(transactionNumber);
			responseDto.setCreditAccount(mapper.toAccountDto(accountCredit.get()));
			responseDto.setDebitAccount(mapper.toAccountDto(accountDebit.get()));
			responseDto.setAmount(transactionDto.getTransactionAmount());
			
			logger.info("Amount avaliable for transaction");

			debitFromAccount(accountDebit.get(), transactionDto.getTransactionAmount(), transactionNumber);
			creditToAccount(accountCredit.get(), transactionDto.getTransactionAmount(), transactionNumber);

			Optional<Transaction> txnDebitOpt = getTransactionByAccountAndTxnNumber(accountDebit, transactionNumber);
			Optional<Transaction> txnCreditOpt = getTransactionByAccountAndTxnNumber(accountCredit, transactionNumber);
			
			//Optional<Transaction> txnDebitOpt = transactionRepo.findByTransactionNumberAndTransactionAccountNumber(transactionNumber, accountDebit.get().getAccountNumber());
			//Optional<Transaction> txnCreditOpt = transactionRepo.findByTransactionNumberAndTransactionAccountNumber(transactionNumber, accountDebit.get().getAccountNumber());
			
			if(txnDebitOpt.isPresent() && txnCreditOpt.isPresent()) {
				Transaction txnDebit = txnDebitOpt.get();
				Transaction txnCredit = txnCreditOpt.get();
				responseDto.setTransactions(Arrays.asList(mapper.toTransactionDto(txnDebit), mapper.toTransactionDto(txnCredit)));
				responseDto.setTxnState(Transaction.State.COMPLETE.name());
				logger.error("TransactionD:" + txnDebit.getTransactionType() +":TxnNumber= "+ txnDebit.getTransactionNumber());
				logger.error("TransactionC:" + txnCredit.getTransactionType() +":TxnNumber= "+ txnCredit.getTransactionNumber());
			}else {
				logger.error("Transaction not found due to techincal issue");
				responseDto.setTxnState(Transaction.State.FAILED.name());
				throw new ResourceNotFoundException("Transaction not found");
			}
			
			
		} catch (Exception e) {
			logger.error("TSImpl:buildTransaction:Exception:\t" + e.getMessage());
			responseDto = null;
		}
		return responseDto;
	}

	private Optional<Transaction> getTransactionByAccountAndTxnNumber(Optional<Account> accountDebit, String transactionNumber) {
		logger.info("TSImpl:getTransactionByAccountAndTxnNumber\t" + transactionNumber +"|" + accountDebit.isPresent());
		return transactionRepo.findByTransactionNumberAndTransactionAccountNumber(transactionNumber, accountDebit.get().getAccountNumber());
	}

	private boolean isBothAccountsAreActive(Optional<Account> accountDebit, Optional<Account> accountCredit) {
		logger.info("TSImpl:isBothAccountsAreActive\t" + accountDebit.isPresent() +"|" + accountCredit.isPresent());
		return accountDebit.isPresent() && accountCredit.isPresent();
	}

	private boolean validateCreditLimit(TransactionRequestDTO transactionDto, Optional<Account> accountDebit) {
		logger.info("TSImpl:validateCreditLimit\t" + transactionDto.toString() +"|" + accountDebit.isPresent());
		return transactionDto.getTransactionAmount() < accountDebit.get().getAccountBalance();
	}

	private Transaction createTransaction(Transaction transaction) {
		logger.info("TS:createTransaction: TxnType" + transaction.getTransactionType() +": TxnNumber="+ transaction.getTransactionNumber());
		return transactionRepo.save(transaction);
	}

	private void creditToAccount(Account creditAcc, Double amount, String txnNumber) {
		logger.info("TS:creditToAccount:" + amount +":"+ txnNumber);
		try {
			creditAcc.setAccountBalance(creditAcc.getAccountBalance() + amount);
			accountRepo.save(creditAcc);
			logger.info("TS:creditToAccount:" + amount +":"+ txnNumber + ":\t Amount Credited");
			Transaction creditTxn = new Transaction();
			creditTxn.setTransactionAccountId(creditAcc);
			creditTxn.setTransactionAccountNumber(creditAcc.getAccountNumber());
			creditTxn.setTransactionAmount(amount);
			creditTxn.setTransactionNumber(txnNumber);
			creditTxn.setTransactionType(Transaction.TxnType.CREDIT);
			creditTxn.setTransactionstate(Transaction.State.COMPLETE);//This should be in State Design Pattern
			createTransaction(creditTxn);
		} catch (Exception e) {
			logger.error("TSImpl:creditToAccount:\t" + e.getMessage());
		}
	}

	private void debitFromAccount(Account debitAcc, Double amount, String txnNumber) {
		logger.info("TS:debitFromAccount:" + amount +":"+ txnNumber);
		try {
			debitAcc.setAccountBalance(debitAcc.getAccountBalance() - amount);
			accountRepo.save(debitAcc);
			logger.info("TS:debitFromAccount:" + amount +":"+ txnNumber + ":\t Amount Debited");
			Transaction debitTxn = new Transaction();
			debitTxn.setTransactionAccountId(debitAcc);
			debitTxn.setTransactionAccountNumber(debitAcc.getAccountNumber());
			debitTxn.setTransactionAmount(amount);
			debitTxn.setTransactionNumber(txnNumber);
			debitTxn.setTransactionType(Transaction.TxnType.DEBIT);
			debitTxn.setTransactionstate(Transaction.State.COMPLETE);//This should be in State Design Pattern
			createTransaction(debitTxn);
		} catch (Exception e) {
			logger.error("TSImpl:debitFromAccount:\t" + e.getMessage());
		}
	}

	@Override
	public List<Transaction> getTransactionByAccountId(Long accountId) {
		logger.info("TS:getTransactionByAccountId:" + accountId);
		return transactionRepo.findAllByTransactionAccountId(accountId);
	}

	@Override
	public List<Transaction> getTransactionByTransactionNumber(String transactionNumber) {
		logger.info("TS:getTransactionByTransactionNumber:" + transactionNumber);
		return transactionRepo.findAllByTransactionNumber(transactionNumber);
	}
	
	@Override
	public List<TransactionDto> getTransactionsBetweenDates(TransactionRequestDateDto transactionDto) {
		logger.info("TS:getTransactionsBetweenDates:\t" + transactionDto.getAccountNumber() +"/"+ transactionDto.getFromDate() +"/" + transactionDto.getToDate());
		return transactionRepo.findAllByTransactionAccountNumberAndTransactionOnBetween(transactionDto.getAccountNumber(), transactionDto.getFromDate(), transactionDto.getToDate());
	}

}
