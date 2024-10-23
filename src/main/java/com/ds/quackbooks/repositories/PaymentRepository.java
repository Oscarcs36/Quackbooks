package com.ds.quackbooks.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.quackbooks.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}