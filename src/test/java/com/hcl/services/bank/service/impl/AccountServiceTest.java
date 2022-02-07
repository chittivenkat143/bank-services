package com.hcl.services.bank.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.AccountType;
import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.repo.AccountRepository;
import com.hcl.services.bank.repo.AccountTypeRepository;
import com.hcl.services.bank.repo.CustomerRepository;
import com.hcl.services.bank.utils.MapperHelper;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	
	@Mock
	private AccountRepository repository;
	
	@Mock
	private AccountTypeRepository repositoryAT;
	
	@Mock
	private CustomerRepository repositoryCus;
	
	@Mock
	private MapperHelper mapper;
	
	@InjectMocks
	private AccountService service;
	
	List<Account> accounts = new ArrayList<>();
	
	private AccountRequestDTO accountRequestDto;
	private Account account;
	private Customer customer;
	private AccountType accountType;
	
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	Faker faker;// = Mockito.mock(Faker.class, RETURNS_DEEP_STUBS);
	
	@Mock
	Number number;
	
	@BeforeEach
	void setUp() throws Exception {
		accountRequestDto = new AccountRequestDTO();
		accountRequestDto.setAccountBalance(10000d);
		accountRequestDto.setAccountCode(1001);
		accountRequestDto.setAccountCustomerId(10001);
		accountRequestDto.setAccountNumber("5655464436");

		account = new Account();
		account.setAccountBalance(10000.0d);
		account.setAccountNumber("5655464436");
		customer = new Customer();
		customer.setCustomerId(10001l);
		account.setAccountCustomerId(customer);
		accountType = new AccountType();
		accountType.setAccountCode(1001l);
		account.setAccountType(accountType);
		
		accounts.add(account);		
	}
	
	@Test
	@DisplayName("Save Account: Positive")
	final void testSaveAccount_PC() {
			
		when(repositoryCus.findById(10001l)).thenReturn(Optional.of(customer));
		when(repositoryAT.findById(1001l)).thenReturn(Optional.of(accountType));
		when(mapper.toAccountEntity(accountRequestDto)).thenReturn(account);
		when(faker.number().digits(12)).thenReturn(String.valueOf("475512388322"));
		String accountNumber = faker.number().digits(12);
		when(repository.save(account)).thenAnswer(c->{
			Account account = new Account();
			account.setAccountId(100010l);
			account.setAccountNumber(accountNumber);
			return account;
		});
		
		Account account = service.saveOrUpdateAccount(accountRequestDto);
		assertAll("account", ()->assertEquals(100010l, account.getAccountId()),
				()->assertEquals(accountNumber, account.getAccountNumber()));
	}
	
	@Test
	@DisplayName("Update Account: Positive")
	final void testUpdateAccount_PC() {
		account.setAccountId(100010l);
		account.setAccountBalance(250000.0d);
		when(repositoryCus.findById(10001l)).thenReturn(Optional.of(customer));
		when(repositoryAT.findById(1001l)).thenReturn(Optional.of(accountType));
		when(mapper.toAccountEntity(accountRequestDto)).thenReturn(account);
		when(repository.save(account)).thenReturn(account);
		Account acct = service.saveOrUpdateAccount(accountRequestDto);
		assertAll("accountUpdate", ()->assertEquals(100010l, acct.getAccountId()),
				()->assertEquals(250000.0, acct.getAccountBalance()));
	}

	@Test
	@DisplayName("Get Account By Id: Positive")
	final void testGetAccountById_PC() {
		account.setAccountId(100010l);
		when(repository.findById(100010l)).thenReturn(Optional.of(account));
		Account acct = service.getAccountById(100010l);
		assertAll("accountGet", ()->assertEquals(100010l, acct.getAccountId()),
				()->assertEquals(10000.0, acct.getAccountBalance()));
	}
	
	@Test
	@DisplayName("Get Account By Id: Negative")
	final void testGetAccountById_NC() {
		account.setAccountId(100010l);
		when(repository.findById(100011l)).thenThrow(new ResourceNotFoundException("Account Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->service.getAccountById(100011l));
	}

	@Test
	@DisplayName("Get Account By AccountNumber: Positive")
	final void testGetAccountByAccountNumber_PC() {
		account.setAccountId(100010l);
		when(repository.findByAccountNumber(anyString())).thenReturn(Optional.of(account));
		Account acct = service.getAccountByAccountNumber("565546443699");
		assertAll("accountGetNum", ()->assertEquals(100010l, acct.getAccountId()),
				()->assertEquals(10000.0, acct.getAccountBalance()));
	}
	  
	@Test
	@DisplayName("Get Account By AccountNumber: Negative")
	final void testGetAccountByAccountNumber_NC() {
		when(repository.findByAccountNumber(anyString())).thenThrow(new ResourceNotFoundException("Account Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->service.getAccountByAccountNumber("565546443699"));
	}

	@Test
	@DisplayName("Get Accounts By AccountType: Positive")
	final void testGetAccountsByAccountType_PC() {
		when(repositoryAT.findById(1001l)).thenReturn(Optional.of(accountType));
		when(repository.findByAccountType(accountType)).thenReturn(accounts);
		
		List<Account> accR = service.getAccountsByAccountType(1001l);
		assertEquals(1, accR.size());		
	}
	
	@Test
	@DisplayName("Get Accounts By AccountType: Negative")
	final void testGetAccountsByAccountType_NC() {
		when(repositoryAT.findById(1001l)).thenThrow(new ResourceNotFoundException("AccountType Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->service.getAccountsByAccountType(1001l));
	}

	@Disabled
	@Test
	@DisplayName("Get Account For DTO : Positive")
	final void testGetAccountDtoById_PC() {
		System.out.println("AST: testGetAccountDtoById, Nothing to test");
	}

}
