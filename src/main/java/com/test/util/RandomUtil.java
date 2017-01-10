package com.test.util;

public class RandomUtil {

	public static String random(){
		StringBuilder strBuil=new StringBuilder();
		final String[] strArray=new String[]{"0","1","2","3","4","5","6","7",
				"8","9","A","B","C","D","E","F","G","H","I","J","K","L","M",
				"N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(int i=0;i<32;i++){
			int random = (int)(Math.random()*36);
			strBuil.append(strArray[random]);
		}
		return strBuil.toString();
	}
	
	public static void main(String[] args){
		random();
	}
}
