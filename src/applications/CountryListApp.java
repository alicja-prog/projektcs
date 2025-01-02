package com.example.internal.src.applications;

import com.example.internal.src.model.Country;
import com.example.internal.src.model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;


public class CountryListApp {
    HashMap<Country.Continent, JRadioButton> continentRadioButtonMap = new HashMap<>();
    private DefaultListModel<Country> countryListModel;
    private final App app;
    private JPanel countryListPanel;
    private CurrencyConverterApp currencyConverterApp;
    private JTextField searchField;


    public CountryListApp(App app, CurrencyConverterApp currencyConverterApp) {
        this.currencyConverterApp = currencyConverterApp;
        this.countryListPanel = createCountryListPanel();
        this.app = app;
    }

    // Getter for the panel
    public JPanel getPanel() {
        return this.countryListPanel;
    }


    private JPanel createCountryListPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create the country list model and JList
        countryListModel = new DefaultListModel<>();
        Country.ALL_COUNTRIES.forEach(countryListModel::addElement); // Populate the model with all countries
        JList<Country> countryList = new JList<>(countryListModel);
        countryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(countryList);

        // Add a listener for list selection
        countryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Country selectedCountry = countryList.getSelectedValue();
                if (selectedCountry != null) {
                    showCountryInfo(selectedCountry); // Display country info
                }
            }
        });

        // Create the search bar
        searchField = new JTextField("Search");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        // Add a DocumentListener to the search field
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateCountryListModel();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateCountryListModel();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateCountryListModel();
            }
        });

        // Create a ButtonGroup for the radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();

        // Add radio buttons to the map and group
        continentRadioButtonMap.put(Country.Continent.WHOLE_WORLD, new JRadioButton("All Countries"));
        continentRadioButtonMap.put(Country.Continent.EUROPE, new JRadioButton("Europe"));
        continentRadioButtonMap.put(Country.Continent.AFRICA, new JRadioButton("Africa"));
        continentRadioButtonMap.put(Country.Continent.NORTH_AMERICA, new JRadioButton("North America"));
        continentRadioButtonMap.put(Country.Continent.SOUTH_AMERICA, new JRadioButton("South America"));
        continentRadioButtonMap.put(Country.Continent.OCEANIA, new JRadioButton("Australia"));
        continentRadioButtonMap.put(Country.Continent.ASIA, new JRadioButton("Asia"));

        JPanel radioPanel = new JPanel();
        continentRadioButtonMap.forEach((continent, radioButton) -> {
            buttonGroup.add(radioButton); // Ensure only one button is selected at a time
            radioPanel.add(radioButton); // Add the button to the UI
            radioButton.addActionListener(e -> updateCountryListModel());
        });

        continentRadioButtonMap.get(Country.Continent.WHOLE_WORLD).setSelected(true);
        // Create a top panel to hold the search field and radio buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(radioPanel, BorderLayout.NORTH); // Add radio buttons at the top
        topPanel.add(searchField, BorderLayout.CENTER); // Add search field below

        // Add components to the main panel
        panel.add(listScrollPane, BorderLayout.CENTER); // Add the country list in the center
        panel.add(topPanel, BorderLayout.NORTH); // Add the top panel at the top

        // Add a back button at the bottom
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }


    public List<Country> filterCountriesBasedOnContinent() {
        // Iterate through the radio buttons to find the selected one
        for (Map.Entry<Country.Continent, JRadioButton> entry : continentRadioButtonMap.entrySet()) {
            JRadioButton radioButton = entry.getValue();
            if (radioButton.isSelected()) {
                Country.Continent selectedContinent = entry.getKey();
                if (selectedContinent == Country.Continent.WHOLE_WORLD) {
                    // If "All Countries" is selected, return all countries
                    return new ArrayList<>(Country.ALL_COUNTRIES);
                } else {
                    // Filter countries by the selected continent
                    return Country.ALL_COUNTRIES.stream()
                            .filter(country -> country.getContinent().equals(selectedContinent))
                            .collect(Collectors.toList());
                }
            }
        }
        // Default case: return an empty list if no button is selected
        return new ArrayList<>();
    }
    private void updateCountryListModel() {
        countryListModel.clear();
        List<Country> continentCountries = filterCountriesBasedOnContinent();
        String filterText = searchField.getText().toLowerCase();
        if (filterText.equals("search") || filterText.equals("")) {
            countryListModel.addAll(continentCountries);
            return;
        }
        for (Country country : continentCountries) {
            if(country.getName().toLowerCase().startsWith(filterText.toLowerCase())) {
                countryListModel.addElement(country);
            }
        }
    }

    private void showCountryInfo(Country countryName) {
        Country country = Country.ALL_COUNTRIES.stream().filter((c) -> c.equals(countryName)).findFirst().get();
        String currencyCode = country.getCurrency().getCode();
        Double exchangeRate = this.currencyConverterApp.getCurrencyRates().get(currencyCode);

        String exchangeRateInfo = (exchangeRate != null)
                ? String.format("Exchange Rate (to %s): %.4f PLN", currencyCode, exchangeRate)
                : "Exchange Rate not available";

        JDialog dialog = new JDialog();
        dialog.setTitle("Country Information");
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("<html>You selected: " + countryName + "<br>" + exchangeRateInfo + "</html>", SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);



        User currentUser = app.getLoginManager().getLoggedInUser();
        if (currentUser !=null) {
            // Create heart button
            JButton heartButton = new JButton();
            heartButton.setPreferredSize(new Dimension(50, 50));
            heartButton.setOpaque(true);
            heartButton.setContentAreaFilled(true);
            updateHeartIcon(heartButton,currentUser.isFavoriteCountry(country));
            // Add action listener to toggle heart state
            heartButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        if (currentUser.isFavoriteCountry(country)) {
                            currentUser.removeFavouriteCountry(country);
                        }else {
                            currentUser.addFavouriteCountry(country);
                        }
                    boolean favoriteCountry = currentUser.isFavoriteCountry(country);
                    updateHeartIcon(heartButton, currentUser.isFavoriteCountry(country));
                        app.getLoginManager().saveAccountsToFile();
                }
            });

            // Add heart button below exchange rate info
            JPanel heartPanel = new JPanel();
            // Center-align the button
            heartPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            heartPanel.add(heartButton);

            panel.add(heartPanel, BorderLayout.NORTH);
        }


        JButton calculatorButton = new JButton("Open Currency Calculator");
        calculatorButton.addActionListener(e -> {
            currencyConverterApp.setWantCurrencyCode(currencyCode);
            app.switchPanel("CurrencyConverterPanel");
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculatorButton);
        panel.add(buttonPanel, BorderLayout.SOUTH); //przed było south

        dialog.add(panel);
        dialog.setVisible(true);


    }

    private void updateHeartIcon(JButton heartButton, boolean isFavorited) {
        Font heartFont = new Font("Serif", Font.PLAIN, 30); // Large font size for the heart
        heartButton.setFont(heartFont); // Set font for the heart button
        heartButton.setMargin(new Insets(0, 0, 0, 0)); // Remove extra padding
        heartButton.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the heart

        if (isFavorited) {
            heartButton.setText("\u2665"); // Full heart
            heartButton.setForeground(Color.RED);
        } else {
            heartButton.setText("\u2661"); // Outlined heart
            heartButton.setForeground(Color.RED);
        }
    }


    public  void createAndShowGUI() {
        JFrame frame = new JFrame("Country List");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.add(getPanel());
        frame.setVisible(true);
    }


    public void setContinent(Country.Continent continent) {
        searchField.setText("");
        continentRadioButtonMap.get(continent).setSelected(true);
        updateCountryListModel();
    }
}

