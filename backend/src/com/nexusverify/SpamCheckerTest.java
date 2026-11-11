package com.nexusverify;

import com.nexusverify.checker.SpamChecker;
import com.nexusverify.model.VerificationResult;
import com.nexusverify.service.ResultGenerator;

public class SpamCheckerTest {

    public static void main(String[] args) {
        
        SpamChecker checker = new SpamChecker();
        ResultGenerator resultGenerator = new ResultGenerator();

        System.out.println("==========================================");
        System.out.println("   NEXUS VERIFY - SPAM & EMAIL TEST DRIVE ");
        System.out.println("==========================================");

        // --- SMS TEST CASES ---

        // Test 1: Normal / Safe SMS
        String safeSMS = "Hey, are we still meeting for coffee today at 5 PM?";
        System.out.println("\n--- Test 1: Safe SMS ---");
        VerificationResult r1 = checker.check(safeSMS);
        resultGenerator.displayResult(r1);

        // Test 2: High Risk Fake SMS
        String fakeSMS = "Urgent action required! Your KYC update is pending. Account blocked in 24 hours. Click link: bit.ly/bank-kyc-verify";
        System.out.println("\n--- Test 2: High Risk Fake SMS ---");
        VerificationResult r2 = checker.check(fakeSMS);
        resultGenerator.displayResult(r2);


        // --- EMAIL TEST CASES ---

        // Test 3: Normal / Safe Email
        String safeEmail = "From: hr@company.com - Please review the attached project documentation for tomorrow's meeting.";
        System.out.println("\n--- Test 3: Safe Email ---");
        VerificationResult r3 = checker.check(safeEmail);
        resultGenerator.displayResult(r3);

        // Test 4: High Risk Phishing Email
        String fakeEmail = "Security Alert from support@bank-update.xyz! Verify your account immediately to prevent permanent closure.";
        System.out.println("\n--- Test 4: High Risk Phishing Email ---");
        VerificationResult r4 = checker.check(fakeEmail);
        resultGenerator.displayResult(r4);
    }
}