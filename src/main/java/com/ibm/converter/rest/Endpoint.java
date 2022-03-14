package com.ibm.converter.rest;

import com.ibm.converter.model.Payload;
import com.ibm.converter.service.*;
import io.smallrye.mutiny.Multi;
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
    ServiceRepository serviceRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("/convert")
    public Multi<String> convert(Payload request) {
        return serviceRepository.convert(request);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "pseudonym",
            description = "Request the new pseudonym for a specific context"
    )
    @Path("/pseudonym")
    public Multi<String> getPseudonym(Payload request) {
        return serviceRepository.getPseudonym(request);
    }

}
