package com.commission.billing.repository;

import java.util.Optional;

import com.commission.billing.model.Customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {

}