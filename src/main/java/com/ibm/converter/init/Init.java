package com.ibm.converter.init;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Init implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Check if directory exists...");
        final String workdir_path =
                ConfigProvider.getConfig().getValue("app.workdir.path", String.class);

        if (Files.isDirectory(Paths.get(workdir_path), LinkOption.NOFOLLOW_LINKS)) {
            System.out.println("[INFO] directory found");
        } else {
            throw new Exception("[Error] could not find directory " + workdir_path +
                    ". Make sure directory exists.");
        }

        System.out.println("Starting with initializing crypto...");
        final int keySize =
                ConfigProvider.getConfig().getValue("crypto.keysize", Integer.class);

        File sk = new File(workdir_path + ".rsa");
        File pk = new File(workdir_path + ".rsapub");
        if (!(sk.exists() || pk.exists())) {
            System.out.println("[Info] No keypair found.");
            System.out.println("[Info] Start creating new pair...");
            initKeys(keySize, workdir_path);
        } else {
            System.out.println("[Info] Existing keypair found");

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] b_pk = IOUtils.toByteArray(new FileInputStream(workdir_path + ".rsapub"));
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b_pk);
            int keyLength = ((RSAPublicKey) keyFactory.generatePublic(publicKeySpec)).getModulus().bitLength();

            if (keySize != keyLength) {
                throw new Exception("[Error] recognised difference between configured " +
                        "key size and actual key size. \n Make sure to backup and remove " +
                        "the stored keys to continue");
            }
        }

        System.out.println("[Info] Try to connect to redis instance...");

        final String redisUrl =
                ConfigProvider.getConfig().getValue("quarkus.redis.hosts", String.class);
        final String redisAuth =
                ConfigProvider.getConfig().getValue("quarkus.redis.password", String.class);
        try {
            Jedis jedis = new Jedis(redisUrl);
            jedis.auth(redisAuth);
            System.out.println("[Info] Connection to redis instance established!");
        } catch (Exception e ) {
            System.out.println("[Error] Could not establish connection to the redis instance");
            throw e;
        }

        System.out.println("[Info] Init done!");
        Quarkus.waitForExit();
        return 0;
    }

    public static void initKeys(int keySize, String workdir_path) {
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
    }
}
