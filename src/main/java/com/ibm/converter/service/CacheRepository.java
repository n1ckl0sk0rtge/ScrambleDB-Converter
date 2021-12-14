package com.ibm.converter.service;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CacheRepository {

    @Inject
    ReactiveRedisClient reactiveRedisClient;

    private static final Integer CACHE_TIME = 1200;

    Uni<String> get(String key) {
        Uni<Response> r = reactiveRedisClient.get(key);
        return r.onItem().transform(Response::toString);
    }

    Uni<Void> set(String key, String value) {
        return reactiveRedisClient.set(
                Arrays.asList(key, value, "EX", CACHE_TIME.toString())).map(response -> null);
    }
}
