package com.ibm.converter.service;

import com.ibm.converter.model.Payload;
import com.ibm.unlinkablepseudonyms.PRFSecretExponent;
import com.ibm.unlinkablepseudonyms.PRFSecureRandom;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ServiceRepository {

    @Inject
    CryptoRepository cryptoRepository;

    @Inject
    CacheRepository cacheRepository;

    private final int secretExponentSize =
            ConfigProvider.getConfig().getValue("crypto.secretexponentsize", Integer.class);

    public Multi<String> convert(Payload request) {
        PRFSecretExponent newContext =
                new PRFSecretExponent(new PRFSecureRandom(), secretExponentSize);

        return Uni.createFrom().item(request)
                .onItem().transformToMulti(
                        req -> Multi.createFrom().items(request.getData().stream())
                                .onItem().transformToUniAndConcatenate(
                                        pse -> Uni.combine().all().unis(
                                                Uni.createFrom().item(pse),
                                                cacheRepository.get(pse)
                                                        .onItem().ifNotNull().transform(Response::toString)
                                                        .onItem().ifNull().failWith(new Exception(
                                                                "[Error] Could not find context of pseudonym " + pse))
                                        ).asTuple()
                                )
                                .onItem().transformToUniAndConcatenate(
                                        tuple -> cryptoRepository.convert(
                                                tuple.getItem1(),
                                                new PRFSecretExponent(tuple.getItem2()),
                                                newContext
                                        )
                                )
                );
    }

    public Multi<String> getPseudonym(Payload request) {
        return Uni.createFrom().item(request)
                .onItem().transformToMulti(
                        req -> Multi.createFrom().items(request.getData().stream())
                                .onItem().transformToUniAndConcatenate(
                                        input -> Uni.combine().all().unis(
                                                Uni.createFrom().item(input),
                                                Uni.createFrom().item(new PRFSecretExponent(new PRFSecureRandom(), secretExponentSize))
                                        ).asTuple()
                                )
                                .onItem().transformToUniAndConcatenate(
                                        tuple -> cryptoRepository.getPseudonym(tuple.getItem1(), tuple.getItem2())
                                                .onItem().call(
                                                        pse -> cacheRepository.set(pse, tuple.getItem2().asBase64()))
                                )
                );
    }
}
