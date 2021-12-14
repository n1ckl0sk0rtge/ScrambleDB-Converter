package com.ibm.converter.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public class ConversionContext {

    private String context;
    private List<String> pseudonyms;

    ConversionContext() {  }

    public String getContext() {
        return context;
    }

    public List<String> getPseudonyms() {
        return pseudonyms;
    }
}
