package com.example.ecommerce.dto.reponse;

import com.example.ecommerce.entity.Address;

import java.util.UUID;

public class AddressResponse {
    private UUID id;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private String landmark;

    private Boolean active;

    public AddressResponse() {
    }

    public AddressResponse(UUID id, String street, String city, String state, String postalCode, String country, String landmark, Boolean active) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.landmark = landmark;
        this.active = active;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public static AddressResponse mapTo(Address address){
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getLandmark(),
                address.getActive()
        );
    }
}
