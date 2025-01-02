package com.example.internal.src.applications;

import com.example.internal.src.model.Country;
import com.example.internal.src.model.Currency;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CurrencyConverterApp {

    // Sample country data - in a real application, this could be loaded from a database or an API
    private App app;
    private JPanel currencyConverterPanel; // Attribute for the panel
    private String wantCurrencyCode = "EUR"; // Default value
    private Map<String, Double> currencyRates;
    private String haveCurrencyCode = "PLN";
    private double amount = 100;

    public CurrencyConverterApp(App app) {
        this.app = app;
        this.currencyRates = loadCurrencyRates();
        this.currencyConverterPanel = createCurrencyConverterPanel();
    }

    public JPanel updatePanel() {
        currencyConverterPanel = createCurrencyConverterPanel();
        return currencyConverterPanel;
    }
    // Getter for the panel
    public JPanel getPanel() {
        return currencyConverterPanel;
    }

    // Method to update the selected currency
    public void setWantCurrencyCode(String currencyCode) {
        this.wantCurrencyCode = currencyCode;
    }
    public void setHaveCurrencyCode(String currencyCode) {
        this.haveCurrencyCode = currencyCode;
    }

    private JPanel createCurrencyConverterPanel() {
        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/static_files/background.jpg")); // Update with your image path
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load background image", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create the main panel with a background image
        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    // Draw the image to fill the panel
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // NORTH region: Title label
        JLabel titleLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margins around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField amountField = new JTextField(String.valueOf(amount));
        centerPanel.add(amountField, gbc);

        // "I have" label and dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("I have:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<com.example.internal.src.model.Currency> haveCurrencyBox = new JComboBox<>(com.example.internal.src.model.Currency.ALL_CURRENCIES.toArray(new com.example.internal.src.model.Currency[0]));
        haveCurrencyBox.setSelectedItem(com.example.internal.src.model.Currency.ALL_CURRENCIES.stream()
                .filter(c -> c.getCode().equals(haveCurrencyCode))
                .findFirst()
                .orElse(null));
        centerPanel.add(haveCurrencyBox, gbc);

        // "I want" label and dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("I want:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<com.example.internal.src.model.Currency> wantCurrencyBox = new JComboBox<>(com.example.internal.src.model.Currency.ALL_CURRENCIES.toArray(new com.example.internal.src.model.Currency[0]));
        wantCurrencyBox.setSelectedItem(com.example.internal.src.model.Currency.ALL_CURRENCIES.stream()
                .filter(c -> c.getCode().equals(wantCurrencyCode))
                .findFirst()
                .orElse(null));
        centerPanel.add(wantCurrencyBox, gbc);

        // Result label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        centerPanel.add(new JLabel("Result:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel resultLabel = new JLabel("Click 'Convert' button to calculate");
        centerPanel.add(resultLabel, gbc);

        // Convert button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton convertButton = new JButton("Convert");
        centerPanel.add(convertButton, gbc);

        // Action Listener for the Convert button
        convertButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                com.example.internal.src.model.Currency haveCurrency = (com.example.internal.src.model.Currency) haveCurrencyBox.getSelectedItem();
                com.example.internal.src.model.Currency wantCurrency = (Currency) wantCurrencyBox.getSelectedItem();
                double result = calculateExchangeResult(amount, haveCurrency.getCode(), wantCurrency.getCode());
                resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, haveCurrency.getCode(), result, wantCurrency.getCode()));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid amount entered!");
            }
        });

        panel.add(centerPanel, BorderLayout.CENTER);
        // WEST region: Optional country buttons (if needed)
        JPanel westPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Dynamic rows, single column
        westPanel.setOpaque(false);
        if (this.app.getLoginManager().getLoggedInUser() != null) {
            Collection<Country> favCountries = this.app.getLoginManager().getLoggedInUser().getFavouriteCountries();
            if (favCountries != null) {
                for (Country country : favCountries) {
                    JButton countryButton = new JButton(country.getName());
                    countryButton.addActionListener(e -> showCountryInfo(country));
                    westPanel.add(countryButton);
                }
            }
        }
        panel.add(westPanel, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }



    private double calculateExchangeResult(double amount, String fromCurrency, String toCurrency) {
        double exchangeRate = this.currencyRates.get(toCurrency) / this.currencyRates.get(fromCurrency);
        return amount / exchangeRate;
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

