package de.wermar.dmds.jdbc;

import de.wermar.dmds.jdbc.credentials.Credentials;
import de.wermar.dmds.jdbc.credentials.CredentialsReader;

public class JDBCConnector {
    private final CredentialsReader credentialsReader = new CredentialsReader();

    public void establishConnection() {
        Credentials credentials = this.credentialsReader.readCredentials();
        System.out.println("Credentials: " + credentials.getDBUrl());
    }
}
