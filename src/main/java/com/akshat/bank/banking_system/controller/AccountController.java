package com.akshat.bank.banking_system.controller;

import com.akshat.bank.banking_system.model.Account;
import com.akshat.bank.banking_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.*;

record TransactionRequest(double amount) {}

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount() {
        return ResponseEntity.ok(accountService.createAccount());
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransactionRequest req) {
        return ResponseEntity.ok(accountService.deposit(req.amount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody TransactionRequest req) {
        return ResponseEntity.ok(accountService.withdraw(req.amount()));
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        return ResponseEntity.ok(accountService.getBalance());
    }

}
