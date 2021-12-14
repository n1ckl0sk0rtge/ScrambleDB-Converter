package com.ibm.converter.service;

import com.ibm.unlinkablepseudonyms.PRFSecretExponent;
import com.ibm.unlinkablepseudonyms.Pseudonym;
import io.smallrye.mutiny.Uni;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import java.io.FileInputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ApplicationScoped
public class CryptoRepository {

    private final int secretExponentSize =
            ConfigProvider.getConfig().getValue("crypto.secretexponentsize", Integer.class);

    public PrivateKey sk;
    private PublicKey pk;

    private final Base64.Encoder b64Encoder = Base64.getEncoder();

    CryptoRepository() {
        final String workdir_path =
                ConfigProvider.getConfig().getValue("app.workdir.path", String.class);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] b_pk = IOUtils.toByteArray(new FileInputStream(workdir_path + ".rsapub"));
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b_pk);
            this.pk = keyFactory.generatePublic(publicKeySpec);

            byte[] b_sk = IOUtils.toByteArray(new FileInputStream(workdir_path + ".rsa"));
            EncodedKeySpec secretKeySpec =new PKCS8EncodedKeySpec(b_sk);
            this.sk = keyFactory.generatePrivate(secretKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Uni<String> getPseudonym(
            String pseudonym,
            PRFSecretExponent context) {
        try {
            return Uni.createFrom().item(
                b64Encoder.encodeToString(
                    Pseudonym.generate(
                            pseudonym.getBytes(),
                            context,
                            (RSAPublicKey) this.pk
                    )
                )
            );
        } catch (Exception e) {
            return Uni.createFrom().failure(new Exception("[Error] Error while running crypto operation"));
        }
    }

    Uni<String> convert(
            String pseudonym,
            PRFSecretExponent sourceContext,
            PRFSecretExponent targetContext) {
        try {
            return Uni.createFrom().item(
                    b64Encoder.encodeToString(
                        Pseudonym.convert(pseudonym.getBytes(),
                                sourceContext,
                                targetContext,
                                (RSAPrivateCrtKey) this.sk
                        )
                    )
            );
        } catch (Exception e) {
            return Uni.createFrom().failure(new Exception("[Error] Error while running crypto operation"));
        }
    }

}
