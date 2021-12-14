package com.ibm.converter;

import com.ibm.converter.service.CryptoInit;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(CryptoInit.class, args);
    }
}