
package com.nexusverify;

import java.util.List;
import java.util.Scanner;

import com.nexusverify.checker.FakeNewsChecker;
import com.nexusverify.checker.LinkChecker;
import com.nexusverify.checker.SpamChecker;
import com.nexusverify.checker.VerificationChecker;
import com.nexusverify.model.VerificationResult;
import com.nexusverify.service.HistoryManager;
import com.nexusverify.service.ResultGenerator;
import com.nexusverify.service.RiskCalculator;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        FakeNewsChecker newsChecker = new FakeNewsChecker();
        SpamChecker spamChecker = new SpamChecker();
        LinkChecker linkChecker = new LinkChecker();

        HistoryManager historyManager = new HistoryManager();
        ResultGenerator resultGenerator = new ResultGenerator();
        RiskCalculator riskCalculator = new RiskCalculator();

        boolean running = true;

        while (running) {

            System.out.println("\n======================================");
            System.out.println("          NEXUS VERIFY");
            System.out.println("       VERIFY. ANALYZE. STAY SAFE.");
            System.out.println("======================================");

            System.out.println("1. Fake News Verification");
            System.out.println("2. Spam SMS / Email Verification");
            System.out.println("3. Fake Link Verification");
            System.out.println("4. View Verification History");
            System.out.println("5. Clear Verification History");
            System.out.println("0. Exit");

            System.out.print("\nEnter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                case "2":
                case "3":

                    VerificationChecker checker;

                    if (choice.equals("1")) {
                        checker = newsChecker;
                    } else if (choice.equals("2")) {
                        checker = spamChecker;
                    } else {
                        checker = linkChecker;
                    }

                    System.out.print("\nEnter content to verify: ");

                    String input = scanner.nextLine();

                    VerificationResult result = checker.check(input);

                    resultGenerator.displayResult(result);

                    System.out.println("Risk Level: "
                            + riskCalculator.getRiskLevel(
                                result.getRiskScore()));

                    System.out.println("Recommendation: "
                            + riskCalculator.getRecommendation(
                                result.getRiskScore()));

                    historyManager.saveHistory(input, result);

                    break;

                case "4":

                    List<String> history =
                            historyManager.getHistory();

                    System.out.println("\n========== HISTORY ==========");

                    if (history.isEmpty()) {
                        System.out.println("No verification history found.");
                    } else {
                        for (String record : history) {
                            System.out.println(record);
                        }
                    }

                    break;

                case "5":

                    System.out.print(
                        "Are you sure you want to clear history? (yes/no): "
                    );

                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {
                        historyManager.clearHistory();
                        System.out.println("History cleared successfully.");
                    } else {
                        System.out.println("Operation cancelled.");
                    }

                    break;

                case "0":

                    running = false;
                    System.out.println("\nThank you for using NEXUS VERIFY!");

                    break;

                default:

                    System.out.println(
                        "Invalid choice! Please enter a valid option."
                    );
            }
        }

        scanner.close();
    }
}
