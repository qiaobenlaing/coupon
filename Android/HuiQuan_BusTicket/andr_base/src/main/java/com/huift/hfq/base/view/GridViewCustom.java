package com.huift.hfq.base.view;

import android.content.Context;
import android.util.AttributeSet;

public class GridViewCustom extends GridViewWithHeaderAndFooter {

	public GridViewCustom(Context context, AttributeSet atts) {
		super(context, atts);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
