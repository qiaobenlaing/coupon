package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * @author wensi.yu
 */
public class ScanMainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private TextView mScanning_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_main);
		mScanning_result = (TextView)findViewById(R.id.scanning_result);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				mScanning_result.setText(bundle.getString("result"));
				Uri uri = Uri.parse(bundle.getString("result")); 
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                startActivity(intent);  
			}
			break;
		}
    }	
}
