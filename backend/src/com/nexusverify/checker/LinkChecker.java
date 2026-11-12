
package com.nexusverify.checker;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nexusverify.model.VerificationResult;
import com.nexusverify.util.InputValidator;

public class LinkChecker implements VerificationChecker {

    private static final String URL_REGEX =
        "(?i)\\b(?:https?://|www\\.)[^\\s<>\"']+";

    private static final String[] SHORTENERS = {
        "bit.ly", "tinyurl.com", "t.co",
        "cutt.ly", "is.gd", "shorturl.at"
    };

    private static final String[] SUSPICIOUS_TLDS = {
        ".xyz", ".top", ".click", ".win",
        ".loan", ".work", ".zip"
    };

    @Override
    public VerificationResult check(String input) {

        if (!InputValidator.isValidinput(input)) {
            return new VerificationResult(
                false, 0, "Empty input provided.", "FAKE LINK"
            );
        }

        String text = input.toLowerCase();
        int score = 0;

        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(input);

        boolean foundLink = false;

        while (matcher.find()) {

            foundLink = true;

            String url = matcher.group()
                    .replaceAll("[.,!?;:]+$", "");

            String lowerUrl = url.toLowerCase();

            // Rule 1: Shortened URLs
            for (String shortener : SHORTENERS) {
                if (lowerUrl.contains(shortener)) {
                    score += 30;
                    break;
                }
            }

            // Rule 2: Suspicious domain extensions
            for (String tld : SUSPICIOUS_TLDS) {
                if (lowerUrl.matches(".*"
                        + Pattern.quote(tld)
                        + "([/:?#].*)?$")) {
                    score += 20;
                    break;
                }
            }

            // Rule 3: IP address used instead of domain
            try {
                URI uri = URI.create(
                    lowerUrl.startsWith("www.")
                        ? "http://" + lowerUrl
                        : lowerUrl
                );

                String host = uri.getHost();

                if (host != null &&
                    host.matches("\\d{1,3}(\\.\\d{1,3}){3}")) {
                    score += 30;
                }

                // Rule 4: @ symbol in URL
                if (url.contains("@")) {
                    score += 25;
                }

                // Rule 5: HTTP instead of HTTPS
                if ("http".equalsIgnoreCase(uri.getScheme())) {
                    score += 10;
                }

            } catch (IllegalArgumentException e) {
                score += 20;
            }
        }

        // Rule 6: Suspicious link-related words
        if (text.contains("verify your account") ||
            text.contains("claim your prize") ||
            text.contains("login immediately")) {
            score += 15;
        }

        int finalScore = Math.min(score, 100);

        boolean suspicious = finalScore >= 30;

        String message;

        if (!foundLink) {
            message = "No valid URL detected in the provided input.";
        } else if (finalScore >= 60) {
            message = "HIGH RISK: Multiple suspicious URL indicators detected.";
        } else if (finalScore >= 30) {
            message = "SUSPICIOUS: Verify this link before opening.";
        } else {
            message = "LOW RISK: No major suspicious URL patterns detected.";
        }

        return new VerificationResult(
            suspicious,
            finalScore,
            message,
            "FAKE LINK"
        );
    }
}
