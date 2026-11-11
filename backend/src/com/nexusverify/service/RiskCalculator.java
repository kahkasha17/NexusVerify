//Score ko human-readable risk mein categorize krega
package com.nexusverify.service;

public class RiskCalculator {
	
	public String evaluateRiskLevel(int Score)
	{
		if(Score>=60)
			return "HIGH RISK";
		else if(Score>=30)
			return "MODERATE/SUSPICIOUS";
		else
			return "LOW RISK/SAFE";
	}
}
