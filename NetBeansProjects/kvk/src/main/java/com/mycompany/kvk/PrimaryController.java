package com.mycompany.kvk;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import org.json.JSONObject;

public class PrimaryController {

    // Weather components
    @FXML private FlowPane WeatherForecast;
    @FXML private FlowPane RealTimeWeather;

    // Energy components
    @FXML private FlowPane ReakTimeEnergy;
    @FXML private FlowPane EnergyForecast;

    // Consumer components
    @FXML private FlowPane ConsumerKvK;
    @FXML private FlowPane Consumer308;

    // Solar components
    @FXML private FlowPane BiggerSolarPlant;
    @FXML private FlowPane SmallerSolarPlant;

    // Grid components
    @FXML private FlowPane Grid;
    @FXML private FlowPane Battery;

    // Scenario components
    @FXML private FlowPane Scenario;
    @FXML private Slider Slider;

    @FXML
    public void Test() {
        clearAllPanes();

        // === Weather Fetching ===
        WeatherService weatherService = new WeatherService();
        new Thread(() -> {
            try {
                JSONObject weatherData = weatherService.getCurrentWeather(weatherService.KLAIPEDA_CITY[0]);
                JSONObject location = weatherData.getJSONObject("location");
                JSONObject current = weatherData.getJSONObject("current");

                String cityName = location.getString("name");
                double temperature = current.getDouble("temp_c");
                String condition = current.getJSONObject("condition").getString("text");
                double windSpeed = current.getDouble("wind_kph");
                int humidity = current.getInt("humidity");
                double pressure = current.getDouble("pressure_mb");

                String weatherInfo = String.format(
                    "Weather in %s:\nTemperature: %.1f°C\nCondition: %s\nWind: %.1f km/h\nHumidity: %d%%\nPressure: %.1f mb",
                    cityName, temperature, condition, windSpeed, humidity, pressure
                );

                Platform.runLater(() -> {
                    if (cityName.equals("Vilnius")) {
                        RealTimeWeather.getChildren().add(new Text(weatherInfo));
                    } else {
                        WeatherForecast.getChildren().add(new Text(weatherInfo + "\n"));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // === Electricity Price Fetching ===
        new Thread(() -> {
            try {
                JSONObject priceData = ElectricityPriceFetcher.getCurrentPrice();

                if (priceData != null) {
                    String priceInfo = String.format(
                        "Current Electricity Price:\nHour Start (LT): %s\nPrice: %.2f EUR/MWh",
                        priceData.getString("Hour Start (LT)"),
                        priceData.getDouble("Price (EUR/MWh)")
                    );

                    Platform.runLater(() -> {
                        ReakTimeEnergy.getChildren().add(new Text(priceInfo));
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // === Scenario Matching ===
        new Thread(() -> {
            // Simulated input (replace with real inputs later if needed)
            String weather = "Bad";
            String price = "Low";
            String load = "Low";

            // ✅ Use the implemented method
            ScenarioMatcher.Scenario match = ScenarioMatcher.findMatchingScenario(weather, price, load);

            if (match != null) {
                String output = String.format(
                    "Selected Scenario #%d\nWeather: %s\nPrice: %s\nLoad: %s\nBattery State: %s\nReasoning: %s",
                    match.number, match.weather, match.price, match.load, match.batteryState, match.reasoning
                );
                Platform.runLater(() -> {
                    Scenario.getChildren().add(new Text(output));
                });
            } else {
                Platform.runLater(() -> {
                    Scenario.getChildren().add(new Text("No matching scenario found."));
                });
            }
        }).start();
    }

    private void clearAllPanes() {
        Platform.runLater(() -> {
            WeatherForecast.getChildren().clear();
            RealTimeWeather.getChildren().clear();
            ReakTimeEnergy.getChildren().clear();
            EnergyForecast.getChildren().clear();
            ConsumerKvK.getChildren().clear();
            Consumer308.getChildren().clear();
            BiggerSolarPlant.getChildren().clear();
            SmallerSolarPlant.getChildren().clear();
            Grid.getChildren().clear();
            Battery.getChildren().clear();
            Scenario.getChildren().clear();
        });
    }
}