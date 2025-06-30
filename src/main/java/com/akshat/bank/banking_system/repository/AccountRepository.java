package com.akshat.bank.banking_system.repository;

import com.akshat.bank.banking_system.model.Account;
import com.akshat.bank.banking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUser(User user);
}
