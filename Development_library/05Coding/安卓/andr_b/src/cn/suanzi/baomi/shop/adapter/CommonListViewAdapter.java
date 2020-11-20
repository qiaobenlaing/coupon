// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.14
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 继承BaseAdapter的抽象类，子类只需重写抽象方法就可以建立适配器
 * @author 
 * 
 * @param <T>//未指定的泛型T
 */
public abstract class CommonListViewAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected Activity mActivity;
	protected List<T> mDatas;
	/**视图填充类对象*/
	protected LayoutInflater mInflater;
	
	
	public CommonListViewAdapter(Context context, List<T> datas) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
	}
	
	public CommonListViewAdapter(Activity activity, List<T> datas) {
		super();
		this.mActivity = activity;
		mInflater = LayoutInflater.from(activity);
		this.mDatas = datas;
	}
	
	public void addItems(List<T> datas){
		if (datas == null) {
			return ;
		}
		if (mDatas == null) {
			mDatas = datas;
		}else{
			mDatas.addAll(datas);
		}
		notifyDataSetChanged();
	}
	
	public void setItems(List<T> datas){
		mDatas=datas;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mDatas == null){
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		if(mDatas == null){
			return null;
		}
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
