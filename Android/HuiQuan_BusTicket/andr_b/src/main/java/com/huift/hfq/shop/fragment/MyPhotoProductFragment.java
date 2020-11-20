package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.AddPhotoActivity;
import com.huift.hfq.shop.adapter.MyPhotoProductsAdapter;
import com.huift.hfq.shop.model.MyPhotoListTask;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家相册 (产品)
 * @author qian.zhou
 */
public class MyPhotoProductFragment extends Fragment{
	/** startActivityForResult()的requestCode: 添加产品子相册信息 */
	public static final int INTENT_REQ_ADD_INFO = Util.NUM_ONE;
	/** 用listview显示所有产品的具体信息 **/
	private GridView mGvProduct;
	/**判断Fragment的状态**/
	static MyPhotoProductFragment mInstance = null;
	/**添加子相册*/
	private TextView mTvAddPhoto;
	/** 相册列表适配器*/
	private MyPhotoProductsAdapter mPhotoProductAdapter;
	private List<ShopDecoration> mShopDecorationList;
	/**新建相册*/
	private TextView mTvAddAlbumPhoto;
	private boolean mUppFlag = false;
	/** 没有相册*/
	private TextView mTvNoPhoto;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private RelativeLayout mLyContent;

	/**
	 * 需要传递参数时有利于解耦
	 * @return MyStaffFragment
	 */
	public static MyPhotoProductFragment newInstance() {
		if (mInstance == null) {
			Bundle args = new Bundle();
			mInstance = new MyPhotoProductFragment();
			mInstance.setArguments(args);
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_product, container, false);
		init(view);
		Util.addLoginActivity(getActivity());
		return view;
	}

	// 初始化方法
	private void init(View v) {
		//初始化视图
		mTvAddPhoto = (TextView) getActivity().findViewById(R.id.tv_msg);
		mTvAddPhoto.setVisibility(View.VISIBLE);
		mGvProduct = (GridView) v.findViewById(R.id.gv_photo);
		mTvNoPhoto = (TextView) v.findViewById(R.id.tv_nophoto_title);//没有相册
		mTvAddPhoto.setBackgroundResource(R.drawable.album_add);
		mTvAddPhoto.setOnClickListener(addListener);
		mTvAddAlbumPhoto = (TextView) v.findViewById(R.id.tv_add_photo);
		mTvAddAlbumPhoto.setOnClickListener(addListener);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLyContent = (RelativeLayout) v.findViewById(R.id.ry_content);
		// 正在加载数据
		setData(0);
		//调用产品子相册的列表
		getPhotoProductList();
	}

	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}

	/**
	 * 添加子相册
	 */
	OnClickListener  addListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), AddPhotoActivity.class);
			intent.putExtra(AddPhotoActivity.INDEX, "add");
			startActivity(intent);
		}
	};

	/**
	 * 查询产品子相册列表
	 */
	public void getPhotoProductList(){
		new MyPhotoListTask(getActivity(), new MyPhotoListTask.Callback() {
			@Override
			public void getResult(JSONArray jsonArray) {
				if (jsonArray == null ) {
					return;
				} else {
					setData(1); // 有数据
					if (jsonArray.size() == 0) {
						mTvNoPhoto.setVisibility(View.VISIBLE);
						mTvAddAlbumPhoto.setVisibility(View.VISIBLE);
						mGvProduct.setVisibility(View.GONE);
					} else {
						mTvNoPhoto.setVisibility(View.GONE);
						mTvAddAlbumPhoto.setVisibility(View.GONE);
						mGvProduct.setVisibility(View.VISIBLE);
						mShopDecorationList = new ArrayList<ShopDecoration>();
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							ShopDecoration mShopDecoration = Util.json2Obj(jsonObject.toString(), ShopDecoration.class);
							mShopDecorationList.add(mShopDecoration);
						}
						mPhotoProductAdapter = new MyPhotoProductsAdapter(getActivity(),mShopDecorationList);
						mGvProduct.setAdapter(mPhotoProductAdapter);
					}
				}
			}
		}).execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUppFlag = DB.getBoolean(ShopConst.Key.UPP_DECORATION);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.UPP_DECORATION, false);
			getPhotoProductList();
		}
	}

}
