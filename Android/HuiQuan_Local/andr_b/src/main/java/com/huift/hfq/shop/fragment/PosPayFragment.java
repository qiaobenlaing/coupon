// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.huift.hfq.base.Util;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.huift.hfq.shop.R;

/**
 * 手机连接POS机的支付界面
 * @author yanfang.li
 */
public class PosPayFragment extends Fragment {
	private static final String TAG = "PosPayFragment";
	/**消费金额**/
	private EditText mEdtPay;
	/**原流水**/
	private EditText mEdtTraceNum;
	/**交易日期**/
	private EditText mEdtDate;
	private static String componentName = "com.icbc.mpos";
	private static String mainActivty = "com.icbc.mpos.IcbcActivity";
	private MyBroadcastReciver myReciver;

	/**
	 * 需要传递参数时有利于解耦 
	 * @return PosPayFragment
	 */
	public static PosPayFragment newInstance() {
		Bundle args = new Bundle();
		PosPayFragment fragment = new PosPayFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pospay, container, false);
		
		// 注册一个广播
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.abel.action.broadcast");
        myReciver = new MyBroadcastReciver();
        getActivity().registerReceiver(myReciver, intentFilter);
        Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		Button btnSale = (Button) v.findViewById(R.id.btn_pos_sale);// 消费
		Button btnAgainSure = (Button) v.findViewById(R.id.btn_pos_again_sure);// 再次确认指令
		Button btnSearchDtl = (Button) v.findViewById(R.id.btn_pos_search_dtl);// 查看明细
		Button btnSetLogo = (Button) v.findViewById(R.id.btn_pos_set_logo);// 更换logo
		mEdtPay = (EditText) v.findViewById(R.id.edt_pos_pay_menoy);// 消费金额
		mEdtTraceNum = (EditText) v.findViewById(R.id.edt_pos_old_trace);// 流水号
		mEdtDate = (EditText) v.findViewById(R.id.edt_pos_dates);// 交易日期
		Button btnResult = (Button) v.findViewById(R.id.btn_pos_result);// 获取结果
		// 设置背景颜色
		mEdtPay.setTextColor(Color.RED);
		// 一边输入一边获取里面内容的长度
		mEdtPay.setSelection(mEdtPay.getText().toString().length());
		// 设置交易时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mEdtDate.setText(sdf.format(new Date()));
		// 给四个按钮设置点击事件
		btnSale.setOnClickListener(btnClick);
		btnAgainSure.setOnClickListener(btnClick);
		btnSearchDtl.setOnClickListener(btnClick);
		btnSetLogo.setOnClickListener(btnClick);
		btnResult.setOnClickListener(btnClick);

	}

	/**
	 * 接收一个广播
	 */
	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e("PosPayFragment", "action:" + action);
			if (action.equals("cn.abel.action.broadcast")) {
				String RecvData = intent.getStringExtra("RecvData");
				// 在控制台显示接收到的广播内容
				Log.e("PosPayFragment", "RecvData:" + RecvData);
				// 在android端显示接收到的广播内容
				Toast.makeText(getActivity(), RecvData, 1).show();
			}
		}
	}

	/**
	 * 消费 ，再次确认指令，查看明细，更换logo的点击事件
	 */
	OnClickListener btnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_pos_sale:// 点击消费按钮
				startSale();
				break;
			case R.id.btn_pos_again_sure:// 点击再次确认指令按钮
				startAgainSure();
				break;
			case R.id.btn_pos_search_dtl:// 点击查看明细按钮
				searchDetail();
				break;
			case R.id.btn_pos_set_logo:// 点击更换logo按钮
				setLogo();
				break;
			case R.id.btn_pos_result:// 点击更换logo按钮
				Toast.makeText(
						getActivity(),
						"金额：" + mEdtPay.getText().toString() + "原流水："
							   + mEdtTraceNum.getText().toString() + "时间："
							   + mEdtDate.getText().toString(),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 消费
	 */
	private void startSale() {
		System.out.println("======startSale========");
		Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String amt = mEdtPay.getText().toString();
		if (!amt.isEmpty() && Double.parseDouble(amt) != 0) {
			String trace = mEdtTraceNum.getText().toString();
			if (!trace.isEmpty()) {
				StringBuffer sendBuf = new StringBuffer("1001|");
				sendBuf.append("004").append(amt).append("|");
				sendBuf.append("011").append(trace).append("|");
				sendBuf.append("621").append("0").append("|");
				sendBuf.append("061").append("收款方").append("|");
				sendBuf.append("062").append("唯品会").append("|");
				sendBuf.append("063").append("订单号").append("|");
				sendBuf.append("065").append("收货人").append("|");
				intent.putExtra("SendData", sendBuf.toString());

				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "请输入流水号", Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 再次确认
	 */
	private void startAgainSure() {
		System.out.println("======startAgainSure========");
		Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String trace = mEdtTraceNum.getText().toString();
		if (!trace.isEmpty()) {
			StringBuffer sendBuf = new StringBuffer("1015|");
			sendBuf.append("011").append(trace).append("|");
			intent.putExtra("SendData", sendBuf.toString());
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), "请输入流水号", Toast.LENGTH_LONG).show();
			return;
		}
	}

	/**
	 * 查询详情
	 */
	private void searchDetail() {
		System.out.println("======searchDetail========");
		Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String trace = mEdtTraceNum.getText().toString();
		if (!trace.isEmpty()) {
			StringBuffer sendBuf = new StringBuffer("1037|");
			sendBuf.append("011").append(trace).append("|");
			intent.putExtra("SendData", sendBuf.toString());
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), "请输入流水号", Toast.LENGTH_LONG).show();
			return;
		}
	}

	/**
	 * 设置logo
	 */
	private void setLogo() {
		Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		StringBuffer sendBuf = new StringBuffer("1038|");
		sendBuf.append("011").append(0000000000).append("|");
		sendBuf.append("050").append("/mnt/sdcard/mpos/q2.png").append("|");
		intent.putExtra("SendData", sendBuf.toString());
		startActivity(intent);
	}

}
