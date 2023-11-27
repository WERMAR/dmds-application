package de.wermar.dmds.tomtom.service;

public class RoutingAlgorithm {

    private static RoutingAlgorithm instance;

    public static RoutingAlgorithm instance() {
        if (instance == null)
            instance = new RoutingAlgorithm();

        return instance;
    }


}
