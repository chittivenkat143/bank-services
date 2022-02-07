package com.hcl.services.bank.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerId")
	private Long customerId;

	@Column(name = "customerName")
	private String customerName;

	@Column(name = "customerMobile", unique = true)
	private String customerMobile;

	@Column(name = "customerEmail", unique = true)
	private String customerEmail;

	@Column(name = "customerUsername", unique = true)
	private String customerUsername;

	@Column(name = "customerPassword")
	private String customerPassword;

	@Column(name = "customerAddress")
	private String customerAddress;

	@Column(name = "customerType")
	private Integer customerType;// General(101), Wealth(102)

	@CreationTimestamp
	@Column(name = "customerCreatedOn", updatable = false)
	private LocalDateTime createDateTime;
	
	@UpdateTimestamp
	@Column(name = "customerUpdatedOn")
    private LocalDateTime updateDateTime;

	@Column(name = "customerStatus")
	private Integer customerStatus;// Active(0) , Inactive(1), Closed(2), Hold(3)

	@OneToMany(mappedBy = "accountCustomerId")
	@JsonBackReference
	public List<Account> accounts;

}
