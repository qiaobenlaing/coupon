package com.huift.hfq.cust.fragment;

import java.util.ArrayList;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.adapter.NewShopServiceAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.RemindToShopTask;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.NewShopProduct;
import com.huift.hfq.cust.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 店铺首页的商品/服务的Fragment
 * @author yingchen
 *
 */
public class NewShopServiceFragment extends Fragment {
	/**商品/服务列表*/
	private ListView mPromotionListView;
	
	/**默认显示*/
	private LinearLayout mNodataLinearLayout;
	
	/**提示更新*/
	private TextView mPromptTextView;
	
	/**商家编码*/
	private String mShopCode;
	
	public static NewShopServiceFragment newInstance(Bundle args) {
		NewShopServiceFragment fragment = new NewShopServiceFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =View.inflate(getActivity(), R.layout.fragment_shop_service, null);
		initView(view);
		Bundle arguments = getArguments();
		ArrayList<NewShopProduct> products = (ArrayList<NewShopProduct>) arguments.getSerializable("Service");
		mShopCode = arguments.getString("shopCode");
		
		if(null == products || products.size()==0){
			mPromotionListView.setVisibility(View.GONE);
			mNodataLinearLayout.setVisibility(View.VISIBLE);
			
		}else{
			mPromotionListView.setVisibility(View.VISIBLE);
			mNodataLinearLayout.setVisibility(View.GONE);
			showView(products);
		}
		
		return view;
	}

	/**
	 * 显示列表
	 * @param products
	 */
	private void showView(final ArrayList<NewShopProduct> products) {
		mPromotionListView.setAdapter(new NewShopServiceAdapter(products, getActivity()));
		Util.setListViewHeight(mPromotionListView);
		
		mPromotionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getActivity(), ActThemeDetailActivity.class);
				intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.SHOP_PRODUCT_SERVICE);
				intent.putExtra("productId", products.get(position).getProductId());
				
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view) {
		mPromotionListView = (ListView) view.findViewById(R.id.lv_shop_service);
		mNodataLinearLayout = (LinearLayout) view.findViewById(R.id.ll_no_data);
		mPromptTextView = (TextView) view.findViewById(R.id.tv_prompt);
		//提示更新的点击
		mPromptTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				propmtShopToUpdate();
			}
		});
	}
	
	/**
	 * 提示商家更新 商品/服务
	 */
	private void propmtShopToUpdate() {
		//判断是否登录
		if (!DB.getBoolean(DB.Key.CUST_LOGIN)) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}else{
			new RemindToShopTask(getActivity(), new RemindToShopTask.CallBack() {
				
				@Override
				public void getResult(boolean result) {
					if(result){
						Util.showToastZH("已经提示商家");
					}
				}
			}).execute(mShopCode,String.valueOf(Util.NUM_THIRD));
		}
	}
}
