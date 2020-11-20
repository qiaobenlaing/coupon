package com.huift.hfq.shop.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

public class MPosPluginHelper {

	public final static String componentName = "com.icbc.mpos";
	private static String mainActivty = "com.icbc.mpos.IcbcActivity";
	public static final String TAG = MPosPluginHelper.class.getSimpleName();

	public static boolean consume(Activity act, String money, String trace, String mcht, String mchtName,
			String orderNO, String contact,String shopId) {
		if (money == null || money.length() == 0) {
			Util.showToastZH("请输入消费金额");
			return false;
		}
		if (money.indexOf(".") >= 0) {
			if (money.substring(money.indexOf(".") + 1).length() > 2) {
				Util.showToastZH("输入消费金额异常");
				return false;
			}
		}
		BigInteger bigInteger = new BigDecimal(money).multiply(new BigDecimal("100")).toBigInteger();
		money = String.format("%012d", bigInteger);
		if (trace == null || trace.length() == 0) {
			Util.showToastZH("请输入流水号");
			return false;
		}
		Intent intent = new Intent();
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		if (!money.isEmpty() && Double.parseDouble(money) != 0) {
			if (!trace.isEmpty()) {
				DB.saveStr(Const.MPOS_CSMCODE, orderNO);
				StringBuffer sendBuf = new StringBuffer("1001|");
				sendBuf.append("004").append(money).append("|"); // 金额
				sendBuf.append("011").append(orderNO).append("|");// 订单号 
				sendBuf.append("621").append("0").append("|");  // 1  静默 
				sendBuf.append("061").append("").append("|"); // TODO
				sendBuf.append("062").append(shopId).append("|"); // 商家号 shopId
				sendBuf.append("063").append("").append("|");
				sendBuf.append("065").append(contact).append("|");
				Log.d(TAG, "money="+money+",orderNO="+orderNO+",shopId="+shopId);
				Log.d(TAG, "消费：" + sendBuf.toString());
				intent.putExtra("SendData", sendBuf.toString());
				act.startActivity(intent);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
