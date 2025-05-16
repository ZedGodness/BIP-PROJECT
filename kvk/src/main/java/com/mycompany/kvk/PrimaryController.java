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
        new Thread(() -> {
            try {
                JSONObject weatherData = WeatherService.getCurrentWeather(WeatherService.KLAIPEDA_CITY[0]);
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

                Platform.runLater(() -> RealTimeWeather.getChildren().add(new Text(weatherInfo)));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> RealTimeWeather.getChildren().add(new Text("Failed to load weather data.")));
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

                    Platform.runLater(() -> ReakTimeEnergy.getChildren().add(new Text(priceInfo)));
                } else {
                    Platform.runLater(() -> ReakTimeEnergy.getChildren().add(new Text("No electricity price data found.")));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> ReakTimeEnergy.getChildren().add(new Text("Failed to load electricity price.")));
            }
        }).start();

        // === Scenario Matching ===
        new Thread(() -> {
            // Simulated values; replace with actual logic or user inputs later
            String weather = "Bad";
            String price = "Low";
            String load = "Low";

            ScenarioMatcher.Scenario match = ScenarioMatcher.findMatchingScenario(weather, price, load);

            Platform.runLater(() -> {
                if (match != null) {
                    String output = String.format(
                        "Selected Scenario #%d\nWeather: %s\nPrice: %s\nLoad: %s\nBattery State: %s\nReasoning: %s",
                        match.number, match.weather, match.price, match.load, match.batteryState, match.reasoning
                    );
                    Scenario.getChildren().add(new Text(output));
                } else {
                    Scenario.getChildren().add(new Text("No matching scenario found."));
                }
            });
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
