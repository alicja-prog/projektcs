package com.example.internal;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Country> favouriteCountries;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favouriteCountries = new ArrayList<>();
        this.favouriteCountries.add(new Country("United States", "USD"));
        this.favouriteCountries.add(new Country("Eurozone", "EUR"));
        this.favouriteCountries.add(new Country("United Kingdom", "GBP"));
        this.favouriteCountries.add(new Country("Japan", "JPY"));
        ;
        //favouriteCountries = List.of(new Country[]{

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Country> getFavouriteCountries() {
        return favouriteCountries;
    }

    public void setFavouriteCountries(List<Country> favouriteCountries) {
        this.favouriteCountries = favouriteCountries;
    }
    public void addFavouriteCountry(Country country) {
        if (!isFavoriteCountry(country.getName())) {
            this.favouriteCountries.add(country);
        }
    }
    public void removeFavouriteCountry(String countryName) {
        this.favouriteCountries.removeIf(country -> country.getName().equalsIgnoreCase(countryName));
    }

    public boolean isFavoriteCountry(String countryName) {
        return this.favouriteCountries.stream()
                .anyMatch(country -> country.getName().equalsIgnoreCase(countryName));
    }
}