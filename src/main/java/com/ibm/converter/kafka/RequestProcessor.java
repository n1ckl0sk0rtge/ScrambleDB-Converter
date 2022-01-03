package com.ibm.converter.kafka;

import com.ibm.converter.model.Payload;
import com.ibm.converter.service.ServiceRepository;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.Record;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RequestProcessor {

    private static final String pseudonymTaskIdentifier = "pseudonym";
    private static final String conversionTaskIdentifier = "convert";

    private static final String responseTaskIdentifier = "response";

    @Inject
    ServiceRepository serviceRepository;

    @Incoming("requests")
    @Outgoing("response")
    @NonBlocking
    public Uni<Record<String, Payload>> process(ConsumerRecord<String, Payload> record) {
        try {
            if (record.key().contains(pseudonymTaskIdentifier)) {
                Multi<String> pseudonyms = serviceRepository.getPseudonym(record.value());
                return pseudonyms.collect().asList()
                        .onItem().transform(Payload::new)
                        .onItem().transform(payload -> Record.of(
                                record.key().replace(pseudonymTaskIdentifier, responseTaskIdentifier), payload));

            } else if (record.key().contains(conversionTaskIdentifier)) {
                Multi<String> identities = serviceRepository.convert(record.value());
                return identities.collect().asList()
                        .onItem().transform(Payload::new)
                        .onItem().transform(payload -> Record.of(
                                record.key().replace(conversionTaskIdentifier, responseTaskIdentifier), payload));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uni.createFrom().nullItem();
    }

}
