package com.example.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.internal.Currency.findCurrencyByCode;


public class Country {
    private String name;
    private Currency currency;
    private Continent continent;

    // Constructor
    public Country(String name, Currency currency, Continent continent) {
        this.name = name;
        this.currency = currency;
        this.continent = continent;
        currency.addCountry(this); // Associate the country with its currency
    }

    public String getLabel() {
        return this.currency + "   " + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,continent, currency);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", continent=" + continent +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public enum Continent {
        AFRICA,
        ASIA,
        EUROPE,
        NORTH_AMERICA,
        SOUTH_AMERICA,
        OCEANIA,
        ANTARCTICA
    }
    public static final List<Country> ALL_COUNTRIES = new ArrayList<>();

    static {
            Currency euro = findCurrencyByCode("EUR");
            Currency bgn = findCurrencyByCode("BGN");
            Currency cad = findCurrencyByCode("CAD");
            Currency clp = findCurrencyByCode("CLP");
            Currency cny = findCurrencyByCode("CNY");
            Currency czk = findCurrencyByCode("CZK");
            Currency dkk = findCurrencyByCode("DKK");
            Currency hkd = findCurrencyByCode("HKD");
            Currency huf = findCurrencyByCode("HUF");
            Currency inr = findCurrencyByCode("INR");
            Currency idr = findCurrencyByCode("IDR");
            Currency ils = findCurrencyByCode("ILS");
            Currency jpy = findCurrencyByCode("JPY");
            Currency mxn = findCurrencyByCode("MXN");
            Currency nzd = findCurrencyByCode("NZD");
            Currency nok = findCurrencyByCode("NOK");
            Currency php = findCurrencyByCode("PHP");
            Currency pln = findCurrencyByCode("PLN");
            Currency ron = findCurrencyByCode("RON");
            Currency sgd = findCurrencyByCode("SGD");
            Currency sek = findCurrencyByCode("SEK");
            Currency chf = findCurrencyByCode("CHF");
            Currency thb = findCurrencyByCode("THB");
            Currency zar = findCurrencyByCode("ZAR");
            Currency gbp = findCurrencyByCode("GBP");
            Currency usd = findCurrencyByCode("USD");

            // Add countries to the list
            ALL_COUNTRIES.add(new Country("Belgium", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Germany", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("France", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Italy", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Spain", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Portugal", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Netherlands", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Austria", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Finland", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Ireland", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Luxembourg", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Malta", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Slovakia", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Slovenia", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Estonia", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Latvia", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Lithuania", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Andorra", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Monaco", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("San Marino", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Vatican City", euro, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Bulgaria", bgn, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Canada", cad, Continent.NORTH_AMERICA));
            ALL_COUNTRIES.add(new Country("Chile", clp, Continent.SOUTH_AMERICA));
            ALL_COUNTRIES.add(new Country("China", cny, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Czech Republic", czk, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Denmark", dkk, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Hong Kong", hkd, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Hungary", huf, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("India", inr, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Indonesia", idr, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Israel", ils, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Japan", jpy, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Mexico", mxn, Continent.NORTH_AMERICA));
            ALL_COUNTRIES.add(new Country("New Zealand", nzd, Continent.OCEANIA));
            ALL_COUNTRIES.add(new Country("Norway", nok, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Philippines", php, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Poland", pln, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Romania", ron, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Singapore", sgd, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("Sweden", sek, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Switzerland", chf, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("Thailand", thb, Continent.ASIA));
            ALL_COUNTRIES.add(new Country("South Africa", zar, Continent.AFRICA));
            ALL_COUNTRIES.add(new Country("United Kingdom", gbp, Continent.EUROPE));
            ALL_COUNTRIES.add(new Country("United States", usd, Continent.NORTH_AMERICA));
            ALL_COUNTRIES.add(new Country("Puerto Rico", usd, Continent.NORTH_AMERICA));
            ALL_COUNTRIES.add(new Country("Guam", usd, Continent.OCEANIA));
            ALL_COUNTRIES.add(new Country("American Samoa", usd, Continent.OCEANIA));
            ALL_COUNTRIES.add(new Country("US Virgin Islands", usd, Continent.NORTH_AMERICA));
            ALL_COUNTRIES.add(new Country("Northern Mariana Islands", usd, Continent.OCEANIA));
        }


    }

