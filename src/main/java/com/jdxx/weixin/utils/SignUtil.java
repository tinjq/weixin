package com.jdxx.weixin.utils;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
	
	private static final Log log = Logs.get();
    
    /**
     * 验证签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce){
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token, timestamp, nonce 三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 shal 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("checkSignature."+ Thread.currentThread().getStackTrace()[1].getMethodName()+"> error:异常", e);
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()): false;
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * @param digest
     * @return
     */
    private static String byteToStr(byte[] digest) {
        String strDigest = "";
        for(int i = 0; i < digest.length; i++){
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }
    
    /**
     * 将字节转换为十六进制字符串
     * @param b
     * @return
     */
    private static String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];
        
        String s = new String(tempArr);
        return s;
    }
    
}
