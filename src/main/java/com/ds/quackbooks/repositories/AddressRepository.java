package com.ds.quackbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.quackbooks.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
