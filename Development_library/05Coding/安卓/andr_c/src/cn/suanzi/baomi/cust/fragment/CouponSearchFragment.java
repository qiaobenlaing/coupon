package cn.suanzi.baomi.cust.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.CouponEffectCardAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.CouponSerachTask;
import cn.suanzi.baomi.cust.model.CouponSerachTask.Callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 模糊搜索商店
 * 
 * @author xx
 */
public class CouponSearchFragment extends Fragment implements IXListViewListener {
	public static final String TYPE = "type";
	/** 城市的数据源 */
	public static final String CITYDATTAS = "mCityDates";
	private static final String TAG = CouponSearchFragment.class.getSimpleName();
	private XListView listView;
	private EditText keywordEdt;// 搜索输入框
	private String searchWord, type;
	private int mPage = Util.NUM_ONE;
	private Type jsonType = new TypeToken<List<Shop>>() {
	}.getType();// 所属类别
	private Gson gson;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户编码 **/
	private String mUserCode;
	/** 线程 **/
	private Handler mHandler;
	/** 切换地区 */
	private TextView mTvArea;
	private LinearLayout mLyArea;
	private String mCitysName;
	/** 取值 */
	private Citys mCitys;
	private List<Citys> mCityDatas;
	/**显示内容*/
	private LinearLayout mLyNodate;
	
	List<BatchCoupon> mCouponDatas;
	CouponEffectCardAdapter adapter;
	
	boolean end = false;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static CouponSearchFragment newInstance() {
		Bundle args = new Bundle();
		CouponSearchFragment fragment = new CouponSearchFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		init(v);
		Util.addLoginActivity(getMyActivity());
		getPageData();// 全查询
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	private void init(View v) {
		TextView mIvBack = (TextView) v.findViewById(R.id.tv_turn);
		mIvBack.setBackgroundResource(R.drawable.backup);
		
		mCouponDatas = new ArrayList<BatchCoupon>();
		listView = (XListView) v.findViewById(R.id.listView);
		adapter = new CouponEffectCardAdapter(getMyActivity(), mCouponDatas);
		listView.setAdapter(adapter);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		// 上拉刷新
		listView.setXListViewListener(this);// 实现xListviewListener接口
		mCitys = new Citys();
		// 取出定位的城市
		mCitys = DB.getObj(CustConst.Key.CITY_OBJ, Citys.class);
		if (mCitys != null) {
			Log.d(TAG, "传过来的城市的名称：：：：：：：:::::" + mCitys.getName());
			mTvArea = (TextView) v.findViewById(R.id.tv_area);
			mTvArea.setVisibility(View.GONE);
			if (mCitys.getName() == null || "".equals(mCitys.getName())) {// 定位的城市为空
				mCitysName = "";
				mTvArea.setText(getResources().getString(R.string.home_selcitys));
			} 
			else {
				mTvArea.setText(mCitys.getName());
				mCitysName = mCitys.getName();
			}
		}
		// 取出所有城市的集合
		mCityDatas = new ArrayList<Citys>();
		Intent intent = getMyActivity().getIntent();
		mCityDatas = (List<Citys>) intent.getSerializableExtra(CITYDATTAS);

		mLyArea = (LinearLayout) v.findViewById(R.id.ly_area);
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		// 判断userCode是否为空
		if (Util.isEmpty(mUserCode)) {
			mUserCode = "";
		} else {
			mUserCode = mUserToken.getUserCode();// 用户编码
		}
		type = StringUtils.stripToEmpty(getMyActivity().getIntent().getStringExtra(TYPE));
		gson = new Gson();
		keywordEdt = (EditText) v.findViewById(R.id.edt_search);
		keywordEdt.setHint(getString(R.string.search_shopcoupon));
		keywordEdt.findFocus();
		keywordEdt.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					mPage = Util.NUM_ONE;
					if(mCouponDatas != null){
						mCouponDatas.clear();
					}
					if(adapter != null){
						adapter.notifyDataSetChanged();
					}
					getPageData();
				}
				return false;
			}
		});
		mHandler = new Handler();
	}

	private void getPageData() {// 有提示
		searchWord = keywordEdt.getText().toString();// 搜索的输入信息
		// 获得一个用户信息对象
		new CouponSerachTask(getMyActivity(), new Callback() {
			@Override
			public void getResult(JSONObject data) {
				if (data == null || data.size() == Util.NUM_ZERO) {
					listView.setPullLoadEnable(false);
					end = true;
					return ;
				}
				listView.setPullLoadEnable(true);
				Log.i(TAG, "result is : " + data);
				int total = Integer.parseInt("" + data.get("totalCount"));
				int page = Integer.parseInt("" + data.get("page"));
				if(1 == page || "1".equals(mPage)){
					mCouponDatas.clear();
				}
				JSONArray result = (JSONArray) data.get("couponList");
				if(result == null){
					return;
				}
				int size = result.size();
				
				for (int i = 0; i < size; i++) {
					BatchCoupon coupon = Util.json2Obj(result.get(i).toString(), BatchCoupon.class);
					mCouponDatas.add(coupon);
				}
				int mCount = mCouponDatas.size();
				if(mCount == 0){
					mLyNodate.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}else {
					mLyNodate.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				if(mCount >= total){
				//	Toast.makeText(getActivity(), R.string.no_more, Toast.LENGTH_LONG).show();
					listView.setPullLoadEnable(false);
					end = true;
				}
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
			}
		}).execute("0",searchWord,Double.toString(mCitys == null ? 0 : mCitys.getLongitude()),
				Double.toString(mCitys == null ? 0 : mCitys.getLatitude()),
				String.valueOf(mPage), mCitysName);
	}

	/**
	 * 切换地名
	 */
	@OnClick(R.id.tv_area)
	public void searchArea(View v) {
		View view = LayoutInflater.from(getMyActivity()).inflate(
				R.layout.popupwindow_homearea, null);
		final ListView lvArea = (ListView) view.findViewById(R.id.lv_area);
		CitysAdapter adapter = new CitysAdapter(getMyActivity(), mCityDatas);
		lvArea.setAdapter(adapter);
		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(mLyArea, 0, 0);
		lvArea.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long longId) {
				Citys citys = (Citys) lvArea.getItemAtPosition(position);
				String cityName = citys.getName();
				mTvArea.setText(cityName);
					mCitysName = cityName;
					mPage = 1;
					getPageData();
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 城市的列表
	 */
	private class CitysAdapter extends CommonListViewAdapter<Citys> {
		public CitysAdapter(Activity activity, List<Citys> datas) {
			super(activity, datas);
		}

		@Override
		public android.view.View getView(int position,
				android.view.View convertView, ViewGroup parent) {
			CommenViewHolder holder = CommenViewHolder.get(getMyActivity(),
					convertView, parent, R.layout.item_city, position);
			Citys citys = (Citys) getItem(position);
			((TextView) holder.getView(R.id.tv_homearea)).setText(citys
					.getName());
			return holder.getConvertView();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(end){
					listView.setPullLoadEnable(false);
					return;
				}
				mPage++;
				getPageData();
				listView.stopLoadMore();
			}
		}, 2000);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.tv_turn)
	public void ivBank(View view) {
		getMyActivity().finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponSearchFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponSearchFragment.class.getSimpleName());
	}
}
