package com.huift.hfq.base.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import com.huift.hfq.base.R;

/**
 * 自定义进度条
 * @author yanfang.li
 */
public class ProgressDialogUtil extends Dialog{

	public ProgressDialogUtil(Context context, String strMessage) {  
        this(context, R.style.CustomProgressDialog, strMessage);  
    }  
  
    public ProgressDialogUtil(Context context, int theme, String strMessage) {  
        super(context, theme);  
        this.setContentView(R.layout.progress_dialog);  
        this.getWindow().getAttributes().gravity = Gravity.CENTER;  
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);  
        if (tvMsg != null) {  
            tvMsg.setText(strMessage);  
        }  
    }  
  
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
  
        if (!hasFocus) {  
            dismiss();  
        }  
    } 

}
