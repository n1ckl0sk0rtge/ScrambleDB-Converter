package com.ibm.converter.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ConversionRequest {

    private String authToken;
    private String tableName;

    public ConversionRequest() {    }

    public ConversionRequest(String authToken, String tableName){
        this.authToken = authToken;
        this.tableName = tableName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
