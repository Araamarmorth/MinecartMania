package com.afforess.minecartmaniacore.utils;

public class StringUtils {
	
	
	public static String getNumber(String s)
	{
		String n = "";
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if (Character.isDigit(c) || c == '.' || c == '-')
				n += c;
		}
		return n;
	}
	
	public static boolean containsLetters(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String removeWhitespace(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') 
				s1 += s.charAt(i);
		}
		return s1;
	}
	
	public static String removeBrackets(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if (c == ']' || c == '[') continue;
			str += c;
		}
		return str;
	}
	
	public static String addBrackets(String s) {
		return "[" + s + "]";
	}
}
