package com.example.BankingSystem.mapper;

import com.example.BankingSystem.dto.AccountDto;
import com.example.BankingSystem.entity.Account;
import com.example.BankingSystem.entity.Customer;

public class AccountMapper {
    public static Account mapToAccount(AccountDto accountDto, Customer customer){
        if (accountDto == null) {
            throw new IllegalArgumentException("AccountDto cannot be null");
        }
        return new Account(
                accountDto.getAccountNumber(),
                accountDto.getAccountType(),
                accountDto.getBalance(),
                accountDto.getStatus(),
                customer
        );
    }

    public static AccountDto mapToAccountDto(Account account){
        if (account== null) {
            throw new IllegalArgumentException("AccountDto cannot be null");
        }
        Long customerId;
        if (account.getCustomer() != null) {
            customerId = account.getCustomer().getId();
        } else {
            customerId = null;
        }

        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                customerId
        );
    }
}
