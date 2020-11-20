package cn.suanzi.baomi.cust.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideViewAadapter extends PagerAdapter {
	private int[] pics;
	private Context context;

	public GuideViewAadapter(Context context, int[] pics) {
		this.pics = pics;
		this.context  =context;
		}

	@Override
	public int getCount() {
		return pics.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView iv = new ImageView(context);
		iv.setImageResource(pics[position]);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		container.addView(iv);
		return iv;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeViewInLayout((View)object);
	}
	

}
