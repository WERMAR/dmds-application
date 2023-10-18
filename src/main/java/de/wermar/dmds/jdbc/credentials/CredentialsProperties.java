package de.wermar.dmds.jdbc.credentials;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CredentialsProperties {
    DB_PASSWORD("database.password"),
    DB_USER("database.user"),
    DB_URL("database.url");

    private String value;
}
