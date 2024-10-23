package com.ds.quackbooks.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.quackbooks.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}