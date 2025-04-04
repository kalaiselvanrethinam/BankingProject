package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.dto.AccountDto;
import com.example.BankingSystem.dto.CustomerDto;
import com.example.BankingSystem.entity.Account;
import com.example.BankingSystem.entity.Customer;
import com.example.BankingSystem.mapper.AccountMapper;
import com.example.BankingSystem.mapper.CustomerMapper;
import com.example.BankingSystem.repository.AccountRepository;
import com.example.BankingSystem.repository.CustomerRepository;
import com.example.BankingSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Object> createCustomer(CustomerDto customerDto) {
        if (customerDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of("Invalid request body")));
        }

        List<String> missingFields = validateCustomerFields(customerDto);

        if (!missingFields.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", missingFields));
        }

        // Validate email format
        if (!isValidEmail(customerDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of("Invalid email format")));
        }

        if (customerRepository.existsByEmail(customerDto.getEmail()) && customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of("Email or phone number already exists")));
        }

        if (customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of("Phone number already exists")));
        }
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", List.of("Email already exists")));
        }

        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Customer created successfully", "customerId", savedCustomer.getId()));
    }

    private List<String> validateCustomerFields(CustomerDto customerDto) {
        List<String> missingFields = new ArrayList<>();

        if (isEmpty(customerDto.getFirstName())) missingFields.add("First name is required");
        if (isEmpty(customerDto.getLastName())) missingFields.add("Last name is required");
        if (isEmpty(customerDto.getEmail())) missingFields.add("Email is required");
        if (isEmpty(customerDto.getPhoneNumber())) missingFields.add("Phone Number is required");
        if (isEmpty(customerDto.getAddress())) missingFields.add("Address is required");

        return missingFields;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


//    @Override
//    public ResponseEntity<Object> createCustomer(CustomerDto customerDto) {
//        if (customerDto == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Invalid request body"));
//        }
//
//        List<String> missingFields = new ArrayList<>();
//
//        if (customerDto.getFirstName() == null || customerDto.getFirstName().isEmpty()) {
//            missingFields.add("First name is required");
//        }
//        if (customerDto.getLastName() == null || customerDto.getLastName().isEmpty()) {
//            missingFields.add("Last name is required");
//        }
//        if (customerDto.getEmail() == null || customerDto.getEmail().isEmpty()) {
//            missingFields.add("Email is required");
//        }
//        if (customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().isEmpty()) {
//            missingFields.add("Phone Number is required");
//        }
//        if (customerDto.getAddress() == null || customerDto.getAddress().isEmpty()) {
//            missingFields.add("Address is required");
//        }
//
//        // If any required fields are missing, return JSON response with all missing fields
//        if (!missingFields.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", missingFields));
//        }
//
//        // Validate email format
//        if (!customerDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Invalid email format"));
//        }
//        if (customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Phone number already exists"));
//        }
//
//        // Check if email already exists
//        if (customerRepository.existsByEmail(customerDto.getEmail())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Email already exists"));
//        }
//
//        // Save customer
//        Customer customer = CustomerMapper.mapToCustomer(customerDto);
//        Customer savedCustomer = customerRepository.save(customer);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(Map.of("message", "Customer created successfully", "customerId", savedCustomer.getId()));
//    }


//    @Override
//    public ResponseEntity<Object> createCustomer(CustomerDto customerDto) {
//
//        List<String> missingFields = new ArrayList<>();
//
//        if (customerDto == null ||  (customerDto.getFirstName() == null &&
//                    customerDto.getLastName() == null &&
//                    customerDto.getEmail() == null &&
//                    customerDto.getPhoneNumber() == null &&
//                    customerDto.getAddress() == null)){
//                throw new IllegalArgumentException("Invalid request body");
//            }
//        if (customerDto.getFirstName() == null || customerDto.getFirstName().isEmpty()){
//            missingFields.add("First name is required");
//        }
//        if (customerDto.getLastName() == null || customerDto.getLastName().isEmpty()) {
//            missingFields.add("Last name is required");
//        }
//        if (customerDto.getEmail() == null || customerDto.getEmail().isEmpty()) {
//            missingFields.add("Email is required");
//        }
//        if (customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().isEmpty()) {
//            missingFields.add("Phone Number is required");
//        }
//        if (customerDto.getAddress() == null || customerDto.getAddress().isEmpty()) {
//            missingFields.add("Address is required");
//        }
//
//        // If any required fields are missing, throw error with all missing fields
//        if (!missingFields.isEmpty()) {
//            throw new IllegalArgumentException(String.join("error", missingFields));
//        }
////            if (customerDto.getLastName() == null &&
////                    customerDto.getEmail() == null &&
////                    customerDto.getPhoneNumber() == null &&
////                    customerDto.getAddress() == null){
////                throw new IllegalArgumentException("Last name is required, Email is required, Phone Number is required, Address is required");
////            }
////            if (customerDto.getEmail() == null &&
////                    customerDto.getPhoneNumber() == null &&
////                    customerDto.getAddress() == null){
////                throw new IllegalArgumentException("Email, Phone Number and Address is required");
////            }
////            if (customerDto.getPhoneNumber() == null &&
////                    customerDto.getAddress() == null){
////                throw new IllegalArgumentException("Phone Number and Address is required");
////            }
//            if (customerDto.getFirstName() == null || customerDto.getFirstName().isEmpty()) {
//                throw new IllegalArgumentException("First name is required");
//            }
////            if (customerDto.getLastName() == null || customerDto.getLastName().isEmpty()) {
////                throw new IllegalArgumentException("Last name is required");
////            }
////            if (customerDto.getEmail()==null || customerDto.getEmail().isEmpty()){
////                throw new IllegalArgumentException("Email is required");
////            }
//            if (!customerDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//                throw new IllegalArgumentException("Invalid email format");
//            }
//            if (customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().isEmpty()) {
//                throw new IllegalArgumentException("Phone Number is required");
//            }
//            if (customerDto.getAddress() == null || customerDto.getAddress().isEmpty()) {
//                throw new IllegalArgumentException("Address is required");
//            }
//            if (customerRepository.existsByEmail(customerDto.getEmail())) {
//                throw new IllegalArgumentException("Email already exists: " + customerDto.getEmail());
//            }
//
//            Customer customer = CustomerMapper.mapToCustomer(customerDto);
//            Customer savedCustomer = customerRepository.save(customer);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(Map.of("message", "Customer created successfully", "customerId", savedCustomer.getId()));
//    }
//public ResponseEntity<Object> createCustomer(CustomerDto customerDto) {
//    List<String> missingFields = new ArrayList<>();
//
//    // Check if the request body is completely empty
//    if (customerDto == null ||
//            (customerDto.getFirstName() == null &&
//                    customerDto.getLastName() == null &&
//                    customerDto.getEmail() == null &&
//                    customerDto.getPhoneNumber() == null &&
//                    customerDto.getAddress() == null)) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body");
//    }
//
//    // Collect missing fields
//    if (customerDto.getLastName() == null || customerDto.getLastName().isEmpty()) {
//        missingFields.add("Last name is required");
//    }
//    if (customerDto.getEmail() == null || customerDto.getEmail().isEmpty()) {
//        missingFields.add("Email is required");
//    }
//    if (customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().isEmpty()) {
//        missingFields.add("Phone Number is required");
//    }
//    if (customerDto.getAddress() == null || customerDto.getAddress().isEmpty()) {
//        missingFields.add("Address is required");
//    }
//
//    // If any required fields are missing, throw error with all missing fields
//    if (!missingFields.isEmpty()) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", missingFields));
//    }
//
//    // Email validation
//    if (!customerDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
//    }
//
//    // Check for duplicate email
//    if (customerRepository.existsByEmail(customerDto.getEmail())) {
//        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + customerDto.getEmail());
//    }
//
//    // Save the customer
//    Customer customer = CustomerMapper.mapToCustomer(customerDto);
//    Customer savedCustomer = customerRepository.save(customer);
//
//    return ResponseEntity.status(HttpStatus.CREATED)
//            .body(Map.of("message", "Customer created successfully", "customerId", savedCustomer.getId()));
//}


    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        if (allCustomers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return allCustomers.stream().map(CustomerMapper :: mapToCustomerDto).toList();
    }

//    @Override
//    public ResponseEntity<?> getCustomerByEmail(String email) {
//        if (email == null || email.trim().isEmpty()) {
//            throw new IllegalArgumentException("EmailId is required");
//        }
//        try {
//            Customer customer = customerRepository.findByEmail(email)
//                    .orElseThrow(() -> new IllegalArgumentException("Customer not found or Invalid email"));
//
////            CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer);
//            CustomerDto customerDto = new CustomerDto();
//            customerDto.setId(customer.getId());
//            customerDto.setFirstName(customer.getFirstName());
//            customerDto.setLastName(customer.getLastName());
//            customerDto.setEmail(customer.getEmail());
//            customerDto.setPhoneNumber(customer.getPhoneNumber());
//            customerDto.setAddress(customer.getAddress());
//            return ResponseEntity.ok(customer);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching customer details.");
//        }
//    }

public ResponseEntity<?> getCustomerByEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
        throw new IllegalArgumentException("EmailId is required");
    }
    try {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found or Invalid email"));

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setAddress(customer.getAddress());

        return ResponseEntity.ok(customerDto);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while fetching customer details.");
    }
}

    @Override
    public ResponseEntity<?> getCustomerWithAccounts(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID provided.");
        }

        try {
            // Fetch customer, return 404 if not found
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found."));

            // Fetch associated accounts
            List<Account> accounts = accountRepository.findAccountByCustomerId(customerId);

            // Convert accounts to DTOs
            List<AccountDto> accountDtos = accounts.stream()
                    .map(AccountMapper::mapToAccountDto)
                    .toList();

            // Create CustomerDto
            CustomerDto customerDto = new CustomerDto(
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getAddress(),
                    accountDtos.isEmpty() ? null : accountDtos  // Set null if no accounts
            );

            return ok(customerDto);

        } catch (IllegalArgumentException e) {
            return ok().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching customer details.");
        }
    }


    @Override
    public void deleteCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<Account> accounts = accountRepository.findAccountByCustomerId(customerId);
        if (accounts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accounts not found");
        }else {
            accountRepository.deleteAll(accounts);
            customerRepository.delete(customer);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeCustomerPhoneNumber(String email, String phoneNumber) {
        Map<String, Object> response = new LinkedHashMap<>();
        if ((email == null) && (phoneNumber==null)){
            response.put("error", "Email and PhoneNumber is required");
            return ResponseEntity.badRequest().body(response);
        }
        if (email == null){
            response.put("error", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }
        if (phoneNumber == null){
            response.put("error", "PhoneNumber is required");
            return ResponseEntity.badRequest().body(response);
        }
        if (!phoneNumber.matches("\\d{10}")) {
            response.put("error", "Phone number must be exactly 10 digits and contain no spaces or letters.");
            return ResponseEntity.badRequest().body(response);
        }
        if (customerRepository.existsByPhoneNumber(phoneNumber)){
            response.put("error", "PhoneNumber is already exist");
            return ResponseEntity.badRequest().body(response);
        }
        Customer customer = customerRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Customer details not found or Invalid email"));
        String oldNumber = customer.getPhoneNumber();
        customer.setPhoneNumber(phoneNumber);
        Customer updatedCustomer = customerRepository.save(customer);
        String updatedPhoneNumber = updatedCustomer.getPhoneNumber();
        response.put("message", "PhoneNumber changed successfully");
        response.put("existing phoneNumber", oldNumber);
        response.put("updated phoneNumber", updatedPhoneNumber);
        return ResponseEntity.ok(response);
    }
}
