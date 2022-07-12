package com.paymybuddy.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private int transactionId;

	/*@OneToOne(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			orphanRemoval = true
	)
	@JoinColumn(name = "fk_issuer_id")
	private int issuerId;

	@OneToOne(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			orphanRemoval = true
	)
	@JoinColumn(name = "fk_payee_id")
	private int payeeId;*/

	private LocalDateTime date;

	private BigDecimal amount;

	private String description;
}
