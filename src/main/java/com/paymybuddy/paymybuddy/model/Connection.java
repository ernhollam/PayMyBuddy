package com.paymybuddy.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "connection_id")
	private Long connectionId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Long initializerId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Long receiverId;

	private LocalDateTime startingDate;
}
