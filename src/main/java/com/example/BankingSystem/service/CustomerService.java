package com.example.BankingSystem.service;

import com.example.BankingSystem.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    ResponseEntity<Object>createCustomer(CustomerDto customerDto);
    List<CustomerDto> getAllCustomers();
    ResponseEntity<?> getCustomerByEmail(String email);
    ResponseEntity<?> getCustomerWithAccounts(Long customerId);
    void deleteCustomerById(Long customerId);
    ResponseEntity<Map<String, Object>> changeCustomerPhoneNumber(String email, String phoneNumber);
}
