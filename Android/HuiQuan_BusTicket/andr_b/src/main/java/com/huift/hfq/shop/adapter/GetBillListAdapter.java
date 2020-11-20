package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Bill;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 顾客清单的列表
 * @author wensi.yu
 *
 */
public class GetBillListAdapter extends CommonListViewAdapter<Bill>{
	
	private final static String TAG = "GetBillListAdapter";
	
	private String type;
	
	public GetBillListAdapter(Activity activity, List<Bill> datas,String type) {
		super(activity, datas);
		this.type = type;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_bill_customer, position);
		Bill item = (Bill) getItem(position);
		
		ImageView customerImage = ((ImageView) holder.getView(R.id.iv_bill_customer));
		Util.showImage(mActivity, item.getAvatarUrl(), customerImage);//显示用户头像
		Log.d(TAG, "type adapter == " +type);
		TextView nickName = (TextView) holder.getView(R.id.tv_bill_customer);//用户昵称
		nickName.setText(!Util.isEmpty(item.getNickName()) ? item.getNickName(): "");
		((TextView) holder.getView(R.id.tv_bill_phone)).setText(item.getMobileNbr());//手机号码后四位
		
		LinearLayout lyConsumption = (LinearLayout)holder.getView(R.id.ly_bill_consumption);//第二行
		LinearLayout lyPay = (LinearLayout)holder.getView(R.id.ly_pay);//第三行
		
		//第二行的三个个控件
		LinearLayout lyConsumnumber = (LinearLayout)holder.getView(R.id.ly_consumnumber);//消费次数
		LinearLayout lyConsummoney = (LinearLayout)holder.getView(R.id.ly_consummoney);//消费金额
		LinearLayout lyPaymoneyone = (LinearLayout)holder.getView(R.id.ly_bill_paymoneyone);//支付金额
		
		//第三行四个控件
		LinearLayout lyPaymoney = (LinearLayout)holder.getView(R.id.ly_bill_paymoney);//支付金额
		LinearLayout lyDiscountmoney = (LinearLayout)holder.getView(R.id.ly_bill_discountmoney);//优惠金额
		LinearLayout lyRefund = (LinearLayout)holder.getView(R.id.ly_refund);//退款金额
		LinearLayout lySubsidy = (LinearLayout)holder.getView(R.id.ly_subsidy);//补贴金额
		
		//其他
		RelativeLayout lyLast = (RelativeLayout)holder.getView(R.id.rl_last);//最后消费时间
		RelativeLayout lyConsumptiondate = (RelativeLayout)holder.getView(R.id.rl_consumptiondate);//消费时间
		RelativeLayout lyRefunddate = (RelativeLayout)holder.getView(R.id.rl_refunddate);//退款时间
		RelativeLayout lyOrder = (RelativeLayout)holder.getView(R.id.rl_order);//订单号
		
		//各个空间的值
		TextView mBillConsumnumber = (TextView) holder.getView(R.id.tv_bill_consumnumber);//消费次数
		TextView mBillConsummoney = (TextView) holder.getView(R.id.tv_bill_consummoney);//消费金额
		TextView mBillPaymoney = (TextView) holder.getView(R.id.tv_bill_paymoney);//支付金额
		TextView mBillDiscountmoney = (TextView) holder.getView(R.id.tv_bill_discountmoney);//优惠金额
		TextView mBillRefundone = (TextView) holder.getView(R.id.tv_bill_refundone);//退款金额
		TextView mBillSubsidy = (TextView) holder.getView(R.id.tv_bill_subsidy);//补贴金额
		TextView mBillPaymoneyone = (TextView) holder.getView(R.id.tv_bill_paymoneyone);//支付金额（第二行的控件）
		
		TextView mBillDate = (TextView) holder.getView(R.id.tv_bill_date);//最后消费时间
		TextView mBillDateOne = (TextView) holder.getView(R.id.tv_bill_dateone);//消费时间
		TextView mBillRefunddate = (TextView) holder.getView(R.id.tv_bill_refunddate);//退款时间
		TextView mBillOrder = (TextView) holder.getView(R.id.tv_bill_order);//订单号
		
		if (type.equals(String.valueOf(Util.NUM_ONE))) {//1-顾客清单
			lyConsumption.setVisibility(View.VISIBLE);
			lyPay.setVisibility(View.VISIBLE);
			lyConsumnumber.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoney.setVisibility(View.VISIBLE);
			lyDiscountmoney.setVisibility(View.VISIBLE);
			lyLast.setVisibility(View.VISIBLE);
			
			mBillConsumnumber.setText(item.getConsumptionNbr() + "次");//消费次数
			mBillConsummoney.setText(item.getConsumptionAmount()+ "元");//消费金额
			mBillPaymoney.setText(item.getPayAmount() + "元");//支付金额
			mBillDiscountmoney.setText(item.getDiscountAmount() + "元");//优惠金额
			mBillDate.setText(item.getLastConsumeTime());//最后消费时间
			
		} else if (type.equals(String.valueOf(Util.NUM_TWO))){//2-退款清单
			lyConsumption.setVisibility(View.VISIBLE);
			lyPay.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoney.setVisibility(View.VISIBLE);
			lyRefund.setVisibility(View.VISIBLE);
			
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyRefunddate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			mBillPaymoney.setText(item.getPayAmount() + "元");//支付金额
			mBillRefundone.setText(item.getRefundAmount() + "元");//退款金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillRefunddate.setText(item.getRefundTime());//退款时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		} else if (type.equals(String.valueOf(Util.NUM_THIRD))) {//3-消费未结算账单
			lyConsumption.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoneyone.setVisibility(View.VISIBLE);
			
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			mBillPaymoneyone.setText(item.getPayAmount() + "元");//支付金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		} else if (type.equals(String.valueOf(Util.NUM_FOUR))) {//4-补贴未结算账单 
			lyConsumption.setVisibility(View.VISIBLE);
			lyPay.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoney.setVisibility(View.VISIBLE);
			lySubsidy.setVisibility(View.VISIBLE);
			
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			mBillPaymoney.setText(item.getPayAmount() + "元");//支付金额
			mBillSubsidy.setText(item.getSubsidyAmount() + "元");//补贴金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		} else if (type.equals(String.valueOf(Util.NUM_FIVE))) {//5-支付结算对账
			lyConsumption.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoneyone.setVisibility(View.VISIBLE);
			
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			mBillPaymoneyone.setText(item.getPayAmount() + "元");//支付金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		} else if (type.equals(String.valueOf(Util.NUM_SIX))) {//6-补贴结算对账 
			lyConsumption.setVisibility(View.VISIBLE);
			lyPay.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoney.setVisibility(View.VISIBLE);
			lySubsidy.setVisibility(View.VISIBLE);
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			mBillPaymoney.setText(item.getPayAmount() + "元");//支付金额
			mBillSubsidy.setText(item.getSubsidyAmount() + "元");//补贴金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		} else if (type.equals(String.valueOf(Util.NUM_SEVEN))) {//7-账单查询
			lyConsumption.setVisibility(View.VISIBLE);
			lyPay.setVisibility(View.VISIBLE);
			lyConsummoney.setVisibility(View.VISIBLE);
			lyPaymoney.setVisibility(View.VISIBLE);
			lyDiscountmoney.setVisibility(View.GONE);
			lyConsumptiondate.setVisibility(View.VISIBLE);
			lyOrder.setVisibility(View.VISIBLE);
			
			mBillConsummoney.setText(item.getConsumptionAmount() + "元");//消费金额
			((TextView) holder.getView(R.id.tv_bill_paymoney)).setText(item.getPayAmount() + "元");//支付金额
			((TextView) holder.getView(R.id.tv_bill_discountmoney)).setText(item.getDiscountAmount() + "元");//优惠金额
			mBillDateOne.setText(item.getConsumptionTime());//消费时间
			mBillOrder.setText(item.getOrderNbr());//订单号
			
		}
	
		return holder.getConvertView();
	}

}
