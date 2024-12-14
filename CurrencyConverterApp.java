package com.example.internal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterApp {

    // Sample country data - in a real application, this could be loaded from a database or an API
    private static final Country[] COUNTRIES = {
            new Country("United States", "USD"),
            new Country("Eurozone", "EUR"),
            new Country("United Kingdom", "GBP"),
            new Country("Japan", "JPY"),
            new Country("Poland", "PLN"),
            // Add more countries as needed
    };

    public static void main(Map<String, Double> args) {
        SwingUtilities.invokeLater(CurrencyConverterApp::createAndShowGUI);
    }
    /*private static Map<String, Double> currencyRates; // This should be set properly when launching the app.

    public static void main(Map<String, Double> rates) {
        currencyRates = rates; // Assign the currency rates passed from CountryListApp
        SwingUtilities.invokeLater(CurrencyConverterApp::createAndShowGUI);*/

    public static void createAndShowGUI() {
        // Creating the frame
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        // Adding panel to frame and displaying it
        frame.add(getCurrencyConverterPanel());
        frame.setVisible(true);
    }
        // Creating main panel
    public static JPanel getCurrencyConverterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title label
        JLabel titleLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Creating currency conversion components (existing code)

        // Amount label and field
        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(amountLabel, gbc);

        JTextField amountField = new JTextField("100.00");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(amountField, gbc);

        // "I have" label and currency dropdown
        JLabel haveLabel = new JLabel("I have:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(haveLabel, gbc);

        JComboBox<String> haveCurrency = new JComboBox<>(loadCurrencyRates().keySet().toArray(new String[0]));
        haveCurrency.setSelectedItem("PLN");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(haveCurrency, gbc);

        // "I want" label and currency dropdown
        JLabel wantLabel = new JLabel("I want:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(wantLabel, gbc);

        JComboBox<String> wantCurrency = new JComboBox<>(loadCurrencyRates().keySet().toArray(new String[0]));
        wantCurrency.setSelectedItem("EUR");
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(wantCurrency, gbc);

        // Result label
        JLabel resultLabel = new JLabel("100.00 PLN = 23.38 EUR");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(resultLabel, gbc);

        // Convert button
        JButton convertButton = new JButton("Convert");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(convertButton, gbc);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Currency conversion logic
                double amount = Double.parseDouble(amountField.getText());
                String fromCurrency = (String) haveCurrency.getSelectedItem();
                String toCurrency = (String) wantCurrency.getSelectedItem();

                Map<String, Double> rates = loadCurrencyRates();
                double exchangeRate = rates.get(toCurrency) / rates.get(fromCurrency);
                double result = amount / exchangeRate;
                resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, result, toCurrency));
            }
        });

        // Adding country buttons
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        for (Country country : COUNTRIES) {
            JButton countryButton = new JButton(country.getName());
            countryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showCountryInfo(country);
                }
            });
            panel.add(countryButton, gbc);
            gbc.gridx++;
        }
        return panel;
    }


    private static void showCountryInfo(Country country) {
        // Show country info and currency rate calculator for the respective currency
        JOptionPane.showMessageDialog(null,
                "Country: " + country.getName() + "\nCurrency: " + country.getCurrency(),
                "Country Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static Map<String, Double> loadCurrencyRates() {
        Map<String, Double> rates = new HashMap<>();
        try {
            // Connect to the NBP API
            String url = "https://api.nbp.pl/api/exchangerates/tables/A/?format=json";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON
            String jsonResponse = response.toString();
            String ratesSection = jsonResponse.split("\"rates\":\\[")[1].split("]")[0];
            String[] currencyEntries = ratesSection.split("},\\{");

            for (String entry : currencyEntries) {
                entry = entry.replace("{", "").replace("}", "");
                try {
                    String currencyName = entry.split("\"currency\":\"")[1].split("\"")[0].trim();
                    String currencyCode = entry.split("\"code\":\"")[1].split("\"")[0].trim();
                    Double rate = Double.valueOf(entry.split("\"mid\":")[1].trim());

                    rates.put(currencyCode, rate);
                } catch (Exception e) {
                    System.out.println("Error processing entry: " + entry);
                }
            }

            rates.put("PLN", 1.0); // Add Polish złoty
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    // Class to hold country information
    static class Country {
        private final String name;
        private final String currency;

        public Country(String name, String currency) {
            this.name = name;
            this.currency = currency;
        }

        public String getName() {
            return name;
        }

        public String getCurrency() {
            return currency;
        }
    }
}

