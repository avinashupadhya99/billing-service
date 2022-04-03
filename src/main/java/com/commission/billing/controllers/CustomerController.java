package com.commission.billing.controllers;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.commission.billing.model.Customer;
import com.commission.billing.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @RequestMapping(value = "/new", 
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            Customer newCustomer = customerRepository.save(customer);
            logger.info("New customer created with CustomerID - {}", newCustomer.getId());
            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch(ConstraintViolationException ex) {
            logger.error("Constraint Validation error {} ", ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            logger.error("Internal error {} ", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}