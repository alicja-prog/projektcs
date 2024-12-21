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
        this.favouriteCountries = new ArrayList<Country>();
        favouriteCountries = List.of(new Country[]{
                new Country("United States", "USD"),
                new Country("Eurozone", "EUR"),
                new Country("United Kingdom", "GBP"),
                new Country("Japan", "JPY"),
        });
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
    public void addFavouriteCountry(Country country) {
        this.favouriteCountries.add(country);
    }


}
