// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.GetShopDayBrowseQuantityTask;
import com.huift.hfq.shop.model.GetShopTodayBrowseQuantityTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * 用户统计
 *
 * @author qian.zhou
 */
public class UserAccessFragment extends Fragment {
    private LinearLayout mLyUserAccessChart;
    /**
     * 浏览量显示的数据
     **/
    private String[][] mChartDate;
    /**
     * 浏览总量
     */
    private TextView mtvUserAccessCount;
    /**
     * 今日浏览量
     */
    private TextView mToDayAccessCount;
    /**
     * 获取的日期
     */
    private String mDay;

    /**
     * 需要传递参数时有利于解耦
     *
     * @return PosPayFragment
     */
    public static UserAccessFragment newInstance() {
        Bundle args = new Bundle();
        UserAccessFragment fragment = new UserAccessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_access, container, false);
        ViewUtils.inject(this, v);
        Util.addLoginActivity(getActivity());
        init(v);
        return v;
    }

    /**
     * 初始化数据
     */
    private void init(View v) {
        //标题
        TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
        tvContent.setText(R.string.user_access);
        //取值
        String accessCount = DB.getStr(ShopConst.Access.USER_ACCESS_COUNT);
        mLyUserAccessChart = (LinearLayout) v.findViewById(R.id.ly_user_access);
        mtvUserAccessCount = (TextView) v.findViewById(R.id.tv_access_count);
        mToDayAccessCount = (TextView) v.findViewById(R.id.tv_today_access);
        if (!Util.isEmpty(accessCount)) {
            mtvUserAccessCount.setText(accessCount);
        }
        //获取商户近一个星期的浏览情况
        getDayAccessCount();
        //获取当前日期的浏览总量
        getToDayAccessCount();
    }


    /**
     * 获得商户近一个星期的浏览情况
     */
    public void getDayAccessCount() {
        new GetShopDayBrowseQuantityTask(getActivity(), new GetShopDayBrowseQuantityTask.Callback() {
            @Override
            public void getResult(JSONArray result) {
                mChartDate = new String[result.size()][2];
                for (int i = 0; i < result.size(); i++) {
                    JSONObject Consume = (JSONObject) result.get(i);
                    mDay = Consume.get("day").toString();// 月份
                    //截取月份
                    String reday = mDay.substring(5, mDay.length());
                    String count = Consume.get("count").toString();// 这个月新增人数
                    mChartDate[i][0] = reday;
                    mChartDate[i][1] = count;
                }
                // 初始化柱状图
                initView(mChartDate);
            }
        }).execute();
    }

    /**
     * 获取当前日期的浏览总量
     */
    public void getToDayAccessCount() {
        new GetShopTodayBrowseQuantityTask(getActivity(), new GetShopTodayBrowseQuantityTask.Callback() {
            @Override
            public void getResult(String result) {
                if (!Util.isEmpty(result)) {
                    mToDayAccessCount.setText(result);
                }
            }
        }).execute();
    }


    /**
     * 初始化柱状图
     *
     * @param chartDate 图形数据
     * @param flag      判断是哪个图的数据
     */
    private void initView(String[][] chartDate) {
        View viewChart; // 显示折线图
        viewChart = getBarChart("月份", "浏览量增长", chartDate, getActivity());
        mLyUserAccessChart.removeAllViews();
        mLyUserAccessChart.addView(viewChart, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 绘制柱状图
     *
     * @param XLable    x轴的标题
     * @param YLable    y轴的标题
     * @param chartDate 图形形成所需要的数据
     * @param activity  当前的Activity
     * @return 返回一图形
     */
    public View getBarChart(String XLable, String YLable, String[][] chartDate, Activity activity) {
        XYSeries Series = new XYSeries(YLable);
        XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
        Dataset.addSeries(Series);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer yRenderer = new XYSeriesRenderer();
        renderer.addSeriesRenderer(yRenderer);
        renderer.setMarginsColor(Color.WHITE); // 設定圖外圍背景顏色
        renderer.setTextTypeface(null, Typeface.BOLD); // 設定文字style
        renderer.setShowGrid(true); // 設定網格
        renderer.setGridColor(Color.TRANSPARENT); // 設定網格顏色
        renderer.setPanLimits(new double[]{0, 31, 0, 0});
        renderer.setLabelsColor(Color.BLACK); // 設定標頭文字顏色
        renderer.setClickEnabled(true);////是否可移动折线，true:折线是固定不能移动的；false：折线可以移动；
        renderer.setXLabels(7);//设置x轴上的标签数量，最大值根据所给坐标而定
        renderer.setDisplayChartValues(true);//是否显示图标上的数据
        renderer.setMargins(new int[]{30, 30, 30, 30});//设置外边距
        renderer.setAxesColor(Color.BLACK); // 設定雙軸顏色
        renderer.setBarSpacing(1.0); // 設定bar間的距離
        renderer.setPointSize(5f);
        renderer.setPanEnabled(true, true);

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
        renderer.setXAxisMax(8);// 设置X轴的最大值为6

        yRenderer.setColor(Color.RED); // 設定Series顏色
        yRenderer.setLineWidth(3);
        yRenderer.setPointStyle(PointStyle.CIRCLE);//设置点的样式
        yRenderer.setChartValuesSpacing(10);//显示的点的值与图的距离
        yRenderer.setChartValuesTextSize(25);//点的值的文字大小
        yRenderer.setFillPoints(false);//填充点（显示的点是空心还是实心）
        yRenderer.setDisplayChartValues(true); // 展現Series數值
        yRenderer.setDisplayChartValuesDistance(30);
        yRenderer.setChartValuesTextAlign(Align.RIGHT);
        yRenderer.setFillBelowLine(true);//是否填充折线图的下方
        yRenderer.setFillBelowLineColor(0xffFFB6C1);//填充的颜色，如果不设置就默认与线的颜色一致
        Series.add(0, 0);
        renderer.setAxisTitleTextSize(20);
        renderer.setYTitle("每天浏览/人"); // y轴的说明文字
        renderer.setXTitle("时间/日期"); // x轴说明

        renderer.addXTextLabel(0, "0");
        int i = 0;
        double maxValue = 0.0;
        for (; i < chartDate.length; i++) {
            renderer.setYAxisMin(0); // 数轴下限
            renderer.addXTextLabel(i + 1, chartDate[i][0]);
            String Dates = chartDate[i][1].replace(",", "");
            Double v = Double.parseDouble(Dates);
            if (v > maxValue) {
                maxValue = v;
            }
            Series.add(i + 1, v);
        }
        renderer.setChartValuesTextSize(20);
        Series.add(chartDate.length + 1, 0);
        renderer.addXTextLabel(chartDate.length + 1, "");
        Series.add(i + 1, maxValue * 1.8);
        renderer.setBarWidth(50);
        renderer.setFitLegend(true);
        return ChartFactory.getLineChartView(activity, Dataset, renderer);
    }

    /**
     * 点击返回
     **/
    @OnClick(R.id.layout_turn_in)
    private void ivuppBackClick(View v) {
        getActivity().finish();
    }
}
