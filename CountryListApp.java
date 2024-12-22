package com.example.internal;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.DefaultListModel;


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
        // Create the country list model and JList
        DefaultListModel<String> countryListModel = new DefaultListModel<>();

        for (String country : COUNTRIES) {
            countryListModel.addElement(country);
        }
        JList<String> countryList = new JList<>(countryListModel);
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(countryList);

        // Add action listener for selection
        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedCountry = countryList.getSelectedValue();
                    if (!selectedCountry.equals("No matches found")) {
                        showCountryInfo(selectedCountry); // Call method to show info


                    }    }
            }
        });
        // Create the search bar (loupe)
        JTextField searchField = new JTextField();
        searchField.setText("Search");
        searchField.setForeground(Color.GRAY); // Placeholder text color

        searchField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK); // User input text color
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY); // Placeholder text color
                }
            }
        });
        /*searchField.addKeyListener(new java.awt.event.KeyAdapter(){
                @Override
                public void keyTyped (java.awt.event.KeyEvent e){
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK); // Change to input text color
                }
            }

    });
*/

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                System.out.println("Insert event detected."); // Debugging
                filterList();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                System.out.println("Change event detected."); // Debugging
                filterList();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            // Filter the country list based on the search bar input
            private void filterList() {
                String filterText = searchField.getText().toLowerCase();
                System.out.println("Filtering for: " + filterText); // Debug log
                countryListModel.clear();

                System.out.println("Filtering for: " + filterText); // Debugging

                for (String country : COUNTRIES) {
                    String lowerCaseCountry = country.toLowerCase();

                    // Match the beginning of the name, whole name, or first letter
                    if (lowerCaseCountry.startsWith(filterText)) {
                        countryListModel.addElement(country);
                        System.out.println("Matched: " + country); // Debugging
                    }
                }

                // If no match is found, add a message to the list
                if (countryListModel.isEmpty()) {
                    countryListModel.addElement("No matches found");
                    System.out.println("No matches found."); // Debugging
                }
            }
        });System.out.println("Updated list size: " + countryListModel.getSize()); // Debugging

        // Add components to the panel
        panel.add(searchField, BorderLayout.NORTH); // Add search bar at the top
        panel.add(listScrollPane, BorderLayout.CENTER); // Add the list in the center

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> combinedApp.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    private void showCountryInfo(String countryName) {
        String currencyCode = getCurrencyCode(countryName);
        Country country = new Country(countryName, currencyCode);
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



        User currentUser = combinedApp.getLoginManager().getLoggedInUser();
        if (currentUser !=null) {
            // Create heart button
            JButton heartButton = new JButton();
            heartButton.setPreferredSize(new Dimension(50, 50));
            heartButton.setOpaque(true);
            heartButton.setContentAreaFilled(true);
            // TODO Heart is not visible at the start of app (even though its liked)
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
                        combinedApp.getLoginManager().saveAccountsToFile();
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
            combinedApp.switchPanel("CurrencyConverterPanel");
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

