package com.example.internal;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CountryListApp {

    private static final List<String> COUNTRIES = Arrays.asList(
            "Andorra", "Australia", "Austria", "Belarus", "Belgium", "Bulgaria", "Canada", "Chile", "China",
            "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hong Kong", "Hungary",
            "India", "Indonesia", "Ireland", "Israel", "Italy", "Japan", "Kosovo", "Latvia", "Lithuania", "Luxembourg",
            "Malta", "Mexico", "Monaco", "Montenegro", "New Zealand", "Norway", "Philippines", "Poland", "Portugal",
            "Republic of South Africa", "Romania", "Slovakia", "Slovenia", "Singapore", "Spain", "Sweden",
            "Switzerland", "Thailand", "Turkey", "United Kingdom", "United States", "Vatican City"
    );
    private final CombinedApp combinedApp;


    private JPanel countryListPanel;
    private CurrencyConverterApp currencyConverterApp;

    public CountryListApp(CombinedApp combinedApp, CurrencyConverterApp currencyConverterApp) {
        this.currencyConverterApp = currencyConverterApp;
        this.countryListPanel = createCountryListPanel();
        this.combinedApp = combinedApp;
    }

    // Getter for the panel
    public JPanel getPanel() {
        return countryListPanel;
    }

    private JPanel createCountryListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> countryList = new JList<>(COUNTRIES.toArray(new String[0]));
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(countryList);

        // Add action listener for selection
        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedCountry = countryList.getSelectedValue();
                    showCountryInfo(selectedCountry); // Call method to show info
                }
            }
        });

        panel.add(listScrollPane, BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> combinedApp.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    private void showCountryInfo(String country) {
        String currencyCode = getCurrencyCode(country);
        Double exchangeRate = this.currencyConverterApp.getCurrencyRates().get(currencyCode);

        String exchangeRateInfo = (exchangeRate != null)
                ? String.format("Exchange Rate (to %s): %.4f PLN", currencyCode, exchangeRate)
                : "Exchange Rate not available";

        JDialog dialog = new JDialog();
        dialog.setTitle("Country Information");
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("<html>You selected: " + country + "<br>" + exchangeRateInfo + "</html>", SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton calculatorButton = new JButton("Open Currency Calculator");
        calculatorButton.addActionListener(e -> {
            currencyConverterApp.setWantCurrencyCode(currencyCode);
            combinedApp.switchPanel("CurrencyConverterPanel");
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculatorButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }


    public  void createAndShowGUI() {
        JFrame frame = new JFrame("Country List");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.add(getPanel());
        frame.setVisible(true);
    }


    public static List<Country> getAllCountries(){
        List<Country> countryList=new ArrayList<>();
        for (int i = 0; i < COUNTRIES.size(); i++) {
            countryList.add(new Country(COUNTRIES.get(i),getCurrencyCode(COUNTRIES.get(i))));
        }
        return countryList;
    }



    private static String getCurrencyCode(String country) {
        // Mapowanie krajów na kody walut
        switch (country) {
            case "Andorra":
                return "EUR";
            case "Australia":
                return "AUD";
            case "Austria":
                return "EUR";
            case "Belarus":
                return "BYN";
            case "Belgium":
                return "EUR";
            case "Bulgaria":
                return "BGN";
            case "Canada":
                return "CAD";
            case "Chile":
                return "CLP";
            case "China":
                return "CNY";
            case "Czech Republic":
                return "CZK";
            case "Denmark":
                return "DKK";
            case "Estonia":
                return "EUR";
            case "Finland":
                return "EUR";
            case "France":
                return "EUR";
            case "Germany":
                return "EUR";
            case "Greece":
                return "EUR";
            case "Hong Kong":
                return "HKD";
            case "Hungary":
                return "HUF";
            case "India":
                return "INR";
            case "Indonesia":
                return "IDR";
            case "Ireland":
                return "EUR";
            case "Israel":
                return "ILS";
            case "Italy":
                return "EUR";
            case "Japan":
                return "JPY";
            case "Kosovo":
                return "EUR";
            case "Latvia":
                return "EUR";
            case "Lithuania":
                return "EUR";
            case "Luxembourg":
                return "EUR";
            case "Malta":
                return "EUR";
            case "Mexico":
                return "MXN";
            case "Monaco":
                return "EUR";
            case "Montenegro":
                return "EUR";
            case "New Zealand":
                return "NZD";
            case "Norway":
                return "NOK";
            case "Philippines":
                return "PHP";
            case "Poland":
                return "PLN";
            case "Portugal":
                return "EUR";
            case "Republic of South Africa":
                return "ZAR";
            case "Romania":
                return "RON";
            case "Slovakia":
                return "EUR";
            case "Slovenia":
                return "EUR";
            case "Singapore":
                return "SGD";
            case "Spain":
                return "EUR";
            case "Sweden":
                return "SEK";
            case "Switzerland":
                return "CHF";
            case "Thailand":
                return "THB";
            case "Turkey":
                return "TRY";
            case "United Kingdom":
                return "GBP";
            case "United States":
                return "USD";
            case "Vatican City":
                return "EUR";
            default:
                return null; // Jeśli kraj nie został znaleziony
        }
    }
}

