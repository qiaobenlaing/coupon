package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.CardAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.CardListTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 模糊搜索商店
 * @author yanfang.li
 */
public class CardInfoFragment extends Fragment implements IXListViewListener {
	private static final String TAG = CardInfoFragment.class.getSimpleName();
	public static final String ISFOCUS = "IsFocus";
	private XListView mListView;
	private AutoCompleteTextView keywordEdt;// 搜索输入框
	private CardAdapter mCardAdapter = null;// 适配器
	private String searchWord;
	private int mPage = Util.NUM_ONE;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户编码 **/
	private String mUserCode;
	/** 线程 **/
	private Handler mHandler;
	private String mCitysName;
	/** 经度 */
	private String mLongitude;
	/** 纬度 */
	private String mLatitude;
	private String mIsFollow;
	/**显示内容*/
	private LinearLayout mLyNodate;
	/**判断api是否调用成功*/
	private boolean mFlagData = false;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static CardInfoFragment newInstance() {
		Bundle args = new Bundle();
		CardInfoFragment fragment = new CardInfoFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_card, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(v);
		getPageData();// 全查询
		return v;
	}

	private void init(View v) {
		if(mPage == Util.NUM_ONE){
			mFlagData = true;
		}
		Intent intent = getActivity().getIntent();
		String isfocus = intent.getStringExtra(ISFOCUS);
		if(String.valueOf(Util.NUM_ONE).equals(isfocus)){//如果是关注商家
			mIsFollow = String.valueOf(Util.NUM_ONE);
		} else if(String.valueOf(Util.NUM_ZERO).equals(isfocus)){//其他商家
			mIsFollow = String.valueOf(Util.NUM_ZERO);
		}
		// 取出定位的经纬度
		SharedPreferences preferences = getActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + cityName);
		if(Util.isEmpty(cityName)){
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			Log.d("TAG", "取出DB的定位城市的经度为 ：：：：：：： " + mLongitude);
			Log.d("TAG", "取出DB的定位城市的纬度为 ：：：：：：： " + mLatitude);
			mCitysName = "";
		} else{
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
			mCitysName = cityName;
		}
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();// 用户编码
		TextView tvBack = (TextView) v.findViewById(R.id.tv_area);
		tvBack.setBackgroundResource(R.drawable.backup);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		keywordEdt = (AutoCompleteTextView) v.findViewById(R.id.edt_search);
		keywordEdt.findFocus();
		keywordEdt.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					getPageData();
				}
				return false;
			}
		});
		mListView = (XListView) v.findViewById(R.id.listView);
		// 上拉刷新
		mListView.setXListViewListener(this);// 实现xListviewListener接口
		mHandler = new Handler();
	}

	private void getPageData() {// 有提示
		searchWord = keywordEdt.getText().toString();// 搜索的输入信息
		new CardListTask(getActivity(), new CardListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				if (result == null) {
					mListView.setPullLoadEnable(false);
					if(mPage > 1){
						mLyNodate.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
					} else{
						mLyNodate.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
					}
				} else {
					mLyNodate.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					mListView.setPullLoadEnable(true);
					// 总记录数
					int totalCount = (Integer.parseInt(result.get("totalCount").toString()));
					int totalpage = 0;
					if (totalCount % 10 == 0) {
						totalpage = totalCount / 10;
					} else {
						totalpage = totalCount / 10 + 1;
					}
					// 当前页数
					int page = (Integer.parseInt(result.get("page").toString()));
					// 显示记录数
					int count = (Integer.parseInt(result.get("count").toString()));
					if (page == totalpage) {
						if (page > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mListView.setPullLoadEnable(false);
					}
					if (count < CustConst.PAGE_NUM) {
						mListView.setPullLoadEnable(false);
					}
					List<Shop> data = new ArrayList<Shop>();
					JSONArray mArray = (JSONArray) result.get("cardList");
					for (int i = 0; i < mArray.size(); i++) {
						Shop shop = Util.json2Obj(mArray.get(i).toString(), Shop.class);
						data.add(shop);
					}
					if (mCardAdapter == null) {
						mCardAdapter = new CardAdapter(getActivity(), data);
						mListView.setAdapter(mCardAdapter);
					} else{
						if(mPage == 1){
							mCardAdapter.setItems(data);
						}else{
							mCardAdapter.addItems(data);
						}
					}
				}
			}
		}).execute(searchWord, mCitysName, mLongitude, mLatitude, mUserCode, String.valueOf(mPage), mIsFollow);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLoadMore() {
		if(mFlagData){
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPage++;
					getPageData();
					mListView.stopLoadMore();
				}
			}, 2000);
		}
	}

	/**
	 * 点击返回查看到活动列表
	 */
	@OnClick(R.id.tv_area)
	public void tvBackClick(View view) {
		getActivity().finish();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CardInfoFragment.class.getSimpleName()); //统计页面
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(CardInfoFragment.class.getSimpleName()); 
	}
}