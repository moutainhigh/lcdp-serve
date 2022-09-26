package com.redxun.common.utils;

import com.redxun.common.tool.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 密码加密与解密类
 * 
 * @author csx
 * @Email chshxuan@163.com
 * @Copyright (c) 2014-2020 广州红迅软件有限公司（http://www.redxun.cn）
 *            本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Slf4j
public class EncryptUtil {
	
	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * 使用MD5加密
	 * 
	 * @param inStr
	 * @return
	 * @throws Exception
	 */
	public static String encryptMd5(String inStr) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			return new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Md5加密
	 * @param inStr
	 * @return
	 */
	public static String encryptMd5Hex(String inStr){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return bytesToHex(md.digest(inStr.getBytes("utf-8")));
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {// 16 == bytes.length;

			t = bytes[i];
			if (t < 0){
				t += 256;
			}

			sb.append(hexDigits[(t >>> 4)]);
		}
		return sb.toString();
	}

	/**
	 * 输出明文按sha-256加密后的密文
	 * 
	 * @param inputStr
	 *            明文
	 * @return
	 */
	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte digest[] = md.digest(inputStr.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 将hex编码 改成 base64编码。
	 * 958d51602bbfbd18b2a084ba848a827c29952bfef170c936419b0922994c0589
	 * lY1RYCu/vRiyoIS6hIqCfCmVK/7xcMk2QZsJIplMBYk=
	 * @param input
	 * @return
	 * @throws DecoderException
	 */
	public static String hexToBase64(String input) throws DecoderException{
		String out=null;
		try{
			byte[] bytes= Hex.decodeHex(input.toCharArray());
		    out= new String(Base64.encodeBase64(bytes));
		}catch(Exception ex){
	    	//ex.printStackTrace();
	    }
	    return out;
	}
    

	/**
	 * 密钥
	 */
	private static final String key = "*&^%$#@!";

	/**
	 * 对称解密算法
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message) throws Exception {
		return decrypt(message,key);
	}

	/**
	 * 使用KEY对称解密。
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message,String key) throws Exception {
		byte[] bytesrc = stringToBytes(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, "UTF-8");
	}

	/**
	 * String转Byte数组
	 * 
	 * @param temp
	 * @return
	 */
	private static byte[] stringToBytes(String temp) {
		byte digest[] = new byte[temp.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = temp.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	/**
	 * 对称加密算法
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message) throws Exception {
		return  encrypt(message,key);
	}

	/**
	 * 使用key对数据进行堆成加密。
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message,String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		String str = bytesToString(cipher.doFinal(message.getBytes("UTF-8")));
		return str;
	}

	/**
	 * Byte数组转String
	 * 
	 * @param b
	 * @return
	 */
	private static String bytesToString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}

	/**
	 * 生成密钥对。
	 * <pre>
	 * 生成的数组
	 * 1.第一个值为私钥
	 * 2.第二个值为公钥
	 * </pre>
	 * @return
	 * @throws Exception 
	 */
	public static String[] genKeyPair()
			throws Exception {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为1024位
		keyPairGen.initialize(1024);
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String privateKeyStr = FileUtil.serializeToString(privateKey);
		String publicKeyStr = FileUtil.serializeToString(publicKey);

		return new String[]{privateKeyStr,publicKeyStr};

	}
	
	


	/**
	 * 加解密。
	 * @param k
	 * @param data		数据
	 * @param encrypt	1 加密 0解密 
	 * @return
	 * @throws Exception
	 */
	private static byte[] handleData(Key k, byte[] data, int encrypt) throws Exception {
		if (k != null) {
			Cipher cipher = Cipher.getInstance("RSA");
			if (encrypt == 1) {
				cipher.init(Cipher.ENCRYPT_MODE, k);
				byte[] resultBytes = cipher.doFinal(data);
				return resultBytes;
			} else if (encrypt == 0) {
				cipher.init(Cipher.DECRYPT_MODE, k);
				byte[] resultBytes = cipher.doFinal(data);
				return resultBytes;
			} else {
				System.out.println("参数必须为: 1 加密 0解密");
			}
		}
		return null;
	}
	
	private static String charset="utf-8";
	private static String isoCharset="iso-8859-1";
	
	/**
	 * 使用公钥加密。
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptPublicKey(String key,String data) throws Exception{
		RSAPublicKey pubKey=(RSAPublicKey) FileUtil.deserializeToObject(key);
		byte[] result = handleData(pubKey, data.getBytes(charset), 1);  
		String str=new String(Base64Util.encode(result),isoCharset);
		return str;
	}
	
	/**
	 * 使用私钥解密。
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decryptPrivateKey(String privateKey,String data) throws Exception{
		RSAPrivateKey priKey=(RSAPrivateKey)FileUtil.deserializeToObject(privateKey) ;
		byte[] tmpBytes=data.getBytes(charset);
		byte[] tmp= Base64Util.decode(tmpBytes);
	    byte[] deresult = handleData(priKey, tmp, 0);  
	    return new String(deresult,isoCharset);
	}
	
	
	/**
	 * 使用公钥加密。
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptPrivateKey(String key,String data) throws Exception{
		RSAPrivateKey pubKey=(RSAPrivateKey) FileUtil.deserializeToObject(key);
		byte[] result = handleData(pubKey, data.getBytes(charset), 1);  
		byte[] tmp= Base64Util.encode(result);
		String str=new String(tmp,isoCharset);
		return str;
	}
	
	/**
	 * 使用公钥解密。
	 * @param publicKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decryptPublicKey(String publicKey,String data) throws Exception{
		RSAPublicKey pubKey=(RSAPublicKey)FileUtil.deserializeToObject(publicKey) ;
		byte[] tmp= data.getBytes(isoCharset);
		byte[] tmpBytes=Base64Util.decode(tmp);
	    byte[] deresult = handleData(pubKey, tmpBytes, 0);  
	    return new String(deresult,charset);
	}


	/**
	 * 解压id.
	 * @param id
	 * @return
	 */
	public static String decryptKey(String id){
		if(StringUtils.isEmpty(id)){
			return id;
		}
		boolean encrypt= SysPropertiesUtil.getBoolean("encryptkey");
		if(!encrypt){
			return id;
		}
		try{
			if(id.length()<=20){
				return id;
			}
			String tmp= EncryptUtil.decrypt(id);
			return tmp;

		}
		catch (Exception ex){
			log.error(ExceptionUtil.getExceptionMessage(ex));
			return "-1";
		}
	}

	/**
	 * 加密ID.
	 * @param id
	 * @return
	 */
	public static String encryptKey(String id){
		boolean encrypt= SysPropertiesUtil.getBoolean("encryptkey");
		if(!encrypt){
			return id;
		}
		try{
			String tmp=EncryptUtil.encrypt(id);
			return tmp;
		}
		catch (Exception ex){
			log.error(ExceptionUtil.getExceptionMessage(ex));
			return id;
		}

	}

	
	
	public static void main(String[] args) throws Exception {
		 //产生加密密钥对。
//		KeyPairObject keyPair= genKeyPair();
//		
//		//获取私钥
//		String privateKey=keyPair.getPrivateKey();
//		//产生公钥
//		String publicKey=keyPair.getPublicKey();
//		
//		System.out.println("公钥:" + publicKey);
//		System.out.println("私钥:" + privateKey);
		
		String publicKey="rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAAAojCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA2I7aAFOtZIRa4wdYHStxMHiOKrgOMIHAilbxHEtW0wJNNJIkOuwY+daq2WdgRZlC7sgl/a4eHYRK2p+ilkcBivMJg+qND2+ihqoiiLUgyuEuJa4oRmv8GaJ+K8KXHztf5Tk3ygU7r8jy4vEUGFxMYHIeAflnXIeBQg2DwNsTtA0CAwEAAXQABVguNTA5fnIAGWphdmEuc2VjdXJpdHkuS2V5UmVwJFR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAZQVUJMSUM=";
		String privateKey="rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAACezCCAncCAQAwDQYJKoZIhvcNAQEBBQAEggJhMIICXQIBAAKBgQDYjtoAU61khFrjB1gdK3EweI4quA4wgcCKVvEcS1bTAk00kiQ67Bj51qrZZ2BFmULuyCX9rh4dhEran6KWRwGK8wmD6o0Pb6KGqiKItSDK4S4lrihGa/wZon4rwpcfO1/lOTfKBTuvyPLi8RQYXExgch4B+Wdch4FCDYPA2xO0DQIDAQABAoGAFPWmhdeTdaIVxdllHtWgi+dvIxVTUkCMqRcHGQz1p1CWtlrapNVLCYtMV+RYfgP6ZW/7tVTP112BfS1sKA1RSbrtIQG7TnVhBLqSjvbhX1aZRNaEMTGzO7ZRz5jVEoULwqqKmQMh2MMPbSsFu8HzEkzQjedMCt1QvVweJdP2UIECQQDurbhlz3pnK5YAzxUYLuh3NhxvZ/GmmVz6v8vfjO2JUfvC/3ppgMh37tFBjUKCQQO3KI+LVn4B/JEoKarqEe3tAkEA6EYqP25EplTfUsiiQDnBe1bc+r6RJzMi8bOPxIBFjC+VVyxC8kQ18MBBiKoeT0ulDx02YY6AbeI3inEiFc8aoQJAH4ntF+b2sbNcuvaiPvPT3AzWbRI7KFyToL6/XebtbHvc3MONlWtjEhYIqLTV2QhmSUmezja7p9+L/taisxNzcQJBAK9iE5Jzo3hoi3wJrKGMOrDz5MWcUSPlM9SHPd4k8N6qKzx4WlBt+sC/mnwj3+EGACsKZr6BCC5wanmpdRA8oiECQQDpI2cPAOE2zdiE5znB0PyS1rIYf0fA46eksuMuYKBfQzT7z0/eP0tyLXhgs4c55HA79HY/dIlcYdfDy88AmO1CdAAGUEtDUyM4fnIAGWphdmEuc2VjdXJpdHkuS2V5UmVwJFR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAdQUklWQVRF";
		
		
		//使用公钥加密
		String encStr= encryptPrivateKey(privateKey,"admin@redxun.cn");
		
		String encStr2= encryptPrivateKey(privateKey,"admin@redxun.cn");
		
		
		System.out.println(encStr);
		
		//使用私钥解密
		String tmp= decryptPublicKey(publicKey,"AhhR68bNcUQffe5dLZU+zA5mM93ybyd/NXuaKa3xaV8FJVEvT9kPLHlv3I/qoxfzAB0fqAEZeCRZCiBxIRaDKnS8A+7KtQSz0z+4QMEy8o2SDReUcbaHcuuU28xMXYDBEprFLXcaNfmrnpKjE6BE0l0A0nwQvIiXQRkjhxGkyCg=");
		
		System.out.println(tmp);
		//使用私钥加密
		String encStr1= encryptPrivateKey(privateKey,"测试加解密");
		//使用公钥解密
		String tmp1= decryptPublicKey(publicKey,encStr1);
		
		System.out.println(tmp1);
		  
	}

}
