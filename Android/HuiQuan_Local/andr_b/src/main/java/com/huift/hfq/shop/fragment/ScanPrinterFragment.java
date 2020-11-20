package com.huift.hfq.shop.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.service.PrintBillService;
import com.huift.hfq.shop.utils.Convert;
import com.lidroid.xutils.ViewUtils;

/**
 * 打印
 * @author wensi.yu
 *
 */
public class ScanPrinterFragment extends Fragment {

	private final static String TAG = "ScanPrinterFragment";
	
	private final static String PRNT_ACTION = "android.prnt.message";
	/** 打印*/
	private Button mPrinteBtn;
	/** 图片*/
    private Button mPrinte1;
    /** 条码*/
    private Button mPrinte2;
    /** 输入打印信息*/
    private EditText mPrintInfo;
    private SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss:SSS");
    private PrinterManager  printer = new PrinterManager();
    private final int DEF_TEMP_THROSHOLD = 50;
    private int mTempThresholdValue = DEF_TEMP_THROSHOLD;
    
    private static final String[] mTempThresholdTable ={
    		"80", "75", "70", "65", "60", 
    		"55", "50", "45", "40", "35", 
    		"30", "25", "20", "15", "10", 
    		"5", "0", "-5", "-10", "-15", 
    		"-20", "-25", "-30", "-35", "-40",
    };
    
    private Spinner mSpinerThreshold;
    private static final String[] mBarTypeTable ={
        "3", "20", "25", 
        "29", "34", "55", "58", 
        "71", "84", "92",
    };
    
    private int mBarcodeTypeValue;
    private final static String SPINNER_PREFERENCES_FILE = "SprinterPrefs";
    private final static String SPINNER_SELECT_POSITION_KEY = "spinnerPositions";
    private final static int DEF_SPINNER_SELECT_POSITION = 6;
    private final static String SPINNER_SELECT_VAULE_KEY = "spinnerValue";
    private final static String DEF_SPINNER_SELECT_VAULE = mTempThresholdTable[DEF_SPINNER_SELECT_POSITION];
    private int mSpinnerSelectPosition;
    private String mSpinnerSelectValue;
    
    private BroadcastReceiver mPrtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int ret = intent.getIntExtra("ret", 0);
            if(ret == -1){
            	Util.getContentValidate(R.string.toast_scanprinter_paper);
            }
        }
    };
	
	public static ScanPrinterFragment newInstance() {
		Bundle args = new Bundle();
		ScanPrinterFragment fragment = new ScanPrinterFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scanprinter,container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
        mPrintInfo = (EditText) view.findViewById(R.id.printer_info);//输入打印信息
        mPrinteBtn = (Button) view.findViewById(R.id.print);//打印
        mPrinte1 = (Button) view.findViewById(R.id.button1);//图片
        mPrinte2 = (Button) view.findViewById(R.id.button2);//条码
        
		mPrinteBtn.setOnClickListener(scanClickListener);
		mPrinte1.setOnClickListener(scanClickListener);
		mPrinte2.setOnClickListener(scanClickListener);
		
        ArrayAdapter mBarcodeTypeAdapter = ArrayAdapter.createFromResource(getMyActivity(), R.array.barcode_type,android.R.layout.simple_spinner_item);
        mBarcodeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
	}
	
	private OnClickListener scanClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			StringBuffer sb = new StringBuffer();
			sb.append("              惠圈 ");
			
			sb.append("\n");
			
			sb.append("      好吃不如饺子");
			sb.append("\n");
			
			sb.append("***************************");
			sb.append("\n");
			
			sb.append("支付时间：");
			sb.append("12-31  13:00");
			sb.append("\n");
			
			sb.append("用户：");
			sb.append("888");
			sb.append("\n");
			
			sb.append("订单号：");
			sb.append("88888");
			sb.append("\n");
			sb.append("\n");
			
			sb.append("消费金额：");
			sb.append("100");
			sb.append("\n");
			
			sb.append("优惠金额：");
			sb.append("20");
			sb.append("\n");
			
			sb.append("------------------------------");
			sb.append("\n");
			
			sb.append("实际支付：");
			sb.append("80");
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			
			String messgae = sb.toString();
			
			switch (v.getId()) {
				case R.id.button1://图片
					long curS =System.currentTimeMillis();
	            	Date   curDate   =   new   Date(curS);
	            	String   str   =   formatter.format(curDate);
	            	mPrintInfo.append("start time :  "+str+"\n");
	                doPrint(2);
	                long curE = System.currentTimeMillis();
	            	Date   edcurDate   =   new   Date(curE);
	            	String   sstr   =   formatter.format(edcurDate);
	            	long t_tame =curE-curS;
	            	mPrintInfo.append("stop time :: "+sstr+"\n");
	            	mPrintInfo.append("take time :: "+t_tame+"\n\n");
					break;
					
				case R.id.button2://条码
	                if(messgae.length() > 0) {
	                    doPrint(1);
	                } else {
	                	Util.getContentValidate(R.string.scanPrint_input);
	                }
					break;
					
				case R.id.print://打印
	                if(messgae.length() > 0) {                
	                    doprintwork(messgae);
	                } else {
	                    Util.getContentValidate(R.string.scanPrint_input);
	                }
					break;
					
				default:
					break;
			}
		}
	};
	
	@Override
	public void onDestroy() {
        super.onDestroy();

    }

    @Override
	public void onPause() {
        super.onPause();
        getMyActivity().unregisterReceiver(mPrtReceiver);
        writeSpinnerPrefsState(getMyActivity());
    }
    
    private boolean hasChineseChar(String text) {
        boolean hasChar = false;
        int length = text.length();
        int byteSize = text.getBytes().length;
        hasChar = (length != byteSize);
        return hasChar;
    }

   private void doprintwork(String msg) {
         Intent intentService = new Intent(getMyActivity(), PrintBillService.class);
         intentService.putExtra("SPRT", msg);
         getMyActivity().startService(intentService);
     }

    @Override
	public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PRNT_ACTION);
        getMyActivity().registerReceiver(mPrtReceiver, filter);
        readSpinnerPrefsState(getMyActivity());
        
    }

    private void doPrint(int type) {
        printer.setupPage(384, -1);
        switch (type) {
        	case 1:
	           /*String text = printInfo.getText().toString();
	           if(hasChineseChar(text)){
	               printer.drawBarcode(text, 50, 10, 58, 8, 120, 0);
	               
	           } else {
	               printer.drawBarcode(text, 196, 300, 20, 2, 70, 0);
	           
	               printer.drawBarcode(text, 196, 300, 20, 2, 70, 1);
	           
	               printer.drawBarcode(text, 196, 300, 20, 2, 70, 2);
	           
	               printer.drawBarcode(text, 196, 300, 20, 2, 70, 3);
	           } */
        	    String text = mPrintInfo.getText().toString();
                Log.d(TAG, "text ==" + text);  
                  switch(mBarcodeTypeValue){
                     case 20:// CODE128, alphabet + no.
                     case 25:// CODE93, alphabet + no.
                         printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 0);
                         printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 1);
                         printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 2);
                         printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 3);
                         break;
                     case 34:// UPCA, no., UPCA needs short length of No.
                     //case 2:// Chinese25MATRIX, no.
                         if(Convert.isNumeric(text,getMyActivity().getApplicationContext())){
//                           printer.prn_drawBarcode(text, 50, 10, mBarcodeTypeValue, 2, 70, 0);
                             printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 0);
                             printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 1);
                             printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 2);
                             printer.prn_drawBarcode(text, 196, 300, mBarcodeTypeValue, 2, 70, 3);
                         }else{
                             Toast.makeText(getMyActivity().getApplicationContext(), "Not support for non-numeric!!!", Toast.LENGTH_SHORT).show();
                             mPrintInfo.requestFocus();
                             return;
                         }
                         break;
                     
                     case 3:// Chinese25INTER, no.
                     case 29:// RSS14, no.
                         if(Convert.isNumeric(text,getMyActivity().getApplicationContext())){
                             printer.prn_drawBarcode(text, 50, 10, mBarcodeTypeValue, 2, 40, 0);
                         }else{
                             Toast.makeText(getMyActivity().getApplicationContext(), "Not support for non-numeric!!!", Toast.LENGTH_SHORT).show();
                             mPrintInfo.requestFocus();
                             return;
                         }
                         break;
                     case 55:// PDF417, setHue: 3
                         printer.prn_drawBarcode(text, 25, 5, mBarcodeTypeValue, 3, 60, 0);
                         break;
                     case 58:// QRCODE
                     case 71:// DATAMATRIX
                         printer.prn_drawBarcode(text, 50, 10, mBarcodeTypeValue, 8, 120, 0);
                         break;
                     case 84:// uPDF417
                         printer.prn_drawBarcode(text, 25, 5, mBarcodeTypeValue, 4, 60, 0);
                         break;
                     case 92:// AZTEC
                         printer.prn_drawBarcode(text, 50, 10, mBarcodeTypeValue, 8, 120, 0);
                         break;
                  }
	           break; 

        	case 2://图片
        		BitmapFactory.Options opts = new BitmapFactory.Options();    
        		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        		opts.inDensity = getResources().getDisplayMetrics().densityDpi;
        		opts.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
        		Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ticket, opts);
        		printer.drawBitmap(img, 30, 0);
        		break;
        	case 3:
        		printer.drawLine(264, 50, 48, 50, 4); 
        		printer.drawLine(156, 0, 156, 120, 2);
        		printer.drawLine(16, 0, 300, 100, 2);
        		printer.drawLine(16, 100, 300, 0, 2);
        		break;
	       }
        
		int ret= printer.printPage(0);
		Intent intent = new Intent("urovo.prnt.message");
		intent.putExtra("ret", ret);
		getMyActivity().sendBroadcast(intent);
    }
    
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
		public void onItemSelected(AdapterView<?> arg0,View arg1, int arg2,long arg3) {
            mTempThresholdValue = Integer.parseInt(mTempThresholdTable[arg2]);
            // prepare prefs and write it to files
            mSpinnerSelectPosition = (int) arg3;
        }

        @Override
		public void onNothingSelected(AdapterView<?> arg0) {
        	
        }
    } 
    
    // read prefs to restore
    private boolean readSpinnerPrefsState(Context c){
    	SharedPreferences sharedPrefs = c.getSharedPreferences(SPINNER_PREFERENCES_FILE, getMyActivity().MODE_PRIVATE);
    	mSpinnerSelectPosition = sharedPrefs.getInt(SPINNER_SELECT_POSITION_KEY, DEF_SPINNER_SELECT_POSITION);
    	mSpinnerSelectValue = sharedPrefs.getString(SPINNER_SELECT_VAULE_KEY, DEF_SPINNER_SELECT_VAULE);
    	return (sharedPrefs.contains(SPINNER_SELECT_POSITION_KEY));
    }
    
    // write prefs to file for restroing
    private boolean writeSpinnerPrefsState(Context c){
    	SharedPreferences sharedPrefs = c.getSharedPreferences(SPINNER_PREFERENCES_FILE, getMyActivity().MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putInt(SPINNER_SELECT_POSITION_KEY, mSpinnerSelectPosition);
    	editor.putString(SPINNER_SELECT_VAULE_KEY, mSpinnerSelectValue);
    	return (editor.commit());
    }
}
