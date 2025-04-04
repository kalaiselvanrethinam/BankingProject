package com.example.BankingSystem.repository;

import com.example.BankingSystem.dto.AccountDto;
import com.example.BankingSystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId")
    List<Account> findAccountsByCustomerId(@Param("customerId") Long customerId);
    List<Account> findAccountByCustomerId(Long customerId);
}
