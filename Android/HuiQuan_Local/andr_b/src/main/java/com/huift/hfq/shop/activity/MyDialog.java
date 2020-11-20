package com.huift.hfq.shop.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.huift.hfq.shop.R;

public class MyDialog extends Dialog {

	 Context context;     
	 public MyDialog(Context context)
	 {         
		 super(context);         
		 this.context = context;     
	 } 
	 
	 public MyDialog(Context context, int theme)
	 {         
		 super(context, theme);         
		 this.context = context;     
	 }  
	 
	 @Override    
	 protected void onCreate(Bundle savedInstanceState) 
	 {         
		 super.onCreate(savedInstanceState);         
		 this.setContentView(R.layout.dialog);     
	 }   

}
