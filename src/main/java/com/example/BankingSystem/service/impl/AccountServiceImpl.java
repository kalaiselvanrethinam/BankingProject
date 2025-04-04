package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.dto.AccountDto;
import com.example.BankingSystem.entity.Account;
import com.example.BankingSystem.entity.Customer;
import com.example.BankingSystem.mapper.AccountMapper;
import com.example.BankingSystem.repository.AccountRepository;
import com.example.BankingSystem.repository.CustomerRepository;
import com.example.BankingSystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public AccountDto createAccount(Long customerId, AccountDto accountDto) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Valid customerId is required and should be greater than 0.");
        }
        if (accountDto.getAccountType() == null || accountDto.getBalance() == null) {
            throw new IllegalArgumentException("Account type and balance are required.");
        }

    // Find customer by ID
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with customerId " + customerId));

    // Generate 10-digit account number
        String accountNumber = generateAccountNumber();

    // Convert DTO to Entity (Account)
        Account account = new Account(
                accountNumber,
                accountDto.getAccountType(),
                accountDto.getBalance(),
                "Active", // default status
                customer
        );

        // Save the account entity
        Account savedAccount = accountRepository.save(account);

        // Convert Entity back to DTO
        return new AccountDto(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                customerId
        );
}

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }



    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((AccountMapper :: mapToAccountDto)).toList();
    }

    public ResponseEntity<Map<String, Object>> getAccountsByCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Valid customerId is required and should be greater than 0.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with customerId " + customerId));

        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        List<Map<String, Object>> accountsDto = accounts.stream()
                .map(account -> {
                    Map<String, Object> accountMap = new HashMap<>();
                    accountMap.put("accountNumber", account.getAccountNumber());
                    accountMap.put("accountType", account.getAccountType());
                    accountMap.put("balance", account.getBalance());
                    accountMap.put("status", account.getStatus());
                    accountMap.put("customerId", account.getCustomer().getId());
                    return accountMap;
                })
                .toList();
        String customerName = customer.getFirstName();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customerId", customerId);
        response.put("customerName", customerName);
        response.put("accounts", accountsDto.isEmpty() ? "No accounts found" : accountsDto);

        return ResponseEntity.ok(response);
    }



    @Override
    public ResponseEntity<Map<String, Object>> deposit(Long customerId, String accountNumber, Double amount) {
        if ((customerId == null && accountNumber==null || accountNumber.isEmpty()) && amount == null){
            throw new IllegalArgumentException("CustomerId, Account number and deposit amount is required");
        }
        if (accountNumber==null || accountNumber.isEmpty()){
            throw new IllegalArgumentException("Account number should be required");
        }
        if (customerId==null){
            throw new IllegalArgumentException("Invalid customerId or amount");
        }
        if (customerId<=0){
            throw new IllegalArgumentException("CustomerId should be greater than 0");
        }
        if (amount == null){
            throw new IllegalArgumentException("Deposit amount is required");
        }
        if (amount<=0){
            throw new IllegalArgumentException("amount should be greater 0");
        }
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        if (accounts.isEmpty()){
            throw new IllegalArgumentException("No accounts found for customerId "+ customerId);
        }
        Account account = accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst().orElseThrow(() -> new IllegalArgumentException("Account not found with accountNumber " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Account savedAccount = accountRepository.save(account);
        Double currentBalance = savedAccount.getBalance();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Amount successfully deposited.");
        response.put("currentBalance", currentBalance);
        response.put("accountNumber", savedAccount.getAccountNumber());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> withdraw(Long customerId, String accountNumber, Double amount) {
        if (customerId==null&&accountNumber==null&&amount==null){
            throw new IllegalArgumentException("customerId, accountNumber and withdrawal amount is required");
        }
        if (customerId==null){
            throw new IllegalArgumentException("CustomerId is required");
        }
        if (customerId<=0){
            throw new IllegalArgumentException("CustomerId is should be greater than 0");
        }
        if (accountNumber==null){
            throw new IllegalArgumentException("accountNumber is required");
        }
        if (amount == null){
            throw new IllegalArgumentException("Withdrawal amount is required");
        }
        if (amount<=0){
            throw new IllegalArgumentException("amount should be greater 0");
        }
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        if (accounts.isEmpty()){
            throw new IllegalArgumentException("No accounts found for customerId " + customerId);
        }
        Account account = accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst().orElseThrow(() -> new IllegalArgumentException("Account number is not found or mismatch"));
        if (account.getBalance() < amount){
            throw new IllegalArgumentException("Withdraw amount is greater than your current balance");
        }
        account.setBalance(account.getBalance() - amount);
        Account savedAccount = accountRepository.save(account);
        Double currentBalance = savedAccount.getBalance();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Amount successfully withdraw.");
        response.put("currentBalance", currentBalance);
        response.put("accountNumber", savedAccount.getAccountNumber());
        return ResponseEntity.ok(response);
    }

    @Override
    public void deleteAccount(Long customerId, String accountNumber) {
        if ((accountNumber == null || accountNumber.isEmpty()) && customerId == null){
            throw new IllegalArgumentException("Both Account number and CustomerId is required");
        }
        if (accountNumber == null || accountNumber.isEmpty()){
            throw new IllegalArgumentException("Account number is required");
        }
        if (customerId==null){
            throw new IllegalArgumentException("CustomerId is required");
        }
        if( customerId <=0){
            throw new IllegalArgumentException("CustomerId should be greater than 0");
        }
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        if (accounts.isEmpty()){
            throw new IllegalArgumentException("No accounts found for customer ID: " + customerId);
        }
        Account account = accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst().orElseThrow(() -> new IllegalArgumentException("Account number is mismatch or invalid"));
        accountRepository.delete(account);
    }
}
