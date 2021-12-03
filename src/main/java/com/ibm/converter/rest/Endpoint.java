package com.ibm.converter.rest;

import com.ibm.converter.model.ConversionRequest;
import com.ibm.converter.model.ConversionResponse;
import com.ibm.converter.service.*;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1")
@Tag(
        name = "Version one api endpoint for Converter",
        description = "This endpoint defines all available functionalities for the converter application."
)
public class Endpoint {

    @Inject
    AuthRepository authRepository;
    @Inject
    DataLakeRepository dataLakeRepository;
    @Inject
    Conversions conversions;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "convert",
            description = "Request the result of a conversion for a specific source"
    )
    @Path("convert/")
    public Multi<ConversionResponse> convert(ConversionRequest request) {
        return conversions.convert(dataLakeRepository.getPseudonyms(request.getTableName()));
    }

}
