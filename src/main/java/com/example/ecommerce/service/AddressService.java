package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.AddressRequest;
import com.example.ecommerce.entity.Address;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    String addAddress(AddressRequest request);
    String deleteAddress(UUID id);
    Address setActiveAddress(UUID id);
    List<Address> getAllAddress();
    Address updateAddress(UUID id, AddressRequest request);
}
