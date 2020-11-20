// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.pojo.CountCard;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CardGradeListActivity;
import com.huift.hfq.shop.activity.CardListActitivity;
import com.huift.hfq.shop.adapter.CardQueryAdapter;
import com.huift.hfq.shop.model.CountCardTask;
import com.huift.hfq.shop.model.GetGeneralCardStasticsTask;
import com.huift.hfq.shop.model.ListConsumeTrendTask;
import com.huift.hfq.shop.model.ListIncreasingTrendTask;
import com.huift.hfq.shop.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * 会员卡主页的界面
 * 
 * @author yanfang.li
 */
public class CardHomeFragment extends BaseFragment {

	private final static String TAG = CardHomeFragment.class.getSimpleName();
	
	public final static String CARD_FISRT = "cardFirst";
	/** 查询数据为空就输入"0" **/
	private final static String IS_NULL_CODE = "0";
	/** 查询界面 **/
	private MyGridView mGvCard;
	/** 会员卡规则设定 **/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvCardVipSet;
	/** 会员增长显示的数据 **/
	private String[][] mChartDate;
	/** 获取会员增长走势图 **/
	@ViewInject(R.id.ly_cardvip_growthchart)
	private LinearLayout mLyGrowthChart;
	/** 金额消费走势图 **/
	@ViewInject(R.id.ly_cardvip_csp_chart)
	private LinearLayout mLyCspChart;
	/** 会员卡等级的数据 */
	private List<Card> mCardDatas;
	private String mCardflag = "";
	private boolean mCardAdd = false;
	private View view;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return CardHomeFragment
	 */
	public static CardHomeFragment newInstance() {
		Bundle args = new Bundle();
		CardHomeFragment fragment = new CardHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void viewVisible() {
		super.viewVisible();
		DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
		boolean jpushFlag = DB.getBoolean(ShopConst.Key.JPUSH_CARDACCEPT);
		String first = DB.getStr(CARD_FISRT);
		if (!Util.isEmpty(first)) {
			if (first.equals(Util.NUM_ONE + "")) {
				// 第一次进来
				DB.saveStr(CARD_FISRT, Util.NUM_TWO + "");
			} else {
				if (jpushFlag) {
					DB.saveBoolean(ShopConst.Key.JPUSH_CARDACCEPT, false);
					// 获得会员卡的统计信息
					getCountCard(view);
					getGeneralCardStastics();
					// 会员增长走势图
					listIncreasingTrend();
					// 会员消费走势图
					listConsumeTrend();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_card_home, container, false);
		ViewUtils.inject(this, view);
		// 保存互动的activity
		SzApplication.setCurrActivity(getActivity());
		Util.addHomeActivity(getActivity());
		// 初始化控件
		init(view);
		DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
		return view;
	}

	/**
	 * 初始化方法
	 */
	private void init(View view) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		Log.d(TAG, "mSettledflag===="+mSettledflag);
		// 第一次进来
		DB.saveStr(CARD_FISRT, Util.NUM_TWO + "");
		DB.saveBoolean(ShopConst.Key.JPUSH_CARDACCEPT, false);
		// 设置scrollView滚动条的位置
		ScrollView svCard = (ScrollView) view.findViewById(R.id.sv_card);
		svCard.smoothScrollTo(0, 0);
		mGvCard = (MyGridView) view.findViewById(R.id.gv_card_query);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);
		tvSet.setText(getResources().getString(R.string.app_set));
		Shop shop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
		TextView tvShopName = (TextView) view.findViewById(R.id.tv_mid_content);
		if (shop != null) {
			if (!Util.isEmpty(shop.getShopTitle())) {
				tvShopName.setText(shop.getShopTitle());
			}
		}
		// 设置会员卡
		tvSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSettledflag ) {
					if (v.getId() == R.id.tv_msg) {
						Intent intent = new Intent(getActivity(), CardGradeListActivity.class);
						intent.putExtra(ShopConst.Card.CARD_VALUE, mCardflag);
						intent.putExtra(ShopConst.Card.CARD_LIST, (Serializable) mCardDatas);
						startActivity(intent);
						// 友盟统计
						MobclickAgent.onEvent(getActivity(), "cardhome_set");
					}
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
			}
		});
		
		// 设置背景图片为空
		mIvCardVipSet.setBackgroundResource(0);
		mIvCardVipSet.setVisibility(View.GONE);
		// 获得会员卡的统计信息
		getCountCard(view);
		getGeneralCardStastics();
		// 会员增长走势图
		listIncreasingTrend();
		// 会员消费走势图
		listConsumeTrend();
	}

	/**
	 * 获得每个等级会员卡的等级和会员数量
	 */
	private void getGeneralCardStastics() {
		final ProgressBar progCenterData = (ProgressBar) view.findViewById(R.id.prog_center_nodata);
		new GetGeneralCardStasticsTask(getActivity(), new GetGeneralCardStasticsTask.Callback() {

			@Override
			public void getResult(JSONArray rsult) {
				progCenterData.setVisibility(View.GONE);
				// 准备GridView的适配器所用的数据
				mCardDatas = new ArrayList<Card>();
				// 获取自定义个数组信息
				if (rsult == null) {
					mCardflag = ShopConst.Card.CARD_NULL;
					for (int i = 1; i < 4; i++) {
						Card card = new Card();
						card.setCardLvl(i + "");
						mCardDatas.add(card);
					}

				} else {
					for (int i = 0; i < rsult.size(); i++) {
						mCardflag = ShopConst.Card.CARD_VALUE;
						Card card = Util.json2Obj(rsult.get(i).toString(), Card.class);
						mCardDatas.add(card);
					}
				}
				// 给GridView绑定适配器
				CardQueryAdapter adapter = new CardQueryAdapter(getActivity(), mCardDatas);
				mGvCard.setAdapter(adapter);

				mGvCard.setOnItemClickListener(gvItemListener);
			}
		}).execute();
	}

	/**
	 * GridView每格的点击事件
	 */
	OnItemClickListener gvItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long longId) {
			Intent intent = new Intent(getActivity(), CardListActitivity.class);
			Card card = (Card) mGvCard.getItemAtPosition(position);
			intent.putExtra(CardListFragment.CARD_OBJ, (Serializable)card);
			startActivity(intent);
		}
	};

	/**
	 * 获得会员卡的统计信息
	 */
	private void getCountCard(final View view) {
		final ProgressBar progTopData = (ProgressBar) view.findViewById(R.id.prog_top_nodata);
		// 调用异步任务类
		new CountCardTask(getActivity(), new CountCardTask.Callback() {

			// 对查询的结果进行操作
			@Override
			public void getResult(JSONObject rsult) {
				progTopData.setVisibility(View.GONE);
				// 会员总人数
				TextView tvPersonSum = (TextView) view.findViewById(R.id.tv_cardvip_p_sum);
				// 近一个月新增人数
				TextView tvAddPersonSum = (TextView) view.findViewById(R.id.tv_cardvip_add_sum);
				// 总消费金额
				TextView tvCspSum = (TextView) view.findViewById(R.id.tv_cardvip_csp_sum);
				// 总积分
				TextView tvIglSum = (TextView) view.findViewById(R.id.tv_cardvip_igl_sum);
				// 三个月到期的积分
				TextView tvIglOdSum = (TextView) view.findViewById(R.id.tv_cardvip_igl_od_sum);
				// 判断获取的数据是否为空,为空就是给个默认值 "0"
				if (rsult == null) {
					tvPersonSum.setText(IS_NULL_CODE + "人");
					tvAddPersonSum.setText(IS_NULL_CODE + "人");
					tvCspSum.setText(IS_NULL_CODE + "元");
					tvIglSum.setText(IS_NULL_CODE + "元");
					tvIglOdSum.setText(IS_NULL_CODE + "分");
				} else {
					// 统计人数的对象
					CountCard count = Util.json2Obj(rsult.toString(), CountCard.class);
					// 会员总人数
					tvPersonSum.setText(count.getNbrOfVip() + "人");
					// 近一个月新增会员人数
					tvAddPersonSum.setText(count.getNbrOfNewVip() + "人");
					// 会员的总消费金额
					tvCspSum.setText(count.getAmountOfConsumption() + "元");
					// 会员的总积分
					tvIglSum.setText(count.getAmountOfPoint() + "分");
					// 三个月内到期积分
					tvIglOdSum.setText(count.getAmountOfExpiringPoint() + "分");
					/*
					 * 会员的总积分 amountOfPoint 三个月内到期积分 amountOfExpiringPoint
					 */
				}
			}

		}).execute();
	}

	/**
	 * 会员消费走势图
	 */
	private void listConsumeTrend() {
		final ProgressBar progBottom2Data = (ProgressBar) view.findViewById(R.id.prog_bottom2_nodata);
		new ListConsumeTrendTask(getActivity(), new ListConsumeTrendTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				progBottom2Data.setVisibility(View.GONE);
				mChartDate = new String[result.size()][2];
				for (int i = 0; i < result.size(); i++) {
					JSONObject Consume = (JSONObject) result.get(i);
      
					String month = Consume.get("month").toString();// 月份
					String fee = Consume.get("fee").toString();// 这个月新增人数
					mChartDate[i][0] = month;
					mChartDate[i][1] = fee;
				}
				// 初始化柱状图
				initView(mChartDate, 0);

			}
		}).execute();
	}

	/**
	 * 会员增长走势图
	 */
	private void listIncreasingTrend() {
		final ProgressBar progBottom1Data = (ProgressBar) view.findViewById(R.id.prog_bottom1_nodata);
		new ListIncreasingTrendTask(getActivity(), new ListIncreasingTrendTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				progBottom1Data.setVisibility(View.GONE);
				mChartDate = new String[result.size()][2];
				for (int i = 0; i < result.size(); i++) {
					JSONObject obj = (JSONObject) result.get(i);

					String month = obj.get("month").toString();// 月份
					String nbr = obj.get("nbr").toString();// 这个月新增人数
					mChartDate[i][0] = month;

					mChartDate[i][1] = nbr;
				}
				initView(mChartDate, 1);
			}
		}).execute();
	}

	/**
	 * 初始化柱状图
	 * 
	 * @param chartDate
	 *            图形数据
	 * @param flag
	 *            判断是哪个图的数据
	 */
	private void initView(String[][] chartDate, int flag) {

		View viewChart; // 显示柱状图
		if (flag == 1) {
			viewChart = getBarChart("月份", "会员增长", chartDate, getActivity(), Util.NUM_ONE);
			mLyGrowthChart.removeAllViews();
			mLyGrowthChart.addView(viewChart, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		} else if (flag == 0) {
			viewChart = getBarChart("月份", "消费金额", chartDate, getActivity(), Util.NUM_TWO);
			mLyCspChart.removeAllViews();
			mLyCspChart.addView(viewChart, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/**
	 * 绘制柱状图
	 * 
	 * @param XLable
	 *            x轴的标题
	 * @param YLable
	 *            y轴的标题
	 * @param chartDate
	 *            图形形成所需要的数据
	 * @param activity
	 *            当前的Activity
	 * @return 返回一图形
	 */
	public View getBarChart(String XLable, String YLable, String[][] chartDate, Activity activity, int flag) {
		XYSeries Series = new XYSeries(YLable);
		XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
		Dataset.addSeries(Series);
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer yRenderer = new XYSeriesRenderer();
		renderer.addSeriesRenderer(yRenderer);
		/*
		 * Renderer.setApplyBackgroundColor(true); //設定背景顏色
		 * Renderer.setBackgroundColor(Color.BLACK); //設定圖內圍背景顏色
		 */
		renderer.setMarginsColor(Color.WHITE); // 設定圖外圍背景顏色
		renderer.setTextTypeface(null, Typeface.BOLD); // 設定文字style
		renderer.setShowGrid(true); // 設定網格
		renderer.setGridColor(Color.GRAY); // 設定網格顏色
		renderer.setPanLimits(new double[] { 0, 31, 0, 0 });
		renderer.setLabelsColor(Color.BLACK); // 設定標頭文字顏色

		renderer.setAxesColor(Color.BLACK); // 設定雙軸顏色
		renderer.setBarSpacing(3.9); // 設定bar間的距離

		renderer.setXLabelsColor(Color.BLACK); // 設定X軸文字顏色
		renderer.setYLabelsColor(0, Color.BLACK); // 設定Y軸文字顏色

		renderer.setXLabelsAlign(Align.CENTER); // 設定X軸文字置中
		renderer.setYLabelsAlign(Align.LEFT); // 設定Y軸文字置中
		renderer.setXLabelsAngle(-0); // 設定X軸文字傾斜度
		renderer.setXLabels(0); // 設定X軸不顯示數字, 改以程式設定文字
		renderer.setYAxisMin(0); // 設定Y軸文最小值
		// 设置标签文字大小
		renderer.setLabelsTextSize(20);
		// 设置说明文字的大小
		renderer.setLegendTextSize(18);
		renderer.setXAxisMin(0);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(6);// 设置X轴的最大值为5

		yRenderer.setColor(Color.RED); // 設定Series顏色
		yRenderer.setDisplayChartValues(true); // 展現Series數值
		yRenderer.setChartValuesTextAlign(Align.RIGHT);
		Series.add(0, 0);
		renderer.setAxisTitleTextSize(20);
		if (flag == Util.NUM_ONE) {
			renderer.setYTitle("会员每月增长人数/人"); // y轴的说明文字
			renderer.setXTitle("时间/月"); // x轴说明
		} else if (flag == Util.NUM_TWO) {
			renderer.setYTitle("会员卡每月金额消费/元"); // y轴的说明文字
			renderer.setXTitle("时间/月"); // x轴说明
		} else if (flag == Util.NUM_THIRD) {
			renderer.setYTitle("优惠券每批次每月金额消费/元"); // y轴的说明文字
			renderer.setXTitle("批次/批"); // x轴说明
		} else if (flag == Util.NUM_ZERO) {
			renderer.setYTitle("优惠券每批次每月金额消费人数/人"); // y轴的说明文字
			renderer.setXTitle("批次/批"); // x轴说明
		}

		renderer.addXTextLabel(0, "0");
		for (int i = 0; i < chartDate.length; i++) {
			renderer.setYAxisMin(0); // 数轴下限
			renderer.addXTextLabel(i + 1, chartDate[i][0]);
			Log.d("TAG", "chartDate[i][1]=" + chartDate[i][1]);
			String Dates = chartDate[i][1].replace(",", "");
			Log.d("TAG", "chartDate[i][1]=" + Dates);
			Series.add(i + 1, Double.parseDouble(Dates));
		}
		renderer.setChartValuesTextSize(20);
		Series.add(chartDate.length + 1, 0);
		renderer.addXTextLabel(chartDate.length + 1, "");
		
		renderer.setBarWidth(50);
		renderer.setFitLegend(true);
		return ChartFactory.getBarChartView(activity, Dataset, renderer, null);
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
		mCardAdd = DB.getBoolean(ShopConst.Key.CARD_ADD);
		if (mCardAdd) {
			DB.saveBoolean(ShopConst.Key.CARD_ADD, false);
			getGeneralCardStastics();
		}
	}
}
