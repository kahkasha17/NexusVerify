
package com.nexusverify.util;

import java.util.regex.Pattern;

public class RegexUtil {

    private static final String EMAIL_REGEX =
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final String URL_REGEX =
        "(?i)\\b(?:https?://|www\\.)[^\\s<>\"']+";

    public static boolean isValidEmail(String input) {

        return input != null &&
            Pattern.matches(EMAIL_REGEX, input.trim());
    }

    public static boolean containsURL(String input) {

        return input != null &&
            Pattern.compile(URL_REGEX).matcher(input).find();
    }

    public static boolean isValidURL(String input) {

        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            String url = input.trim();

            if (url.startsWith("www.")) {
                url = "https://" + url;
            }

            java.net.URI uri = java.net.URI.create(url);

            return ("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()))
                    && uri.getHost() != null
                    && uri.getHost().contains(".");

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
