package com.akshat.bank.banking_system.controller;

import com.akshat.bank.banking_system.model.*;
import com.akshat.bank.banking_system.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/accounts")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    // Create account
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request, Principal principal) {
        try {
            Account account = bankingService.createAccount(request.getAccountNumber(), principal);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody AmountRequest request, Principal principal) {
        try {
            Account account = bankingService.deposit(request.getAccountNumber(), request.getAmount(), principal);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody AmountRequest request, Principal principal) {
        try {
            Account account = bankingService.withdraw(request.getAccountNumber(), request.getAmount(), principal);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Transfer
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, Principal principal) {
        try {
            bankingService.transfer(request.getFromAccountNumber(), request.getToAccountNumber(), request.getAmount(), principal);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
