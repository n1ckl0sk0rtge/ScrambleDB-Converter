package com.ibm.converter.rest;

import com.ibm.converter.model.ConversionRequest;
import com.ibm.converter.model.GenerationRequest;
import com.ibm.converter.service.*;
import com.ibm.unlinkablepseudonyms.PRFSecretExponent;
import com.ibm.unlinkablepseudonyms.PRFSecureRandom;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Tag(
        name = "Api endpoint for Converter",
        description = "This endpoint defines all available functionalities for the converter application."
)
public class Endpoint {

    @Inject
    CryptoRepository cryptoRepository;

    @Inject
    CacheRepository cacheRepository;

    private final int secretExponentSize =
            ConfigProvider.getConfig().getValue("crypto.secretexponentsize", Integer.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("/convert")
    public Multi<String> convert(ConversionRequest request) {
        PRFSecretExponent newContext =
                new PRFSecretExponent(new PRFSecureRandom(), secretExponentSize);

        return Uni.createFrom().item(request)
            .onItem().transformToMulti(
                req -> Multi.createFrom().items(request.getPseudonyms().stream())
                        .onItem().transformToUniAndConcatenate(
                                pse -> Uni.combine().all().unis(
                                        Uni.createFrom().item(pse),
                                        cacheRepository.get(pse)
                                                .onItem().ifNotNull().transform(Response::toString)
                                                .onItem().ifNull().failWith(new Exception("[Error]"))
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


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "pseudonym",
            description = "Request the new pseudonym for a specific context"
    )
    @Path("/pseudonym")
    public Multi<String> getPseudonym(GenerationRequest request) {
        PRFSecretExponent context =
                new PRFSecretExponent(new PRFSecureRandom(), secretExponentSize);

        return Uni.createFrom().item(request)
            .onItem().transformToMulti(
                req -> Multi.createFrom().items(request.getInput().stream())
                    .onItem().transformToUniAndConcatenate(
                            input -> Uni.combine().all().unis(
                                    Uni.createFrom().item(input),
                                    Uni.createFrom().item(new PRFSecretExponent(new PRFSecureRandom(), secretExponentSize))
                            ).asTuple()
                    )
                    .onItem().transformToUniAndConcatenate(
                            tuple -> cryptoRepository.getPseudonym(tuple.getItem1(), tuple.getItem2())
                                        .onItem().invoke(
                                            pse -> cacheRepository.set(pse, tuple.getItem2().asBase64()).await().indefinitely())
                    )
            );
    }

}
