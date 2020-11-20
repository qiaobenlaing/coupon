package com.huift.hfq.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class SynchHorizonScrollView extends HorizontalScrollView {
	private View mView;  
    
    public SynchHorizonScrollView(Context context) {  
        super(context);  
        // TODO Auto-generated constructor stub  
    }  
  
    public SynchHorizonScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
  
    @Override  
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {  
        super.onScrollChanged(l, t, oldl, oldt);  
        if(mView!=null){  
            mView.scrollTo(l, t);  
        }  
    }  
      
    public void setAnotherView(View v){  
        this.mView=v;  
    }  
}
