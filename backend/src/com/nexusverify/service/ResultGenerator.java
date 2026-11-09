//Output ko GUI screen pr print krne ke liye
package com.nexusverify.service;

import com.nexusverify.model.VerificationResult;

public class ResultGenerator {
		
		public void displayResult(VerificationResult result)
		{
			System.out.println("\n==========================================");
	        System.out.println("          NEXUS VERIFY REPORT             ");
	        System.out.println("==========================================");
	        System.out.println("Category   : " + result.getCategory());
	        System.out.println("Risk Score : " + result.getRiskScore() + "/100");
	        System.out.println("Suspicious : " + (result.isSuspicious() ? "YES" : "NO"));
	        System.out.println("Status     : " + result.getMessage());
	        System.out.println("==========================================\n");
		}
}
