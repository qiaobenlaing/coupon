package cn.suanzi.baomi.cust.activity;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Bonus;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.MyHomeRedBagAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.ListRedBagTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MyRedBagActivity extends Activity{
	
	private static final String TAG = MyRedBagActivity.class.getSimpleName();

	private ListView redBagRecyclerView;
	private MyHomeRedBagAdapter redBagAdapter;
	private LinearLayoutManager layoutManager;
	private Type jsonType = new TypeToken<List<Bonus>>() {
	}.getType();
	private List<Bonus> bonus;
	private Gson gson;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myhome_redbag);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
		initData();
	}

	private void initData() {
		String userCode = "";
		String tokenCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			userCode = userToken.getUserCode();
			tokenCode = userToken.getTokenCode();
		}
		
		if (Util.isNetworkOpen(MyRedBagActivity.this)) {
			new ListRedBagTask(this, new ListRedBagTask.Callback() {
				@Override
				public void getResult(JSONArray result) {
						if (null == result.toString() || "[]".equals(result.toString())) {
							ViewSolveUtils.morePageOne(redBagRecyclerView, mLyView, mIvView, mProgView, 1);
						}else{
							ViewSolveUtils.setNoData(redBagRecyclerView, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
							Log.d(TAG, result.toJSONString());
							bonus = gson.fromJson(result.toJSONString(), jsonType);
							if (bonus == null || bonus.size() ==0) {
									//mLyNodate.setVisibility(View.VISIBLE);
									redBagRecyclerView.setVisibility(View.GONE);
									Log.d(TAG, "+++++++++++");
							}
							redBagAdapter =  new MyHomeRedBagAdapter(MyRedBagActivity.this, bonus);
							redBagRecyclerView.setAdapter(redBagAdapter);
						}
						
					}
			}).execute(userCode, tokenCode);
		
		}else{
			ViewSolveUtils.morePageOne(redBagRecyclerView, mLyView, mIvView, mProgView, 1);
			Util.getContentValidate(R.string.toast_data_error);
		}
	}

	private void init() {
		mLyView = (LinearLayout) findViewById(R.id.ly_nodate);
		mIvView = (ImageView) findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
		
		redBagRecyclerView = (ListView)findViewById(R.id.recycler_view);
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		
	}

	@OnClick({ R.id.backup })
	private void click(View view) {
		switch (view.getId()) {
		case R.id.backup:
			finish();
			break;
		}
	}

	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	 }

}
