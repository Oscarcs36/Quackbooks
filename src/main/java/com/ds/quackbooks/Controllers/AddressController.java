package com.ds.quackbooks.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.quackbooks.models.User;
import com.ds.quackbooks.payload.AddressDTO;
import com.ds.quackbooks.services.AddressService;
import com.ds.quackbooks.util.AuthUtil;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<?> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);

        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses() {
        List<AddressDTO> addressDTOs = addressService.getAddresses();

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<?> getAddressesById(@PathVariable Long addressId) {
        AddressDTO addressDTOs = addressService.getAddressesById(addressId);

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<?> getAddressesByUser() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getAddressesByUser(user);

        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);

        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    
}
