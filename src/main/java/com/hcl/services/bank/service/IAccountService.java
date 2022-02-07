package com.hcl.services.bank.service;

import java.util.List;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.dto.AccountDto;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;

public interface IAccountService {
	public Account saveOrUpdateAccount(AccountRequestDTO accountRequestDTO);

	public Account getAccountById(Long accountId);

	public Account getAccountByAccountNumber(String accountNumber);

	public List<Account> getAccountsByAccountType(Long accountType);

	public AccountDto getAccountDtoById(Long accountId);
}
