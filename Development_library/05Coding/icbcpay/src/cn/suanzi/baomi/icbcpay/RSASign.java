// ---------------------------------------------------------
// @author    刘卫平
// @version   1.0.0
// @copyright 版权所有 (c) 2013 杭州新利科技有限公司 保留所有版权
// ---------------------------------------------------------

package cn.suanzi.baomi.icbcpay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author 刘卫平
 * @version 1.0.0	2014-4-2
 */
public class RSASign {
	
	protected String privateKeyPath = null;
	protected String publicKeyPath = null;
	
	private byte[] privateKeyByte = null;
	private byte[] publicKeyBytes = null;
	
	private Logger log = Logger.getLogger(RSASign.class);
	
	/**
	 * 构造函数
	 * @param priKeyPath 私钥路径
	 * @param pubKeyPath 公钥路径
	 */
	public RSASign(String priKeyPath, String pubKeyPath) {
		this.privateKeyPath = priKeyPath;
		this.publicKeyPath = pubKeyPath;
	}
	
	/**
	 * 用私钥进行签名
	 * @param srcData 加密数据，例如 data.getBytes("UTF-8")
	 * @param key	密钥
	 * @return
	 * @throws Exception
	 */
	public String signByPrivateKey(byte[] srcData) throws Exception{
		// 创建密钥对象
		byte[] privateKeyByte = this.getPrivateKeyByte();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
		
		// 定义签名数据
		byte [] signData = null;
		// 生成签名
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(priKey);
		sig.update(srcData);
		signData = sig.sign();
		
		BASE64Encoder b64enc = new BASE64Encoder();
		return b64enc.encode(signData);
	}
	
	/**
	 * 验证签名并返回结果
	 * @param signData 签名数据
	 * @param srcData 原数据
	 * @param publicKey 用于验证的公钥
	 * @return
	 * @throws Exception
	 */
	public boolean verifySignature(String signData, byte[] srcData, Key publicKey) throws Exception{
		// 重新编码一下用来验证签名的公钥
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		// 定义 keyFactory
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		// 重新定义公钥
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		// 定义验证模式
		Signature sig = Signature.getInstance("SHA1WithRSA");
		// 验证初始化
		sig.initVerify(pubKey);
	    sig.update(srcData);
	    
	    BASE64Decoder b64dec = new BASE64Decoder();
	    // base64 解码
	    byte[] decodedSignData = b64dec.decodeBuffer(signData);
	    // 获取验证结果
	    boolean isOk = sig.verify(decodedSignData);
	    
	    return isOk;
	}
	

	/**
	 * 从文件中获取密钥字节
	 * @param filePath 密钥文件路径，该文件中是二进制的密钥数据
	 * @return	密钥
	 * @throws IOException
	 * @throws AwcException 
	 */
	public byte[] getKeyContent(String filePath) throws IOException  {
		byte [] key = null;
		log.debug("absolute path: " + new File(".").getAbsolutePath());
		try (
            /** 将文件中的公钥对象读出 */
				FileInputStream fis = new FileInputStream(new File(filePath));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
		    int ch = -1;
		    while((ch = fis.read()) != -1)
		    	baos.write((byte)ch);

		    key = baos.toByteArray();
			return key;
        }
	}

	/**
	 * 单例模式读取本地private key
	 * @return
	 * @throws IOException
	 */
	public byte[] getPrivateKeyByte() throws IOException {
		if (this.privateKeyByte == null) {
			this.privateKeyByte = getKeyContent(this.privateKeyPath);
		}
		return privateKeyByte;
	}


	/**
	 * 单例模式读取本地public key
	 * @return
	 * @throws IOException
	 */
	public byte[] getPublicKeyBytes() throws IOException {
		if (this.publicKeyBytes == null) {
			this.publicKeyBytes = getKeyContent(this.publicKeyPath);
		}
		return publicKeyBytes;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}

	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

}
