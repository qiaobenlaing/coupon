package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;

import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.HqBook;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.activity.BillClassActivity;
import com.huift.hfq.shop.model.GetHqBookTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 惠圈账本
 *
 * @author wensi.yu
 */
public class BillHomeFragment extends Fragment {

    private static final String BILL_TITLE = "惠圈账本";

    public static BillHomeFragment newInstance() {
        Bundle args = new Bundle();
        BillHomeFragment fragment = new BillHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_home, container, false);
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
        TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
        tvCampaignName.setText(BILL_TITLE);
        TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
        tvSet.setVisibility(View.GONE);
        getHqBook(view);
    }

    /**
     * 惠圈账本
     */
    private void getHqBook(View view) {
        final TextView tv_usedCount = (TextView) view.findViewById(R.id.tv_usedCount);
        final TextView tv_usedMoney = (TextView) view.findViewById(R.id.tv_usedMoney);
        final TextView tv_unusedCount = (TextView) view.findViewById(R.id.tv_unusedCount);
        final TextView tv_unusedMoney = (TextView) view.findViewById(R.id.tv_unusedMoney);
        final TextView incomeAmount = (TextView) view.findViewById(R.id.tv_bill_allincome);

        new GetHqBookTask(getMyActivity(), new GetHqBookTask.Callback() {

            @Override
            public void getResult(JSONObject result) {
                if (null == result) {
                    return;
                }

                HqBook hqBook = Util.json2Obj(result.toString(), HqBook.class);

                tv_usedCount.setText(!Util.isEmpty(hqBook.getCouponHasUsed()) ? hqBook.getCouponHasUsed() + "笔" : "");
                tv_usedMoney.setText(!Util.isEmpty(hqBook.getHasUsedMoney()) ? hqBook.getHasUsedMoney() + "元" : "");
                tv_unusedCount.setText(!Util.isEmpty(hqBook.getNeedConfirm()) ? hqBook.getNeedConfirm() + "笔" : "");
                tv_unusedMoney.setText(!Util.isEmpty(hqBook.getNeedConfirmMoney()) ? hqBook.getNeedConfirmMoney() + "元" : "");
                incomeAmount.setText(!Util.isEmpty(hqBook.getRealGetMoney()) ? hqBook.getRealGetMoney() + "元" : "");

            }
        }).execute();
    }

    /**
     * 跳转的页面
     *
     * @param view
     */
    @OnClick({R.id.layout_turn_in, R.id.ly_bill_one, R.id.ly_bill_two, R.id.ly_bill_three, R.id.ly_bill_four, R.id.ly_bill_five, R.id.ly_bill_six, R.id.ly_bill_seven})
    public void trunBack(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_turn_in:
                getMyActivity().finish();
                break;

            case R.id.ly_bill_one://顾客清单
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_ONE));
                startActivity(intent);
                break;
            case R.id.ly_bill_two://退款清单
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_TWO));
                startActivity(intent);
                break;
            case R.id.ly_bill_three://消费未结算清单
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_THIRD));
                startActivity(intent);
                break;
            case R.id.ly_bill_four://补贴未结算清单
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_FOUR));
                startActivity(intent);
                break;
            case R.id.ly_bill_five://支付结算对账
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_FIVE));
                startActivity(intent);
                break;
            case R.id.ly_bill_six://支付结算对账
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_SIX));
                startActivity(intent);
                break;
            case R.id.ly_bill_seven://账单查询
                intent = new Intent(getMyActivity(), BillClassActivity.class);
                intent.putExtra(BillClassFragment.BILL_TITLE, String.valueOf(Util.NUM_SEVEN));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
