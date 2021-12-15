package com.ibm.converter.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class ConversionRequest {

    private String authToken;
    private List<String> pseudonyms;

    ConversionRequest() {    }

    public String getAuthToken() {
        return authToken;
    }

    public List<String> getPseudonyms() {
        return pseudonyms;
    }
}

