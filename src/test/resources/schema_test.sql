DROP DATABASE IF EXISTS db_paymybuddy_test ;

CREATE DATABASE db_paymybuddy_test;
USE db_paymybuddy_test;

CREATE TABLE user (
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    balance DECIMAL(5 , 2 )
);

CREATE TABLE connection (
    connection_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_initializer_id INT NOT NULL,
    fk_receiver_id INT NOT NULL,
    starting_date DATETIME NOT NULL,
    FOREIGN KEY (fk_initializer_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (fk_receiver_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE transaction (
    transaction_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_issuer_id INT NOT NULL,
    fk_payee_id INT NOT NULL,
    date DATETIME NOT NULL,
    amount DECIMAL(5 , 2 ) NOT NULL,
    description VARCHAR(200),
    FOREIGN KEY (fk_issuer_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (fk_payee_id)
        REFERENCES user (user_id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);