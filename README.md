# BIP-PROJECT
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

public class ElectricityPricesLT {
    public static void main(String[] args) {
        try {
            // Set timezone for Lithuania
            ZoneId ltZone = ZoneId.of("Europe/Vilnius");

            // Calculate tomorrow's UTC date range
            LocalDate tomorrow = LocalDate.now(ZoneOffset.UTC).plusDays(1);
            String start = tomorrow + "T00:00:00Z";
            String end = tomorrow + "T23:59:59Z";

            // API URL
            String urlStr = String.format("https://dashboard.elering.ee/api/nps/price?start=%s&end=%s", start, end);
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(responseStr.toString());
            JSONArray ltPrices = json.getJSONObject("data").optJSONArray("lt");

            if (ltPrices == null || ltPrices.isEmpty()) {
                System.out.println("Tomorrow's price data is not available yet. Try again after 15:00 LT (EEST).");
                return;
            }

            System.out.println("\nElectricity Prices for Lithuania (Tomorrow):");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (int i = 0; i < ltPrices.length(); i++) {
                JSONObject entry = ltPrices.getJSONObject(i);
                long ts = entry.getLong("timestamp");
                double price = entry.getDouble("price");

                // Detect and convert timestamp
                Instant instant = (ts > 1e12) ? Instant.ofEpochMilli(ts) : Instant.ofEpochSecond(ts);
                ZonedDateTime utcTime = instant.atZone(ZoneOffset.UTC);
                ZonedDateTime ltTime = utcTime.withZoneSameInstant(ltZone);

                System.out.printf("%s (LT) | %.2f EUR/MWh%n", fmt.format(ltTime), price);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to fetch or process data.");
        }
    }
}
///
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20210307</version>
</dependency>






Weather forecast API:
import okhttp3.*;

public class WeatherAPIExample {
    public static void main(String[] args) {
        String apiKey = "e38a66095e5e4e22a87100213251405";
        String city = "Vilnius";

        OkHttpClient client = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host("api.weatherapi.com")
            .addPathSegment("v1")
            .addPathSegment("current.json")
            .addQueryParameter("key", apiKey)
            .addQueryParameter("q", city)
            .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error: " + response.code() + " - " + response.message());
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
