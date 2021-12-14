package com.ibm.converter.rest;

import com.ibm.converter.model.ConversionRequest;
import com.ibm.converter.model.ConversionResponse;
import com.ibm.converter.service.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1")
@Tag(
        name = "Version one api endpoint for Converter",
        description = "This endpoint defines all available functionalities for the converter application."
)
public class Endpoint {

    @Inject
    Conversions conversions;

    @Inject
    CryptoRepository cryptoRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("convert/")
    //public Multi<ConversionResponse> convert(ConversionRequest request) {
    public ConversionResponse convert(ConversionRequest request) {

        System.out.println(request.getConversionContext().get(0).pseudonyms);

        return new ConversionResponse("", "");
    }

}
