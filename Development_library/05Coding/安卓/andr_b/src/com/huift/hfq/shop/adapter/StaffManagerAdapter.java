package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.MyStaffItem;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.shop.activity.StoreSetActivity;
import com.huift.hfq.shop.activity.UppStaffActivity;
import com.huift.hfq.shop.fragment.MyUppManagerFragment;
import com.huift.hfq.shop.fragment.StaffManagerFragment;
import com.huift.hfq.shop.fragment.StoreSetFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 店员管理适配器
 * @author qian.zhou
 */
public class StaffManagerAdapter extends CommonListViewAdapter<MyStaffItem>{
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	public StaffManagerAdapter(Activity activity, List<MyStaffItem> list) {
		super(activity, list);
	}
	@Override
	public void setItems(List<MyStaffItem> myorderItem) {
		super.setItems(myorderItem);
	}
	@Override
	public void addItems(List<MyStaffItem> myorderItem) {
		super.addItems(myorderItem);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_staff_manager, position);
		final MyStaffItem myStaffItem = (MyStaffItem) getItem(position);
		
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		//初始化数据
		TextView tvManagerName = holder.getView(R.id.tv_manager_name);//店长姓名
		TextView tvManagerPhone = holder.getView(R.id.tv_manager_phone);//店长电话
		TextView tvEditManager = holder.getView(R.id.tv_edit_manager);//编辑店长
		TextView tvSetShop = holder.getView(R.id.tv_set_shop);//设置门店
		ListView lvManagerShop = holder.getView(R.id.lv_staff_manager);
		RelativeLayout rySetManager = holder.getView(R.id.ry_set_manager);//编辑店长和设置门店的一整行
		
		rySetManager.setVisibility(String.valueOf(Util.NUM_TWO).equals(mUserToken.getUserLvl()) ? View.GONE : View.VISIBLE);
		tvManagerName.setText(!Util.isEmpty(myStaffItem.getRealName()) ? myStaffItem.getRealName() + ":" : "店长名称：");
		tvManagerPhone.setText(myStaffItem.getMobileNbr());
		
		StaffManagerShopAdapter adapter = new StaffManagerShopAdapter(mActivity, myStaffItem.getShopList());
		Log.d("TAG", "shopName>>>>" +  myStaffItem.getShopList().size());
		lvManagerShop.setAdapter(adapter);
		Util.setListViewHeight(lvManagerShop);
		//编辑店长            
		tvEditManager.setTag(position);
		tvEditManager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, UppStaffActivity.class);
				intent.putExtra(MyUppManagerFragment.STAFF_ITEM, myStaffItem);
				mActivity.startActivityForResult(intent, StaffManagerFragment.UPP_MANAGER_INFO);
			}
		});
		
		//设置门店
		tvSetShop.setTag(position);
		tvSetShop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				String staffCode = mDatas.get(index).getStaffCode();
				Intent intent = new Intent(mActivity, StoreSetActivity.class);
				intent.putExtra(StoreSetFragment.STAFF_CODE, staffCode);
				mActivity.startActivityForResult(intent, StaffManagerFragment.SET_STORE_SHOP);
			}
		});
		return holder.getConvertView();
	}
}
