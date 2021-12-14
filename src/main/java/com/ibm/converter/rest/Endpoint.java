package com.ibm.converter.rest;

import com.ibm.converter.model.ConversionContext;
import com.ibm.converter.model.ConversionRequest;
import com.ibm.converter.model.ConversionResponse;
import com.ibm.converter.model.GenerationRequest;
import com.ibm.converter.service.*;
import com.ibm.unlinkablepseudonyms.PRFSecretExponent;
import com.ibm.unlinkablepseudonyms.PRFSecureRandom;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Base64;
import java.util.List;

@Path("/v1")
@Tag(
        name = "Version one api endpoint for Converter",
        description = "This endpoint defines all available functionalities for the converter application."
)
public class Endpoint {

    @Inject
    CryptoRepository cryptoRepository;

    @Inject
    CacheRepository cacheRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("convert/")
    //public Multi<ConversionResponse> convert(ConversionRequest request) {
    public ConversionResponse convert(ConversionRequest request) {
        // check Auth Token
        // request.getAuthToken();

        PRFSecretExponent targetContext = new PRFSecretExponent(new PRFSecureRandom(), 256);

        for (ConversionContext c : request.getConversionContext()) {
            // get PRFSecretExponent sourceConetxt from cache
            // else error
            List<String> ps = c.getPseudonyms();
            //cryptoRepository...
        }

        return new ConversionResponse("", "");
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "getPseudonym",
            description = "Request the new pseudonym for a specific context"
    )
    @Path("getPseudonym/")
    public Multi<String> getPseudonym(GenerationRequest request) {
        PRFSecretExponent context =
                new PRFSecretExponent(new PRFSecureRandom(), 256);

        return Uni.createFrom().item(request)
            .onItem().transformToUni(
                  req -> cacheRepository.get(request.getContext())
                          .onItem().ifNotNull().transform(Response::toString)
                          .onItem().ifNull().switchTo(() ->
                                  cacheRepository.set(
                                    request.getContext(),
                                    context.asBase64()
                                  )
                            )
                  )
            .onItem().transformToMulti(
                  b64con -> Multi.createFrom().items(request.getPayload().stream())
                          .onItem().transformToUniAndMerge(
                                  input -> cryptoRepository.getPseudonym(input, new PRFSecretExponent(b64con)))
                  );
    }

    @Path("renameContext/")
    public String renameContext() {
        return "";
    }

}
