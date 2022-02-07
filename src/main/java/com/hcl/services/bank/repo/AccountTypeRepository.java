package com.hcl.services.bank.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.services.bank.domain.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long>{

	Optional<AccountType> findByAccountCode(Long valueOf);
	
}
