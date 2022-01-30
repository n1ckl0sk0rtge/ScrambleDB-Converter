package com.ibm.converter.init;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(Init.class);

    @Override
    public int run(String... args) throws Exception {
        LOG.info("Check if directory exists...");
        String workdir_path =
                ConfigProvider.getConfig().getValue("app.workdir.path", String.class);
        if (!workdir_path.endsWith("/")) {
            workdir_path += "/";
        }

        if (Files.isDirectory(Paths.get(workdir_path), LinkOption.NOFOLLOW_LINKS)) {
            LOG.info("directory found");
        } else {
            throw new Exception("Error: could not find directory " + workdir_path +
                    ". Make sure directory exists.");
        }

        LOG.info("Starting with initializing crypto...");
        final int keySize =
                ConfigProvider.getConfig().getValue("crypto.keysize", Integer.class);

        File sk = new File(workdir_path + ".rsa");
        File pk = new File(workdir_path + ".rsapub");
        if (!(sk.exists() || pk.exists())) {
            LOG.warn("No keypair found.");
            LOG.info("Start creating new pair...");
            initKeys(keySize, workdir_path);
        } else {
            LOG.info("Existing keypair found");

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] b_pk = IOUtils.toByteArray(new FileInputStream(workdir_path + ".rsapub"));
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b_pk);
            int keyLength = ((RSAPublicKey) keyFactory.generatePublic(publicKeySpec)).getModulus().bitLength();

            if (keySize != keyLength) {
                throw new Exception("Error: recognised difference between configured " +
                        "key size and actual key size. \n Make sure to backup and remove " +
                        "the stored keys to continue");
            }
        }

        LOG.info("Try to connect to redis instance...");

        final String redisUrl =
                ConfigProvider.getConfig().getValue("quarkus.redis.hosts", String.class);
        final String redisAuth =
                ConfigProvider.getConfig().getValue("quarkus.redis.password", String.class);
        try {
            Jedis jedis = new Jedis(redisUrl);
            jedis.auth(redisAuth);
            LOG.info("Connection to redis instance established!");
        } catch (Exception e ) {
            LOG.error("Could not establish connection to the redis instance");
            throw e;
        }

        LOG.info("Init done!");
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
            LOG.info("New pair created!");
        } catch (Exception e) {
            LOG.error("Error while creating new keypair");
            e.printStackTrace();
        }
    }
}
