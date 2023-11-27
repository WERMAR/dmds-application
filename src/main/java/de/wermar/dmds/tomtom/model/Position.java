package de.wermar.dmds.tomtom.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Position {
    private String city;
    private Double longCor;
    private Double latCor;
}
