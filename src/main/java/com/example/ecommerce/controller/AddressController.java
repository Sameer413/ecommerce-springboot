package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.AddressResponse;
import com.example.ecommerce.dto.request.AddressRequest;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.payload.ApiResponse;
import com.example.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressRequest>> addAddressHandler(@Valid @RequestBody AddressRequest request){
        String message = addressService.addAddress(request);

        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAddressHandler(@PathVariable UUID id){

        return ResponseEntity.ok(new ApiResponse<>(true, ""));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> setActiveAddressHandler(@PathVariable UUID id){
        Address address = addressService.setActiveAddress(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "", AddressResponse.mapTo(address)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesHandler(){
        List<Address> responses = addressService.getAllAddress();

        List<AddressResponse> addresses = responses.stream().map(AddressResponse::mapTo).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "", addresses));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressesHandler(@PathVariable UUID id, @Valid @RequestBody AddressRequest request){
        Address address = addressService.updateAddress(id, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "", AddressResponse.mapTo(address)));
    }
}
