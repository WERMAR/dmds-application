package de.wermar.dmds;

import de.wermar.dmds.jdbc.JDBCConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static boolean cancel = false;

    public static void main(String[] args) throws SQLException {
        var instance = new Main();
        var scanner = new Scanner(System.in);
        System.out.println("Wähle Szenario aus: ");
        System.out.print("""
                #1 - JDBCConnection Establishment
                #2 - Get available Tables in AWC Schema
                #3 - SELECT, UPDATE, INSERT Data into the AWC Schema
                """);
        System.out.print("Szenario: ");
        var input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1 -> JDBCConnector.instance().establishConnection();
            case 2 -> JDBCConnector.instance().loadTablesFromAWC();
            case 3 -> startDBModifier(scanner);
        }
        JDBCConnector.instance().closeConnection();
        scanner.close();
    }

    private static void startDBModifier(Scanner scanner) {
        System.out.print("""
                Which Operation you will want to do: 
                #1 - SELECT some Data include a WHERE Clause
                #2 - INSERT Data to a specific Table
                #3 - UPDATE Data to a specific Table with a WHERE Clause
                #4 - Abort Sequence with C
                Please Choose S(ELECT), U(PDATE), I(NSERT), C(ANCEL)
                """);
        while (!cancel) {
            System.out.print("Next Execution: ");
            var input = scanner.nextLine();
            if (input.length() < 0)
                throw new RuntimeException("Further processing is not possible");
            var selection = input.toUpperCase().charAt(0);
            switch (selection) {
                case 'S' -> doSelectStatement(scanner);
                case 'U' -> doUpdateStatement(scanner);
                case 'I' -> doInsertStatement(scanner);
                case 'C' -> doCancel();
            }
        }
    }

    private static void doInsertStatement(Scanner scanner) {
        System.out.print("Table-Name to Insert the Data: ");
        var tableName = scanner.nextLine();
        List<String> values = new ArrayList<>();
        boolean breakLoop = false;
        while (!breakLoop) {
            System.out.print("Add the Inserted Values - Please without () | Or Break with CANCEL: ");
            var input = scanner.nextLine();
            if ("CANCEL".equalsIgnoreCase(input)) {
                breakLoop = true;
                break;
            }
            var finalInput = "(" + input + ")";
            values.add(finalInput);
        }
        JDBCConnector.instance().doInsert(tableName, values);
    }

    private static void doUpdateStatement(Scanner scanner) {
        System.out.print("Table-Name for the Selection: ");
        var tableName = scanner.nextLine();
        System.out.print("WHERE Conditions: ");
        var whereCondition = scanner.nextLine();
        if (whereCondition.length() == 0)
            throw new RuntimeException("No WHERE Condition is not allowed");
        System.out.print("Field which should be Updated: ");
        var field = scanner.nextLine();
        System.out.print("Value of the Update: ");
        var valueOfUpdate = scanner.nextLine();

        JDBCConnector.instance().doUpdate(tableName, whereCondition, field, valueOfUpdate);
    }

    private static void doSelectStatement(Scanner scanner) {
        System.out.print("Table-Name for the Selection: ");
        var tableName = scanner.nextLine();
        System.out.print("Field-Names for the Selection divided by ',' or using '*' for All: ");
        var fieldNames = scanner.nextLine();
        System.out.print("WHERE Conditions: ");
        var whereCondition = scanner.nextLine();

        JDBCConnector.instance().doSelect(tableName, fieldNames, whereCondition);
    }

    private static void doCancel() {
        cancel = true;
    }
}
