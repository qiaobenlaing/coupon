 package com.huift.hfq.cust.fragment;

import java.io.Serializable;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XGridView;
import com.huift.hfq.base.view.XGridView.IXGridViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.MyPhotoAlbumActivity;
import com.huift.hfq.cust.activity.ShopBigPhotoActivity;
import com.huift.hfq.cust.adapter.PhotoEnviromentAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.cGetShopDecorationTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 商家相册 (环境)
 * @author yanfang.li
 */
public class MyPhotoEnviromentFragment extends Fragment implements IXGridViewListener{
	private static final String TAG = MyPhotoProductFragment.class.getSimpleName();
	/** 布局*/
	private XGridView mGvProduct;
	private int mPage = 1;
	private PhotoEnviromentAdapter mPhotoAdapter;
	private Handler mHandler;
	private LinearLayout mLyView;
	private ImageView mIvView ;
	private ProgressBar mProgView;
	private boolean mDataFlag = false; 
	
	public static MyPhotoEnviromentFragment newInstance() {
		Bundle args = new Bundle();
		MyPhotoEnviromentFragment fragment = new MyPhotoEnviromentFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_enviroment, container, false);
		ViewUtils.inject(this, view);
		init(view);
		Util.addLoginActivity(getMyActivity());
		return view;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		mDataFlag = true;
		mGvProduct = (XGridView) view.findViewById(R.id.gv_envir_photo);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mGvProduct.setPullLoadEnable(true);
		mGvProduct.setXGridViewListener(this);
		mHandler = new Handler();
		cGetShopDecoration();
	}
	
	/**
	 * 获取商家相册
	 * @param view
	 */
	private void cGetShopDecoration() {
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		String shopCode = getMyActivity().getIntent().getStringExtra(MyPhotoAlbumActivity.SHOP_CODE);
		new cGetShopDecorationTask(getMyActivity(), new cGetShopDecorationTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mGvProduct, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mGvProduct.setPullLoadEnable(true);
					PageData page = new PageData(result, "shopDecoList", new TypeToken<List<Decoration>>(){}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mGvProduct.setPullLoadEnable(false);
					} else {
						mGvProduct.setPullLoadEnable(true);
					}
					final List<Decoration> list = (List<Decoration>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mGvProduct, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						if (mPhotoAdapter == null) {
							mPhotoAdapter = new PhotoEnviromentAdapter(getMyActivity(), list);
							mGvProduct.setAdapter(mPhotoAdapter);
						} else {
							if (page.getPage() == 1) {
								mPhotoAdapter.setItems(list);
							} else {
								mPhotoAdapter.addItems(list);
							}
						}
					}
					
					// 查看大图
					mGvProduct.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
							Decoration decoration = (Decoration) mGvProduct.getItemAtPosition(position);
							Intent intent = new Intent(getMyActivity(), ShopBigPhotoActivity.class);
							intent.putExtra(ShopBigPhotoActivity.DECORATION, decoration);
							intent.putExtra(ShopBigPhotoActivity.DECORATION_LIST, (Serializable)list);
							intent.putExtra(ShopBigPhotoActivity.POSITION, position);
							intent.putExtra(ShopBigPhotoActivity.TYPE, "0"); // 0 代表是环境的图片
							startActivity(intent);
						}
					});
				}
			}
		}).execute(shopCode,mPage+"");
	}
	
	@Override
	public void onLoadMore() {
		if (mDataFlag) {
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					cGetShopDecoration();
					mGvProduct.stopLoadMore();
				}
			}, 2000);
		
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyPhotoEnviromentFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyPhotoEnviromentFragment.class.getSimpleName()); //统计页面
	}
}
