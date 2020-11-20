package com.huift.hfq.cust.util;

import java.io.Serializable;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.activity.NewShopInfoActivity;
import com.huift.hfq.cust.activity.ShopActivity;
import com.huift.hfq.cust.activity.ShopIcbcActivity;
import com.huift.hfq.cust.activity.ShopPayBillActivity;
import com.huift.hfq.cust.activity.TimeLimitActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.ShopDetailFragment;
import com.huift.hfq.cust.fragment.ShopFragment;
import com.huift.hfq.cust.fragment.ShopPayBillFragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.HomeTemplate;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.AppUtils;

/**
 * Activity之间的跳转
 * 
 * @author yanfang
 */
public class SkipActivityUtil {

	private final static String TAG = SkipActivityUtil.class.getSimpleName();
	/** 店铺详情的h5 */
	private final static String GET_SHOP_INFO = "getShopInfo";

	/**
	 * 跳转H5
	 * 
	 * @param activity
	 * @param shopCode
	 */
	public static void skipNewShopDetailActivity(Activity activity, String shopCode) {
		if (null != activity && !Util.isEmpty(shopCode)) {
			Intent intent = new Intent(activity, NewShopInfoActivity.class);
			intent.putExtra("shopCode", shopCode);
			activity.startActivity(intent);
		}
	}
	
	/**
	 * 跳转H5
	 * 
	 * @param activity
	 * @param shopCode
	 */
	public static void skipH5ShopDetailActivity(Activity activity, String shopCode, String type) {
		if (null != activity && !Util.isEmpty(shopCode)) {
			Intent intent = new Intent(activity, H5ShopDetailActivity.class);
			intent.putExtra(H5ShopDetailActivity.SHOP_CODE, shopCode);
			intent.putExtra(H5ShopDetailActivity.TYPE, type);
			activity.startActivity(intent);
		}
	}

	/**
	 * 跳转app原生态设计的h5
	 * 
	 * @param activity
	 * @param shopCode
	 */
	public static void skipShopDetailActivity(Activity activity, String shopCode) {
		if (null != activity && !Util.isEmpty(shopCode)) {
			Intent intent = new Intent(activity, H5ShopDetailActivity.class);
			intent.putExtra(ShopDetailFragment.SHOP_CODE, shopCode);
			activity.startActivity(intent);
		}
	}

	/**
	 * 跳转买单界面
	 * 
	 * @param activity
	 * @param shop
	 *            买单的对象
	 * @param orderCode
	 *            订单编码
	 * @param isDiscount
	 *            是否参与折扣
	 * @param haveMeal
	 *            是堂食还是外卖
	 */
	public static void skipPayBillActivity(Activity activity, Shop shop, String orderCode, String isDiscount,
			String haveMeal) {
		if (null != activity && null != shop) {
			Intent intent = new Intent(activity, ShopPayBillActivity.class);
			intent.putExtra(ShopPayBillFragment.PAY_OBJ, (Serializable) shop);
			intent.putExtra(ShopPayBillFragment.ORDER_CODE, orderCode);
			intent.putExtra(ShopPayBillFragment.NO_DISCOUNT, isDiscount);
			intent.putExtra(ShopPayBillFragment.HAVE_MEAL, haveMeal);
			activity.startActivity(intent);
		}
	}

	/**
	 * 首页跳转
	 * 
	 * @param activity
	 *            活动的activity
	 * @param homeTemplate
	 *            小模块对象
	 */
	public static void skipHomeActivity(Activity activity, HomeTemplate homeTemplate, String moduleValue) {
		if (null != activity && null != homeTemplate) {
			if (CustConst.Home.LINK_COUPON_LIST.equals(homeTemplate.getLinkType())) {
				activity.startActivity(new Intent(activity, TimeLimitActivity.class));
			} else if (CustConst.Home.LINK_ICBC.equals(homeTemplate.getLinkType())) {
				activity.startActivity(new Intent(activity, ShopIcbcActivity.class));
			}
			if (Util.isEmpty(homeTemplate.getContent())) {
				// TODO 跳转为空
			} else {
				Intent intent = null;
				if (CustConst.Home.LINK_H5.equals(homeTemplate.getLinkType())) {
					if (!Util.isEmpty(homeTemplate.getContent()) && homeTemplate.getContent().contains(GET_SHOP_INFO)) {
						skipNewShopDetailActivity(activity, homeTemplate.getShopCode());
					} else if (!Util.isEmpty(homeTemplate.getContent())) {
						intent = new Intent(activity, ActThemeDetailActivity.class);
						intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
						intent.putExtra(ActThemeDetailActivity.THEME_URL, homeTemplate.getContent());
						activity.startActivity(intent);
					} else {
						// TODO 不跳转
					}
				} else if (CustConst.Home.LINK_SHOP_LIST.equals(homeTemplate.getLinkType())) {
					intent = new Intent(activity, ShopActivity.class);
					intent.putExtra(ShopFragment.TYPE, homeTemplate.getContent());
					intent.putExtra(ShopFragment.MODULE_VALUE, moduleValue);
					Log.d(TAG, "MODULE_VALUE>>>> " + moduleValue + " ,, Content >>> " + homeTemplate.getContent());
					activity.startActivity(intent);
				}
			}
		} else {
			Log.d(TAG, "homeTemplate  >>> 4");
		}
	}

	/**
	 * 跳转登登陆
	 */
	public static void login(String loginType, int reqCode) {
		Intent intent = new Intent(AppUtils.getActivity(), LoginActivity.class);
		intent.putExtra(LoginTask.ALL_LOGIN, loginType);
		AppUtils.getActivity().startActivityForResult(intent, reqCode);
	}

	/**
	 * 跳转登登陆
	 */
	public static void login(String loginType) {
		Intent intent = new Intent(AppUtils.getActivity(), LoginActivity.class);
		intent.putExtra(LoginTask.ALL_LOGIN, loginType);
		AppUtils.getActivity().startActivity(intent);
	}

	/**
	 * 跳转登登陆
	 */
	public static void skipShopList() {
		Intent intent = new Intent(AppUtils.getActivity(), ShopActivity.class);
		intent.putExtra(ShopFragment.TYPE, ShopFragment.TYPE_ALL);
		intent.putExtra(ShopFragment.MODULE_VALUE, ShopFragment.NO_MODULE);
		AppUtils.getActivity().startActivity(intent);
	}

}
