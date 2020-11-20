package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Enroll;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.adapter.ListActParticipantAdapter;
import com.huift.hfq.shop.model.ListActParticipantTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 报名人数
 * @author wensi.yu
 *
 */
public class ContentEnrollFragment extends Fragment implements IXListViewListener{

	public static final String CONTENTCODE = "contentCode";
	/**返回**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/**活动列表**/
	private XListView mLvAclistEnrolllist;
	/** 页码 **/
	private int mPage = 1;
	/**所属类别**/
	private Type jsonType = new TypeToken<List<Enroll>>() {
	}.getType();
	/**背景**/
	@ViewInject(R.id.ly_enroll_nodate)
	private LinearLayout mLyNoDate;
	/**活动编码**/
	private String actCode;
	/**判断api是否请求**/
	private boolean mFlage = false;

	private Handler mHandler;
	private ListActParticipantAdapter queryAdapter = null;

	public static ContentEnrollFragment newInstance() {
		Bundle args = new Bundle();
		ContentEnrollFragment fragment = new ContentEnrollFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_actlist_enroll,container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		Util.addActivity(getActivity());
		if(mPage == Util.NUM_ONE){
			mFlage = true;
		}
		//设置标题
		mTvdesc.setText(R.string.tv_actlistpeople_title);
		mIvBackup.setVisibility(View.VISIBLE);
		Intent intent = getActivity().getIntent();
		actCode = intent.getStringExtra(CONTENTCODE);
		//报名
		listActParticipant();
		mHandler = new Handler();
		//活动内容的列表
		mLvAclistEnrolllist = (XListView) view.findViewById(R.id.lv_aclist_enroll_list);
		//刷新
		mLvAclistEnrolllist.setPullLoadEnable(true); // 上拉刷新
		mLvAclistEnrolllist.setXListViewListener(this);//实现xListviewListener接口
	}

	/**
	 * 报名
	 */
	private void listActParticipant(){

		new ListActParticipantTask(getActivity(), new ListActParticipantTask.Callback() {

			@Override
			public void getResult(JSONObject mResult) {
				mFlage = true;
				if(mResult == null){
					if(mPage > 1){
						mLyNoDate.setVisibility(View.GONE);
						mLvAclistEnrolllist.setVisibility(View.VISIBLE);
					}else{
						mLyNoDate.setVisibility(View.VISIBLE);
						mLvAclistEnrolllist.setVisibility(View.GONE);
					}
					mLvAclistEnrolllist.setPullLoadEnable(false);
				}else{
					mLyNoDate.setVisibility(View.GONE);
					mLvAclistEnrolllist.setVisibility(View.VISIBLE);
					mLvAclistEnrolllist.setPullLoadEnable(true);
					PageData page = new PageData(mResult,"participantList",jsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvAclistEnrolllist.setPullLoadEnable(false);
					}else{
						mLvAclistEnrolllist.setPullLoadEnable(true);
					}

					if(queryAdapter == null){
						queryAdapter = new ListActParticipantAdapter(getActivity(), (List<Enroll>)page.getList());
						mLvAclistEnrolllist.setAdapter(queryAdapter);
					}else{
						if(page.getPage() == 1){
							queryAdapter.setItems((List<Enroll>)page.getList());
						}else{
							queryAdapter.addItems((List<Enroll>)page.getList());
						}
						mLvAclistEnrolllist.setAdapter(queryAdapter);
					}
				}
			}
		}).execute(actCode,String.valueOf(mPage));
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void backClick(View view){
		getActivity().finish();
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onLoadMore() {
		if(mFlage){
			mFlage = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					listActParticipant();
					mLvAclistEnrolllist.stopLoadMore();
				}
			}, 2000);
		}
	}
}
