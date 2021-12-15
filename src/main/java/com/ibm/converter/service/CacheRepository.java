package com.ibm.converter.service;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class CacheRepository {

    @Inject
    ReactiveRedisClient reactiveRedisClient;

    private static final Integer CACHE_TIME = 1200;

    public Uni<Response> get(String key) {
        return reactiveRedisClient.get(key);
    }

    public Uni<String> set(String key, String value) {
        return reactiveRedisClient.set(Arrays.asList(key, value))
                .onItem().ifNotNull().transform(res -> value)
                .onItem().ifNull().failWith(new Exception("[Error] Error while connection to Redis"));
    }
}
