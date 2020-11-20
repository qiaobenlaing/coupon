package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Message;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.MyHomeMessageAdapter;
import cn.suanzi.baomi.cust.model.MessageListTask;
import cn.suanzi.baomi.cust.model.MessageListTask.Callback;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的消息
 * @author qian.zhou
 */
public class MyHomeMessageFragment extends Fragment {
	private static final String TAG = MyHomeMessageFragment.class.getSimpleName();
	
	/** 加载商家广播页面 **/
	public static final String TYPE_0 = "0";
	/** 加载会员卡页面 **/
	public static final String TYPE_1 = "1";
	/** 加载优惠券 页面**/
	public static final String TYPE_2 = "2";
	public static final String TYPE_3 = "3";
	public static final String TYPE_4 = "4";
	public static final String TYPE_5 = "5";
	public static final String TYPE_6 = "6";
	private int page = 1;
	private String type;
	/**优惠券按钮**/
	@ViewInject(R.id.btn_cardlist_coupon)
	Button mBtnCardListCoupon;
	/**会员卡按钮**/
	@ViewInject(R.id.btn_cardlist_card)
	Button mBtnCardListCard;
	/**商家沟通按钮**/
	@ViewInject(R.id.btn_cardlist_communicate)
	Button mBtnCardListCommunicate;
	/**商家广播按钮**/
	@ViewInject(R.id.btn_cardlist_broadcast)
	Button mBtnCardListBroadcast;
	/** Fragment管理器 */
	private RadioGroup couponTypeGroup;
	private RadioButton mBtnCoupon;
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private List<Message> lists;
	private Handler handler;
	private MyHomeMessageAdapter adapter;
	
    View v;

	public static MyHomeMessageFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeMessageFragment fragment = new MyHomeMessageFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(v != null){
			return v;
		}
		v = inflater.inflate(R.layout.fragment_myhome_message, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getMyActivity());
		init();
		initData();
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;    
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mFragmentManager = getMyActivity().getFragmentManager();
		mBtnCardListCoupon.setBackgroundResource(R.drawable.card_set_btn);
		changeFragment(getMyActivity(),R.id.linear_cardlist_content, new MyFocusListFragment());
		type = TYPE_2;
		handler = new Handler();
		lists = new ArrayList<Message>();
		
		ListView listView = (ListView) v.findViewById(R.id.lv_message);
		adapter = new MyHomeMessageAdapter(getMyActivity(), lists);
		listView.setAdapter(adapter);
	}
	
	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity,int id, Fragment fragment) {
		mFragmentManager = activity.getFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();
	}
	
	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.btn_cardlist_coupon,R.id.btn_cardlist_card,R.id.btn_cardlist_communicate,R.id.btn_cardlist_broadcast })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cardlist_coupon://加载优惠券页面
			type = TYPE_2;
			getPageData();
			break;
		case R.id.btn_cardlist_card://加载会员卡页面
			type = TYPE_1;
			getPageData();
			break;
		case R.id.btn_cardlist_communicate://加载商家沟通页面

			break;
		case R.id.btn_cardlist_broadcast://加载商家广播页面
			type = TYPE_0;
			getPageData();
			break;
		default:
			break;
		}
	}
	
	private Runnable dataChanged = new Runnable(){
		
		public void run(){
			
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
		}
	};

	private void initData() {
		getPageData();
	}

	private void getPageData() {
		lists.clear();
		handler.post(dataChanged);
		new MessageListTask(getMyActivity(), new Callback() {

			@Override
			public void getResult(JSONObject data) {
				if(data == null){
					return;
				}
				Log.i("===data===", "data is : " + data);
				JSONArray result = (JSONArray)data.get("messageList");
				if(result == null ){
					return;
				}
				int size = result.size();
				for (int i = 0; i < size; i++) {
					JSONObject jsonresult = (JSONObject) result.get(i);
					Message cardlist = Util.json2Obj(jsonresult.toString(), Message.class);
					lists.add(cardlist);
				}
				handler.post(dataChanged);
			}
		}).execute(type, page + "");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeMessageFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeMessageFragment.class.getSimpleName());
	}
}
