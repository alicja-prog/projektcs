package com.example.internal;

public class Country {
    private final String name;
    private final String currency;

    public Country(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }
}

