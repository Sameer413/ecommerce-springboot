package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.reponse.AddressResponse;
import com.example.ecommerce.dto.request.AddressRequest;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.ResourceNotFound;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addAddress(AddressRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User not found with the email: " + email));

        Address address = new Address();

        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setUser(user);
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setStreet(request.getStreet());

        if (request.getLandmark() != null){
            address.setLandmark(request.getLandmark());
        }

        addressRepository.save(address);
        return "Address added successfully.";
    }

    @Override
    public String deleteAddress(UUID id) {
        return "";
    }

    @Override
    public Address setActiveAddress(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Address not found with id: " + id));

        address.setActive(true);

        return address;
    }

    @Override
    public List<Address> getAllAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User not found with the email: " + email));

        return addressRepository.findByUser_Id(user.getUserId());
    }

    @Override
    public Address updateAddress(UUID id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Address not found with id: " + id));

        // Update fields
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setLandmark(request.getLandmark());
        address.setActive(address.getActive());

        // Save and return
        return addressRepository.save(address);
    }
}
