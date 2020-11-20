// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base.api;

import android.util.Log;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.SzException;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import net.minidev.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 服务端基类
 *
 * @author Weiping Liu
 * @version 1.0.0
 */
public class API {

    private final static String TAG = "API";
    /**
     * JSON RPC请求中最大的ID（不包括）
     */
    public static final int MAX_PRC_ID = 100000;

    /**
     * 请求目标类型
     */
    public interface Target {
        int COMM = 0;
        int SHOP = 1;
        int CUST = 2;
        int WEB_SHOP = 3;
    }

    /**
     * session（user info）状态
     */
    public static interface SessStatus {
        /**
         * 本地没有用户信息，即用户从未登录过
         */
        public static final int NO_USER_INFO = -1;
        /**
         * session已经过期
         */
        public static final int SESS_EXPIRED = 0;
        /**
         * session有效
         */
        public static final int SESS_VALID = 1;
    }

    /**
     * UserCode被用于API Sign（VCode）中的部分的长度
     */
    public static final int USER_CODE_LEN_IN_SIGN = 6;

    /**
     * APP无需带vcode的方法列表（数组），不需要令牌认证
     */
    private static String[] NO_VCODE_METHODS = {
            "getValidateCode", "login", "findPwd", "logoff", "applyEntry", "getActType", "getGuideInfo",
            "register", "getNewestShopAppVersion", "activate", "applyEntry",
            "getStudentStarInfo", "listShopClass", "getClassRemarkList", "getClassInfo", "getShopTeacherInfo",
            "ifShowRecommend", "listShopTeacher", "register", "searchShop", "getHomeInfo", "listSearchWords",
            "recordUserAddress", "getHomeTabList", "getActList", "getBatchCouponInfo", "cGetSubAlbumPhoto",
            "cGetSubAlbumPhoto", "cGetShopProductAlbum", "cGetShopDecoration", "getHomeModule", "getHomeShopList",
            "searchCard", "getSystemParam", "getAct", "getClientHomePage", "listActModule", "getNewestClientAppVersion",
            "getPlateBonus", "getShopInfo", "listZhejiangCity", "listOpenCity", "getScrollInfo", "getActivityList",
            "getActivityInfo", "listCoupon", "errorInfo"};

    /**
     * JSONRPC请求ID
     */
    protected static int nextId = 0;

    /**
     * 请求公共API
     *
     * @param method    请求的方法名
     * @param oriParams 请求的参数列表，Map形式。注意：如果某个参数为数组，必须为对象数组。例如整型数组必须为Integer[] param2 = {new Integer(2), new Integer(3)}。否则将出现类转换异常。
     * @return 返回一切正常时的结果，结果需要根据具体的方法进行强制转换。
     * @throws SzException 当请求失败或某种原因出错时，抛出该异常。
     */
    public static Object reqComm(String method, LinkedHashMap<String, Object> oriParams) throws SzException {
        return request(Target.COMM, method, oriParams);
    }

    /**
     * 请求商店端API
     *
     * @param method    method 请求的方法名
     * @param oriParams 请求的参数列表，Map形式。注意：如果某个参数为数组，必须为对象数组。例如整型数组必须为Integer[] param2 = {new Integer(2), new Integer(3)}。否则将出现类转换异常。
     * @return 返回一切正常时的结果，结果需要根据具体的方法进行强制转换。
     * @throws SzException 当请求失败或某种原因出错时，抛出该异常。
     */
    public static Object reqShop(String method, LinkedHashMap<String, Object> oriParams) throws SzException {
            return request(Target.SHOP, method, oriParams);
    }

    public static Object reqWebShop(String method, LinkedHashMap<String, Object> oriParams) throws SzException {
        return request(Target.WEB_SHOP, method, oriParams);
    }


    /**
     * 通用服务API调用方法
     *
     * @param method    method 请求的方法名
     * @param oriParams 请求的参数列表，Map形式。注意：如果某个参数为数组，必须为对象数组。例如整型数组必须为Integer[] param2 = {new Integer(2), new Integer(3)}。否则将出现类转换异常。
     * @return 返回一切正常时的结果，结果需要根据具体的方法进行强制转换。
     * @throws SzException 当请求失败或某种原因出错时，抛出该异常。
     */
    public static Object reqCust(String method, LinkedHashMap<String, Object> oriParams) throws SzException {
        return request(Target.CUST, method, oriParams);
    }

    /**
     * 通用服务API调用方法
     *
     * @param target    请求目标，见Target接口中定义的常量。
     * @param method    请求的方法名
     * @param oriParams 请求的参数列表，Map形式。注意：如果某个参数为数组，必须为对象数组。例如整型数组必须为Integer[] param2 = {new Integer(2), new Integer(3)}。否则将出现类转换异常。
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
    protected static Object request(int target, String method, LinkedHashMap<String, Object> oriParams) throws SzException {

        // 检查本地是否联网 ErrorCode.CLI_NET_NOT_CONN
        if (!Util.isNetworkOpen(AppUtils.getActivity())) {
            throw new SzException(ErrorCode.CLI_NET_NOT_CONN);
        }

        URL serverURL = null;
        try {
            switch (target) {
                case Target.CUST:
                    serverURL = new URL(Const.ApiAddr.CUST);
                    break;
                case Target.SHOP:
                    serverURL = new URL(Const.ApiAddr.SHOP);
                    break;
                case Target.WEB_SHOP:
                    serverURL = new URL(Const.ApiAddr.WEB_SHOP);
                    break;
                default:
                    serverURL = new URL(Const.ApiAddr.COMM);
                    Log.i(TAG, "serverURL" + serverURL);
                    break;

            }
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
                // 业务参数
//				List<Object> paramList = new ArrayList<Object>();
//				paramList.addAll(Arrays.asList(paramsArr));
                LinkedHashMap<String, Object> params = oriParams == null ?
                        new LinkedHashMap<String, Object>() :
                        new LinkedHashMap<String, Object>(oriParams);

                // 添加reqtime
                params.put("reqtime", System.currentTimeMillis() / 1000);
                // 如果需要附加sign，在此添加。
                if (!Util.inArray(method, NO_VCODE_METHODS)) {
                    //  判断是否存在用户信息以及session是否已经过期
                    switch (getTokenStatus()) {
                        case SessStatus.NO_USER_INFO://从未登陆
                            Log.d(TAG, "USER_NOT_AUTHORIZED >>> " + ErrorCode.USER_NOT_AUTHORIZED);
                            throw new SzException(ErrorCode.USER_NOT_AUTHORIZED);

                        case SessStatus.SESS_EXPIRED://session过期
                            if (!autoLogin()) {
                                throw new SzException(ErrorCode.USER_PASS_INVALID);
                            }
                        default://session有效
                            break;
                    }
                    //paramList.add(genSign(method, firstParam));

                    String mGenSign = "";//调用签名
                    if (oriParams.get("tokenCode") == null || "".equals(oriParams.get("tokenCode"))) {
                        //调用API的时候不需要tokenCode
                        mGenSign = genSign(method, getFirstParam(params));
                    } else {
                        //调用API的时候需要tokenCode
                        mGenSign = oriParams.get("tokenCode").toString() + genSign(method, getFirstParam(params));
                    }
                    params.put("vcode", mGenSign);
                    Log.i("===vcode===", method + ": " + mGenSign);
                }

                // Create a new JSON-RPC 2.0 request
                int id = nextId();
                JSONRPC2Request request = new JSONRPC2Request(method, params, id);
                // 发送API请求
                Log.d(TAG, "request[" + tri + "]=== " + request.toString());
                response = mySession.send(request);
                Log.d(TAG, "response=== " + response.toJSONString());

                // 处理API返回的结果
                if (response.indicatesSuccess()) {
                    return response.getResult();
                } else {
                    throw new SzException(response.getError().getCode());
                }
                // 网络异常，服务器或者本地网络异常
            } catch (JSONRPC2SessionException sessEx) {
                if (tri < Const.API_MAX_TRY) {
                    Log.w(TAG, "网络异常。", sessEx);
                } else {
                    throw new SzException(ErrorCode.NETWORK_PROBLEM, sessEx);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 生成rpc请求ID
     *
     * @return 请求ID
     */
    public synchronized static int nextId() {
        return (++nextId % MAX_PRC_ID);
    }

    /**
     * 获取API的第一参数。如果是第一个参数，返回数组的第一个元素。
     *
     * @param params API的原参数。
     * @return API的第一参数。如果是第一个参数，返回数组的第一个元素
     */
    public static String getFirstParam(LinkedHashMap<String, Object> params) {
        String firstParam = "";
        if (params.size() > 0) {
            for (Entry<String, Object> param : params.entrySet()) {
                if (param.getValue() != null) {
                    if (Util.isArray(param.getValue())) {
                        firstParam = String.valueOf(((Object[]) param.getValue())[0]);
                    } else {
                        firstParam = String.valueOf(param.getValue());
                    }
                }
                break;
            }
        }
        return firstParam;
    }

    /**
     * 检查本地token是否(在未来的600s以内都)还有效（即未过期）。
     *
     * @return -1: 用户从来未登陆过，本地没有用户信息；0： 即将过期； 1：未过期；
     */
    public static int getTokenStatus() {
        UserToken utoken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        return utoken == null ? SessStatus.NO_USER_INFO :
                (utoken.getExpiresAt() > (System.currentTimeMillis() / 1000 + 600)
                        ? SessStatus.SESS_VALID : SessStatus.SESS_EXPIRED);
    }

    /**
     * 生成API调用所需sign。
     *
     * @param method     调用方法吗
     * @param firstParam 参数列表的第一个参数，无参数时可给null或者“”。
     * @return vcode
     */
    public static String genSign(String method, String firstParam) {
        UserToken u = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        if (u == null) {
            Log.e(TAG, "UserToken is null");
            return null;
        }
        String oriStr = method + (firstParam == null ? "" : firstParam) + Util.md5(u.getStaffCode().substring(0, USER_CODE_LEN_IN_SIGN));
        return Util.md5(oriStr);
    }

    /**
     * @return ture:登录成功；false:登录失败
     * @throws SzException API.reqComm()出现的各种异常
     */
    public static boolean autoLogin() throws SzException {
        // 验证令牌
        UserToken utoken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);

        LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
        try {
            reqParams.put("mobileNbr", Long.valueOf(utoken.getMobileNbr()));
        } catch (Exception e) {
            Log.e(TAG, "手机号码输入有误");
        }
        reqParams.put("password", utoken.getPassword());
        reqParams.put("loginType", Const.APP_TYPE);
//		List<Object> reqParams = new ArrayList<Object>();
//		reqParams.add(Long.valueOf(utoken.getMobileNbr()));
//		reqParams.add(utoken.getPassword());
//		reqParams.add(SzApplication.getApplication().getCurrAppType());

        JSONObject result = null;
        result = (JSONObject) API.reqComm("login", reqParams);
        Log.d(TAG, "SessStatus.SESS_EXPIRED=" + result.toString());
        return (Long) result.get("code") == ErrorCode.SUCC;
    }

}
