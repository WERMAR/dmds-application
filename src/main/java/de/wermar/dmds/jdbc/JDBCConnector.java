package de.wermar.dmds.jdbc;

import de.wermar.dmds.jdbc.credentials.Credentials;
import de.wermar.dmds.jdbc.credentials.CredentialsReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCConnector {

    private static JDBCConnector jdbcConnectorInstance;

    private Connection currConnection;

    public static final JDBCConnector instance() {
        if (jdbcConnectorInstance == null)
            jdbcConnectorInstance = new JDBCConnector();
        return jdbcConnectorInstance;
    }

    private final CredentialsReader credentialsReader = new CredentialsReader();

    private JDBCConnector() {
        // private Constructor due Singleton-Pattern Reasons
    }

    public void establishConnection() {
        Credentials credentials = this.credentialsReader.readCredentials();
        try {
            var connection = DriverManager.getConnection(credentials.getDBUrl(), credentials.getUser(), credentials.getPassword());
            if (connection != null) {
                this.currConnection = connection;
                System.out.println("Connection established");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTablesFromAWC() {
        this.establishConnection();
        System.out.println("Start Executing Query to fetch available Tables");
        try {
            var statement = this.currConnection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM information_schema.TABLES where TABLE_SCHEMA like 'awc'");
            extractResult(result);
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractResult(ResultSet result) throws SQLException {
        System.out.println("Result: ");
        while (result.next()) {
            System.out.println("Table: {" + result.getString("TABLE_NAME") + "} exists");
        }
    }

    public void closeConnection() throws SQLException {
        this.currConnection.close();
    }

    public void doInsert(String tableName, List<String> values) {
        this.establishConnection();
        try {
            var statement = this.currConnection.createStatement();
            StringBuilder query = new StringBuilder("INSERT INTO ");
            query.append(tableName);
            query.append(" VALUES ");

            values.stream().forEach(valueObject -> {
                query.append(valueObject);
                query.append(",");
            });
            query.replace(query.lastIndexOf(","), query.lastIndexOf(",") + 1, ";");
            System.out.println("Executed Query: { " + query.toString() + " }");
            statement.executeUpdate(query.toString());
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }


    public void doSelect(String tableName, String fieldNames, String whereCondition) {
        this.establishConnection();
        try {
            var statement = this.currConnection.createStatement();
            var query = "SELECT ";
            query += fieldNames;
            query += " FROM ";
            query += tableName;
            if (whereCondition.length() > 0) {
                query += " WHERE ";
                query += whereCondition;
            }
            var result = statement.executeQuery(query);
            extractResult(result, fieldNames);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(final String SQL_QUERY) {
        this.establishConnection();
        try {
            var statement = this.currConnection.createStatement();
            return statement.executeQuery(SQL_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractResult(ResultSet result, String fieldNames) throws SQLException {
        List<String> fieldNamesSplitted = new ArrayList<>();
        if (!"*".equals(fieldNames)) {
            fieldNamesSplitted = Arrays.stream(fieldNames.split(",")).map(e -> e.trim()).toList();
        } else {
            int columnCount = result.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                fieldNamesSplitted.add(result.getMetaData().getColumnName(i));
            }
        }
        int i = 1;
        while (result.next()) {
            StringBuilder builder = new StringBuilder("{ ");
            fieldNamesSplitted.stream().forEach(columnName -> {
                try {
                    builder.append(columnName + " : ");
                    builder.append(result.getObject(columnName) + " | ");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            builder.append(" }");
            System.out.println("Result [" + i + " ]: " + builder.toString());
            i++;
        }
    }

    public void doUpdate(String tableName, String whereCondition, String field, String valueOfUpdate) {
        this.establishConnection();
        var query = "UPDATE " + tableName + " SET " + field + " = ? WHERE " + whereCondition;
        try (var preparedStatement = this.currConnection.prepareStatement(query)) {
            System.out.println("Execute Query: { " + query + " }");
            preparedStatement.setString(1, valueOfUpdate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
