package com.commission.billing.repository;

import com.commission.billing.model.Billing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BillingRepository extends CrudRepository<Billing, UUID> {

}