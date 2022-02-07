package com.hcl.services.bank.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account_type")
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountCode")
	private Long accountCode;

	@Column(name = "accountName", unique = true)
	private String accountName;

	@Column(name = "accountDescription")
	private String accountDescription;
	
	@OneToMany(mappedBy = "accountType")
	@JsonBackReference
	private List<Account> accounts;
}
