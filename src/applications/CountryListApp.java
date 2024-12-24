package com.example.internal.src.applications;

import com.example.internal.src.model.Country;
import com.example.internal.src.model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;


public class CountryListApp {


    private final App app;
    private JPanel countryListPanel;
    private CurrencyConverterApp currencyConverterApp;
    private DefaultListModel<Country>  countryListModel;

    public CountryListApp(App app, CurrencyConverterApp currencyConverterApp) {
        this.currencyConverterApp = currencyConverterApp;
        this.countryListPanel = createCountryListPanel();
        this.app = app;
    }

    // Getter for the panel
    public JPanel getPanel() {
        return this.countryListPanel;
    }

    public void selectContinent(String continent) {
        if (continent != null) {
            countryListModel.removeAllElements();
            countryListModel.addAll(Country.ALL_COUNTRIES.stream().filter(country -> country.getContinent().equals(Country.Continent.EUROPE)).collect(Collectors.toList()));
            return;
        }

    }


    private JPanel createCountryListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // Create the country list model and JList
        countryListModel = new DefaultListModel<>();

        for (Country country : Country.ALL_COUNTRIES) {
            countryListModel.addElement(country);
        }
        JList<Country> countryList = new JList<>(countryListModel);
        countryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(countryList);

        // Add action listener for selection
        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Country selectedCountry = countryList.getSelectedValue();
                    if (selectedCountry != null) {
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
                if (filterText.equals("search")) {
                    countryListModel.addAll(Country.ALL_COUNTRIES);
                    return;
                }
                System.out.println("Filtering for: " + filterText); // Debug log
                countryListModel.clear();

                System.out.println("Filtering for: " + filterText); // Debugging

                for (Country country : Country.ALL_COUNTRIES) {
                    String lowerCaseCountry = country.getName().toLowerCase();

                    // Match the beginning of the name, whole name, or first letter
                    if (lowerCaseCountry.startsWith(filterText)) {
                        countryListModel.addElement(country);
                        System.out.println("Matched: " + country); // Debugging
                    }
                }

                // If no match is found, add a message to the list
                if (countryListModel.isEmpty()) {
//                    countryListModel.addElement("No matches found"); TODO
                    System.out.println("No matches found."); // Debugging
                }
            }
        });System.out.println("Updated list size: " + countryListModel.getSize()); // Debugging

        // Add components to the panel
        panel.add(searchField, BorderLayout.NORTH); // Add search bar at the top
        panel.add(listScrollPane, BorderLayout.CENTER); // Add the list in the center

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.switchPanel("MainAppPanel"));
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
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




}

