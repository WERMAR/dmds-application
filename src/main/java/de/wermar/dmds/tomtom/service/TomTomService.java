package de.wermar.dmds.tomtom.service;

import de.wermar.dmds.jdbc.JDBCConnector;
import de.wermar.dmds.tomtom.api.TomTomAPI;
import de.wermar.dmds.tomtom.model.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TomTomService {

    public static void main(String[] args) throws SQLException, InterruptedException {
        new TomTomService().run();
    }

    private void run() throws SQLException, InterruptedException {
        final ResultSet resultSet = JDBCConnector.instance().executeQuery("""
                select city, count(AddressID) from Person_Address
                join awc.Person_StateProvince PSP on Person_Address.StateProvinceID = PSP.StateProvinceID
                            where PSP.CountryRegionCode = 'US'
                group by city
                order by count(AddressID) desc
                limit 10;
                """);

        List<String> cities = new ArrayList<>();
        while (resultSet.next()) {
            cities.add(resultSet.getString("city"));
        }
        cities.forEach(e -> System.out.println("City: " + e));

        var positions = TomTomAPI.instance().getCoordinates(cities);

        positions.forEach(e -> System.out.println("Lat | Long => " + e.getLatCor() + " | " + e.getLongCor()));

        Map<Position, Map<Position, Long>> graph = new HashMap<>();
        for (var start : positions) {
            var targets = new HashMap<Position, Long>();
            for (var end : positions) {
                if (start == end)
                    continue;
                var distance = TomTomAPI.instance().getDistance(start, end);
                Thread.sleep(100);
                targets.put(end, distance);
            }
            graph.put(start, targets);
        }
        printGraph(graph);

        // MISSING ALGO Logic

    }

    public void printGraph(Map<Position, Map<Position, Long>> graph) {
        for (Map.Entry<Position, Map<Position, Long>> entry : graph.entrySet()) {
            String node = entry.getKey().getCity();
            Map<Position, Long> neighbors = entry.getValue();
            StringBuilder neighborString = new StringBuilder();

            for (Map.Entry<Position, Long> neighborEntry : neighbors.entrySet()) {
                neighborString.append(neighborEntry.getKey().getCity()).append(" (")
                        .append(neighborEntry.getValue()).append(") ");
            }

            System.out.println(node + " -> " + neighborString);
        }
    }
}
