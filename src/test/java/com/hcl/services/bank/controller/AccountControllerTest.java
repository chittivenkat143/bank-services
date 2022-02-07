package com.hcl.services.bank.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.AccountType;
import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.AccountRequestDTO;
import com.hcl.services.bank.domain.dto.BaseResponse;
import com.hcl.services.bank.exception.ResourceNotFoundException;
import com.hcl.services.bank.service.impl.AccountService;
import com.hcl.services.bank.utils.MapperHelper;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Mock
	private AccountService accountService;

	@Mock
	private BindingResult errors;

	@Mock
	private MapperHelper mapper;

	@InjectMocks
	private AccountController accountController;

	private AccountRequestDTO accountRequestDto;
	private Account account;
	private Customer customer;
	private AccountType accountType;

	@BeforeEach
	void setUp() throws Exception {
		accountRequestDto = new AccountRequestDTO();
		accountRequestDto.setAccountBalance(10000d);
		accountRequestDto.setAccountCode(1001);
		accountRequestDto.setAccountCustomerId(10001);
		accountRequestDto.setAccountNumber("565546443699");

		account = new Account();
		account.setAccountBalance(10000.0d);
		account.setAccountNumber("565546443699");
		customer = new Customer();
		customer.setCustomerId(10001l);
		account.setAccountCustomerId(customer);
		accountType = new AccountType();
		accountType.setAccountCode(1001l);
		account.setAccountType(accountType);

	}

	@Test
	@DisplayName("Save Account: Positive")
	final void testSaveAccount_PC() {
		when(accountService.saveOrUpdateAccount(accountRequestDto)).thenAnswer(acc -> {
			Account account = new Account();
			account.setAccountId(100002l);
			account.setAccountNumber("565546443699");
			return account;
		});
		BaseResponse baseResponse = accountController.saveOrUpdateAccount(accountRequestDto, errors);
		Account accountR = (Account) baseResponse.getResponse();
		assertEquals(100002l, accountR.getAccountId());
		assertEquals("565546443699", accountR.getAccountNumber());
	}

	@Test
	@DisplayName("Update Account: Positive")
	final void testUpdateAccount_PC() {
		when(accountService.saveOrUpdateAccount(accountRequestDto)).thenReturn(account);
		BaseResponse baseResponse = accountController.saveOrUpdateAccount(accountRequestDto, errors);
		Account accountR = (Account) baseResponse.getResponse();
		assertEquals("565546443699", accountR.getAccountNumber());
	}

	@Test
	@DisplayName("Save Or Update Account: Error : Negative")
	final void testSaveOrUpdateAccount_Error_NC() {
		when(errors.hasErrors()).thenReturn(true);
		BaseResponse baseResponse = accountController.saveOrUpdateAccount(accountRequestDto, errors);
		assertEquals(HttpStatus.BAD_REQUEST, baseResponse.getStatus());
		verify(errors, times(1)).hasErrors();
		assertTrue(errors.hasErrors());
	}

	@Test
	@DisplayName("Save Or Update Account: Exception : Negative")
	final void testSaveOrUpdateAccount_Exception_NC() {
		when(accountService.saveOrUpdateAccount(accountRequestDto))
				.thenThrow(new RuntimeException("Account Number Already Exists"));
		assertThrows(RuntimeException.class, () -> accountController.saveOrUpdateAccount(accountRequestDto, errors));
	}

	@Test
	@DisplayName("Get Account By Id : Positive")
	final void testGetAccountById_PC() {
		account.setAccountId(100002l);
		when(accountService.getAccountById(anyLong())).thenReturn(account);
		BaseResponse baseResponse = accountController.getAccountById(100002l);
		Account accountR = (Account) baseResponse.getResponse();
		assertEquals(HttpStatus.OK, baseResponse.getStatus());
		assertAll("Account", () -> assertEquals(100002l, accountR.getAccountId()),
				() -> assertEquals(10000.0, accountR.getAccountBalance()),
				() -> assertEquals("565546443699", accountR.getAccountNumber()),
				() -> assertEquals(accountType, accountR.getAccountType()));
	}
	
	@Test
	@DisplayName("Get Account By Id : Negative")
	final void testGetAccountById_NC() {
		when(accountService.getAccountById(anyLong())).thenThrow(new ResourceNotFoundException("Account Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->accountController.getAccountById(100002l));
	}

	@Test
	@DisplayName("Get Account By Number : Positive")
	final void testGetAccountByNumber_PC() {
		account.setAccountId(100002l);
		when(accountService.getAccountByAccountNumber(anyString())).thenReturn(account);
		BaseResponse baseResponse = accountController.getAccountByNumber("565546443699");
		Account accountR = (Account) baseResponse.getResponse();
		assertEquals(HttpStatus.OK, baseResponse.getStatus());
		assertAll("Account", () -> assertEquals(100002l, accountR.getAccountId()),
				() -> assertEquals(10000.0, accountR.getAccountBalance()),
				() -> assertEquals("565546443699", accountR.getAccountNumber()),
				() -> assertEquals(accountType, accountR.getAccountType()));
	}
	
	@Test
	@DisplayName("Get Account By Number : Negative")
	final void testGetAccountByNumber_NC() {
		when(accountService.getAccountByAccountNumber(anyString())).thenThrow(new ResourceNotFoundException("Account Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->accountController.getAccountByNumber("565546443699"));
	}

	@Test
	@DisplayName("Get Accounts By AccountType : Positive")
	final void testGetAccountsByAccountType_PC() {
		account.setAccountId(100002l);
		when(accountService.getAccountsByAccountType(anyLong())).thenReturn(Arrays.asList(account));
		BaseResponse baseResponse = accountController.getAccountsByAccountType(1001l);
		@SuppressWarnings("unchecked")
		List<Account> accountR = (List<Account>) baseResponse.getResponse();
		assertEquals(HttpStatus.OK, baseResponse.getStatus());
		assertEquals(1, accountR.size());
	}
	
	@Test
	@DisplayName("Get Accounts By AccountType : Negative")
	final void testGetAccountsByAccountType_NC() {
		when(accountService.getAccountsByAccountType(anyLong())).thenThrow(new ResourceNotFoundException("Account Not Found"));
		assertThrows(ResourceNotFoundException.class, ()->accountController.getAccountsByAccountType(1001l));
	}

	@Test
	@DisplayName("Get Accunt DTO By Account Id: Positive")
	final void testGetAccountDtoById() {
		//when(accountService.getAccountDtoById(anyLong())).thenReturn(null);
		BaseResponse baseR = accountController.getAccountDtoById(100002l);
		assertEquals(HttpStatus.OK, baseR.getStatus());
	}

}
