package com.ibm.converter.service;

import io.agroal.api.AgroalDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TableRepository {

    @Inject
    AgroalDataSource dataSource;

    private static final String SCRAMBLED_TABLE_SEPARATOR = "_";

    public TableRepository() {  }

    public List<String> getTable(String tableName) {
        try {
            Connection connection = dataSource.getConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(
                    null,
                    null,
                    tableName + SCRAMBLED_TABLE_SEPARATOR + "%",
                    new String[]{"TABLE"}
            );
            List<String> pseudonyms = new ArrayList<>();
            while (tables.next()) {
                String name = tables.getString("TABLE_NAME");

                if (!(name.equals(tableName))) {
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery("select * from " + name);
                    while(result.next()) {
                        pseudonyms.add(result.getString(1));
                    }
                    result.close();
                    statement.close();
                }
            }
            tables.close();
            connection.close();
            return pseudonyms;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("");
        }
    }


}
