package com.example.internal.src.applications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;


public class App {


    private  CardLayout cardLayout = new CardLayout();
    private  JPanel mainPanel = new JPanel(cardLayout);
    private CurrencyConverterApp currencyConverterApp;



    private CountryListApp countryListApp;
    private JFrame mainFrame;
    private WorldMapApp worldMapApp;
    private LoginManager loginManager;


    public App() {
        loginManager = new LoginManager();
        currencyConverterApp= new CurrencyConverterApp(this);
        countryListApp = new CountryListApp(this,currencyConverterApp);

        worldMapApp = new WorldMapApp(this);

        mainFrame =  new JFrame("Combined Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(750, 550);
        mainFrame.setLocationRelativeTo(null);
        mainPanel.add(createMainAppPanel(), "MainAppPanel");
        mainPanel.add(countryListApp.getPanel(), "CountryListPanel");
        mainPanel.add(currencyConverterApp.getPanel(), "CurrencyConverterPanel");
        mainPanel.add(worldMapApp.getUI(), "WorldMapAppPanel");
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
            if (loginManager.validateLogin(username, password)) {
                loginManager.login(username);
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
                if (loginManager.registerUser(username, password)) {
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
    private JPanel createMainAppPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Main Application", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton countryListButton = new JButton("Show Country List");
        JButton worldMapButton = new JButton("Open world map");
        JButton currencyConverterButton = new JButton("Currency Converter");

        countryListButton.addActionListener(e -> switchPanel("CountryListPanel"));
        worldMapButton.addActionListener(e -> switchPanel("WorldMapAppPanel"));
        currencyConverterButton.addActionListener(e -> switchPanel("CurrencyConverterPanel"));

        buttonPanel.add(countryListButton);
        buttonPanel.add(worldMapButton);
        buttonPanel.add(currencyConverterButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }
    public CountryListApp getCountryListApp() {
        return countryListApp;
    }
}


