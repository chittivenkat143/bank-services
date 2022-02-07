package com.hcl.services.bank.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.domain.dto.TransactionDto;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionRequestDateDto;
import com.hcl.services.bank.domain.dto.TransactionResponseDTO;
import com.hcl.services.bank.service.ITransactionService;
import com.hcl.services.bank.utils.AppUtils;

@RestController
@RequestMapping("/bank/transactions")
public class TransactionController {

	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private ITransactionService transactionService;

	@PostMapping("/transaction")
	public BaseResponse saveOrUpdateTransaction(@Valid @RequestBody TransactionRequestDTO transactionDto,
			BindingResult errors) {
		if (errors.hasErrors()) {
			logger.error("TC:/transactions/transaction:\t" + AppUtils.getInstance().getBindingResultToStrings(errors));
			return new BaseResponse(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		logger.info("TC:/transactions/transaction:->TSImpl");
		TransactionResponseDTO responseDTO = transactionService.buildTransaction(transactionDto);
		if(responseDTO==null) {
			logger.info("TC:responseDTO:empty");
			return new BaseResponse("Transaction got failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new BaseResponse(responseDTO, HttpStatus.CREATED);
	}

	@GetMapping("/transaction/{transactionNumber}")
	public BaseResponse getTransactionByNumber(@PathVariable("transactionNumber") String transactionNumber) {
		logger.info("TC:/transactions/{" + transactionNumber +"}:->TSImpl");
		List<Transaction> transactions = transactionService.getTransactionByTransactionNumber(transactionNumber);
		if(transactions!=null && transactions.size()==0) {
			return new BaseResponse("No Transaction Found", HttpStatus.OK);
		}
		return new BaseResponse(transactions, HttpStatus.OK);
	}

	@GetMapping("/accounts/{accountId}")
	public BaseResponse getTransactionByAccountId(@PathVariable("accountId") Long accountId) {
		logger.info("TC:/transactions/{" + accountId +"}:->TSImpl");
		List<Transaction> transactions = transactionService.getTransactionByAccountId(accountId);
		if(transactions!=null && transactions.size()==0) {
			return new BaseResponse("No Transaction Found", HttpStatus.OK);
		}
		return new BaseResponse(transactions, HttpStatus.OK);
	}
	
	@PostMapping("/betweendates")
	public BaseResponse getTransactionsBetweenDates(@Valid @RequestBody TransactionRequestDateDto transactionDto,
			BindingResult errors) {
		logger.info("TC:/transactions/betweendates:->TSImpl");
		if(errors.hasErrors()) {
			logger.error("TC:/transactions/betweendates:\t" + AppUtils.getInstance().getBindingResultToStrings(errors));
			return new BaseResponse(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		logger.info("TC:/transactions/betweendates" + transactionDto.getAccountNumber() +"/"+ transactionDto.getFromDate() +"/" + transactionDto.getToDate());
		List<TransactionDto> transactions = transactionService.getTransactionsBetweenDates(transactionDto);
		if(transactions!=null && transactions.size()==0) {
			return new BaseResponse("No Transaction found between selected dates", HttpStatus.OK);
		}
		return new BaseResponse(transactions, HttpStatus.OK);
	}

}
