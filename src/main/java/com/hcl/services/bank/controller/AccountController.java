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

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.dto.AccountDto;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.exception.BaseException;
import com.hcl.services.bank.service.IAccountService;
import com.hcl.services.bank.utils.AppUtils;
import com.hcl.services.bank.utils.MapperHelper;

@RestController
@RequestMapping("/bank/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private IAccountService accountService;

	@PostMapping("/account")
	public BaseResponse saveOrUpdateAccount(@Valid @RequestBody AccountRequestDTO accountDto, BindingResult errors) {
		logger.info("AC:saveOrUpdateAccount:\t");
		if (errors.hasErrors()) {
			logger.error("AC:saveOrUpdateAccount:\t" + AppUtils.getInstance().getBindingResultToStrings(errors));
			return new BaseResponse(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Account account = accountService.saveOrUpdateAccount(accountDto);
		return new BaseResponse(account, HttpStatus.CREATED);
	}

	@GetMapping("/id/{accountId}")
	public BaseResponse getAccountById(@PathVariable("accountId") Long accountId) {
		logger.info("AC:getAccountById:\t" + accountId);
		Account account = accountService.getAccountById(accountId);
		return new BaseResponse(account, HttpStatus.OK);
	}

	@GetMapping("/number/{accountNumber}")
	public BaseResponse getAccountByNumber(@PathVariable("accountNumber") String accountNumber) {
		logger.info("AC:getAccountByNumber:\t" + accountNumber);
		Account account = accountService.getAccountByAccountNumber(accountNumber);
		return new BaseResponse(account, HttpStatus.OK);
	}

	@GetMapping("/type/{accountType}")
	public BaseResponse getAccountsByAccountType(@PathVariable("accountType") Long accountType) {
		logger.info("AC:getAccountsByAccountType:\t" + accountType);
		List<Account> accounts = accountService.getAccountsByAccountType(accountType);
		return new BaseResponse(accounts, HttpStatus.OK);
	}
	
	@GetMapping("/dto/{accountId}")
	public BaseResponse getAccountDtoById(@PathVariable Long accountId) {
		logger.info("AC:getAccountDtoById:\t" + accountId);
		//AccountDto accountDto = accountService.getAccountDtoById(accountId);
		return new BaseResponse("No Code", HttpStatus.OK);
	}
	
}
