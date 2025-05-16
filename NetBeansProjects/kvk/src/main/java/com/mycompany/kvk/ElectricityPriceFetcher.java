package com.mycompany.kvk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ElectricityPriceFetcher {

    // Method to fetch the current electricity price
    public static JSONObject getCurrentPrice() {
        try {
            // Time zone definitions
            ZoneId ltZone = ZoneId.of("Europe/Vilnius");
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

            // Build today's date range in UTC
            String todayStr = now.toLocalDate().toString();
            String start = todayStr + "T00:00:00Z";
            String end = todayStr + "T23:59:59Z";

            // Fetch data from the Elering API
            String urlStr = "https://dashboard.elering.ee/api/nps/price?start=" + start + "&end=" + end;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse the JSON response
            JSONObject root = new JSONObject(response.toString());
            JSONObject data = root.getJSONObject("data");

            // Check if data exists for Lithuania
            if (!data.has("lt")) {
                System.out.println("⚠️ No Lithuania price data found.");
                return null;
            }

            JSONArray ltPrices = data.getJSONArray("lt");

            // Format for output
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            JSONObject latestMatch = null;
            ZonedDateTime utcTime = null;
            ZonedDateTime ltTime = null;

            // Reverse scan to find the most recent price that is not in the future
            for (int i = ltPrices.length() - 1; i >= 0; i--) {
                JSONObject entry = ltPrices.getJSONObject(i);
                long timestamp = entry.getLong("timestamp");

                // Handle milliseconds vs seconds timestamps
                Instant instant = timestamp > 1e12 ? Instant.ofEpochMilli(timestamp) : Instant.ofEpochSecond(timestamp);
                ZonedDateTime entryUtcTime = instant.atZone(ZoneOffset.UTC);

                if (entryUtcTime.isBefore(now) || entryUtcTime.isEqual(now)) {
                    latestMatch = entry;
                    utcTime = entryUtcTime;
                    ltTime = entryUtcTime.withZoneSameInstant(ltZone);
                    break;
                }
            }

            // If data is found, return it as a JSONObject
            if (latestMatch != null) {
                double price = latestMatch.getDouble("price");

                // Prepare result JSON object
                JSONObject result = new JSONObject();
                result.put("Hour Start (UTC)", utcTime.format(formatter));
                result.put("Hour Start (LT)", ltTime.format(formatter));
                result.put("Price (EUR/MWh)", price);
                return result;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
