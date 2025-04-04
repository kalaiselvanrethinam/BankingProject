package com.example.BankingSystem.mapper;

import com.example.BankingSystem.dto.CustomerDto;
import com.example.BankingSystem.entity.Account;
import com.example.BankingSystem.entity.Customer;

import java.util.List;

public class CustomerMapper {
    public static Customer mapToCustomer(CustomerDto customerDto){
        if(customerDto == null){
            throw new IllegalArgumentException("CustomerDto cannot be empty");
        }
        return new Customer(
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getEmail(),
                customerDto.getPhoneNumber(),
                customerDto.getAddress()
        );
    }

    public static CustomerDto mapToCustomerDto(Customer customer){
        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be empty");
        }
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );

    }
}
