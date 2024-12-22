package com.example.internal;

import java.util.Objects;

public class Country {
    private String name;
    private String currency;
    


    public Country(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) && Objects.equals(currency, country.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currency);
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

