package com.akshat.bank.banking_system.service;

import com.akshat.bank.banking_system.model.*;
import com.akshat.bank.banking_system.repository.AccountRepository;
import com.akshat.bank.banking_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public BankingService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // Create Account for logged in user
    @Transactional
    public Account createAccount(String accountNumber, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new RuntimeException("Account number already exists");
        }

        Account account = new Account(accountNumber, user);
        return accountRepository.save(account);
    }

    // Deposit money
    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount, Principal principal) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        Account account = getAccountIfOwnedByUser(accountNumber, principal);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    // Withdraw money
    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount, Principal principal) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdraw amount must be positive");
        }

        Account account = getAccountIfOwnedByUser(accountNumber, principal);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    // Transfer money from one account to another
    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, Principal principal) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        Account fromAccount = getAccountIfOwnedByUser(fromAccountNumber, principal);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private Account getAccountIfOwnedByUser(String accountNumber, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not own this account");
        }
        return account;
    }
}
