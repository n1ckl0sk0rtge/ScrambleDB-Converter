package com.ibm.converter.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;

@RegisterForReflection
public class ConversionResponse {

    private String pseudonym;
    private String bldIdentity;

    public ConversionResponse() {}

    public ConversionResponse(String pseudonym, String bldIdentity) {
        this.pseudonym = pseudonym;
        this.bldIdentity = bldIdentity;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getBldIdentity() {
        return bldIdentity;
    }

    public void setBldIdentity(String bldIdentity) {
        this.bldIdentity = bldIdentity;
    }
}
