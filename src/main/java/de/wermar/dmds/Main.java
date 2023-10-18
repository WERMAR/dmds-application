package de.wermar.dmds;

import de.wermar.dmds.jdbc.JDBCConnector;

import java.util.Scanner;

public class Main {

    private final JDBCConnector jdbcConnector = new JDBCConnector();

    public static void main(String[] args) {
        var instance = new Main();
        var scanner = new Scanner(System.in);
        System.out.println("Wähle Szenario aus: ");
        System.out.println("""
                #1 - JDBCConnection Establishment
                """);
        var input = scanner.nextInt();
        switch (input) {
            case 1 -> instance.jdbcConnector.establishConnection();

        }
    }
}
