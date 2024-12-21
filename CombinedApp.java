package com.example.internal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CombinedApp {


    private static final String FILE_NAME = "accounts.txt";
    private  Map<String, String> users = new HashMap<>(); // Stores username and password
    private  CardLayout cardLayout = new CardLayout();
    private  JPanel mainPanel = new JPanel(cardLayout);
    private CurrencyConverterApp currencyConverterApp;
    private CountryListApp countryListApp;
    private JFrame mainFrame;
    private User loggedInUser = null;


    public CombinedApp() {
        loadAccountsFromFile();
        currencyConverterApp= new CurrencyConverterApp(this);
        countryListApp = new CountryListApp(this,currencyConverterApp);
        mainFrame =  new JFrame("Combined Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        mainPanel.add(createMainAppPanel(), "MainAppPanel");
        mainPanel.add(countryListApp.getPanel(), "CountryListPanel");
        mainPanel.add(currencyConverterApp.getPanel(), "CurrencyConverterPanel");

        mainPanel.add(createInitialPanel(), "InitialPanel");
        mainPanel.add(createLoginPanel(), "LoginPanel");
        mainPanel.add(createRegisterPanel(), "RegisterPanel");
        mainPanel.add(createMainAppPanel(), "MainAppPanel");
//            mainPanel.add(createCurrencyConverterPanel(), "CurrencyConverterPanel");
//            mainPanel.add(createCountryListPanel(), "CountryListPanel");

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        // Show the initial panel
        cardLayout.show(mainPanel, "InitialPanel");
    }

    // Helper method to switch panels
    public void switchPanel(String panelName) {
        if (panelName=="CurrencyConverterPanel") {
            mainPanel.remove(currencyConverterApp.getPanel());
            mainPanel.add(currencyConverterApp.updatePanel(), "CurrencyConverterPanel");
        }
        cardLayout.show(mainPanel, panelName);
    }

    // Dummy methods for user data handling
    private boolean validateLogin(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
    private void login(User user) {
        this.loggedInUser = user;
    }

    private boolean registerUser(String username, String password) {
        if (users.containsKey(username)){
            return false;
        }
        users.put(username, password);
        saveAccountToFile(username, password); // Save the new account to the file
        return true;
    }




    private static void saveAccountToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + "," + password);
            writer.newLine(); // Write each account on a new line
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    private void loadAccountsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) { // Ensure we have both username and password
                    users.put(data[0], data[1]); // Load user into the map
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    //---------
    // from chatgpt


    private JPanel createInitialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Do you have an account?", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Yes");
        JButton registerButton = new JButton("No");

        loginButton.addActionListener(e -> switchPanel("LoginPanel"));
        registerButton.addActionListener(e -> switchPanel("RegisterPanel"));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Login");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("InitialPanel"));
        gbc.gridy = 4;
        panel.add(backButton, gbc);


        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (validateLogin(username, password)) {
                login(new User(username, password));
                JOptionPane.showMessageDialog(null, "Login successful!");
                switchPanel("MainAppPanel");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Try again.");
            }
        });
        panel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && panel.isShowing()) {
                SwingUtilities.getRootPane(panel).setDefaultButton(loginButton);
            }
        });
        return panel;
    }

    private  JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Register");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("InitialPanel"));
        gbc.gridy = 4;
        panel.add(backButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    switchPanel("LoginPanel");
                } else{
                    JOptionPane.showMessageDialog(null,"Registration failed. Account already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed. Fields cannot be empty.");
            }
        });

        return panel;
    }
    private  JPanel createMainAppPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Main Application", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton countryListButton = new JButton("Show Country List");
        JButton currencyConverterButton = new JButton("Currency Converter");

        countryListButton.addActionListener(e -> switchPanel("CountryListPanel"));
        currencyConverterButton.addActionListener(e -> switchPanel("CurrencyConverterPanel"));

        buttonPanel.add(countryListButton);
        buttonPanel.add(currencyConverterButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }


    public User getLoggedInUser() {
        return this.loggedInUser;
    }
}


