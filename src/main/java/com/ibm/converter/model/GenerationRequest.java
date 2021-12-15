package com.ibm.converter.model;

import java.util.List;

public class GenerationRequest {

    private String authToken;
    private List<String> input;

    GenerationRequest() {}

    public String getAuthToken() {
        return authToken;
    }

    public List<String> getInput() {
        return input;
    }
}
