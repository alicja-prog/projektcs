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
import java.util.Arrays;
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
    private JPanel currencyConverterPanel; // Attribute for the panel
    private String selectedCountryCode = "EUR"; // Default value
    private Map<String, Double> currencyRates;

    public CurrencyConverterApp() {
        this.currencyRates = loadCurrencyRates();
        this.currencyConverterPanel = createCurrencyConverterPanel();
    }

    // Getter for the panel
    public JPanel getPanel() {
        return currencyConverterPanel;
    }

    // Method to update the selected currency
    public void setSelectedCountryCode(String currencyCode) {
        this.selectedCountryCode = currencyCode;
    }

    private JPanel createCurrencyConverterPanel() {
        // Use BorderLayout for the main panel
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // 10px gaps between components

        // NORTH region: Title label
        JLabel titleLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margins around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

// Amount label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3; // Give some space for the label
        centerPanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7; // Stretch the field to fill the remaining space
        JTextField amountField = new JTextField("100.00");
        centerPanel.add(amountField, gbc);

// "I have" label and dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("I have:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<String> haveCurrency = new JComboBox<>(createLabelArray());
        haveCurrency.setSelectedItem("PLN");
        centerPanel.add(haveCurrency, gbc);

// "I want" label and dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("I want:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<String> wantCurrency = new JComboBox<>(createLabelArray());
        wantCurrency.setSelectedItem(selectedCountryCode);
        centerPanel.add(wantCurrency, gbc);

// Result label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("Result:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel resultLabel = new JLabel("100.00 PLN = 23.38 EUR");
        centerPanel.add(resultLabel, gbc);

// Convert button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Span both columns
        gbc.weightx = 1.0; // Center the button
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        JButton convertButton = new JButton("Convert");
        centerPanel.add(convertButton, gbc);

// Action Listeners
        convertButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String fromCurrency = (String) haveCurrency.getSelectedItem();
                String toCurrency = (String) wantCurrency.getSelectedItem();

                Map<String, Double> rates = loadCurrencyRates();
                double exchangeRate = rates.get(toCurrency) / rates.get(fromCurrency);
                double result = amount / exchangeRate;
                resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, result, toCurrency));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid amount entered!");
            }
        });
        panel.add(centerPanel, BorderLayout.CENTER);

        // WEST region: Optional country buttons (if needed)
        JPanel westPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Dynamic rows, single column
        for (Country country : COUNTRIES) {
            JButton countryButton = new JButton(country.getName());
            countryButton.addActionListener(e -> showCountryInfo(country));
            westPanel.add(countryButton);
        }
        panel.add(westPanel, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> CombinedApp.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    private String[] createLabelArray() {
        String[] array = this.currencyRates.keySet().toArray(new String[0]);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i]
        }
    }


    private static void showCountryInfo(Country country) {
        // Show country info and currency rate calculator for the respective currency
        JOptionPane.showMessageDialog(null,
                "Country: " + country.getName() + "\nCurrency: " + country.getCurrency(),
                "Country Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Map<String, Double> loadCurrencyRates() {
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

    public Map<String, Double> getCurrencyRates() {
        return currencyRates;
    }
}

