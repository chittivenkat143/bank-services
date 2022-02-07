package com.hcl.services.bank.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.services.bank.domain.Account;
import com.hcl.services.bank.domain.AccountType;
import com.hcl.services.bank.domain.dto.AccountDto;
import com.hcl.services.bank.domain.dto.projection.AccountView;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(String accountNumber);

	List<Account> findByAccountType(AccountType accountType);
	
	//AccountDto findByAccountId(Long accountId);

	AccountView findByAccountIdAndAccountNumber(Long accountId, String accountNumber);
}
