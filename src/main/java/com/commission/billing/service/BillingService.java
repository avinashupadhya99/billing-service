package com.commission.billing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.commission.billing.model.Billing;
import com.commission.billing.repository.BillingRepository;

import java.util.Random;

@Component
public class BillingService {
    @Autowired
    BillingRepository billingRepository;

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    public Billing createBill(Billing billing) {
        Random random = new Random();
        long price = Long.valueOf(random.nextInt(10000));
        billing.setAmount(billing.getQuantity()*price);
        return billingRepository.save(billing); 
    }
}