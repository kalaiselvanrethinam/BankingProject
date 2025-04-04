package com.example.BankingSystem.controller;

import com.example.BankingSystem.dto.AccountDto;
import com.example.BankingSystem.entity.Account;
import com.example.BankingSystem.service.AccountService;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("createAccount/{customerId}")
    public ResponseEntity<?> createAccount(@PathVariable(required = false) Long customerId, @RequestBody AccountDto accountDto){
        try {
            AccountDto createdAccount = accountService.createAccount(customerId, accountDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);

        }catch (IllegalArgumentException e){
            return ResponseEntity.ok(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing to createTheAccount.");
        }
    }

    @GetMapping("getAllAccounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/getAccountsByCustomerId/{customerId}")
    public ResponseEntity<?> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            return accountService.getAccountsByCustomerId(customerId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while processing getAccountsByCustomerId."));
        }
    }


    @PutMapping("deposit/{customerId}/{accountNumber}")
    public ResponseEntity<?>deposit(@PathVariable(required = false) Long customerId, @PathVariable(required = false) String accountNumber, @RequestParam(required = false) Double amount){
        try {
            return accountService.deposit(customerId, accountNumber, amount);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error ","An error occurred while processing the deposit."));
        }

    }

    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) Double amount) {
        try {
            return accountService.withdraw(customerId, accountNumber, amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error ",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred while processing the withdrawal."));
        }
    }

    @DeleteMapping("deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestParam(required = false) Long customerId, @RequestParam(required = false) String accountNumber){
        try {
            accountService.deleteAccount(customerId, accountNumber);
            return ResponseEntity.ok("Account deleted successfully for account number: " + accountNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the account.");
        }
    }

}
