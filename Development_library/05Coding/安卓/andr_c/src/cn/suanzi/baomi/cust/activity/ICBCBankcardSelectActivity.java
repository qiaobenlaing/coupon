package cn.suanzi.baomi.cust.activity;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BankList;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.CardSelectAdapter;
import cn.suanzi.baomi.cust.fragment.MyHomeAddBankFragment;
import cn.suanzi.baomi.cust.model.MyHomeBankListTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ICBCBankcardSelectActivity extends Activity {
	public static final int REQUEST_CODE = 100;
	public static final int PAY_PWD_REQUEST_CODE = 102;
	public static final int RESULT_OK = 101;

	private int mPage = 1;
	private ListView mListView;
	private CardSelectAdapter mCardSelectAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_icbc_pay_cardbank_select);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		init();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.lv_cards);
		// 给mLvBankList添加点击事件
		mListView.setOnItemClickListener(bankListListener);
		
		LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fragment_myhomebank_list_footer,null);
		mListView.addFooterView(view);
		
		//添加银行卡
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentAdd = new Intent(ICBCBankcardSelectActivity.this, MyHomeAddBankActivity.class);
				intentAdd.putExtra("title", ICBCBankcardSelectActivity.this.getResources().getString(R.string.myhome_add));
				intentAdd.putExtra(MyHomeAddBankFragment.NORMALADD, false);
				startActivity(intentAdd);
			}
		});
		
		getBankAccountList();
	}

	/**
	 * 获取用户银行卡列表
	 */
	private void getBankAccountList() {
		// 银行卡列表的异步
		new MyHomeBankListTask(this, new MyHomeBankListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null != result) {
					JSONArray mActBanktArray = (JSONArray) result.get("bankAccountList");
					List<BankList> mLvBankListData = new ArrayList<BankList>();

					for (int i = 0; i < mActBanktArray.size(); i++) {
						JSONObject myHomeBankObject = (JSONObject) mActBanktArray.get(i);
						// 把JsonObject对象转换为实体
						BankList item = Util.json2Obj(myHomeBankObject.toString(), BankList.class);
						mLvBankListData.add(item);
					}
					mCardSelectAdapter = new CardSelectAdapter(ICBCBankcardSelectActivity.this, mLvBankListData);
					mListView.setAdapter(mCardSelectAdapter);
				}

			}
		}).execute(String.valueOf(mPage));
	}

	/**
	 * 给列表添加点击事件
	 */
	OnItemClickListener bankListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			BankList bankItem = (BankList) mListView.getAdapter().getItem(position);
			Intent intent = new Intent();
			intent.putExtra("accountNbrPre6", bankItem.getAccountNbrPre6());
			intent.putExtra("accountNbrLast4", bankItem.getAccountNbrLast4());
			intent.putExtra("bankName", bankItem.getBankName());
			intent.putExtra("bankAccountCode", bankItem.getBankAccountCode());
			intent.putExtra("mobileNbr", bankItem.getMobileNbr());
			setResult(RESULT_OK, intent);
			finish();
		}
	};

	@OnClick({ R.id.backup})
	private void click(View v) {
		switch (v.getId()) {
		case R.id.backup:
			finish();
			break;
		}
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	  }
	 
	 @Override
	protected void onRestart() {
		super.onRestart();
		getBankAccountList();
	}
}
