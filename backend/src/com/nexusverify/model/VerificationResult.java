package com.nexusverify.model;

public class VerificationResult {

    private boolean suspicious;
    private int riskScore;
    private String message;
    private String category;

    public VerificationResult(boolean suspicious, int riskScore, String message, String category) {
        this.suspicious = suspicious;
        this.riskScore = riskScore;
        this.message = message;
        this.category = category;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getMessage() {
        return message;
    }

    public String getCategory() {
        return category;
    }
}