
package com.nexusverify.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nexusverify.model.VerificationResult;

public class HistoryManager {

    private static final String FILE_PATH = "data/history.txt";

    public HistoryManager() {
        createHistoryFile();
    }

    private void createHistoryFile() {

        File file = new File(FILE_PATH);

        try {
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            System.out.println("Unable to create history file: "
                    + e.getMessage());
        }
    }

    public void saveHistory(String input, VerificationResult result) {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            writer.write("Input: " + input.replace("\n", " "));
            writer.newLine();

            writer.write("Category: " + result.getCategory());
            writer.newLine();

            writer.write("Risk Score: " + result.getRiskScore());
            writer.newLine();

            writer.write("Suspicious: " + result.isSuspicious());
            writer.newLine();

            writer.write("Status: " + result.getMessage());
            writer.newLine();

            writer.write("----------------------------------------");
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Unable to save history: "
                    + e.getMessage());
        }
    }

    public List<String> getHistory() {

        List<String> history = new ArrayList<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {
                history.add(line);
            }

        } catch (IOException e) {
            System.out.println("Unable to read history: "
                    + e.getMessage());
        }

        return history;
    }

    public void clearHistory() {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(FILE_PATH, false))) {

            writer.write("");

        } catch (IOException e) {
            System.out.println("Unable to clear history: "
                    + e.getMessage());
        }
    }
}
