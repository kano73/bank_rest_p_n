package org.example.bank_rest_p_n.service;

import java.security.SecureRandom;

public class CardNumberGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String BIN = "400000";

    public static String generateCardNumber() {
        StringBuilder card = new StringBuilder(BIN);
        for (int i = 0; i < 9; i++) {
            card.append(random.nextInt(10));
        }
        int checksum = calculateLuhnChecksum(card.toString());
        card.append(checksum);
        return card.toString();
    }

    private static int calculateLuhnChecksum(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(number.length() - 1 - i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }
}
