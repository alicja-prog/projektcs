package com.example.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class User {
    private String username;
    private String password;
    private HashSet<Country> favouriteCountries;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favouriteCountries = new HashSet<>();

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

    public Collection<Country> getFavouriteCountries() {
        return favouriteCountries;
    }

    public void addFavouriteCountry(Country country) {
            this.favouriteCountries.add(country);
    }
    public void removeFavouriteCountry(Country country) {
        favouriteCountries.remove(country);
    }

    public boolean isFavoriteCountry(Country country) {
        return favouriteCountries.contains(country);
    }

    public void setFavouriteCountries(List<Country> favoriteCountries) {
        this.favouriteCountries.clear();
        this.favouriteCountries.addAll(favoriteCountries);
    }
}