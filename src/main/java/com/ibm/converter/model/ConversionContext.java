package com.ibm.converter.model;

import java.util.List;

public class ConversionContext {

    public String context;
    public List<String> pseudonyms;

    ConversionContext() {  }

    public String getContext() {
        return context;
    }

    public List<String> getPseudonyms() {
        return pseudonyms;
    }
}
