package com.ibm.converter.service;

import com.ibm.converter.model.ConversionResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

@ApplicationScoped
public class Conversions {

    @Inject
    CryptoRepository cryptoRepository;

    @Inject
    CacheRepository cacheRepository;

    Conversions() {}

    public Multi<ConversionResponse> convert() {
        /*return tableRelatedPseudonymStream.onItem().transformToUniAndMerge(
                    pseudonym ->
                        Uni.join().first(
                            cacheRepository.get(pseudonym),
                            cryptoRepository.convert(pseudonym))
                        .toTerminate()
                        .onItem().call(
                                bldKey -> cacheRepository.set(pseudonym, bldKey)
                        )
                        .onItem().transform(
                                bldKey -> new ConversionResponse(pseudonym, bldKey)
                        )
                    );*/
        return Multi.createFrom().item(new ConversionResponse("",""));
    }



}
