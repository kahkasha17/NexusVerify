
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
        spamKeywords.put("you have won", 25);
        spamKeywords.put("click here", 15);
        spamKeywords.put("claim your", 15);

        // Bank Phishing Scam
        spamKeywords.put("bank account", 25);
        spamKeywords.put("account blocked", 25);
        spamKeywords.put("will be blocked", 25);
        spamKeywords.put("verify your account", 25);
        spamKeywords.put("bank details", 25);
        spamKeywords.put("kyc update", 30);
        spamKeywords.put("otp", 20);


        // Job and Scholarship Scams
        spamKeywords.put("registration fee", 35);
        spamKeywords.put("processing fee", 35);
        spamKeywords.put("work from home", 15);
        spamKeywords.put("scholarship approved", 15);

        // Prize and Promotional Scams
        spamKeywords.put("free prize", 25);
        spamKeywords.put("claim your free", 25);
        spamKeywords.put("claim your coupon", 15);
        spamKeywords.put("limited time offer", 15);
        spamKeywords.put("click here to claim", 25);

        // Password and Account Phishing
        spamKeywords.put("enter your password", 45);
        spamKeywords.put("account will be suspended", 30);
        spamKeywords.put("share this otp", 40);
        spamKeywords.put("share your otp", 40);


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

        if (containsAny(text, "scholarship", "education grant")
                && containsAny(text, "processing fee",
                               "registration fee")) {

            category = "SCHOLARSHIP SCAM";

        } else if (containsAny(text, "job", "work from home",
                              "employment")
                && containsAny(text, "registration fee",
                               "processing fee", "pay rs")) {

            category = "JOB SCAM";

        } else if (containsAny(text,
                "enter your password",
                "share your password",
                "account will be suspended",
                "password reset")
                && containsAny(text,
                "click", "verify", "enter",
                "immediately", "urgent")) {

            category = "EMAIL PHISHING";

        } else if (containsAny(text,
                "lottery", "cash prize", "free prize",
                "you won", "you have won",
                "claim your prize")
                && containsAny(text,
                "claim", "click", "won", "prize")) {

            category = "LOTTERY / PRIZE SCAM";

        } else if (containsAny(text,
        "bank account", "kyc update",
        "bank details", "otp")
        && containsAny(text,
        "blocked", "verify", "share",
        "urgent", "immediately")
        && !containsAny(text,
        "do not share", "don't share",
        "never share")) {

            category = "BANK PHISHING SCAM";


        } else if (containsAny(text,
                "electricity bill unpaid",
                "electricity disconnected",
                "power supply disconnected")) {

            category = "BILL PAYMENT SCAM";

        } else if (containsAny(text,
                "bit.ly", "tinyurl.com",
                "t.co", "cutt.ly")) {

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

            // Avoid showing a confirmed scam label for low risk
            if (category.equals("SPAM EMAIL") ||
                category.equals("SPAM SMS")) {

                category = isEmail ? "NORMAL EMAIL" : "NORMAL SMS";

            } else {

                category = "POTENTIAL " + category;

            }
        }

        return new VerificationResult(
            suspicious,
            finalScore,
            statusMessage,
            category
        );
    }

    // Helper method for checking multiple keywords
    private boolean containsAny(String text, String... keywords) {

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
   