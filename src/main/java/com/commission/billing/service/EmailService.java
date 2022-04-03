package com.commission.billing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.commission.billing.model.Billing;
import com.commission.billing.model.Customer;
import com.commission.billing.model.EmailDetails;

@Service
public class EmailService {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${javainuse.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;	
	String kafkaTopic = "email_details_topic";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
    public EmailDetails createEmailDetails(Billing billing, Customer customer) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setCustomerId(customer.getId());
        emailDetails.setEmail(customer.getEmail());
        emailDetails.setName(customer.getName());
        emailDetails.setOrderId(billing.getOrderId());
        emailDetails.setProduct(billing.getProduct());
        emailDetails.setQuantity(billing.getQuantity());
        emailDetails.setAmount(billing.getAmount());

        return emailDetails;
    }

	public void send(EmailDetails emailDetails) {
		amqpTemplate.convertAndSend(exchange, routingkey, emailDetails);
		logger.info("Sending message to topic - {} for orderId - {}", kafkaTopic, emailDetails.getOrderId());
	}
}
