package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.pojo.ProductList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 订单列表的产品列表
 * @author wensi.yu
 *
 */
public class ProductListAdapter extends CommonListViewAdapter<ProductList>{

	private final static String TAG = "ProductListAdapter";
	/** 是否显示点击图标*/
	private boolean mShowIcon = false;
	/** 选中*/
	private boolean mIsClick = true;
	
	public ProductListAdapter(Activity activity, List<ProductList> datas,boolean mShowIcon) {
		super(activity, datas);
		this.mShowIcon = mShowIcon;
	}
	
	public void setClick(boolean flag){
		this.mIsClick = flag;
	}
	
	public boolean getClick(){
		return this.mIsClick;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.fragment_myafterorderdetail_item, position);
		ProductList item = (ProductList) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_myafter_payname)).setText(item.getProductName());//产品名称
		((TextView) holder.getView(R.id.tv_myafter_paynumber)).setText("x" + item.getProductNbr());//产品数量
		TextView tvProductPrice = holder.getView(R.id.tv_myafter_paymoney);//产品价格
		tvProductPrice.setText(item.getProductPrice() + "元");
		RelativeLayout productLine = holder.getView(R.id.rl_afterorder_line); //点击一行
		CheckBox chAfterMenue = holder.getView(R.id.ck_after_menu);//单选按钮的图标
		
		if (item.getChecked()) {
			chAfterMenue.setButtonDrawable(R.drawable.radio_yes);
			chAfterMenue.setChecked(true);
		} else {
			chAfterMenue.setButtonDrawable(R.drawable.radio_no);
			chAfterMenue.setChecked(false);
		}
		
		if (mShowIcon) {//显示图标
			chAfterMenue.setVisibility(View.VISIBLE);
			if(mIsClick){
				productLine.setEnabled(true);
			}else{ 
				productLine.setEnabled(false);
			}
		} else {
			chAfterMenue.setVisibility(View.GONE);
		}
		
		return holder.getConvertView();
	}
	
}
