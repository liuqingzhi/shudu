package com.test;

public class Test {
	public static void main(String[] args) {
		String template="字符串%1$s,字字符串2='%2$s'";
		String arg1=null;
		String arg2="ssssss";
		String format = String.format(template, arg1,arg2);
		
		System.out.println(format);
	}
}
