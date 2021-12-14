package com.ibm.converter.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoInit implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Starting with initializing crypto...");
        final int keySize =
                ConfigProvider.getConfig().getValue("crypto.keysize", Integer.class);
        final String workdir_path =
                ConfigProvider.getConfig().getValue("app.workdir.path", String.class);

        File sk = new File(workdir_path + ".rsa");
        File pk = new File(workdir_path + ".rsapub");
        if (!(sk.exists() || pk.exists())) {
            System.out.println("[Info] No keypair found.");
            System.out.println("[Info] Start creating new pair...");
            try {
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                keyPairGen.initialize(keySize);
                KeyPair prfPair = keyPairGen.generateKeyPair();

                PKCS8EncodedKeySpec pkcsKeySpec = new PKCS8EncodedKeySpec(
                        prfPair.getPrivate().getEncoded());
                FileOutputStream fos = new FileOutputStream(workdir_path + ".rsa");
                fos.write(pkcsKeySpec.getEncoded());

                X509EncodedKeySpec x509ks = new X509EncodedKeySpec(
                        prfPair.getPublic().getEncoded());
                fos = new FileOutputStream(workdir_path + ".rsapub");
                fos.write(x509ks.getEncoded());
                System.out.println("[Info] New pair created!");
            } catch (Exception e) {
                System.out.println("[Error] Error while creating new keypair");
                e.printStackTrace();
            }
        } else {
            System.out.println("[Info] Existing keypair found. Nothing to do");
        }
        System.out.println("[Info] Init done!");
        Quarkus.waitForExit();
        return 0;
    }
}
