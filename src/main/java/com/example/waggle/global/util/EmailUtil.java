package com.example.waggle.global.util;

public class EmailUtil {
    public static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            String prefix = email.substring(0, atIndex);
            String domain = email.substring(atIndex);
            int maskLength = Math.max(prefix.length() / 3, 1);
            StringBuilder maskedPrefix = new StringBuilder(prefix.substring(0, prefix.length() - maskLength));
            for (int i = 0; i < maskLength; i++) {
                maskedPrefix.append("*");
            }
            return maskedPrefix.toString() + domain;
        }
        return email;
    }
}
