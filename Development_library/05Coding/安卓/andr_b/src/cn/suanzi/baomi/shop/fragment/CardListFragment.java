// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import java.io.Serializable;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Card;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.UserCardVip;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopApplication;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.VipChatActivity;
import cn.suanzi.baomi.shop.adapter.CardListAdapter;
import cn.suanzi.baomi.shop.model.ListCardVipTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;

/**
 * 所有会员卡的信息显示,会员卡列表
 * 
 * @author yanfang.li
 */
public class CardListFragment extends Fragment implements IXListViewListener {

	private final static String TAG = CardListFragment.class.getSimpleName();
	public final static String CARD_OBJ = "cardobj";

	/** 会员列表 */
	private XListView mLvCardVipList;
	/** 排序类别 */
	private String mOrderType = "3";
	/** 页码 */
	private int mPage = 1;
	/** 是一个容器 */
	private PopupWindow mPopupWindow;
	private Handler mHandler;
	private TextView mTvQuery;
	private CardListAdapter queryAdapter = null;
	private Card mCard;
	/** 统计使用优惠券的张数 */
	private TextView tvUseCoupon;
	/** 抵扣总金额*/
	private TextView tvDdtMoney ;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 判断本次api是否调用成功*/
	private boolean mFlagData = false;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	
	public static CardListFragment newInstance() {
		Bundle args = new Bundle();
		CardListFragment fragment = new CardListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cardlist, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		// 初始化数据
		init(v);
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		mFlagData = true;
		Intent intent = getMyActivity().getIntent();
		mCard = (Card) intent.getSerializableExtra(CARD_OBJ);
		// 抵扣总额 和 消费优惠券的张数
		tvUseCoupon = (TextView) getMyActivity().findViewById(R.id.tv_usecoupon_num);
	    tvDdtMoney = (TextView) getMyActivity().findViewById(R.id.tv_usecoupon_money);
	    mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		LinearLayout ivReturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);
		mTvQuery = (TextView) v.findViewById(R.id.tv_msg);
		mTvQuery.setBackgroundResource(R.drawable.sel);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		String cardlv = mCard.getCardLvl();
		if (Util.isEmpty(cardlv)) { return; }
		String title = null;
		if (ShopConst.Card.LV_FIRST.equals(cardlv)) {
			title = getResources().getString(R.string.card_silvercard);

		} else if (ShopConst.Card.LV_SECOND.equals(cardlv)) {
			title = getResources().getString(R.string.card_goldcard);

		} else if (ShopConst.Card.LV_THIRD.equals(cardlv)) {
			title = getResources().getString(R.string.card_platinumcard);
		}

		tvTitle.setText(title);
		mLvCardVipList = (XListView) v.findViewById(R.id.lv_cardvip_list);
		// 调用异步任务类，获得会员列表
		mLvCardVipList.setPullLoadEnable(true); // 上拉刷新
		mLvCardVipList.setXListViewListener(this);// 实现xListviewListener接口
		mHandler = new Handler();
		// 会员卡查询
		listCardVip();
		// 给listView每列添加点击事件
		mLvCardVipList.setOnItemClickListener(lvCardListClick);
		// 给mIvCouponListQuery添加触摸事件
		mTvQuery.setOnTouchListener(touchQuery);
		
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getMyActivity().finish();
				
			}
		});
	}

	/**
	 * 调用异步任务类，获得会员列表
	 */
	private void listCardVip() {
		if (mPage <= 1) {
			// 正在加载
			ViewSolveUtils.setNoData(mLvCardVipList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		String cardCode = mCard.getCardCode();
		new ListCardVipTask(getMyActivity(), new ListCardVipTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mLvCardVipList.stopLoadMore();
				mFlagData = true;
				if (result == null) {
					mLvCardVipList.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvCardVipList, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvCardVipList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
					mLvCardVipList.setPullLoadEnable(true);
					PageData page = new PageData(result, "cardVipList", new TypeToken<List<UserCardVip>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvCardVipList.setPullLoadEnable(false);
					} else {
						mLvCardVipList.setPullLoadEnable(true);
					}
					List<UserCardVip> list = (List<UserCardVip>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCardVipList, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCardVipList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						if (queryAdapter == null) {
							queryAdapter = new CardListAdapter(getMyActivity(), list);
							mLvCardVipList.setAdapter(queryAdapter);
						} else {
							if (page.getPage() == 1) {
								queryAdapter.setItems(list);
							} else {
								queryAdapter.addItems(list);
							}
						}
					}
						
					try {
						String totalCouponUseAmount = result.get("totalCouponUseAmount").toString();
						String totalDeductionPrice = result.get("totalDeductionPrice").toString();
						Log.d(TAG, "消费金额>>>12131231" + result.toString());
						Log.d(TAG, "消费金额>>>" + totalCouponUseAmount + ",totalDeductionPrice>>> " +totalDeductionPrice);
						tvUseCoupon.setText(totalCouponUseAmount);
						tvDdtMoney.setText(totalDeductionPrice);
					} catch (Exception e) {
						Log.e(TAG, "总金额获取出错");
						// TODO: handle exception
					}
					
				}
			}

		}).execute(cardCode, "", mOrderType, String.valueOf(mPage));

	}

	/**
	 * listview的点击事件
	 */
	OnItemClickListener lvCardListClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long longid) {
			if (mSettledflag) {
				UserCardVip user = (UserCardVip) mLvCardVipList.getItemAtPosition(position);
				// Intent intent = new Intent(getMyActivity(),
				// CardDetailActivity.class);
				Intent intent = new Intent(getMyActivity(), VipChatActivity.class);
				intent.putExtra(VipChatFragment.USER_OBJ, (Serializable) user);
				startActivity(intent);
			}else{
				mShopApplication.getDateInfo(getActivity());
			}
		}
	};

	/**
	 * 触摸条件查询
	 */
	OnTouchListener touchQuery = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mSettledflag) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popupwindow_card_query, null);
					view.setBackgroundColor(Color.TRANSPARENT);
					mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					mPopupWindow.setFocusable(true);
					mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
					mPopupWindow.update();
					mPopupWindow.setOutsideTouchable(true);
					mPopupWindow.showAsDropDown(mTvQuery, 10, 0);
					// mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
					TextView tvOrdercard = (TextView) view.findViewById(R.id.tv_order_card);// 办卡时间
					TextView tvOrderCsmDate = (TextView) view.findViewById(R.id.tv_order_csm_date);// 消费时间
					TextView tvOrderCsmMoney = (TextView) view.findViewById(R.id.tv_order_csm_money);// 消费金钱
					TextView tvOrderCsmTime = (TextView) view.findViewById(R.id.tv_order_csm_time);// 消费次数
					// 排序的点击事件
					OnClickListener tvOrderClick = new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.tv_order_card:// 办卡时间
								mOrderType = "1";
								mPage = 1;
								listCardVip();
								mPopupWindow.dismiss();
								break;
							case R.id.tv_order_csm_date:// 消费时间
								mOrderType = "2";
								mPage = 1;
								listCardVip();
								mPopupWindow.dismiss();
								break;
							case R.id.tv_order_csm_money:// 消费金钱
								mOrderType = "3";
								mPage = 1;
								listCardVip();
								mPopupWindow.dismiss();
								break;
							case R.id.tv_order_csm_time:// 消费次数
								mOrderType = "4";
								mPage = 1;
								listCardVip();
								mPopupWindow.dismiss();
								break;

							default:
								break;
							}
						}
					};

					tvOrdercard.setOnClickListener(tvOrderClick);
					tvOrderCsmDate.setOnClickListener(tvOrderClick);
					tvOrderCsmMoney.setOnClickListener(tvOrderClick);
					tvOrderCsmTime.setOnClickListener(tvOrderClick);
				}
			} else {
				mShopApplication.getDateInfo(getActivity());
			}
			
			return true;
		}
	};

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mPage++;
					listCardVip();
				}
			}, 2000);
		}
	}
}
