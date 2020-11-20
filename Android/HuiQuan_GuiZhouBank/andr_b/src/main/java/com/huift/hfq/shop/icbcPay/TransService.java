package com.huift.hfq.shop.icbcPay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.icbc.smartpos.transservice.aidl.ITransService;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by 潘海听 on 2017/12/6.
 * 工行支付服务
 */

public class TransService {

    private static TransService _transService = null;
    private Context _context;
    /***
     * 支付服务AIDL
     */
    private ITransService _itransService;
    /***
     * 是否绑定支付服务
     */
    public static boolean isTransServiceBind = false;

    public TransService(Context context) {
        super();
        if (context != null) {
            this._context = context;
        }
    }

    public static TransService getInstance(Context context) {
        if (_transService == null) {
            _transService = new TransService(context);
        }
        return _transService;
    }

    ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(_context,"支付服务中断", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _itransService = ITransService.Stub.asInterface(service);
            isTransServiceBind = true;
            Toast.makeText(_context,"支付服务绑定成功", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 绑定收单应用提供的服务
     */
    public void bindTransService() {
        if (!isTransServiceBind) {
            LogUtils.i( "bindTransService");
            Intent intent = new Intent();
            intent.setAction("com.icbc.smartpos.transservice.TransService");
            intent.setPackage("com.icbc.smartpos.bankpay");
            _context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindTransService(){
        if (isTransServiceBind){
            _context.unbindService(connection);
            isTransServiceBind = false;
            Toast.makeText(_context, "支付服务解绑成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(_context, "支付服务未绑定无需解绑", Toast.LENGTH_SHORT).show();
        }
    }

    public ITransService getICBCTransService() {
        return _itransService;
    }
}
