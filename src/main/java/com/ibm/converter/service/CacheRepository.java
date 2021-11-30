package com.ibm.converter.service;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.redis.client.Response;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CacheRepository {

    @Inject
    RedisClient redisClient;

    @Inject
    ReactiveRedisClient reactiveRedisClient;

    private static final Integer CACHE_TIME = 1200;

    Uni<Void> del(String key) {
        return reactiveRedisClient.del(List.of(key))
                .map(response -> null);
    }

    String get(String key) {
        Response r = redisClient.get(key);
        if (r != null) {
            return r.toString();
        } else {
            return "";
        }
    }

    void set(String key, String value) {
        redisClient.set(
                Arrays.asList(key, value, "EX", CACHE_TIME.toString()));
    }
}
