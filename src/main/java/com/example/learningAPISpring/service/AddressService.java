package com.example.learningAPISpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    public Address createAddress(AddressRequest addressRequest, Principal principal){
//        User user= (User) userDetailsService.loadUserByUsername(principal.getName());
//        Address address = Address.builder()
//                .name(addressRequest.getName())
//                .street(addressRequest.getStreet())
//                .city(addressRequest.getCity())
//                .state(addressRequest.getState())
//                .zipCode(addressRequest.getZipCode())
//                .phoneNumber(addressRequest.getPhoneNumber())
//                .user(user)
//                .build();
//        return addressRepository.save(address);
//    }
//
//    public void deleteAddress(UUID id) {
//        addressRepository.deleteById(id);
//    }
}
