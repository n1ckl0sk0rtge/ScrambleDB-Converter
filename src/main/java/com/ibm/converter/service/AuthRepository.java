package com.ibm.converter.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthRepository {

    private final String TOKEN = "token";

    AuthRepository() {

    }

    public boolean validate(String token) {
        return token.equals(TOKEN);
    }
}
