package cn.suanzi.baomi.cust.util;

import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.MeasureSpec;

public class MyLayOutManager extends StaggeredGridLayoutManager {

	public MyLayOutManager(int spanCount, int orientation) {
		super(spanCount, orientation);
	}

	
	@Override
	public void onMeasure(Recycler recycler, State state, int widthSpec,
			int heightSpec) {
		super.onMeasure(recycler, state, widthSpec, heightSpec);
		View view = recycler.getViewForPosition(0);  
        if(view != null){  
            measureChild(view, widthSpec, heightSpec);  
            int measuredWidth = MeasureSpec.getSize(widthSpec);  
            int measuredHeight = view.getMeasuredHeight();  
            setMeasuredDimension(measuredWidth, measuredHeight);  
        }  

	}
}
