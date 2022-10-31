package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {
    Optional<BankAccount> findById(Integer id);
    Optional<BankAccount> findByUserId(Integer id);
}
