package com.bookstore.order_service.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// @Embeddable  : not req from hibernate-6
public record Customer(
        @NotBlank(message = "Customer Name is Required") String name,
        @NotBlank(message = "Customer Email is Required") @Email String email,
        @NotBlank(message = "Customer Phone Number is Required") String phone) {}
