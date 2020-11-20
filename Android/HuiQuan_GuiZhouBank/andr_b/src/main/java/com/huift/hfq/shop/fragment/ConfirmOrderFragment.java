package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BaseBean;
import com.huift.hfq.base.pojo.PayOrderBean;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.PayStatusActivity;
import com.huift.hfq.shop.icbcPay.PrintUtils;
import com.huift.hfq.shop.icbcPay.TransResultBean;
import com.huift.hfq.shop.icbcPay.TransService;
import com.huift.hfq.shop.icbcPay.TransType;
import com.huift.hfq.shop.model.UploadPayInfoTask;
import com.icbc.smartpos.transservice.aidl.TransHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

import static com.huift.hfq.shop.icbcPay.TransService.isTransServiceBind;

/**
 * 扫码支付的确认订单界面
 * @author qian.zhou
 */
public class ConfirmOrderFragment extends Fragment {
	private PayOrderBean payOrder;
	private TransResultBean transResultBean;
	private int connectCount = 0;

	/**
	 * 需要传递参数时有利于解耦
	 * @return PosPayFragment
	 */
	public static ConfirmOrderFragment newInstance() {
		Bundle args = new Bundle();
		ConfirmOrderFragment fragment = new ConfirmOrderFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_confirm_order, container, false);
		ViewUtils.inject(this, v);
		ActivityUtils.add(getMyActivity());
		Util.addActivity(getMyActivity());
		init(v);
		return v;
	}

	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		payOrder= (PayOrderBean) getMyActivity().getIntent().getSerializableExtra("payOrder");
		//初始化数据
		TextView tv_title = v.findViewById(R.id.tv_mid_content);//标题
		tv_title.setText("确认订单");
		TextView tv_orderNo = (TextView) v.findViewById(R.id.tv_orderNo);
		TextView tv_totalPrice = (TextView) v.findViewById(R.id.tv_totalPrice);
		TextView tv_noDiscountPrice = (TextView) v.findViewById(R.id.tv_noDiscountPrice);
		TextView tv_orderTime = (TextView) v.findViewById(R.id.tv_orderTime);
		TextView tv_payPrice = (TextView) v.findViewById(R.id.tv_payPrice);
		TextView tv_discountPrice = (TextView) v.findViewById(R.id.tv_discountPrice);
		TextView tv_couponNo = (TextView) v.findViewById(R.id.tv_couponNo);
		TextView tv_couponType = (TextView) v.findViewById(R.id.tv_couponType);
		TextView tv_couponInfo = (TextView) v.findViewById(R.id.tv_couponInfo);
        LinearLayout ll_coupon=v.findViewById(R.id.ll_coupon);
		Button b_pay = (Button) v.findViewById(R.id.b_pay);

		tv_orderNo.setText("订单号:"+payOrder.getOrderNbr());
		tv_totalPrice.setText(payOrder.getOrderAmount());
		tv_noDiscountPrice.setText(payOrder.getNoDiscountPrice());
		tv_orderTime.setText(payOrder.getOrderTime());
		tv_payPrice.setText(payOrder.getRealPay());

		if (TextUtils.isEmpty(payOrder.getCouponCode())){
			ll_coupon.setVisibility(View.GONE);
		}else {
		    ll_coupon.setVisibility(View.VISIBLE);
            tv_discountPrice.setText(payOrder.getDiscountPrice());
            tv_couponNo.setText(payOrder.getCouponCode());
            tv_couponType.setText(payOrder.getCouponType());
            tv_couponInfo.setText(payOrder.getCouponDescription());
		}

		b_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					long payMoney = (long) (Float.parseFloat(payOrder.getRealPay())*100);
					payICBC(payMoney);
				} catch (Exception e) {
					e.printStackTrace();
					Util.showToastZH("金额错误");
				}
			}
		});
	}

	/**
	 * 工行支付
	 * @param money 金额，单位：分
	 */
	private void payICBC(long money){
		if (!isTransServiceBind) {
			Util.showToastZH("支付服务未绑定，无法进行交易");
			return;
		}

		String transType = TransType.TRANSTYPE_MULTI_PURCHASE;//收银

		Bundle ctrlData = new Bundle();
		ctrlData.putString("APP_NAME", getString(R.string.app_name));
		ctrlData.putString("AIDL_VER", "V0.0.10");

		Bundle transData = new Bundle();
		transData.putLong("AMOUNT", money);//金额，单位：分

		//收银时要求展示的支付方式
		ArrayList<String> TRANS_LIST = new ArrayList<>();
		TRANS_LIST.add(TransType.TRANSTYPE_PURCHASE);
		TRANS_LIST.add(TransType.TRANSTYPE_QRPURCHASE);
		transData.putStringArrayList("TRANS_LIST", TRANS_LIST);

		ArrayList<String> printList = new ArrayList<>();
		String text_print = PrintUtils.assemble_text_print(1, 0, "订单号:" + payOrder.getOrderNbr());
		//商户附加的打印数据，当该字段存在时将把对应信息打印在持卡人联的签名栏位置。
		printList.add(text_print);
		//ADDITIONAL_PRINT 此字段如果存在必须有值，否则总行收单应用会拒绝交易。
		//特色打印信息在【持卡人联】打印。【商户联】没有
		transData.putStringArrayList("ADDITIONAL_PRINT", printList);

		Bundle bill_info = new Bundle();
		//需额外附加在对账单上的信息。如商户可传入自己系统的交易流水号，在工行提供对账单上每一笔交易将会附加该流水号，方便查阅对账。
		bill_info.putString("bill_info", payOrder.getOrderNbr()); //最大60字节
		//ADDITIONAL_INFO 此字段如果存在必须有值，否则总行收单应用会拒绝交易。
		transData.putBundle("ADDITIONAL_INFO", bill_info);

		try {
			TransService.getInstance(null).getICBCTransService().startTrans(transType, ctrlData, transData, transHandler);
		} catch (Exception e) {
			e.printStackTrace();
			Util.showToastZH("支付失败，请重新支付");
		}
	}

	/**
	 * 工行支付回调
	 */
	private TransHandler transHandler = new TransHandler.Stub() {

		@Override
		public void onFinish(final Bundle _baseResult, final Bundle _transResult, final Bundle _extraInfo) {
			getMyActivity().runOnUiThread(new Runnable() {
				public void run() {

					transResultBean = new TransResultBean();
					transResultBean.analysisResult(_baseResult, _transResult, _extraInfo);

					if (transResultBean.getResult() == 0) {//支付成功
						uploadPayInfo();
					} else {//支付失败
						goToPayStatusPage(2, transResultBean.getDescription());
					}
				}
			});
		}
	};

	/**
	 * 上送工行支付数据
	 */
	private void uploadPayInfo(){
		new UploadPayInfoTask(getMyActivity(), new UploadPayInfoTask.Callback() {
			@Override
			public void getResult(BaseBean resultBean) {
				if (resultBean!=null){
					if (resultBean.getCode()== ErrorCode.SUCC){
						goToPayStatusPage(1, "成功收款");
					}else {
						goToPayStatusPage(0, resultBean.getMsg());
					}
				}else {
					goToPayStatusPage(0, "上送数据失败");
				}
			}
		}).execute(payOrder.getOrderNbr(),transResultBean.getPayType(),transResultBean.getRefNo(),transResultBean.getTransTime(),transResultBean.getQrCodeOrder(),transResultBean.getTermNo());
	}

	/**
	 * @param status 0：处理中（工行支付成功，本地接口失败）   1：成功  2：失败
	 * @param msg    支付消息
	 */
	private void goToPayStatusPage(int status, String msg) {
		if (status == 0 && connectCount < 3) {//如果为0，重新请求，最多请求3次
			uploadPayInfo();
			connectCount++;
			return;
		}
		if (status == 0) {
			msg = msg + "\n若已经扣款，请联系管理员退款";
		}
		Intent intent = new Intent(getMyActivity(), PayStatusActivity.class);
		intent.putExtra("payStatus", status);
		intent.putExtra("payMsg", msg);
		startActivity(intent);
		getMyActivity().finish();
	}

	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivuppBackClick(View v) {
		getMyActivity().finish();
	}
}
