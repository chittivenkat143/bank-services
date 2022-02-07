package com.hcl.services.bank.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionRequestDateDto;
import com.hcl.services.bank.domain.dto.TransactionResponseDTO;
import com.hcl.services.bank.repo.AccountRepository;
import com.hcl.services.bank.repo.TransactionRepository;
import com.hcl.services.bank.utils.MapperHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TransactionService.class, UUID.class})

class TransactionServiceTest {
	
	private MapperHelper mapper;
	
	TransactionRequestDTO transactionRequestDTO;
	TransactionResponseDTO transactionResponseDTO;
	TransactionRequestDateDto transactionRequestDateDto;
	List<Transaction> transcations = new ArrayList<>();
	
	Transaction transactionDebit;
	Transaction transactionCredit;
	Account accountD;
	Account accountC;
	
	AccountRepository accountR;
	TransactionRepository transactionR;
	
	TransactionService spyService;
	
	@BeforeEach
	void setUp() throws Exception {
		mapper = PowerMockito.mock(MapperHelper.class);
		transactionR = PowerMockito.mock(TransactionRepository.class);
		accountR = PowerMockito.mock(AccountRepository.class);
		
		spyService = PowerMockito.spy(new TransactionService(transactionR, accountR, mapper));
		
		transactionRequestDTO = new TransactionRequestDTO();
		transactionRequestDTO.setAccountNumberDebit("565546443633");
		transactionRequestDTO.setAccountNumberCredit("565546443699");
		transactionRequestDTO.setTransactionAmount(1000.0d);
		
		transactionResponseDTO = new TransactionResponseDTO();
		transactionResponseDTO.setAmount(1000.0d);
		transactionResponseDTO.setTxnNumber("c2e1bf41-1469-4ceb-8026-3a177729bba0");
		transactionResponseDTO.setTxnState("COMPLETED");
		
		transactionDebit = new Transaction();
		accountD = new Account();
		accountD.setAccountId(100001l);
		accountD.setAccountBalance(10000.0d);
		accountD.setAccountNumber("565546443699");
		transactionDebit.setTransactionAccountId(accountD);
		transactionDebit.setTransactionAccountNumber("565546443699");
		transactionDebit.setTransactionAmount(10000.0d);
		transactionDebit.setTransactionId(1000002l);
		transactionDebit.setTransactionNumber("c2e1bf41-1469-4ceb-8026-3a177729bba0");
		transactionDebit.setTransactionOn(null);
		transactionDebit.setTransactionstate(Transaction.State.COMPLETE);
		transactionDebit.setTransactionType(Transaction.TxnType.DEBIT);
		
		transactionCredit = new Transaction();
		accountC = new Account();
		accountC.setAccountId(100002l);
		accountC.setAccountBalance(10000.0d);
		accountC.setAccountNumber("565546443633");
		transactionCredit.setTransactionAccountId(accountC);
		transactionCredit.setTransactionAccountNumber("565546443633");
		transactionCredit.setTransactionAmount(10000.0d);
		transactionCredit.setTransactionId(1000001l);
		transactionCredit.setTransactionNumber("c2e1bf41-1469-4ceb-8026-3a177729bba0");
		transactionCredit.setTransactionOn(null);
		transactionCredit.setTransactionstate(Transaction.State.COMPLETE);
		transactionCredit.setTransactionType(Transaction.TxnType.CREDIT);
		
		transcations.add(transactionDebit);
		transcations.add(transactionCredit);
	}

	
	/**
	 * https://www.learnbestcoding.com/post/21/unit-test-private-methods-and-classes
	 * @throws Exception
	 */
	@Test
	@Disabled
	final void testBuildTransaction() throws Exception {
		PowerMockito.mockStatic(UUID.class);		
		when(accountR.findByAccountNumber("565546443633")).thenReturn(Optional.of(accountD)).thenReturn(Optional.of(accountC));
		doReturn(transactionResponseDTO).when(spyService).buildTransaction(transactionRequestDTO);
		String strUUID = Whitebox.invokeMethod(UUID.randomUUID()).toString();
		
		PowerMockito.when(spyService, "isBothAccountsAreActive", Optional.of(accountD), Optional.of(accountC)).thenReturn(true);
		PowerMockito.doReturn(true).when(spyService, "validateCreditLimit", transactionRequestDTO, Optional.of(accountD));
		PowerMockito.doReturn(Optional.of(transactionDebit)).when(spyService, "getTransactionByAccountAndTxnNumber", Optional.of(accountD), strUUID);
		
		
		//Boolean isBothAccountsAreActive = Whitebox.invokeMethod(spyService, "isBothAccountsAreActive", Optional.of(accountD), Optional.of(accountC));
		//Boolean isValidateCreditLimit = Whitebox.invokeMethod(spyService, "validateCreditLimit", transactionRequestDTO, Optional.of(accountD));
		
//		PowerMockito.doAnswer(i -> {
//			Transaction txn = i.getArgument(0);
//			txn.setTransactionId(1000001l);
//			txn.setTransactionNumber(strUUID);
//			txn.setTransactionAccountNumber(accountD.getAccountNumber());
//			return Optional.of(txn);
//		}).doAnswer(i -> {
//			Transaction txn = i.getArgument(0);
//			txn.setTransactionId(1000002l);
//			txn.setTransactionNumber(strUUID);
//			txn.setTransactionAccountNumber(accountC.getAccountNumber());
//			return Optional.of(txn);
//		}).when(transactionR).findByTransactionNumberAndTransactionAccountNumber(strUUID, Optional.of(accountD).get().getAccountNumber());
		
		//Optional<Transaction> txnDebitOpt = Whitebox.invokeMethod(spyService, "getTransactionByAccountAndTxnNumber", Optional.of(accountD), strUUID);
		//<Transaction> txnCreditOpt = Whitebox.invokeMethod(spyService, "getTransactionByAccountAndTxnNumber", Optional.of(accountC), strUUID);
		//assertNotNull(txnDebitOpt);
		//assertNotNull(txnCreditOpt);
		//assertEquals(txnDebitOpt.get().getTransactionNumber(), strUUID);
		//assertEquals(txnCreditOpt.get().getTransactionNumber(), strUUID);
		
		assertNotNull(strUUID);
		//assertTrue(isBothAccountsAreActive);
		//assertTrue(isValidateCreditLimit);
		
		TransactionResponseDTO responseDto = spyService.buildTransaction(transactionRequestDTO);
		assertEquals(1000.0, responseDto.getAmount());
	}

	@Test
	final void testCreditToAccount() {
		
	}

	@Test
	final void testDebitFromAccount() {
		
	}

	@Test
	final void testGetTransactionByAccountId() {
		
	}

	@Test
	final void testGetTransactionByTransactionNumber() {
		
	}

	@Test
	final void testGetTransactionsBetweenDates() {
		
	}

}
