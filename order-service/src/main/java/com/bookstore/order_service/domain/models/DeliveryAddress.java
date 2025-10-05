package com.bookstore.order_service.domain.models;

import jakarta.validation.constraints.NotBlank;

public record DeliveryAddress(
        @NotBlank(message = "AddressLine1 is Required") String addressLine1,
        String addressLine2,
        @NotBlank(message = "city is Required") String city,
        @NotBlank(message = "state is Required") String state,
        @NotBlank(message = "zipCode is Required") String zipCode,
        @NotBlank(message = "county is Required") String country) {}
