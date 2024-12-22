package com.example.internal;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginManager {

    private static final String FILE_NAME = "accounts.txt";
    private User loggedInUser;
    private Map<String, User> users = new HashMap<>(); // Stores username and password

    public LoginManager() {
        loadAccountsFromFile();
    }

    public void login(String username) {
        this.loggedInUser = users.get(username);
    }
    public boolean registerUser(String username, String password) {
        System.out.println("Registering username: " + username); // Debugging
        if (users.containsKey(username)){
            System.out.println("Username already exists: " + username); // Debugging
            return false;
        }
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveAccountsToFile(); // Save the new account to the file
        System.out.println("User registered: " + username); // Debugging
        return true;
    }
    public boolean validateLogin(String username, String password) {
        System.out.println("Validating username: " + username + ", password: " + password); // Debugging
        if (users.containsKey(username)) {
            System.out.println("User found: " + username); // Debugging
            boolean passwordMatch = users.get(username).getPassword().equals(password);
            System.out.println("Password match: " + passwordMatch); // Debugging
            return passwordMatch;
        } else {
            System.out.println("User not found: " + username); // Debugging
        }
        return false;
    }
    public void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users.values()) {
                StringBuilder favorites = new StringBuilder();
                for (Country country : user.getFavouriteCountries()) {
                    if (!favorites.isEmpty()) {
                        favorites.append("|");
                    }
                    favorites.append(country.getName()).append(":").append(country.getCurrency());
                }
                String line = user.getUsername() + "," + user.getPassword() + "," + favorites.toString();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    private void loadAccountsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading line: " + line); // Debugging
                String[] data = line.split(",");
                if (data.length >= 2) { // Ensure we have both username and password
                    String username = data[0];
                    String password = data[1];
                    System.out.println("Parsed username: " + username + ", password: " + password); // Debugging
                    List<Country> favoriteCountries = new ArrayList<>();
                    if (data.length == 3 && !data[2].isEmpty()) { // Check for favorite countries
                        String[] countries = data[2].split("\\|");
                        for (String countryData : countries) {
                            String[] countryInfo = countryData.split(":");
                            if (countryInfo.length == 2) {
                                favoriteCountries.add(new Country(countryInfo[0], countryInfo[1]));
                            }
                        }
                    }

                    User user = new User(username, password);
                    for (int i = 0; i < favoriteCountries.size(); i++) {
                        user.addFavouriteCountry(favoriteCountries.get(i));
                    }
//                    user.setFavouriteCountries(favoriteCountries);
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
