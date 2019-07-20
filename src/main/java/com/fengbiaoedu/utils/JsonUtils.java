package com.fengbiaoedu.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengbiaoedu.pojo.ClockInUserInfoBO;

public class JsonUtils {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static String userToJson(ClockInUserInfoBO user) throws IOException{
		user.setUser_name(string2Unicode(user.getUser_name()));
		String str = MAPPER.writeValueAsString(user);
		List<String> byte2HexString = byte2HexString(str.getBytes());
		List<String> myToHexbytes = myToHexbytes(byte2HexString.size());
		String result = "";
		for (String temp : myToHexbytes) {
			result +=temp;
		}
		for(String temp : byte2HexString){
			result +=temp;
		}
		result =  result.substring(0,result.length()-1)+"]";
		return result;
	}
	
	private static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
	}
	
	private static List<String> byte2HexString(byte[] bs){
		List<String> list = new ArrayList<String>();
		for (byte b : bs) {
			String str = "0x"+Integer.toString(b>>4&0xF,16).toUpperCase()+Integer.toString(b&0xF,16).toUpperCase();
	    	list.add(str);
		}
		return list;
	}
	
    private static  List<String> myToHexbytes(int length) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
        DataOutputStream out=new DataOutputStream(baos);
        List<String>  list = new ArrayList<String>(); 
        try {
        	out.writeInt(length);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] bs=baos.toByteArray();
        Arrays.sort(bs);
        byte[] result = new byte[bs.length];
        for(int i=bs.length-1;i>0;i--){
        	result[bs.length-1-i] = bs[i];
        }
    	for(int i=bs.length-1;i>=0;i--) {
	    	String str = "0x"+Integer.toString(bs[i]>>4&0xF,16).toUpperCase()+Integer.toString(bs[i]&0xF,16).toUpperCase();
	    	list.add(str);
	    }
        baos.close();
        out.close();
        return list;
	}
	 
    /**
     * json字符串转用户信息对象
     * @return
     */
	public static ClockInUserInfoBO jsonToClockInUserInfoBo(String userJson) {
		
		ClockInUserInfoBO clockInUserInfoBO = new ClockInUserInfoBO();
		ObjectMapper objectMapper = new ObjectMapper();
		if(userJson != "" && userJson != null) {
			try {
				clockInUserInfoBO = objectMapper.readValue(userJson, ClockInUserInfoBO.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return clockInUserInfoBO;
	}
	
	public static ObjectMapper getMapper() {
		return MAPPER;
	}
}
