// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XXListView;
import cn.suanzi.baomi.base.view.XXListView.IXXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.MassageActivity;
import cn.suanzi.baomi.shop.adapter.MsgListAdapter;
import cn.suanzi.baomi.shop.model.ListMsgTask;
import cn.suanzi.baomi.shop.model.ReadMessageTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;

/**
 * 优惠券消息显示
 * 
 * @author yanfang.li
 */
public class MsgCouponFragment extends Fragment implements IXXListViewListener {

	private final static String TAG = "MsgCouponFragment";
	private final static int HAVE_DATA = 1;
	private final static int NO_DATA = 0;
	/** 会员卡消息列表 */
	private XXListView mLvCoupon;
	private Handler mHandler;
	private LinearLayout lyNodate;
	private Messages mMessages;
	private MsgListAdapter mAdapter;
	private int mPage = 1;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	private boolean mFlagData = false;
	private boolean mUpFlagData = false;
	private static int sCouponMsgCount;

	public static MsgCardFragment newInstance() {

		Bundle args = new Bundle();
		MsgCardFragment fragment = new MsgCardFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_massage_list, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvCoupon = (XXListView) v.findViewById(R.id.lv_msg);
		mLvCoupon.setPullLoadEnable(true);
		mLvCoupon.setPullRefreshEnable(true);
		mLvCoupon.setXXListViewListener(this, Const.PullRefresh.SHOP_COUPON_MSG);
		mHandler = new Handler();
		mMessages = (Messages) getActivity().getIntent().getSerializableExtra(MassageActivity.MESSAGE_OBJ);
		if (null != mMessages) {
			sCouponMsgCount = mMessages.getCoupon();
		}
		listMsg();
		mFlagData = true;
		mUpFlagData = true;
		return v;
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mPage <= 1) {
			// 正在加载
			ViewSolveUtils.setNoData(mLvCoupon, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		new ListMsgTask(getActivity(), new ListMsgTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				mUpFlagData = true;
				mLvCoupon.stopLoadMore(); // 上拉加载完成
				if (result != null) {
					readMessage(); // 阅读消息
					mLvCoupon.setPullLoadEnable(true);
					JSONObject couponMsg = (JSONObject) result.get("couponMsg");
					if (couponMsg != null || !"".equals(couponMsg.toString())) {
						ViewSolveUtils.setNoData(mLvCoupon, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						PageData page = new PageData(couponMsg, "couponMsgList", new TypeToken<List<Messages>>() {}.getType());
						mPage = page.getPage();
						if (page.hasNextPage() == false) {
							if (page.getPage() > 1) {
								Util.getContentValidate(R.string.toast_moredata);
							}
							mLvCoupon.setPullLoadEnable(false);
						} else {
							mLvCoupon.setPullLoadEnable(true);
						}
						List<Messages> list = (List<Messages>) page.getList();
						Log.d(TAG, "消息>>>>="+list.size() + "<<<< result="+couponMsg.toString());
						if (null == list || list.size() <= 0) {
							ViewSolveUtils.morePageOne(mLvCoupon, mLyView, mIvView, mProgView, mPage);
						} else {
							ViewSolveUtils.setNoData(mLvCoupon, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
							if (mAdapter == null) {
								Log.d(TAG, "消息>>>>>>>>>6");
								mAdapter = new MsgListAdapter(getActivity(), list, ShopConst.Massage.MSG_CARD);
								Log.d(TAG, "消息>>>>>>>>>8");
								mLvCoupon.setAdapter(mAdapter);
								Log.d(TAG, "消息>>>>>>>>>8="+mAdapter.getCount());
							} else {
								if (page.getPage() == 1) {
									mAdapter.setItems(list);
								} else {
									mAdapter.addItems(list);
								}
							}
						}
					} else {
						ViewSolveUtils.morePageOne(mLvCoupon, mLyView, mIvView, mProgView, mPage);
					}

				} else {
					ViewSolveUtils.morePageOne(mLvCoupon, mLyView, mIvView, mProgView, mPage);
				}
			}
		}).execute(mPage+"");

	}

	private void readMessage() {
		new ReadMessageTask(getActivity(), new ReadMessageTask.Callback() {

			@Override
			public void getResult(int result) {
				if (result == ErrorCode.SUCC) {
					sCouponMsgCount = 0;
				}
			}
		}).execute(ShopConst.Massage.MSG_COUPON + "");
	}

	/**
	 * 设置消息清零
	 * 
	 * @return
	 */
	public static int setMsgCleared() {
		return sCouponMsgCount;
	}

	@Override
	public void onRefresh() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					listMsg();
					mPage = 1;
					mLvCoupon.stopRefresh();
				}
			}, 2000);
		}

	}

	@Override
	public void onLoadMore() {
		if (mUpFlagData) {
			mUpFlagData = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					listMsg();
				}
			}, 2000);
		}

	}

}
