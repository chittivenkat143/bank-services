package com.hcl.services.bank.domain;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactionId")
	private Long transactionId;

	@Column(name = "transactionNumber")
	private String transactionNumber;// unique for two records

	@CreationTimestamp
	@Column(name = "transactionOn", updatable = false)
	private LocalDateTime transactionOn;
	
	@UpdateTimestamp
	@Column(name = "transactionUpdatedOn")
	private LocalDateTime transactionUpdatedOn;

	@Column(name = "transactionAmount")
	private Double transactionAmount;

	@Column(name = "transactionType")
	@Enumerated(EnumType.ORDINAL)
	private TxnType transactionType;

	@JoinColumn(name = "transactionAccountId")
	@ManyToOne
	@JsonManagedReference
	private Account transactionAccountId;// Sender or Receiver Account Id

	@Column(name = "transactionAccountNumber")
	private String transactionAccountNumber;// Sender or Receiver Account Id

	@Column(name = "transactionstate")
	@Enumerated(EnumType.STRING)
	private State transactionstate;

	public enum State {
		INITIATE(101), PROGRESS(102), TRANSFERRING(103), FAILED(104), COMPLETE(105);

		private final int value;

		private State(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum TxnType {
		CREDIT(0), DEBIT(1);

		private final int value;

		TxnType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
