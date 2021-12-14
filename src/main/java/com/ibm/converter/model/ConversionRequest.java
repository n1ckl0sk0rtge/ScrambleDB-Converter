package com.ibm.converter.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class ConversionRequest {

    public String authToken;
    public List<ConversionContext> conversionContext;

    ConversionRequest() {    }

    public String getAuthToken() {
        return authToken;
    }

    public List<ConversionContext> getConversionContext() {
        return conversionContext;
    }
}

