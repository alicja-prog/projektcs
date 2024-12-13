package com.example.internal;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CurrencyRateLoader {

    public static void main(String[] args) {
        Map<String, Double> rates = loadCurrencyRates();
        System.out.println(rates);
    }

    public static Map<String, Double> loadCurrencyRates() {
        Map<String, Double> rates = new HashMap<>();
        try {
            // Create a URL object
            URL url = new URL("https://api.nbp.pl/api/exchangerates/tables/A/?format=json");

            // Open connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Here you can add your code to parse and store the received rates

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }
}