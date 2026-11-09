package com.nexusverify.checker;

import com.nexusverify.model.VerificationResult;
import com.nexusverify.util.InputValidator;
import java.util.HashMap;
import java.util.Map;

public class SpamChecker implements VerificationChecker{
	
	private final Map<String,Integer> spamKeywords;
	
	public SpamChecker()
	{
		spamKeywords=new HashMap<>();

        // Keywords for both SMS and Email Spam
        spamKeywords.put("kyc update", 30);
        spamKeywords.put("account blocked", 30);
        spamKeywords.put("electricity bill unpaid", 30);
        spamKeywords.put("congratulations", 15);
        spamKeywords.put("you won", 25);
        spamKeywords.put("claim now", 20);
        spamKeywords.put("free recharge", 20);
        spamKeywords.put("cash prize", 25);
        spamKeywords.put("urgent action", 15);
        spamKeywords.put("lottery", 25);
        spamKeywords.put("verify your account", 20);
        spamKeywords.put("security update", 20);
	}
	
	@Override
	public VerificationResult check(String input)
	{
		if(!InputValidator.isValidinput(input))
		{
			return new VerificationResult(false, 0, "Empty input provided.", "UNKNOWN");
		}
		
		String lowerInput=input.toLowerCase();
		int score=0;
		
		
		//Rule A: Keyword Scanning
		for(Map.Entry<String,Integer> entry: spamKeywords.entrySet())
		{
			if(lowerInput.contains(entry.getKey()))
			{
				score+=entry.getValue();
			}
		}
		
		//Rule B: Shortened links inside SMS
		if(lowerInput.contains("bit.ly") || lowerInput.contains("tinyurl.com") || lowerInput.contains("t.co") || lowerInput.contains("cutt.ly"))
		{
			score+=25;
		}
		
		
		//Rule C: Email Specific Checks (Suspicious TLDs / Domain Extensions)
        boolean isEmailFormat = InputValidator.isEmail(input);
        if (isEmailFormat) {
            String[] suspiciousExtensions = {".xyz", ".top", ".click", ".win", ".loan", ".work"};
            for (String ext : suspiciousExtensions) {
                if (lowerInput.contains(ext)) {
                    score += 25;
                }
            }
        }
        
        
		int finalScore=Math.min(score, 100);
		boolean isSuspicious=finalScore>=30;
		
		// Dynamic Category
        String category = isEmailFormat ? "SPAM EMAIL" : "SPAM SMS";
        
		String statusMessage;
		
		if(finalScore>=60)
		{
			statusMessage="HIGH RISK: Potential Scam or Phishing " + (isEmailFormat ? "Email!" : "SMS!");
		}
		else if(finalScore>=30)
		{
			statusMessage="SUSPICIOUS: Exercise caution with this " + (isEmailFormat ? "Email." : "SMS.");
		}
		else
		{
			statusMessage="SAFE: Message appears to be legitimate";
		}
		return new VerificationResult(isSuspicious,finalScore,statusMessage,category);
	}
	
}
