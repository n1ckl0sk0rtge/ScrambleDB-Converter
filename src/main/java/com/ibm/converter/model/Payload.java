package com.ibm.converter.model;

import java.util.List;

public class Payload {

    private List<String> data;

    Payload() {}

    public Payload(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
