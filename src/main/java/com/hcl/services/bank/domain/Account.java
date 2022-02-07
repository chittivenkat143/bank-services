package com.hcl.services.bank.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountId")
	private Long accountId;

	@Column(name = "accountNumber", unique = true)
	private String accountNumber;

	@CreationTimestamp
	@Column(name = "accountCreatedOn", updatable = false)
	private LocalDateTime accountCreatedOn;
	
	@UpdateTimestamp
	@Column(name = "accountUpdatedOn")
	private LocalDateTime accountUpdatedOn;

	@ManyToOne
	@JoinColumn(name = "accountType")
	@JsonManagedReference
	private AccountType accountType;

	@ManyToOne
	@JoinColumn(name = "accountCustomerId")
	@JsonManagedReference
	private Customer accountCustomerId;

	@Column(name = "accountBalance")
	private Double accountBalance;

	@OneToMany(mappedBy = "transactionAccountId")
	@JsonBackReference
	private List<Transaction> transactions;

}
