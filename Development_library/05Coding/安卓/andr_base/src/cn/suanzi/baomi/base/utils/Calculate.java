package cn.suanzi.baomi.base.utils;

import java.math.BigDecimal;

import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.widget.EditText;
import cn.suanzi.baomi.base.Util;

/**
 * 加减乘除运算
 * @author ad
 *
 */
public class Calculate {
	private static final String TAG = Calculate.class.getSimpleName();
	
	/** 整除的精度*/
	private static final int DEF_DIV_SCALE = 10;
	/** 向上取整的位数*/
	private static final int CEIL_NUM = 100;
	/** 0*/
	private static final String ZENO = "0";
	
	/** 
	* 提供精确的小数位四舍五入处理。 
	* @param v 需要四舍五入的数字 
	* @param scale 小数点后保留几位 
	* @return 四舍五入后的结果 
	*/  
	public static double round(double dobuleNum, int scale) {  
	   if (scale < 0) {  
	    throw new IllegalArgumentException(  
	      "The scale must be a positive integer or zero");  
	   }  
	   BigDecimal b = new BigDecimal(Double.toString(dobuleNum));  
	   BigDecimal ne = new BigDecimal("1");  
	   return b.divide(ne, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	
	/**
	 * 两位数相减
	 * @param fisrtNum
	 * @param second
	 * @return
	 */
	public static double suBtraction(double fisrtNum,double second) {
		double ddcPrice = 0;
		BigDecimal bdTotal = new BigDecimal(Double.toString(fisrtNum));
		BigDecimal bdNew = new BigDecimal(Double.toString(second));
	    ddcPrice = bdTotal.subtract(bdNew).doubleValue();
		return ddcPrice;
	}
	
	/** 
	* 提供精确的加法运算。 
	* @param v1 被加数 
	* @param v2 加数 
	* @return 两个参数的和 
	*/  
	  
	public static double add(double v1, double v2) {  
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
	   return b1.add(b2).doubleValue();  
	}
	
	/** 
	* 提供精确的乘法运算。 
	* @param v1 被乘数 
	* @param v2 乘数 
	* @return 两个参数的积 
	*/  
	public static double mul(double v1, double v2) {  
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
	   return b1.multiply(b2).doubleValue();  
	}  
	  
	/** 
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 
	* 小数点以后10位，以后的数字四舍五入。 
	* @param v1 被除数 
	* @param v2 除数 
	* @return 两个参数的商 
	*/  
	  
	public static double div(double v1, double v2) {  
	   return div(v1, v2, DEF_DIV_SCALE);  
	}  
	  
	/** 
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 
	* 定精度，以后的数字四舍五入。 
	* @param v1 被除数 
	* @param v2 除数 
	* @param scale 表示表示需要精确到小数点以后几位。 
	* @return 两个参数的商 
	*/  
	public static double div(double v1, double v2, int scale) {  
	   if (scale < 0) {  
	    throw new IllegalArgumentException(  
	      "The scale must be a positive integer or zero");  
	   }  
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
	   return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	
	/**
	 * 向上取整
	 * @param price
	 * @return double类型的小数
	 */
	public static double ceil(double price) {
		double mulNum = Calculate.mul(price, CEIL_NUM);
		double ceilNum = Math.ceil(mulNum);
		return Calculate.div(ceilNum, CEIL_NUM);
	}
	
	/**
	 * 向下取整
	 * @param price
	 * @return double类型的小数
	 */
	public static double floor(double price) {
		double mulNum = Calculate.mul(price, CEIL_NUM);
		double floorNum = Math.floor(mulNum);
		return Calculate.div(floorNum, CEIL_NUM);
	}
	
	/**
	 * 向下取整
	 * @param price
	 * @return double类型的小数
	 */
	public static BigDecimal floorBigDecimal(double price) {
		double mulNum = Calculate.mul(price, CEIL_NUM);
		double floorNum = Math.floor(mulNum);
		double divNum = Calculate.div(floorNum, 100); 
		return Util.saveTwoDecima(divNum);
	}
	
	/**
	 * 向上取整
	 * @param price
	 * @return BigDecimal类型的两位数
	 */
	public static BigDecimal ceilBigDecimal(double price) {
		double mulNum = Calculate.mul(price, 100);
		double ceilNum = Math.ceil(mulNum);
		double divNum = Calculate.div(ceilNum, 100); 
		return Util.saveTwoDecima(divNum);
	}
	
	/**
	 * 得到第一位数没有0的数字
	 */
	public static String getNum(String oldStr) {
		String newStr = "";
		if (null == oldStr || Util.isEmpty(oldStr) || ZENO.equals(oldStr)) {
			newStr = "0";
		} else {
			if (oldStr.lastIndexOf(".") >= 0) {
				newStr = oldStr.replaceFirst("^0*", "");
//				card.getDiscountRequire().replaceAll("^(0+)", "")
				if (newStr.lastIndexOf(".") == 0) {
					newStr = "0" + newStr;
				}
			} else {
				newStr = oldStr.replaceFirst("^0*", "");
			}
		}
		
		return newStr;
	}
	
	/**
	 * 使用java正则表达式去掉多余的.与0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}
	
	/**
	 * 输入金额为两位数
	 * @param editText
	 */
	public static void getStrInputMoney (EditText editText) {
		String price = editText.getText().toString();
	     if (price.contains(".")) {
             if (price.length() - 1 - price.indexOf(".") > 2) {
            	 price = price.subSequence(0,price.indexOf(".") + 3).toString();
                 editText.setText(price);
                 editText.setSelection(price.length());
             }
         }
         if (price.trim().substring(0).equals(".")) {
        	 price = "0" + price;
             editText.setText(price);
             editText.setSelection(2);
         }

         if (price.startsWith("0") && price.toString().trim().length() > 1) {
        	 Log.d(TAG, "price:" + price);
             if (!price.toString().substring(1, 2).equals(".")) {
            	 Log.d(TAG, "price222:" + price.subSequence(0, 1));
//                 editText.setText(price.subSequence(0, 1));
                 editText.setText("");
                 editText.setSelection(0);
                 return;
             }
         } 
         Calculate.cursorLast(editText); // 光标位于最后
	}
	
	/**
	 * 光标位于editText的最后界面
	 * @param editText
	 */
	public static void cursorLast(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable)text;
			Selection.setSelection(spanText, text.length());
		}
	}
	
}
