// //Score ko human-readable risk mein categorize krega
// package com.nexusverify.service;

// public class RiskCalculator {
	
// 	public String evaluateRiskLevel(int Score)
// 	{
// 		if(Score>=60)
// 			return "HIGH RISK";
// 		else if(Score>=30)
// 			return "MODERATE/SUSPICIOUS";
// 		else
// 			return "LOW RISK/SAFE";
// 	}
// }


package com.nexusverify.service;

public class RiskCalculator {

    public String getRiskLevel(int score) {

        if (score >= 60) {
            return "HIGH";
        } else if (score >= 30) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public String getRiskColor(int score) {

        if (score >= 60) {
            return "#ef4444";
        } else if (score >= 30) {
            return "#f59e0b";
        } else {
            return "#22c55e";
        }
    }

    public String getRecommendation(int score) {

        if (score >= 60) {
            return "High risk detected. Avoid trusting or sharing this content.";
        } else if (score >= 30) {
            return "Suspicious indicators found. Verify through trusted sources.";
        } else {
            return "Low risk detected. Still verify information before relying on it.";
        }
    }
}
