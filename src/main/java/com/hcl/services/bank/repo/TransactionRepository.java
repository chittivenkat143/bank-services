package com.hcl.services.bank.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.hcl.services.bank.domain.Transaction;
import com.hcl.services.bank.domain.dto.TransactionDto;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findAllByTransactionAccountId(Long accountId);

	List<Transaction> findAllByTransactionNumber(String transactionNumber);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	Optional<Transaction> findByTransactionNumberAndTransactionAccountNumber(String transactionNumber, String accountNumber);

	//@Query("SELECT new com.hcl.services.bank.domain.dto.TransactionDto(t.transactionId, t.transactionNumber, t.transactionOn, t.transactionAmount, t.transactionType, t.transactionAccountId, t.transactionAccountNumber, t.transactionstate) FROM Transaction t WHERE t.transactionAccountNumber= :transactionAccountNumber AND t.transactionOn BETWEEN :fromDate AND :toDate")
	List<TransactionDto> findAllByTransactionAccountNumberAndTransactionOnBetween(String transactionAccountNumber, Date fromDate, Date toDate);
}
