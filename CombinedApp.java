package com.example.internal;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinedApp {

    private static final String FILE_NAME = "accounts.txt";
    private static Map<String, String> users = new HashMap<>(); // Stores username and password

    // List of all countries
    private static final List<String> COUNTRIES = Arrays.asList(
            "Andorra", "Australia", "Austria", "Belarus", "Belgium", "Bulgaria", "Canada", "Chile", "China",
            "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hong Kong",
            "Hungary", "India", "Indonesia", "Ireland", "Israel", "Italy", "Japan", "Kosovo", "Latvia",
            "Lithuania", "Luxembourg", "Malta", "Mexico", "Monaco", "Montenegro", "New Zealand", "Norway",
            "Philippines", "Poland", "Portugal", "Republic of South Africa", "Romania", "Slovakia",
            "Slovenia", "Singapore", "Spain", "Sweden", "Switzerland", "Thailand", "Turkey", "United Kingdom",
            "United States", "Vatican City"
    );

    // Variable to hold currency rates
    private static Map<String, Double> currencyRates;

    public static void main(String[] args) {
        // Load currency rates when the application starts
        currencyRates = CurrencyRateLoader.loadCurrencyRates(); // Call static method from CurrencyRateLoader
        // Load existing accounts from the file
        loadAccountsFromFile();
        // Create and show the initial panel asking if the user has an account
        SwingUtilities.invokeLater(CombinedApp::createInitialPanel);
    }


    private static void createInitialPanel() {
        JFrame frame = new JFrame("Account Login");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel questionLabel = new JLabel("Do you have an account?");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(questionLabel);

        panel.add(Box.createVerticalStrut(20)); // Space between label and buttons

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Yes button for login
        JButton loginButton = new JButton("Yes");
        loginButton.setPreferredSize(new Dimension(150, 50)); // Set button size
        loginButton.addActionListener(e -> createAndShowLoginGUI());
        buttonPanel.add(loginButton);

        // No button for registration
        JButton registerButton = new JButton("No");
        registerButton.setPreferredSize(new Dimension(150, 50)); // Set button size
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);

        // Center the button panel
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(buttonPanel);

        // Setting up the window
        frame.add(panel);
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static void createAndShowLoginGUI() {
        JFrame frame = new JFrame("Login System");

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        // Login field
        loginPanel.add(new JLabel("Login:"));
        JTextField usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(200, 30)); // Setting size
        loginPanel.add(usernameField);

        loginPanel.add(Box.createVerticalStrut(5)); // Spacer

        // Password field
        loginPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Setting size
        loginPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40)); // Set preferred size for button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            handleLogin(username, password); // Handle the login process
        });
        loginPanel.add(loginButton);

        // Set up the login frame
        frame.add(loginPanel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }

    private static void handleLogin(String username, String password) {
        // Check if user exists in the map for login
        if (users.containsKey(username) && users.get(username).equals(password)) {
            JOptionPane.showMessageDialog(null, "Login successful!");
            createAndShowMainGUI(); // Proceed to main application on success
        } else {
            JOptionPane.showMessageDialog(null, "Login error. Try again.");
        }
    }

    private static void handleRegister() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        JTextField newUsernameField = new JTextField(10);
        JPasswordField newPasswordField = new JPasswordField(10);

        registerPanel.add(new JLabel("New Username:"));
        registerPanel.add(newUsernameField);
        registerPanel.add(Box.createVerticalStrut(5));
        registerPanel.add(new JLabel("New Password:"));
        registerPanel.add(newPasswordField);

        int registerResult = JOptionPane.showConfirmDialog(null, registerPanel, "Register", JOptionPane.OK_CANCEL_OPTION);

        if (registerResult == JOptionPane.OK_OPTION) {
            String newUsername = newUsernameField.getText();
            String newPassword = new String(newPasswordField.getPassword());

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
                return;
            }

            // Check if username already exists
            if (users.containsKey(newUsername)) {
                JOptionPane.showMessageDialog(null, "Username already exists.");
            } else {
                users.put(newUsername, newPassword); // Store new user in the map
                saveAccountToFile(newUsername, newPassword); // Save the new account to the file
                JOptionPane.showMessageDialog(null, "Registration successful! You can now log in.");
            }
        }
    }

    private static void saveAccountToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + "," + password);
            writer.newLine(); // Write each account on a new line
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    private static void loadAccountsFromFile() {
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

    public static void createAndShowMainGUI() {
        JFrame frame = new JFrame("Countries’ Data Concerning Costs of Living");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Countries' Data Concerning Costs of Living", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);

        JButton countryListButton = new JButton("Show Country List");
        countryListButton.addActionListener(e -> CountryListApp.createAndShowGUI()); // Open the country list

        panel.add(countryListButton, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }
}


