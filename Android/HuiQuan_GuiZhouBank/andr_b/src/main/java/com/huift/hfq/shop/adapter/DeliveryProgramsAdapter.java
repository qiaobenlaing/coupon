package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.DelShopDeliveryTask;

import java.util.List;

/**
 * 配送方案
 * @author qian.zhou
 */
public class DeliveryProgramsAdapter extends CommonListViewAdapter<Shop>{
	private static final String TAG = DeliveryProgramsAdapter.class.getSimpleName();
	
	public DeliveryProgramsAdapter(Activity activity, List<Shop> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_delivery_program, position);
		final Shop shop = mDatas.get(position);
		final EditText editDeliveyRange = (EditText) holder.getView(R.id.et_delivey_distance);
		final EditText editDeliveyMoney = (EditText) holder.getView(R.id.et_delivey_money);//起送价
		final EditText editDeliveyPrice = (EditText) holder.getView(R.id.et_delivey_price);//配送费
		TextView tvProgram = (TextView) holder.getView(R.id.tv_program);//方案
		LinearLayout lyDeldelivery = holder.getView(R.id.ly_update_delivery);
		lyDeldelivery.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogUtils.showDialog(mActivity,Util.getString(R.string.dialog_title),Util.getString(R.string.quit_delete),Util.getString(R.string.dialog_ok),Util.getString(R.string.dialog_cancel),new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						//移除
						mDatas.remove(position);
						delShopDelivery(shop.getDeliveryId());
						DeliveryProgramsAdapter.this.notifyDataSetChanged();
					}
				});
				return false;
			}
		});
		int value = position+1;
		//赋值
		editDeliveyRange.setText(shop.getDeliveryDistance());
		editDeliveyMoney.setText(shop.getRequireMoney());
		editDeliveyPrice.setText(shop.getDeliveryFee());
		tvProgram.setText("方案" + "(" + value + ")" + ":");
		//事件
		editDeliveyRange.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				saveShop(shop,editDeliveyRange, editDeliveyMoney, editDeliveyPrice);
			}
		});
		editDeliveyMoney.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				saveShop(shop,editDeliveyRange, editDeliveyMoney, editDeliveyPrice);
			}
		});
		editDeliveyPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				saveShop(shop,editDeliveyRange, editDeliveyMoney, editDeliveyPrice);				
			}
		});
		return holder.getConvertView();
	}
	
	/**
	 * 保存对象
	 * @param position
	 * @param shop
	 * @param editDeliveyRange
	 * @param editDeliveyMoney
	 * @param editDeliveyPrice
	 */
	private void saveShop (Shop shop,EditText editDeliveyRange,EditText editDeliveyMoney,EditText editDeliveyPrice) {
		shop.setDeliveryDistance(editDeliveyRange.getText().toString());
		shop.setRequireMoney(editDeliveyMoney.getText().toString());
		shop.setDeliveryFee(editDeliveyPrice.getText().toString());
	}
	
	/**
	 * 删除商家配送信息
	 * @param deliveryId
	 */
	public void delShopDelivery (String deliveryId) {
		new DelShopDeliveryTask(mActivity, new DelShopDeliveryTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (ErrorCode.SUCC == retCode) {
					DB.saveBoolean(ShopConst.Key.UPP_DEL_ADD_DELIVERY,true);
				} else {
					Util.getContentValidate(R.string.myafter_order_detail_error);
				}
			}
		}).execute(deliveryId);
	}
}
