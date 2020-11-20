// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;

/**
 * 手机连接pos机支付界面
 * @author yanfang.li
 */
public class PosPayActivity extends Activity implements OnClickListener{
	
	private EditText amount;
	private EditText traceNum;
	private static final String TAG = "PosPayFragment";
	/**消费金额**/
	private EditText mEdtPay;
	/**原流水**/
	private EditText mEdtTraceNum;
	/**交易日期**/
	private EditText mEdtDate;
	
	private static final int SALE = 0x01;
	private static final int SALE_VOID = 0x02;
	private static final int REFUND = 0x04;
	
	private static String componentName = "com.icbc.mpos";
	private static String mainActivty = "com.icbc.mpos.IcbcActivity";
	
	private MyBroadcastReciver myReciver;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pospay);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.abel.action.broadcast");
        myReciver = new MyBroadcastReciver();
        this.registerReceiver(myReciver, intentFilter);
        Button btnSale = (Button) findViewById(R.id.btn_pos_sale);// 消费
		Button btnAgainSure = (Button) findViewById(R.id.btn_pos_again_sure);// 再次确认指令
		Button btnSearchDtl = (Button) findViewById(R.id.btn_pos_search_dtl);// 查看明细
		Button btnSetLogo = (Button)findViewById(R.id.btn_pos_set_logo);// 更换logo
		mEdtPay = (EditText) findViewById(R.id.edt_pos_pay_menoy);// 消费金额
		mEdtTraceNum = (EditText) findViewById(R.id.edt_pos_old_trace);// 流水号
		mEdtDate = (EditText) findViewById(R.id.edt_pos_dates);// 交易日期
		Button btnResult = (Button) findViewById(R.id.btn_pos_result);// 获取结果
		// 设置背景颜色
		mEdtPay.setTextColor(Color.RED);
		// 一边输入一边获取里面内容的长度
		mEdtPay.setSelection(mEdtPay.getText().toString().length());
		// 设置交易时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mEdtDate.setText(sdf.format(new Date()));
		
    }
    
//    @Override
//   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	super.onActivityResult(requestCode, resultCode, data);
//    	if(data != null && data.getStringExtra("RecvData") != null){
//			Log.e("ICBCappDEMO",  data.getStringExtra("RecvData"));
//			switch(resultCode){
//	    	case Activity.RESULT_OK:
//	    		switch(requestCode){
//		   		case SALE:
////			   			StringBuffer buffer = new StringBuffer();
////			   			buffer.append(data.getStringExtra("amount")).append("  ").append(data.getStringExtra("traceNo")).append("  ").
////			   							append(data.getStringExtra("referenceNo")).append("  ").append(data.getStringExtra("cardNo")).
////			   							append("  ").append(data.getStringExtra("issue")).append(" ").append(data.getStringExtra("type"));
////			   			System.out.println("info = ===== " + buffer.toString());
////			   			Toast.makeText(this, "消费成功!\n" + buffer.toString(), Toast.LENGTH_LONG).show();
//		   			Toast.makeText(this, "消费成功:"+ data.getStringExtra("RecvData"), Toast.LENGTH_LONG).show();
//		   			break;
//		   		default:
//		   			Toast.makeText(this, "requestCode不存在", Toast.LENGTH_LONG).show();
//		   			System.out.println("request code = " + requestCode + " not exist!");
//		   			break;
//		   		}
//	    		break;
//	    	default:
//	    		Toast.makeText(this, "交易失败:"+data.getStringExtra("RecvData"), Toast.LENGTH_LONG).show();
//	    		break;
//		    }
//		} else {
//			Toast.makeText(this, "交易失败,返回参数为空", Toast.LENGTH_LONG).show();
//		}
//    	
//   	}
    private class MyBroadcastReciver extends BroadcastReceiver {  
    	@Override
    	public void onReceive(Context context, Intent intent) {
    	   String action = intent.getAction();
    	   Log.e("ICBCAppDemoActivity","action:"+ action);
    	   if(action.equals("cn.abel.action.broadcast")) {
	    	    String RecvData = intent.getStringExtra("RecvData");
	    	    //在控制台显示接收到的广播内容
	    	    Log.e("ICBCAppDemoActivity","RecvData:"+ RecvData);
	    	    //在android端显示接收到的广播内容
	    	    Toast.makeText(PosPayActivity.this, RecvData, 1).show();
    	   }
    	  }
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        unregisterReceiver(myReciver);
//    }
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
				Toast.makeText(this,
						"金额：" + mEdtPay.getText().toString() + "原流水："
							   + mEdtTraceNum.getText().toString() + "时间："
							   + mEdtDate.getText().toString(),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
	}
    
	private void startSale() {
		System.out.println("======startSale========");
		Intent intent = new Intent();
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String amt = amount.getText().toString();
		if(!amt.isEmpty() && Double.parseDouble(amt) != 0) {
			String trace = traceNum.getText().toString();
			if(!trace.isEmpty() ) {
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
			}else{
				Toast.makeText(getApplicationContext(), "请输入流水号", Toast.LENGTH_LONG).show();
				return;
			}
		}else{
			Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_LONG).show();
			return;
		}
//		startActivityForResult(intent, SALE);
	}
	private void startAgainSure() {
		System.out.println("======startAgainSure========");
		Intent intent = new Intent();
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String trace = traceNum.getText().toString();
		if(!trace.isEmpty() ) {
			StringBuffer sendBuf = new StringBuffer("1015|");
			sendBuf.append("011").append(trace).append("|");
			intent.putExtra("SendData", sendBuf.toString());
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "请输入流水号", Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	private void searchDetail() {
		System.out.println("======searchDetail========");
		Intent intent = new Intent();
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		String trace = traceNum.getText().toString();
		if(!trace.isEmpty() ) {
			StringBuffer sendBuf = new StringBuffer("1037|");
			sendBuf.append("011").append(trace).append("|");
			intent.putExtra("SendData", sendBuf.toString());
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "请输入流水号", Toast.LENGTH_LONG).show();
			return;
		}
	}

	private void setLogo() {
		Intent intent = new Intent();
		ComponentName compName = new ComponentName(componentName, mainActivty);
		intent.setComponent(compName);
		StringBuffer sendBuf = new StringBuffer("1038|");
		sendBuf.append("011").append(0000000000).append("|");
		sendBuf.append("050").append("/mnt/sdcard/mpos/q2.png").append("|");
		intent.putExtra("SendData", sendBuf.toString());
		startActivity(intent);
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
