package com.ibm.converter.model;

import java.util.List;

public class GenerationRequest {

    private String authToken;
    private String context;
    private List<String> payload;

    GenerationRequest() {}

    public String getAuthToken() {
        return authToken;
    }

    public String getContext() {
        return context;
    }

    public List<String> getPayload() {
        return payload;
    }
}
