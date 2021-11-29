package com.ibm.converter.rest;

import com.ibm.converter.model.ConversionRequest;
import com.ibm.converter.service.AuthRepository;
import com.ibm.converter.service.CacheRepository;
import com.ibm.converter.service.Conversions;
import com.ibm.converter.service.TableRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1")
@Tag(
        name = "Version one api endpoint for Converter",
        description = "This endpoint defines all available functionalities for the converter application."
)
public class Endpoint {

    @Inject
    AuthRepository authRepository;
    @Inject
    TableRepository tableRepository;
    @Inject
    Conversions conversions;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("convert/")
    public Response convert(ConversionRequest request) {
        if (authRepository.validate(request.getAuthToken())) {
            List<String> pseudonyms = tableRepository.getTable(request.getTableName());
            return Response.ok(conversions.convert(pseudonyms)).build();
        } else {
            return Response.status(403).build();
        }
    }



}
