USE `db_paymybuddy`;

INSERT INTO `user` (`email`, `password`, `firstname`, `lastname`, `balance`) VALUES
	('security@mail.com', '$2a$10$vpDkNfBtWg.ebbkL8VwaG.BrmlIlqRCd0RqoyOIb6hgRZRMfJ51xa', 'Security', 'User', 0.00),
	('hayley@mymail.com', '$2a$10$1NDocQWD9pl52dv/cY7mmOuCYbIVTzCd6ahb5EUDQxwkDMkg1Q54y', 'Hayley', 'James', 10.00),
	('clara@mail.com', '$2a$10$41nUyaddehEi9Slu/4kFWeedO3YrLnGCu5nZqYySX3CH7uyHMrclu', 'Clara', 'Tarazi', 133.56),
	('smith@mail.com', '$2a$10$3TU.lRztZJgEueboxsP2b.AV6TeBsKK.qyyCYGYJXKeozeahFVTuu', 'Smith', 'Sam', 8.00),
	('lambda@mail.com', '$2a$10$prOZuMO22K.itqO3CKrEGuVf2KUxdWOB9fGQh8DvWHPHWIiiR6iZy', 'Lambda', 'User', 96.91);
	
INSERT INTO `connection` (`fk_initializer_id`, `fk_receiver_id`, `starting_date`) VALUES
	(1, 2, '2022-10-24 17:37:33'),
	(1, 3, '2022-10-24 17:37:41'),
	(3, 4, '2022-10-24 17:38:01'),
	(3, 5, '2022-10-24 17:38:08'),
	(5, 2, '2022-10-24 17:38:29'),
	(5, 4, '2022-10-24 17:38:39');

INSERT INTO `transaction` (`fk_issuer_id`, `fk_payee_id`, `date`, `amount`, `description`) VALUES
	(5, 4, '2022-10-24 17:39:55', 8.00, 'Movie tickets'),
	(3, 5, '2022-10-24 17:41:03', 25.00, 'Trip money'),
	(5, 2, '2022-10-24 17:41:40', 10.00, 'Restaurant bill share');

