package com.nexusverify.util;

public class InputValidator {
	
	//Check if input is not empty..
	public static boolean isValidinput(String input)
	{
		return input!=null && !input.trim().isEmpty();
	}
	
	//Check if input is email or normal SMS text
	public static boolean isEmail(String input)
	{
		return input!=null && input.contains("@") && input.contains("."); 
	}
}
