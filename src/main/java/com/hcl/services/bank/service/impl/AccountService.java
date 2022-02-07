package com.hcl.services.bank.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.AccountType;
import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.AccountDto;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.repo.AccountRepository;
import com.hcl.services.bank.repo.AccountTypeRepository;
import com.hcl.services.bank.repo.CustomerRepository;
import com.hcl.services.bank.service.IAccountService;
import com.hcl.services.bank.utils.MapperHelper;

@Service
public class AccountService implements IAccountService {
	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountTypeRepository repositoryAT;
	
	@Autowired
	private MapperHelper mapper;
	
	@Autowired
	private Faker faker;

	@Override
	public Account saveOrUpdateAccount(AccountRequestDTO accountRequestDTO) {
		logger.info("AS:saveOrUpdateAccount");
		Optional<Customer> customer =  customerRepository.findById(Long.valueOf(accountRequestDTO.getAccountCustomerId()));
		if(customer.isPresent()) {
			logger.info("AS:saveOrUpdateAccount: Customer is Present");
			accountRequestDTO.setAccountNumber(faker.number().digits(12));
			Account account = mapper.toAccountEntity(accountRequestDTO);
			Optional<AccountType> accountTypeOpt = repositoryAT.findById(Long.valueOf(accountRequestDTO.getAccountCode()));
			if(accountTypeOpt.isPresent()) {
				logger.info("AS:saveOrUpdateAccount: AccountType is Present");
				account.setAccountType(accountTypeOpt.get());
			}
			account.setAccountCustomerId(customer.get());
			return repository.save(account);
		}else {
			throw new ResourceNotFoundException("Customer details not found for Id:" + accountRequestDTO.getAccountCustomerId());
		} 
	}

	@Override
	public Account getAccountById(Long accountId) {
		logger.info("AS:getAccountById:" + accountId);
		return repository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
	}

	@Override
	public Account getAccountByAccountNumber(String accountNumber) {
		logger.info("AS:getAccountByAccountNumber:" + accountNumber);
		return repository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
	}

	@Override
	public List<Account> getAccountsByAccountType(Long accountType) {
		logger.info("AS:getAccountsByAccountCode:" + accountType);
		Optional<AccountType> accountTypeOpt = repositoryAT.findById(accountType);
		if(accountTypeOpt.isPresent()) {
			return repository.findByAccountType(accountTypeOpt.get());
		}else {
			throw new ResourceNotFoundException("Accounts not found by type "+accountType);
		}
	}
	
	@Override
	public AccountDto getAccountDtoById(Long accountId) {
		return null;//repository.findByAccountId(accountId);
	}

}
