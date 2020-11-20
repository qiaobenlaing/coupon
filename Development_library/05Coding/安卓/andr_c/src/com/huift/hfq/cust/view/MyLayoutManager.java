package com.huift.hfq.cust.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import android.view.View.MeasureSpec;

public class MyLayoutManager extends LinearLayoutManager{

	public MyLayoutManager(Context context) {
		super(context);
	}
	
	@Override
	public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
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
