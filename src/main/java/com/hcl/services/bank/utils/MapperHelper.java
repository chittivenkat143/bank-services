package com.hcl.services.bank.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.AccountDto;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;
import com.hcl.services.bank.domain.dto.CustomerDto;
import com.hcl.services.bank.domain.dto.CustomerRequestDTO;
import com.hcl.services.bank.domain.dto.TransactionDto;
import com.hcl.services.bank.domain.dto.TransactionRequestDTO;

@Component
public class MapperHelper {
	private static MapperHelper instance;

	@Autowired
	private ModelMapper mapper;

	public static MapperHelper getInstance() {
		if (instance == null) {
			instance = new MapperHelper();
		}
		return instance;
	}

	public Customer toCustomerEntity(CustomerRequestDTO customerDto) {
		Customer customer = new Customer();
		mapper.map(customerDto, customer);
		return customer;
	}

	public CustomerRequestDTO toCustomerRequestDto(Customer customer) {
		CustomerRequestDTO customerDto = new CustomerRequestDTO();
		mapper.map(customer, customerDto);
		return customerDto;
	}

	public Account toAccountEntity(AccountRequestDTO accountDto) {
		Account account = new Account();
		mapper.map(accountDto, account);
		return account;
	}

	public AccountDto toAccountDto(Account account) {
		AccountDto accountDto = new AccountDto();
		mapper.map(account, accountDto);
		return accountDto;
	}

	public CustomerDto toCustomerDto(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		mapper.map(customer, customerDto);
		return customerDto;
	}

	public TransactionDto toTransactionDto(Transaction transaction) {
		TransactionDto transactionDto = null;//new TransactionDto();
		mapper.map(transaction, transactionDto);
		return transactionDto;
	}

	public AccountRequestDTO toAccountRequestDto(Account account) {
		AccountRequestDTO accountDto = new AccountRequestDTO();
		mapper.map(account, accountDto);
		return accountDto;
	}

	public Transaction toTransactionEntity(TransactionRequestDTO transactionDto) {
		Transaction transaction = new Transaction();
		mapper.map(transactionDto, transaction);
		return transaction;
	}

	public TransactionRequestDTO toTransactionRequestDto(Transaction transaction) {
		TransactionRequestDTO transactionDto = new TransactionRequestDTO();
		mapper.map(transaction, transactionDto);
		return transactionDto;
	}

}
