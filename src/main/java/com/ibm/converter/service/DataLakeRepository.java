package com.ibm.converter.service;

import io.quarkus.reactive.mysql.client.runtime.MySQLPoolRecorder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DataLakeRepository {

    @Inject
    MySQLPool client;

    private static final String SCRAMBLED_TABLE_SEPARATOR = "_";

    public DataLakeRepository() {}

    public Multi<String> getPseudonyms(String tableName) {
        return getSubTables(tableName)
            .onItem().transformToMultiAndMerge(table ->
                client.query("select * from " + table).execute()
                .onItem().transformToMulti(rowSet -> Multi.createFrom().iterable(rowSet))
                .onItem().transform(row -> row.getString(0))
            );
    }

    public Multi<String> getSubTables(String tableName) {
        return client.query("Show tables").execute()
                .onItem().transformToMulti(rowSet ->
                        Multi.createFrom().iterable(rowSet))
                            .onItem().transform(row -> row.getString(0))
                            .select().where(name -> name.contains(tableName + SCRAMBLED_TABLE_SEPARATOR));
    }


}
