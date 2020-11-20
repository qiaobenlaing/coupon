package com.huift.hfq.shop.model;

import android.app.Activity;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.API;
import com.huift.hfq.base.model.SzAsyncTask;
import com.huift.hfq.base.pojo.BussinessDistrictListBean;
import com.huift.hfq.base.utils.AppUtils;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018/9/19/019.
 */

public class GetBussinessDistrictListTask extends SzAsyncTask<String, Integer, Integer> {
    /** 回调方法 **/
    private Callback mCallback;
    /** 调用api的结果 */
    private JSONObject mResult;
    private Activity mActivity;

    /**
     * 构造函数
     *
     * @param acti 启动本对象的Activity。
     */
    public GetBussinessDistrictListTask(Activity acti) {
        super(acti);
        this.mActivity=acti;
    }

    public GetBussinessDistrictListTask(Activity acti, Callback mCallback) {
        super(acti);
        this.mActivity=acti;
        this.mCallback = mCallback;
    }

    /**
     * 回调方法的接口
     *
     */
    public interface Callback {
        void getResult(BussinessDistrictListBean bean);
    }

    @Override
    protected void onPreExecute() {
        if (null != mActivity) {
            if (null != mProcessDialog) {
                mProcessDialog.dismiss();
            }
        }
    }

    @Override
    protected void handldBuziRet(int retCode) {

    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            mResult = (JSONObject) request("getZone");
            if (mResult!=null){
                BussinessDistrictListBean bean=Util.json2Obj(mResult.toJSONString(), BussinessDistrictListBean.class);
                if (bean!=null&& bean.getCode()==ErrorCode.SUCC){
                    mCallback.getResult(bean);
                    return bean.getCode();
                }else {
                    mCallback.getResult(null);
                    return 0;
                }
            }else {
                mCallback.getResult(null);
                return 0;
            }
        } catch (SzException e) {
            mCallback.getResult(null);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通用服务API调用方法
     *
     * @param method    请求的方法名
     * @return 返回一切正常时的结果，结果需要根据具体的方法进行强制转换。
     * @throws SzException 当请求失败或某种原因出错时，抛出该异常。可能的Exception:
     *                     <ul>
     *                     <li>ErrorCode.CLI_NET_NOT_CONN：客户端未联网</li>
     *                     <li>ErrorCode.NETWORK_PROBLEM：请求API时网络异常</li>
     *                     <li>ErrorCode.JSONRPC_INVALID_JSON, </li>
     *                     <li>ErrorCode.JSONRPC_INVALID_PARAMS,</li>
     *                     <li>ErrorCode.JSONRPC_INVALID_REQUEST,</li>
     *                     <li>ErrorCode.JSONRPC_METHOD_NOT_FOUND</li>
     *                     <li>ErrorCode.USER_NOT_EXISTED：用户不存在 </li>
     *                     <li>ErrorCode.USER_DISABLED：用户已经被禁了 </li>
     *                     <li>ErrorCode.USER_PASS_INVALID：自动登录时密码错误</li>
     *                     <li>ErrorCode.USER_NOT_AUTHORIZED：需要登录才能访问，用户重未登录过</li>
     *                     <li>ErrorCode.API_INTERNAL_ERR：API内部异常</li>
     *                     </ul>
     */
    protected static Object request(String method) throws SzException {

        // 检查本地是否联网 ErrorCode.CLI_NET_NOT_CONN
        if (!Util.isNetworkOpen(AppUtils.getActivity())) {
            throw new SzException(ErrorCode.CLI_NET_NOT_CONN);
        }

        URL serverURL;
        try {
            serverURL = new URL(Const.ApiAddr.COMM);
        } catch (MalformedURLException e) {
            throw new SzException(ErrorCode.APP_INTERNAL_ERR, e);
        }

        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
        mySession.getOptions().setAllowedResponseContentTypes(new String[]{"text/json;charset=utf-8", "application/json"});

        JSONRPC2Response response = null;
        // 请求API。以下特定情况下可多次尝试：
        // 1) 网络异常，服务器或者本地网络异常
        // 2) 会话过期。重新登录。
        for (int tri = 1; tri <= Const.API_MAX_TRY; ++tri) {
            try {
                // Create a new JSON-RPC 2.0 request
                int id = API.nextId();
                JSONRPC2Request request = new JSONRPC2Request(method,id);
                // 发送API请求
                response = mySession.send(request);

                // 处理API返回的结果
                if (response.indicatesSuccess()) {
                    return response.getResult();
                } else {
                    throw new SzException(response.getError().getCode());
                }
                // 网络异常，服务器或者本地网络异常
            } catch (JSONRPC2SessionException sessEx) {
                if (tri < Const.API_MAX_TRY) {
                } else {
                    throw new SzException(ErrorCode.NETWORK_PROBLEM, sessEx);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
