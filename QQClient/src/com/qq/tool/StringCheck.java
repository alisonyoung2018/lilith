package com.qq.tool;

public class StringCheck {
	public static boolean check(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
}
