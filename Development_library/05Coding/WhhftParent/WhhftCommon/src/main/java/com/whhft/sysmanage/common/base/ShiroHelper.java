package com.whhft.sysmanage.common.base;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

public class ShiroHelper {
	private final static ByteSource salt = new SimpleByteSource("grayfox");
	
	public static String generatPwdWithSalt(String password){
		String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
		return hashedPasswordBase64;
	}
	
	public static ByteSource getSalt(){
		return salt;
	}
}
