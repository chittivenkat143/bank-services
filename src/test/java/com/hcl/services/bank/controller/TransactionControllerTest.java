package com.hcl.services.bank.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.domain.dto.TransactionDto;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionRequestDateDto;
import com.hcl.services.bank.domain.dto.TransactionResponseDTO;
import com.hcl.services.bank.exception.InsufficientBalanceException;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.service.impl.TransactionService;

@ExtendWith({MockitoExtension.class})
class TransactionControllerTest {
	
	@Mock
	private TransactionService service;
	
	@Mock
	private BindingResult errors;
	
	@InjectMocks
	private TransactionController controller;
	
	TransactionRequestDTO transactionRequestDTO;
	TransactionResponseDTO transactionResponseDTO;
	TransactionRequestDateDto transactionRequestDateDto;
	
	List<Transaction> transcations = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		transactionRequestDTO = new TransactionRequestDTO();
		transactionRequestDTO.setAccountNumberDebit("565546443633");
		transactionRequestDTO.setAccountNumberCredit("565546443699");
		transactionRequestDTO.setTransactionAmount(1000.0d);
		
		transactionResponseDTO = new TransactionResponseDTO();
		transactionResponseDTO.setAmount(1000.0d);
		transactionResponseDTO.setTxnNumber("c2e1bf41-1469-4ceb-8026-3a177729bba0");
		transactionResponseDTO.setTxnState("COMPLETED");
		
		Transaction transactionDebit = new Transaction();
		Account accountD = new Account();
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
		
		Transaction transactionCredit = new Transaction();
		Account accountC = new Account();
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

	@Test
	@DisplayName("Save Or Update Transaction: Positive")
	final void testSaveOrUpdateTransaction() {
		when(service.buildTransaction(transactionRequestDTO)).thenReturn(transactionResponseDTO);
		BaseResponse baseResponse = controller.saveOrUpdateTransaction(transactionRequestDTO, errors);
		TransactionResponseDTO txnResponseDTO = (TransactionResponseDTO) baseResponse.getResponse();
		assertAll("saveTxn", ()->assertEquals(1000.0, txnResponseDTO.getAmount()),
				()->assertEquals("c2e1bf41-1469-4ceb-8026-3a177729bba0", txnResponseDTO.getTxnNumber()),
				()->assertEquals("COMPLETED", txnResponseDTO.getTxnState()));
	}
	

	@Test
	@DisplayName("Save Or Update Transaction: Negative")
	final void testSaveOrUpdateTransaction_NC() {
		when(service.buildTransaction(transactionRequestDTO)).thenReturn(null);
		BaseResponse baseResponse = controller.saveOrUpdateTransaction(transactionRequestDTO, errors);
		String message = (String) baseResponse.getResponse();
		assertEquals("Transaction got failed", message);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, baseResponse.getStatus());
	}
	
	@Test
	@DisplayName("Insufficient Balance Exception : Negative")
	final void testSaveOrUpdateTransaction_InsufficientBalance_NC() {
		when(service.buildTransaction(transactionRequestDTO)).thenThrow(new InsufficientBalanceException("Insufficient balance for the transaction"));
		assertThrows(InsufficientBalanceException.class, ()->service.buildTransaction(transactionRequestDTO));
	}
	
	@Test
	@DisplayName("Account not found : Negative")
	final void testSaveOrUpdateTransaction_Exce_NC() {
		when(service.buildTransaction(transactionRequestDTO)).thenThrow(new ResourceNotFoundException("Account not Found"));
		assertThrows(ResourceNotFoundException.class, ()->service.buildTransaction(transactionRequestDTO));
	}

	@Test
	@DisplayName("Get Transaction By Number: Positive")
	final void testGetTransactionByNumber() {
		when(service.getTransactionByTransactionNumber(anyString())).thenReturn(transcations);
		BaseResponse baseResponse = controller.getTransactionByNumber("c2e1bf41-1469-4ceb-8026-3a177729bba0");
		List<Transaction> txns = (List<Transaction>) baseResponse.getResponse();
		assertEquals(2, txns.size());
	}
	
	@Test
	@DisplayName("Get Transaction By Number: Negative")
	final void testGetTransactionByNumber_NC() {
		when(service.getTransactionByTransactionNumber(anyString())).thenReturn(Collections.emptyList());
		BaseResponse baseResponse = controller.getTransactionByNumber("c2e1bf41-1469-4ceb-8026-5a177729bba0");
		String message = (String) baseResponse.getResponse();
		assertEquals("No Transaction Found", message);
	}

	@Test
	@DisplayName("Get Transaction By AccountId: Positive")
	final void testGetTransactionByAccountId() {
		when(service.getTransactionByAccountId(anyLong())).thenReturn(transcations);
		BaseResponse baseResponse = controller.getTransactionByAccountId(100001l);
		List<Transaction> txns = (List<Transaction>) baseResponse.getResponse();
		assertEquals(2, txns.size());
	}
	
	@Test
	@DisplayName("Get Transaction By AccountId: Negative")
	final void testGetTransactionByAccountId_NC() {
		when(service.getTransactionByAccountId(anyLong())).thenReturn(Collections.emptyList());
		BaseResponse baseResponse = controller.getTransactionByAccountId(100001l);
		String message = (String) baseResponse.getResponse();
		assertEquals("No Transaction Found", message);
	}

	@Test
	@DisplayName("Get Transactions Between Dates: Positive")
	final void testGetTransactionsBetweenDates() {
		transactionRequestDateDto = new TransactionRequestDateDto();
		List<TransactionDto> transactionDtos = new ArrayList<>();
		TransactionDto tDto = new TransactionDto(100001l, "c2e1bf41-1469-4ceb-8026-3a177729bba0", null, 1000.0, Transaction.TxnType.CREDIT, new Account(), "", Transaction.State.COMPLETE);
		TransactionDto tDto1 = new TransactionDto(100011l, "c2e1bf41-1469-4ceb-8026-3ffs888df4s8d", null, 1000.0, Transaction.TxnType.DEBIT, new Account(), "", Transaction.State.COMPLETE);
		TransactionDto tDto2 = new TransactionDto(100012l, "c2e1bf44-1469-5ceb-8026-3ffs888df5ghd", null, 1000.0, Transaction.TxnType.CREDIT, new Account(), "", Transaction.State.COMPLETE);
		transactionDtos.add(tDto);
		transactionDtos.add(tDto1);
		transactionDtos.add(tDto2);
				
		when(service.getTransactionsBetweenDates(transactionRequestDateDto)).thenReturn(transactionDtos);
		BaseResponse baseResponse = controller.getTransactionsBetweenDates(transactionRequestDateDto, errors);
		List<TransactionDto> transactionDtosRe = (List<TransactionDto>) baseResponse.getResponse();
		assertEquals(3, transactionDtosRe.size());
	}
	
	@Test
	@DisplayName("Get Transactions Between Dates: Negative")
	final void testGetTransactionsBetweenDates_NC() {
		transactionRequestDateDto = new TransactionRequestDateDto();
		List<TransactionDto> transactionDtos = new ArrayList<>();
		when(service.getTransactionsBetweenDates(transactionRequestDateDto)).thenReturn(transactionDtos);
		BaseResponse baseResponse = controller.getTransactionsBetweenDates(transactionRequestDateDto, errors);
		String message = (String) baseResponse.getResponse();
		assertEquals("No Transaction found between selected dates", message);
	}

}
