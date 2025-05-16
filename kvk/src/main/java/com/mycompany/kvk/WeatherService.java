package com.mycompany.kvk;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherService {
    private static final String API_KEY = "e38a66095e5e4e22a87100213251405";
    private static final String BASE_URL = "https://api.weatherapi.com/v1";
    private static final OkHttpClient client = new OkHttpClient();

    // Lithuanian cities with their coordinates
    static final String [] KLAIPEDA_CITY = {
        "Klaipėda", "55.7033,21.1443"
    };

    public static JSONObject getCurrentWeather(String city) throws IOException {
        if (!KLAIPEDA_CITY[0].equals("Klaipėda")) {
            throw new IllegalArgumentException("Wrong city");
        }

        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host("api.weatherapi.com")
            .addPathSegment("v1")
            .addPathSegment("current.json")
            .addQueryParameter("key", API_KEY)
            .addQueryParameter("q", KLAIPEDA_CITY[0])
            .build();

        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error: " + response.code() + " - " + response.message());
            }
            return new JSONObject(response.body().string());
        }
    }

    public static void printWeatherData(JSONObject weatherData) {
        JSONObject location = weatherData.getJSONObject("location");
        JSONObject current = weatherData.getJSONObject("current");

        System.out.println("\nCurrent Weather in " + location.getString("name") + ":");
        System.out.println("--------------------------------");
        System.out.println("Temperature: " + current.getDouble("temp_c") + "°C");
        System.out.println("Condition: " + current.getJSONObject("condition").getString("text"));
        System.out.println("Wind: " + current.getDouble("wind_kph") + " km/h");
        System.out.println("Humidity: " + current.getInt("humidity") + "%");
        System.out.println("Pressure: " + current.getDouble("pressure_mb") + " mb");
    }
}