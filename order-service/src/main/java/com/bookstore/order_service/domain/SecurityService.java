package com.bookstore.order_service.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoggedInUserName() {
        return "user";
    }
}
