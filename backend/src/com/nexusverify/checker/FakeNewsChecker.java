
package com.nexusverify.checker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nexusverify.model.VerificationResult;
import com.nexusverify.util.InputValidator;

public class FakeNewsChecker implements VerificationChecker {

    private final Map<String, Integer> suspiciousKeywords;

    public FakeNewsChecker() {

        suspiciousKeywords = new HashMap<>();

        suspiciousKeywords.put("shocking", 10);
        suspiciousKeywords.put("breaking news", 10);
        suspiciousKeywords.put("secret revealed", 15);
        suspiciousKeywords.put("100% guaranteed", 20);
        suspiciousKeywords.put("share immediately", 15);
        suspiciousKeywords.put("forward this message", 15);
        suspiciousKeywords.put("government hiding", 15);
        suspiciousKeywords.put("miracle cure", 20);
        suspiciousKeywords.put("must watch", 10);
        suspiciousKeywords.put("they don't want you to know", 20);
        suspiciousKeywords.put("confirmed by sources", 10);
        suspiciousKeywords.put("act immediately", 10);
    }

    @Override
    public VerificationResult check(String input) {

        if (!InputValidator.isValidinput(input)) {
            return new VerificationResult(
                false, 0, "Empty input provided.", "FAKE NEWS"
            );
        }

        String text = input.toLowerCase();
        int score = 0;

        // Rule 1: Suspicious keyword detection
        for (Map.Entry<String, Integer> entry :
                suspiciousKeywords.entrySet()) {

            if (text.contains(entry.getKey())) {
                score += entry.getValue();
            }
        }

        // Rule 2: Excessive exclamation marks
        long exclamationCount = input.chars()
                .filter(ch -> ch == '!')
                .count();

        if (exclamationCount >= 3) {
            score += 15;
        }

        // Rule 3: Excessive capital letters
        long upperCount = input.chars()
                .filter(Character::isUpperCase)
                .count();

        long letterCount = input.chars()
                .filter(Character::isLetter)
                .count();

        if (letterCount > 10 &&
                (double) upperCount / letterCount > 0.5) {
            score += 15;
        }

        // Rule 4: Suspicious forwarding language
        List<String> forwardingPatterns = Arrays.asList(
            "send to everyone",
            "forward to all",
            "share with everyone",
            "send this to 10 people"
        );

        for (String pattern : forwardingPatterns) {
            if (text.contains(pattern)) {
                score += 15;
            }
        }

        // Rule 5: Suspicious claims and urgency
        if (text.contains("within 24 hours") ||
                text.contains("before it gets deleted")) {
            score += 10;
        }

        // Final risk score
        int finalScore = Math.min(score, 100);

        boolean suspicious = finalScore >= 30;

        String message;

        if (finalScore >= 60) {
            message = "HIGH RISK: Multiple misleading content indicators detected.";
        } else if (finalScore >= 30) {
            message = "SUSPICIOUS: Verify this news using reliable sources.";
        } else {
            message = "LOW RISK: No major suspicious patterns detected.";
        }

        return new VerificationResult(
            suspicious,
            finalScore,
            message,
            "FAKE NEWS"
        );
    }
}
