package com.paymybuddy.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	private String email;

	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "balance")
	private BigDecimal balance;

	@OneToMany(mappedBy = "initializerId")
	Set<Connection> initializedConnections;

	@OneToMany(mappedBy = "receiverId")
	Set<Connection> receivedConnections;

	@OneToMany(mappedBy = "issuerId")
	Set<Transaction> initiatedTransactions;

	@OneToMany(mappedBy = "payeeId")
	Set<Transaction> receivedTransactions;
}
