DROP DATABASE IF EXISTS db_paymybuddy ;

CREATE DATABASE db_paymybuddy;
USE db_paymybuddy;

CREATE TABLE user (
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.0
);

CREATE TABLE bank_account(
    account_id INT NOT NULL AUTO_INCREMENT,
    fk_user_id INT NOT NULL,
    bank_name VARCHAR(100) DEFAULT NULL,
    iban VARCHAR(34) DEFAULT NULL,
    balance DECIMAL(20,2) NOT NULL DEFAULT 0.0,
    PRIMARY KEY (account_id, fk_user_id),
    FOREIGN KEY (fk_user_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE connection (
    connection_id INT NOT NULL AUTO_INCREMENT,
    fk_initializer_id INT NOT NULL,
    fk_receiver_id INT NOT NULL,
    starting_date DATETIME NOT NULL,
    FOREIGN KEY (fk_initializer_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (fk_receiver_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    PRIMARY KEY (connection_id, fk_initializer_id, fk_receiver_id)
);

CREATE TABLE transaction (
    transaction_id INT NOT NULL AUTO_INCREMENT,
    fk_issuer_id INT NOT NULL,
    fk_payee_id INT NOT NULL,
    date DATETIME NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(140),
    FOREIGN KEY (fk_issuer_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (fk_payee_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    PRIMARY KEY (transaction_id, fk_issuer_id, fk_payee_id)
);
