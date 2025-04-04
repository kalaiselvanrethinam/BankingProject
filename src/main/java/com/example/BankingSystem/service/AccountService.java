package com.example.BankingSystem.service;

import com.example.BankingSystem.dto.AccountDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AccountService {
    AccountDto createAccount(Long customerId, AccountDto accountDto);
    List<AccountDto> getAllAccounts();
    ResponseEntity<Map<String, Object>> getAccountsByCustomerId(Long customerId);
    ResponseEntity<Map<String, Object>>deposit(Long customerId, String accountNumber, Double amount);
    ResponseEntity<Map<String, Object>> withdraw(Long customerId, String accountNumber, Double amount);
    void deleteAccount(Long customerId, String accountNumber);
}
