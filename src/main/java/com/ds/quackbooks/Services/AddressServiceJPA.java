package com.ds.quackbooks.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.models.Address;
import com.ds.quackbooks.models.User;
import com.ds.quackbooks.payload.AddressDTO;
import com.ds.quackbooks.repositories.AddressRepository;
import com.ds.quackbooks.repositories.UserRepository;

@Service
public class AddressServiceJPA implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();

        List<AddressDTO> addressesDTO = addresses.stream()
            .map(address -> modelMapper.map(address, AddressDTO.class))
            .toList();

        return addressesDTO;
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesByUser(User user) {
        List<Address> addresses = user.getAddresses();

        return addresses.stream()
            .map(address -> modelMapper.map(address, AddressDTO.class))
            .toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressDb = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        addressDb.setZipCode(addressDTO.getZipCode());
        addressDb.setState(addressDTO.getState());
        addressDb.setCity(addressDTO.getCity());
        addressDb.setStreet(addressDTO.getStreet());
        addressDb.setExternalNumber(addressDTO.getExternalNumber());
        addressDb.setInternalNumber(addressDTO.getInternalNumber());
        addressDb.setHouseDescription(addressDTO.getHouseDescription());

        Address updatedAddress = addressRepository.save(addressDb);

        User user = addressDb.getUser();
        user.getAddresses().removeIf(address -> address.getId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);

    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressDb = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        User user = addressDb.getUser();
        user.getAddresses().removeIf(address -> address.getId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressDb);

        return "Address deleted successfully";
    }

}
