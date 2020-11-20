package cn.suanzi.baomi.shop.fragment;

import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.TextMessage;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.AddTextMessageActivity;
import cn.suanzi.baomi.shop.adapter.TextMessageAdapter;
import cn.suanzi.baomi.shop.model.GetMRecipientListTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 设置接受短信息
 * @author liyanfang
 */
public class TextMessageFragment extends Fragment {
	
	private final static String TAG = TextMessageFragment.class.getSimpleName();
	/** 跳转码*/
	public static final int REQUEST_CODE = 100;
	/** 响应码*/
	public static final int RESPONSE_CODE = 101;
	/** 短信接受者列表*/
	private ListView mMsgList;
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 本页面第一运行*/
	private boolean mFirstRunFlag = true;
	/** 联系人的适配器*/
	private TextMessageAdapter mMessageAdapter;
	
	public static TextMessageFragment newInstance() {
		Bundle args = new Bundle();
		TextMessageFragment fragment = new TextMessageFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_text_message,container, false);// 说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this, view);
		findView(view);
		initData();
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null){
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 获取控件
	 */
	private void findView(View view) {
		// 标题
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		// 添加短信的人
		TextView tvEdit = (TextView) view.findViewById(R.id.tv_msg);
		tvTitle.setText(Util.getString(R.string.set_msg));
		tvEdit.setText(Util.getString(R.string.set_add));
		mMsgList = (ListView) view.findViewById(R.id.lv_text_msg);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		getMRecipientList(); // 获取数据
	}

	/**
	 * 获取短信设置人
	 */
	private void getMRecipientList () {
		if (mFirstRunFlag) {
			mFirstRunFlag = false;
			ViewSolveUtils.setNoData(mMsgList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new GetMRecipientListTask(getMyActivity(), new GetMRecipientListTask.Callback() {
			
			@Override
			public void getResult(JSONArray result) {
				if (null != result) {
					ViewSolveUtils.setNoData(mMsgList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					List<TextMessage> messages = new Gson().fromJson(result.toString(),  new TypeToken<List<TextMessage>>() {}.getType());
					if (null == mMessageAdapter) {
						mMessageAdapter = new TextMessageAdapter(getMyActivity(), messages);
						mMsgList.setAdapter(mMessageAdapter);
					} else {
						mMessageAdapter.setItems(messages);
					}
				} else {
					ViewSolveUtils.morePageOne(mMsgList, mLyView, mIvView, mProgView, 1);
				}
			}
		}).execute();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == RESPONSE_CODE) {
				getMRecipientList();
			}
			break;
		}
	}
	
	/**
	 * 点击返回按钮 和添加短信接受者
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.tv_msg})
	public void btnActAddBackClick(View view) {		
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getMyActivity().finish();
			break;
		case R.id.tv_msg:
			Intent intent = new Intent(getMyActivity(), AddTextMessageActivity.class);
			startActivityForResult(intent,REQUEST_CODE);
			break;

		default:
			break;
		}
	}
}
