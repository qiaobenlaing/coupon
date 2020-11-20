package com.huift.hfq.cust.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NewShopScrollAdapter extends PagerAdapter {
	private List<ImageView> datas;
	
	public NewShopScrollAdapter(List<ImageView> datas) {
		super();
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	
	@Override
	public int getItemPosition(Object object) {
		
		return super.getItemPosition(object);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		 ((ViewPager) container).removeView(datas.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = datas.get(position);
		container.addView(imageView);
		return imageView;
	}
	
	
	
	
}
