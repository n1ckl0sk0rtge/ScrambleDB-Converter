package com.ibm.converter;

import com.ibm.converter.init.Init;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(Init.class, args);
    }
}