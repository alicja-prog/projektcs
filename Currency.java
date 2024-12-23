package com.example.internal;
import java.util.ArrayList;
import java.util.List;

public class Currency {
    private String code;
    private String name;
    private List<Country> countries; // Countries using this currency

    // Constructor
    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
        this.countries = new ArrayList<>();
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Country> getCountries() {
        return countries;
    }

    // Add a country that uses this currency
    public void addCountry(Country country) {
        countries.add(country);
    }

    @Override
    public String toString() {
        String countriesList = countries.stream()
                .map(Country::getName) // Get the names of associated countries
                .sorted() // Sort alphabetically for better readability
                .reduce((a, b) -> a + ", " + b) // Combine names into a single string
                .orElse("No countries"); // Default if no countries are associated

        if (code=="EUR") {
            return "Euro";
        }else if (code=="USD") {
            return "US Dollar";
        }else {
            return code + " (" + name + ") - Used in: [" + countriesList + "]";
        }
    }

    public static Currency findCurrencyByCode(String code) {
        return Currency.ALL_CURRENCIES.stream()
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency with code " + code + " not found."));
    }


    // Predefined list of all currencies
    public static final List<Currency> ALL_CURRENCIES = new ArrayList<>();


    static {
        ALL_CURRENCIES.add(new Currency("EUR", "Euro"));
        ALL_CURRENCIES.add(new Currency("BGN", "Bulgarian Lev"));
        ALL_CURRENCIES.add(new Currency("HKD", "Hong Kong Dollar"));
        ALL_CURRENCIES.add(new Currency("HUF", "Hungarian Forint"));
        ALL_CURRENCIES.add(new Currency("INR", "Indian Rupee"));
        ALL_CURRENCIES.add(new Currency("IDR", "Indonesian Rupiah"));
        ALL_CURRENCIES.add(new Currency("ILS", "Israeli Shekel"));
        ALL_CURRENCIES.add(new Currency("JPY", "Japanese Yen"));
        ALL_CURRENCIES.add(new Currency("MXN", "Mexican Peso"));
        ALL_CURRENCIES.add(new Currency("NZD", "New Zealand Dollar"));
        ALL_CURRENCIES.add(new Currency("NOK", "Norwegian Krone"));
        ALL_CURRENCIES.add(new Currency("PHP", "Philippine Peso"));
        ALL_CURRENCIES.add(new Currency("PLN", "Polish Zloty"));
        ALL_CURRENCIES.add(new Currency("RON", "Romanian Leu"));
        ALL_CURRENCIES.add(new Currency("SGD", "Singapore Dollar"));
        ALL_CURRENCIES.add(new Currency("SEK", "Swedish Krona"));
        ALL_CURRENCIES.add(new Currency("CHF", "Swiss Franc"));
        ALL_CURRENCIES.add(new Currency("THB", "Thai Baht"));
        ALL_CURRENCIES.add(new Currency("TRY", "Turkish Lira"));
        ALL_CURRENCIES.add(new Currency("CAD", "Canadian Dollar"));
        ALL_CURRENCIES.add(new Currency("CLP", "Chilean Peso"));
        ALL_CURRENCIES.add(new Currency("CNY", "Chinese Yuan"));
        ALL_CURRENCIES.add(new Currency("DKK", "Danish Krone"));
        ALL_CURRENCIES.add(new Currency("CZK", "Czech Koruna"));
        ALL_CURRENCIES.add(new Currency("GBP", "British Pound"));
        ALL_CURRENCIES.add(new Currency("USD", "US Dollar"));
        ALL_CURRENCIES.add(new Currency("ZAR", "South African Rand"));
    }

}
