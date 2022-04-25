package com.commission.billing.controllers;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.commission.billing.model.Billing;
import com.commission.billing.model.Customer;
import com.commission.billing.model.EmailDetails;
import com.commission.billing.repository.CustomerRepository;
import com.commission.billing.service.BillingService;
import com.commission.billing.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {
    @Autowired
    BillingService billingService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
	EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);


    @RequestMapping(value = "/new", 
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        try {
            final Span span = GlobalTracer.get().activeSpan();
            if (span != null) {
                span.setTag("order_id", billing.getOrderId());
            }
            Optional<Customer> customer = customerRepository.findById(billing.getCustomerId());
            if(!customer.isPresent()) {
                logger.error("No customer with ID - {}", billing.getCustomerId());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Billing newBilling = billingService.createBill(billing);
            logger.info("New bill created with BillingID - {}", newBilling.getId());
            EmailDetails emailDetails = emailService.createEmailDetails(newBilling, customer.get());
            emailService.send(emailDetails);
            return new ResponseEntity<>(newBilling, HttpStatus.OK);
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