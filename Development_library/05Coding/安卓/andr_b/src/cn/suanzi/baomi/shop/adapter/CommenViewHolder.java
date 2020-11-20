// ---------------------------------------------------------
// @author    fang.wei
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 通过未指定泛型T 创建的可适用于所有视图的ViewHolder
 * @author Fang.Wei
 */
public class CommenViewHolder {
	/**一种比ArrayList效率更高的集合容器，用于存放View*/
	private SparseArray<View> mViews;
	/**安卓自带的机制所缓存的视图*/
	private View mConvertView;
	private int mPosition;
	
	public CommenViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		mViews = new SparseArray<View>();
		mPosition = position;
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	public static CommenViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if(convertView == null){//若缓存视图为空，即ListView刚开始加载并无Item划出屏幕
			return new CommenViewHolder(context, parent, layoutId, position);//新建一个ViewHolder进行缓存
		} else {//若非上一种情况，通过convertView的getTag()来获取ViewHolder
			CommenViewHolder holder = (CommenViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}
	
	/**
	 * 获取视图，并加入数据源
	 * @param viewId
	 * @return
	 */
	public  <T extends View>T getView(int viewId){
		View view = mViews.get(viewId);
		if(view==null){
		    view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	/**
	 * @return the convertView
	 */
	public View getConvertView() {
		return mConvertView;
	}

}
