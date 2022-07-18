package com.paymybuddy.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.mapping.ToOne;

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
	private Long transactionId;

	@Column(name = "fk_issuer_id")
	private Long issuerId;

	@Column(name = "fk_payee_id")
	private Long payeeId;

	private LocalDateTime date;

	private BigDecimal amount;

	private String description;
}
