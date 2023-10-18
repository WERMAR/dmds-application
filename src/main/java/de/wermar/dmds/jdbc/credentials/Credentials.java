package de.wermar.dmds.jdbc.credentials;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Credentials {

    private String dBUrl;
    private String user;
    private String password;

}
