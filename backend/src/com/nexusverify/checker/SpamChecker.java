
package com.nexusverify.checker;

import com.nexusverify.model.VerificationResult;
import com.nexusverify.util.InputValidator;

import java.util.HashMap;
import java.util.Map;

public class SpamChecker implements VerificationChecker {

    private final Map<String, Integer> spamKeywords;

    public SpamChecker() {

        spamKeywords = new HashMap<>();

        // Lottery and Prize Scam
        spamKeywords.put("congratulations", 15);
        spamKeywords.put("lottery", 25);
        spamKeywords.put("you won", 25);
        spamKeywords.put("claim your prize", 25);
        spamKeywords.put("cash prize", 25);
        spamKeywords.put("free recharge", 20);

        // Bank Phishing Scam
        spamKeywords.put("bank account", 25);
        spamKeywords.put("account blocked", 25);
        spamKeywords.put("will be blocked", 25);
        spamKeywords.put("verify your account", 25);
        spamKeywords.put("bank details", 25);
        spamKeywords.put("kyc update", 30);
        spamKeywords.put("otp", 20);

        // Other Suspicious Messages
        spamKeywords.put("claim now", 20);
        spamKeywords.put("urgent action", 15);
        spamKeywords.put("click this link", 15);
        spamKeywords.put("security update", 20);
        spamKeywords.put("send money", 25);
        spamKeywords.put("electricity bill unpaid", 30);
    }

    @Override
    public VerificationResult check(String input) {

        if (!InputValidator.isValidinput(input)) {
            return new VerificationResult(
                false, 0, "Empty input provided.", "UNKNOWN"
            );
        }

        String text = input.toLowerCase();
        int score = 0;

        boolean isEmail = InputValidator.isEmail(input);

        // Rule 1: Keyword scanning
        for (Map.Entry<String, Integer> entry :
                spamKeywords.entrySet()) {

            if (text.contains(entry.getKey())) {
                score += entry.getValue();
            }
        }

        // Rule 2: Shortened URL detection
        if (text.contains("bit.ly") ||
            text.contains("tinyurl.com") ||
            text.contains("t.co") ||
            text.contains("cutt.ly")) {

            score += 25;
        }

        // Rule 3: Suspicious email extensions
        if (isEmail) {

            String[] extensions = {
                ".xyz", ".top", ".click",
                ".win", ".loan", ".work"
            };

            for (String ext : extensions) {
                if (text.contains(ext)) {
                    score += 25;
                    break;
                }
            }
        }

        // Rule 4: Scam category identification
        String category;

        if (containsAny(text,
                "lottery", "you won", "congratulations",
                "claim your prize", "cash prize",
                "free recharge")) {

            category = "LOTTERY / PRIZE SCAM";

        } else if (containsAny(text,
                "bank account", "account blocked",
                "will be blocked", "verify your account",
                "bank details", "kyc update", "otp")) {

            category = "BANK PHISHING SCAM";

        } else if (containsAny(text,
                "electricity bill unpaid")) {

            category = "BILL PAYMENT SCAM";

        } else if (containsAny(text,
                "bit.ly", "tinyurl.com", "t.co", "cutt.ly")) {

            category = "SUSPICIOUS LINK MESSAGE";

        } else {

            category = isEmail ? "SPAM EMAIL" : "SPAM SMS";
        }

        // Rule 5: Final risk calculation
        int finalScore = Math.min(score, 100);

        boolean suspicious = finalScore >= 30;

        String statusMessage;

        if (finalScore >= 60) {

            statusMessage =
                "HIGH RISK: Potential scam or phishing detected!";

        } else if (finalScore >= 30) {

            statusMessage =
                "MEDIUM RISK: Suspicious message detected. Exercise caution.";

        } else {

            statusMessage =
                "LOW RISK: No major suspicious patterns detected.";

            category = isEmail ? "NORMAL EMAIL" : "NORMAL SMS";
        }

        return new VerificationResult(
            suspicious,
            finalScore,
            statusMessage,
            category
        );
    }

    private boolean containsAny(String text, String... keywords) {

        for (String keyword : keywords) {

            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
