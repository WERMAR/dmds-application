package de.wermar.dmds.jdbc.credentials;

import java.io.IOException;
import java.util.Properties;

public class CredentialsReader {

    public Credentials readCredentials() {
        var properties = new Properties();
        try (var applicationInputStream = ClassLoader.getSystemResourceAsStream("database.properties")) {
            properties.load(applicationInputStream);
            return Credentials.builder()
                    .dBUrl(properties.getProperty(CredentialsProperties.DB_URL.getValue()))
                    .user(properties.getProperty(CredentialsProperties.DB_USER.getValue()))
                    .password(properties.getProperty(CredentialsProperties.DB_PASSWORD.getValue()))
                    .build();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }
}
