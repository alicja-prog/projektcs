package com.example.internal;

public class Country {
    private String name;
    private String currency;


    public Country(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

