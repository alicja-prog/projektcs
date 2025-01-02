package com.example.internal.src.applications;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class App {


    private  CardLayout cardLayout = new CardLayout();
    private  JPanel mainPanel = new JPanel(cardLayout);
    private CurrencyConverterApp currencyConverterApp;
    private CountryListApp countryListApp;
    private JFrame mainFrame;
    private WorldMapApp worldMapApp;
    private LoginManager loginManager;


    public App() {
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 16));
        UIManager.put("Button.background", Color.LIGHT_GRAY);
        UIManager.put("Button.foreground", Color.DARK_GRAY);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.BLACK));
        loginManager = new LoginManager();
        currencyConverterApp= new CurrencyConverterApp(this);
        countryListApp = new CountryListApp(this,currencyConverterApp);

        worldMapApp = new WorldMapApp(this);
        mainFrame =  new JFrame("Combined Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 550);
        mainFrame.setResizable(false); // Prevent resizing

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
        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/static_files/background.jpg")); // Update with your image path
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load background image", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create the main panel
        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    // Draw the image to fill the panel
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Create a GridBagConstraints object for layout control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
        gbc.anchor = GridBagConstraints.CENTER; // Center align components

        // Add the label to the panel
        JLabel label = new JLabel("Do you have an account?", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, gbc);

        // Add the buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Ensure transparency for the background

        JButton loginButton = new JButton("Yes");
        JButton registerButton = new JButton("No");

        loginButton.addActionListener(e -> switchPanel("LoginPanel"));
        registerButton.addActionListener(e -> switchPanel("RegisterPanel"));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridy = 1; // Position below the label
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createLoginPanel() {
        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/static_files/background.jpg")); // Update with your image path
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load background image", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create the main panel
        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    // Draw the image to fill the panel
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
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
                usernameField.setText("");
                passwordField.setText("");
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

    JPanel createRegisterPanel() {
        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("src/static_files/background.jpg")); // Update with your image path
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load background image", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create the main panel
        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    // Draw the image to fill the panel
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between components

        // Title label
        JLabel label = new JLabel("Register");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Register and Back buttons on the same row
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align both buttons
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        gbc.gridx = 1;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("InitialPanel"));
        panel.add(backButton, gbc);

        // Register button action
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                if (loginManager.registerUser(username, password)) {
                    usernameField.setText("");
                    passwordField.setText("");
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    switchPanel("LoginPanel");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Account already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed. Fields cannot be empty.");
            }
        });

        return panel;
    }

    private JPanel createMainAppPanel() {
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

        // Create the top panel for the title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        // Add the logout button to the top panel
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout()); // Replace handleLogout() with your logout logic
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Add the top panel to the main panel
        panel.add(topPanel, BorderLayout.NORTH);
        topPanel.setOpaque(false); // Ensure transparency for the background

        // Create the button panel to center the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = GridBagConstraints.RELATIVE; // Position rows sequentially
        gbc.insets = new Insets(10, 0, 10, 0); // Add spacing between buttons

        buttonPanel.setOpaque(false); // Ensure transparency for the background

        // Create buttons
        JButton countryListButton = new JButton("Show Country List");
        countryListButton.setPreferredSize(new Dimension(200, 50)); // Custom size
        countryListButton.addActionListener(e -> switchPanel("CountryListPanel"));

        JButton worldMapButton = new JButton("Open World Map");
        worldMapButton.setPreferredSize(new Dimension(200, 50)); // Custom size
        worldMapButton.addActionListener(e -> switchPanel("WorldMapAppPanel"));

        JButton currencyConverterButton = new JButton("Currency Converter");
        currencyConverterButton.setPreferredSize(new Dimension(200, 50)); // Custom size
        currencyConverterButton.addActionListener(e -> switchPanel("CurrencyConverterPanel"));

        // Add buttons to the button panel
        buttonPanel.add(countryListButton, gbc);
        buttonPanel.add(worldMapButton, gbc);
        buttonPanel.add(currencyConverterButton, gbc);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }



    private void handleLogout() {
        loginManager.logout();
        switchPanel("InitialPanel");
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public CountryListApp getCountryListApp() {
        return countryListApp;
    }

   // public void selectEuropeOnCountryListAppPanel() {
     //   this.countryListApp.selectContinent("Europe");
 //   }
}


