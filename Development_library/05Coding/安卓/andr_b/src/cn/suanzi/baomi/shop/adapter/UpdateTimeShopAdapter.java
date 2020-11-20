package cn.suanzi.baomi.shop.adapter;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.fragment.UpdateShopTimeFragment;
import cn.suanzi.baomi.shop.model.UpdateShopTimesTask;

/**
 * 修改营业时间
 * @author qian.zhou
 */
public class UpdateTimeShopAdapter extends CommonListViewAdapter<Shop>{
	
	private final static String TAG = UpdateTimeShopAdapter.class.getSimpleName();
	/** 营业开始时间 */
	private TimePickerDialog mPickerStartTime;
	/** 营业结束时间 */
	private TimePickerDialog mPickerEndTime;
	/** 回调*/
	private CallBackData mBackData;
	
	public UpdateTimeShopAdapter(Activity activity, List<Shop> datas, CallBackData backData) {
		super(activity, datas);
		this.mBackData = backData;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_update_shoptime, position);
		final Shop shop = mDatas.get(position);
		final EditText editStartTime = holder.getView(R.id.edt_startuse_time);
		final EditText editEndTime = holder.getView(R.id.edt_enduse_time);
		TextView delete = holder.getView(R.id.bank_item_delete);
		//赋值
		editStartTime.setText(shop.getOpen());
		editEndTime.setText(shop.getClose());
		
		OnClickListener uppTimeListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.edt_startuse_time:
					mPickerStartTime = Util.getTimeDialog(mActivity, editStartTime, new Util().new OnResListener() {
						@Override
						public void onOk() {
							super.onOk();
							saveShop(shop, editStartTime, editEndTime);
						}
					});
					mPickerStartTime.show();
					DB.saveBoolean(UpdateShopTimeFragment.UPDATE_TIME,true);
					break;
				case R.id.edt_enduse_time:
					mPickerEndTime = Util.getTimeDialog(mActivity, editEndTime, new Util().new OnResListener() {
						@Override
						public void onOk() {
							super.onOk();
							saveShop(shop, editStartTime, editEndTime);
							/*boolean flag = TimeUtils.getShopTime(editStartTime, editEndTime);
							if (flag == true) {
								
								saveShop(shop, editStartTime, editEndTime);
							} else {
								Util.getContentValidate(R.string.business_time_out);
							}*/
						}
					});
					mPickerEndTime.show();
					DB.saveBoolean(UpdateShopTimeFragment.UPDATE_TIME,true);
					break;
				case R.id.bank_item_delete:
					//移除
					mDatas.remove(position);
					updateShopTime();
					UpdateTimeShopAdapter.this.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
		};
		//修改时间
		editStartTime.setOnClickListener(uppTimeListener);
		editEndTime.setOnClickListener(uppTimeListener);
		delete.setOnClickListener(uppTimeListener);
		return holder.getConvertView();
	}
	
	
	
	/**
	 * 修改店铺的营业时间
	 */
	public void updateShopTime(){
		String updateKey = "businessHours";
		StringBuffer stringBuffer = new StringBuffer();
		String businessHours = "";
		for (int i = 0; i < mDatas.size(); i++) {
			Shop shop = (Shop) mDatas.get(i);
			businessHours = shop.getOpen() + "," + shop.getClose();
			stringBuffer.append(businessHours + ";");
		}
		new UpdateShopTimesTask(mActivity, new UpdateShopTimesTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (ErrorCode.SUCC == retCode) {
					DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO,true);
				} else {
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(updateKey, stringBuffer.toString());
	}
	
	/**
	 * 保存对象
	 * @param shop
	 * @param startTime
	 * @param EndTime
	 */
	private void saveShop (Shop shop,EditText startTime,EditText EndTime) {
		shop.setOpen(startTime.getText().toString());
		shop.setClose(EndTime.getText().toString());
		mBackData.getItemData(mDatas);
	}
	
	/**
	 * 回调方法
	 */
	public interface CallBackData {
		public void getItemData (List<Shop>  shopList);
	}
} 
