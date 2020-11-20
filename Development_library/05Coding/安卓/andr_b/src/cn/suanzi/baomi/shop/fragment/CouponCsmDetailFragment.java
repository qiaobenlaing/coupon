// ---------------------------------------------------------

//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.5.22 
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.UserCardVip;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.CouponUserDrawAdapter;
import cn.suanzi.baomi.shop.model.ListGrabCouponTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券领取详情
 * @author yanfang.li
 */
public class CouponCsmDetailFragment extends Fragment implements IXListViewListener{
	
	private final static String TAG = CouponCsmDetailFragment.class.getSimpleName();
	/** 上一页面传值的标示码 **/
	public final static String  BATCH_COUPON_CODE = "batchCouponCode";
	public final static int HAS_DATA = 1;
	public final static int NO_DATA = 0;
	/** 每批次列表 **/
	private XListView mLvCouponDraw;
	/** 页码 **/
	private int mPage = 1;
	//
	private Type jsonType = new TypeToken<List<UserCardVip>>() {}.getType();// 所属类别
	/** 下拉刷新的线程*/
	private Handler mHandler;
	/** 没有数据加载的图片*/
	private LinearLayout mLyNodate;
	/** 适配器*/
	private CouponUserDrawAdapter mDrawAdapter;
	
	public static CouponCsmDetailFragment newInstance() {

		Bundle args = new Bundle();
		// 获取Bundle Arguments
		CouponCsmDetailFragment fragment = new CouponCsmDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_couption_csmdetail,container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mLvCouponDraw = (XListView) view.findViewById(R.id.lv_coupon_csm_dtl);
		mLvCouponDraw.setPullLoadEnable(true);
		mLvCouponDraw.setXListViewListener(this);
		mHandler = new Handler();
		LinearLayout ivReturn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);		
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.coupon_draw_detail));
		listGrabCoupon();
		return view;
	}
	
	/**
	 * 调用某一批次优惠券使用详情列表的异步任务类 GetCouponConsumeTask
	 * @param batchCouponCode 每批次编号
	 */
	private void listGrabCoupon() {
	    Intent intent = getActivity().getIntent();//得到一个intent对象
		String batchCouponCode = intent.getStringExtra(BATCH_COUPON_CODE);
		String []params = {batchCouponCode,mPage+""};
		new ListGrabCouponTask(getActivity(), new ListGrabCouponTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					if (mPage > 1) {
						setNoDateBg(HAS_DATA);
					} else {
						setNoDateBg(NO_DATA);
					}
					mLvCouponDraw.setPullLoadEnable(false);
				} else {
					mLvCouponDraw.setPullLoadEnable(true);
					setNoDateBg(HAS_DATA);
					PageData page = new PageData(result,"couponList",jsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvCouponDraw.setPullLoadEnable(false);
					}else{
						mLvCouponDraw.setPullLoadEnable(true);
					}
					if(mDrawAdapter == null){
						mDrawAdapter = new CouponUserDrawAdapter(getActivity(), (List<UserCardVip>)page.getList());
						mLvCouponDraw.setAdapter(mDrawAdapter);
					}else{
						if(page.getPage() == 1){
							mDrawAdapter.setItems((List<UserCardVip>)page.getList());
						}else{
							mDrawAdapter.addItems((List<UserCardVip>)page.getList());
						}
					}
				
				}
			}
		}).execute(params);
	}
	
	/**
	 * 设置什么时候显示无数据的图片
	 * @param status
	 */
	private void setNoDateBg (int status) {
		if (status == HAS_DATA) { //  有数据
			mLyNodate.setVisibility(View.GONE);
			mLvCouponDraw.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLvCouponDraw.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 按钮返回事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	private void ivReturnClick(View view) {
		if (view.getId() == R.id.layout_turn_in) {
			getActivity().finish();
		}
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPage++;
				listGrabCoupon();
				mLvCouponDraw.stopLoadMore();
			}
		}, 2000);
	}
}
