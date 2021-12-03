package com.ibm.converter.service;

import com.ibm.converter.model.ConversionResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Conversions {

    @Inject
    CryptoRepository cryptoRepository;

    @Inject
    CacheRepository cacheRepository;

    Conversions() {}

    public Multi<ConversionResponse> convert(Multi<String> tableRelatedPseudonymStream) {
        return tableRelatedPseudonymStream.onItem().transformToUniAndMerge(
                    pseudonym ->
                        Uni.join().first(
                            cacheRepository.get(pseudonym),
                            cryptoRepository.runCryptoOperation(pseudonym))
                        .toTerminate().onItem().transform(
                                bldKey -> new ConversionResponse(pseudonym, bldKey)
                        )
                    );
    }



}
