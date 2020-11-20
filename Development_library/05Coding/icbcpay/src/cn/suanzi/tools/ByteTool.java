package cn.suanzi.tools;

/**
 * 字节码工具
 * 
 * @author 刘卫平
 * @version 1.0.0	2014-1-7
 */
public class ByteTool {

	public ByteTool() {
	}

	public static byte[] fill(byte src[], int len, boolean isLeft, byte fill) {
		if (src == null)
			return null;
		int srcLen = src.length;
		if (len == srcLen)
			return src;
		byte ret[] = new byte[len];
		for (int i = 0; i < len; i++)
			ret[i] = fill;

		if (isLeft)
			System.arraycopy(src, 0, ret, len - srcLen, srcLen);
		else
			System.arraycopy(src, 0, ret, 0, srcLen);
		return ret;
	}
	
	/**
	 * 转换byte[]为整型
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] b) {
		int intValue = 0;
		for (int i = 0; i < b.length; i++) {
			intValue += (b[i] & 0xFF) << (8 * (3 - i));
			// System.out.print(Integer.toBinaryString(intValue)+" ");
		}
		return intValue;
	}

	/**
	 * 转换整型为byte[]
	 * @param intValue
	 * @return
	 */
	public static byte[] int2Byte(int intValue) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
			// System.out.print(Integer.toBinaryString(b[i])+" ");
			// System.out.print((b[i]& 0xFF)+" ");
		}
		return b;
	}
	
	/** 
     * 将short转成byte[2] 
     * @param a 
     * @return 
     */  
    public static byte[] short2Byte(short a){  
        byte[] b = new byte[2];  
          
        b[0] = (byte) (a >> 8);  
        b[1] = (byte) (a);  
          
        return b;  
    }  

	/** 
     * 将int转成byte[2] 
     * @param a 
     * @return 
     */  
    public static byte[] short2Byte(int a){  
    	byte[] b = new byte[2];
		for (int i = 2; i < 4; i++) {
			b[i - 2] = (byte) (a >> 8 * (3 - i) & 0xFF);
		}
		return b;
    }  
    /** 
     * 将short转成byte[2] 
     * @param a 
     * @param b 
     * @param offset b中的偏移量 
     */  
    public static void short2Byte(short a, byte[] b, int offset){  
        b[offset] = (byte) (a >> 8);  
        b[offset+1] = (byte) (a);  
    }  
      
    /** 
     * 将byte[2]转换成short 
     * @param b 
     * @return 
     */  
    public static short byte2Short(byte[] b){  
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));  
    }  
      
    /** 
     * 将byte[2]转换成short 
     * @param b 
     * @param offset 
     * @return  
     */  
    public static short byte2Short(byte[] b, int offset){  
        return (short) (((b[offset] & 0xff) << 8) | (b[offset+1] & 0xff));  
    }  
    
}
