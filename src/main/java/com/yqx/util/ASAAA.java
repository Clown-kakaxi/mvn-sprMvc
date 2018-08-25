package com.yqx.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ASAAA {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long a = 188855213600L;
		String aaa = "1528905600000";
		String bbb = "1528819200000";
//		Timestamp tm = new Timestamp(Long.parseLong(aaa));
		System.out.println(new Timestamp(format.parse(bbb).getTime()));
//		System.out.println(new Date(new Timestamp(format.parse(bbb).getTime()).getTime()));
	}
	
	/* 
     * 返回长度为【strLength】的随机数，在前面补0 
     */  
    private static String getFixLenthString(int strLength) {  
          
        Random rm = new Random();  
          
        // 获得随机数  
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
      
        // 将获得的获得随机数转化为字符串  
        String fixLenthString = String.valueOf(pross);  
      
        // 返回固定的长度的随机数  
        return fixLenthString.substring(1, strLength + 1);  
    } 
	
}
