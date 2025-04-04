package com.example.BankingSystem.controller;

import com.example.BankingSystem.dto.CustomerDto;
import com.example.BankingSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @PostMapping("/createCustomer")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto){
        try {
            return customerService.createCustomer(customerDto);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating new customer.");
        }

    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers(){
        List<CustomerDto> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/getCustomerByEmail")
    public ResponseEntity<?> getCustomerByEmail(@RequestParam(required = false) String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("EmailId is required");
        }
        return customerService.getCustomerByEmail(email);
    }



    @DeleteMapping("deleteCustomer/{customerId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long customerId){
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @PutMapping("changeCustomerPhoneNumber")
    public ResponseEntity<?> chaneCustomerPhoneNumber(@RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber){
        try {
            return customerService.changeCustomerPhoneNumber(email, phoneNumber);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}