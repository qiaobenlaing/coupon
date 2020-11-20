// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.model.UpdateShopTimeTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 修改商家的简介信息
 * @author qian.zhou
 */
public class UpdateShopDecFragment extends Fragment {
	private static final String TAG = "UpdateShopDecFragment";
	public static final String SHOPOBJ  = "shop";
	/** 店铺的简介信息*/
	private EditText mEditShopDec;
	private Shop mShop;
	/** 完成*/
	private TextView mTvMsg;
	
	/**
	 * 需要传递参数时有利于解耦 
	 * @return PosPayFragment
	 */
	public static UpdateShopDecFragment newInstance() {
		Bundle args = new Bundle();
		UpdateShopDecFragment fragment = new UpdateShopDecFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_update_shopdec, container, false);
		ViewUtils.inject(this, v);
        Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		//取值
		Intent intent = getActivity().getIntent();
		mShop = (Shop) intent.getSerializableExtra(SHOPOBJ);
		Log.d(TAG, "mShop==="+mShop);
		//头部标题
		LinearLayout ivTurn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.shop_introduction));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getString(R.string.toast_setover));
		//初始化数据
		mEditShopDec = (EditText) v.findViewById(R.id.et_shop_dec);
		//赋值
		if(mShop != null){
			String shortdec = !Util.isEmpty(mShop.getShortDes()) ? mShop.getShortDes() : "";
			mEditShopDec.setText(shortdec);
			mEditShopDec.setSelection(shortdec.length());
		}
	}
	
	/**
	 * 修改店铺简介
	 */
	public void updateShopTime(){
		String shopDec = mEditShopDec.getText().toString();
		String updateKey = "shortDes";
		String updateValue = shopDec;
		mTvMsg.setEnabled(false);
		new UpdateShopTimeTask(getActivity(), new UpdateShopTimeTask.Callback() {
			@Override
			public void getResult(int retCode) {
				mTvMsg.setEnabled(true);
				if (ErrorCode.SUCC == retCode) {
					DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO,true);
					Intent intent = new Intent();
					intent.putExtra(MyShopInfoFragment.SHOP_OBJ, mShop);
					getActivity().setResult(MyShopInfoFragment.DE_UPDATE_OPTIME, intent);
				    getActivity().finish(); 
				} else {
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(updateKey, updateValue);
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view      
	 */
	@OnClick({R.id.layout_turn_in, R.id.tv_msg})
	public void btnBackClick(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.tv_msg:
			updateShopTime();
			break;
		default:
			break;
		}
	}
	
	public void onResume(){
    	super.onResume();
    }
}
