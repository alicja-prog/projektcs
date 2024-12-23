package com.example.internal;

public class CaesarCipher {

    private static final int SHIFT = 3; // Fixed shift value

    // Encrypt a password using Caesar Cipher
    public static String encrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                result.append((char) ((c - base + SHIFT) % 26 + base));
            } else {
                result.append(c); // Non-letters remain unchanged
            }
        }
        return result.toString();
    }

    // Decrypt a password using Caesar Cipher
    public static String decrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                result.append((char) ((c - base - SHIFT + 26) % 26 + base));
            } else {
                result.append(c); // Non-letters remain unchanged
            }
        }
        return result.toString();
    }
}