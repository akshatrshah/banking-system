package com.akshat.bank.banking_system.service;

import com.akshat.bank.banking_system.model.*;
import com.akshat.bank.banking_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Account createAccount() {
        User user = getCurrentUser();
        if (accountRepo.findByUser(user).isPresent()) {
            throw new RuntimeException("Account already exists");
        }
        Account account = new Account(user); // Make sure your Account class has this constructor
        account.setBalance(BigDecimal.ZERO); // Initial balance
        return accountRepo.save(account);
    }

    public Account deposit(double amount) {
        Account acc = getUserAccount();
        BigDecimal depositAmount = BigDecimal.valueOf(amount);
        acc.setBalance(acc.getBalance().add(depositAmount));
        return accountRepo.save(acc);
    }

    public Account withdraw(double amount) {
        Account acc = getUserAccount();
        BigDecimal withdrawAmount = BigDecimal.valueOf(amount);
        if (acc.getBalance().compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        acc.setBalance(acc.getBalance().subtract(withdrawAmount));
        return accountRepo.save(acc);
    }

    public BigDecimal getBalance() {
        return getUserAccount().getBalance();
    }

    private Account getUserAccount() {
        User user = getCurrentUser();
        return accountRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
