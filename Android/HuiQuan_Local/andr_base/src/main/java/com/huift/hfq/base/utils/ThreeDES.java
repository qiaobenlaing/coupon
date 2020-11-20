package com.huift.hfq.base.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class ThreeDES {
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
	 
    // DES,DESede,Blowfish
 
    // keybyte为加密密钥，长度为24字节
    // src为被加密的数据缓冲区（源）
    public static String encryptMode(byte[] keybyte, byte[] src)
    {
        try
        {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            // 开始加密运算
            byte[] encryptedByteArray = c1.doFinal(src);
            // 加密运算之后 将byte[]转化为base64的String
            BASE64Encoder enc = new BASE64Encoder();
            return enc.encode(encryptedByteArray);
        }
        catch (java.security.NoSuchAlgorithmException e1)
        {
            e1.printStackTrace();
        }
        catch (javax.crypto.NoSuchPaddingException e2)
        {
            e2.printStackTrace();
        }
        catch (java.lang.Exception e3)
        {
            e3.printStackTrace();
        }
        return null;
    }
     
    // keybyte为加密密钥，长度为24字节
    // src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, String src)
    {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            // 解密运算之前
            BASE64Decoder dec = new BASE64Decoder();
            byte[] encryptedByteArray = dec.decodeBuffer(src);
            // 解密运算 将base64的String转化为byte[]
            return c1.doFinal(encryptedByteArray);
 
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
 
    // 转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
    }
}
