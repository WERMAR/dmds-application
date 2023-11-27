package de.wermar.dmds.tomtom.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.wermar.dmds.tomtom.model.Position;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TomTomAPI {

    private static TomTomAPI instance = null;
    private final OkHttpClient CLIENT = new OkHttpClient();

    public static TomTomAPI instance() {
        if (instance == null)
            instance = new TomTomAPI();
        return instance;
    }

    private static final String API_KEY = "PIE4OlT0jsUxCubzEaqf7ad9LeHAhtVX";

    public ArrayList<Position> getCoordinates(List<String> cities) throws InterruptedException {
        // Replace this URL with your desired API endpoint
        String url = "https://api.tomtom.com/search/2/geocode/${cityNameParam}.json?countrySet=US&key=${API_KEY}";
        var positionList = new ArrayList<Position>();

        for (var city : cities) {
            String newURL = url.replace("${cityNameParam}", city.replace(" ", "")).replace("${API_KEY}", API_KEY).trim();
            System.out.println("URL: " + newURL);
            Request request = new Request.Builder()
                    .url(newURL)
                    .build();

            try {
                Response response = CLIENT.newCall(request).execute();

                if (response.isSuccessful()) {
                    // Get the response body as a string
                    assert response.body() != null;
                    String responseBody = response.body().string();
                    System.out.println("Response: " + responseBody);

                    ObjectMapper objectMapper = new ObjectMapper();

                    // Parse JSON string to JsonNode
                    JsonNode rootNode = objectMapper.readTree(responseBody);
                    JsonNode position = rootNode.get("results").get(0).get("position");
                    if (position != null) {
                        positionList.add(Position.builder().city(city).longCor(position.get("lon").asDouble()).latCor(position.get("lat").asDouble()).build());
                    }

                } else {
                    System.out.println("Request failed: " + response.code() + " - " + response.message());
                }

                // Close the response body
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
        return positionList;
    }

    public Long getDistance(Position start, Position end) {
        String url = getURL(start, end);
        System.out.println("URL: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = CLIENT.newCall(request).execute();
            if (response.isSuccessful()) {
                // Get the response body as a string
                assert response.body() != null;
                String responseBody = response.body().string();
                System.out.println("Repsonse: " + responseBody);
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse JSON string to JsonNode
                JsonNode rootNode = objectMapper.readTree(responseBody);
                response.close();
                return rootNode.get("routes").get(0).get("summary").get("lengthInMeters").asLong();
            } else {
                System.out.println("Request failed: " + response.code() + " - " + response.message());
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private static String getURL(Position start, Position end) {
        String url = "https://api.tomtom.com/routing/1/calculateRoute/${START.LAT},${START.LNG}:${END.LAT},${END.LNG}/json?vehicleHeading=90&sectionType=traffic&report=effectiveSettings&routeType=eco&traffic=true&avoid=unpavedRoads&travelMode=car&vehicleCommercial=false&vehicleEngineType=combustion&key=${API_KEY}";
        String newURL = url.replace("${START.LAT}", start.getLatCor().toString())
                .replace("${START.LNG}", start.getLongCor().toString())
                .replace("${END.LAT}", end.getLatCor().toString())
                .replace("${END.LNG}", end.getLongCor().toString())
                .replace("${API_KEY}", API_KEY).trim();
        return newURL;
    }
}
