package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.CircleItem;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.ShopSort;
import cn.suanzi.baomi.base.pojo.SubShopSortType;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.CircleLeftAdapter;
import cn.suanzi.baomi.cust.adapter.CircleRightAdapter;
import cn.suanzi.baomi.cust.adapter.FilterAdapter;
import cn.suanzi.baomi.cust.adapter.HomeShopListAdapter;
import cn.suanzi.baomi.cust.adapter.IntelligentAdapter;
import cn.suanzi.baomi.cust.adapter.TypeAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.ListSearchWordsTask;
import cn.suanzi.baomi.cust.model.ShopListTask;
import cn.suanzi.baomi.cust.model.ShopListTask.Callback;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 模糊搜索商店
 * @author qian.zhou , yanfang.li,yingchen
 */
public class ShopFragment extends Fragment implements IXListViewListener, TextWatcher {
	
	private static final String TAG = ShopFragment.class.getSimpleName();
	/** 商店类型*/
	public static final String TYPE = "type";
	/** 首页模板号*/
	public static final String MODULE_VALUE = "moduleValue";
	/** 子模块的Id*/
	public static final String SUB_MODULE_VALUE = "subModulePosition";
	/** 没有模板*/
	public static final String NO_MODULE = "0";
	/** 所有 */
	public static final String TYPE_ALL = "0";
	/** 美食 */
	public static final String TYPE_FOOD = "1";
	/** 丽人 */
	public static final String TYPE_COFFEE = "2";
	/** 健身 */
	public static final String TYPE_EXERCISE = "3";
	/** 娱乐 */
	public static final String TYPE_ENTERTAINMENT = "4";
	/** 服装 */
	public static final String TYPE_CLOTHING = "5";
	/** 其他 */
	public static final String TYPE_OTHER = "6";
	/**列表区域*/
	public FrameLayout mFrameLayout;
	/** 列表 */
	private XListView mLvShop;
	/** 搜索输入框 */
	private AutoCompleteTextView mKeywordEdt;
	/** 适配器 */
	private HomeShopListAdapter mHomeShopListAdapter;
	/** 搜素框 */
	private String mSearchWord, mType;
	/** 页码 */
	private int mPage = 1;
	/** 线程 **/
	private Handler mHandler;
	/** 城市名称 */
	private String mCitysName;
	/** 经度 */
	private String mLongitude;
	/** 纬度 */
	private String mLatitude;
	/** 判断api是否调用成功 下拉 */
	private boolean mFlagUpData = false;
	/** 上拉 */
	private boolean mFlagData = false;
	/** 第一次运行*/
	private boolean mFirstRunFlag = true;
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 模板号*/
	private  String mModuleValue;
	/** 商圈和品牌对应的类型Id*/
	private String mContent;
	/** 智能排序*/
	private String mOrder;
	/** 筛选*/
	private String mFilter;
	/**顶部排序的数据*/
	private ShopSort mShopSort;
	/**商圈*/
	private LinearLayout mCircleLinearLayout;
	/**商圈标题*/
	private TextView mCircleTextView;
	/**行业*/
	private LinearLayout mTypeLinearLayout;
	/**行业标题*/
	private TextView mTypeTextView;
	/**智能*/
	private LinearLayout mIntelligentLinearLayout;
	/**智能排序标题*/
	private TextView mIntelligentTextView;
	/**筛选*/
	private LinearLayout mFilterLinearLayout;
	/**筛选标题*/
	private TextView mFilterTextView;
	/**排序栏*/
	private LinearLayout mSortLinearLayout;
	/**显示的popwindow类型*/
	private int popWindowType = -1;
	/**显示的popWindow*/
	private PopupWindow mPopupWindow;
	/**popwidnow的内容视图*/
	private View mPopWindowView;
	/**商圈箭头*/
	private ImageView mCircleArrow;
	/**行业箭头*/
	private ImageView mTypeArrow;
	/**智能排序箭头*/
	private ImageView mIntelligentArrow;
	/**筛选箭头*/
	private ImageView mFilterArrow;
	/**标记针对一个排序类别是否点击*/
	private boolean mCurrentFlag = false;
	/**标记其他排序标签是否点击过*/
	private int mClickPosition = -1;
	/**商圈右侧数据*/
	private List<CircleItem> mChangeSubList;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ShopFragment newInstance() {
		Bundle args = new Bundle();
		ShopFragment fragment = new ShopFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop, container, false);
		ViewUtils.inject(this, view);
		Util.addActivity(getMyActivity());
		// ****记录买单流程*********
		ActivityUtils.add(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		findView(view);
		init(view);
		getShopList();// 全查询
		getSerachWord();//获取查询关键词
		return view;
	}
	
	/**
	 * 获取查询条件关键词   商圈 行业 智能排序 筛选
	 */
	private void getSerachWord() {
		new ListSearchWordsTask(getActivity(), new ListSearchWordsTask.Callback() {
		
			@Override
			public void getResult(JSONObject result) {
				if(null!=result){
					Log.d(TAG, "ListSearchWordsTask====="+result.toString());
					try {
						mShopSort = Util.json2Obj(result.toString(),ShopSort.class);
						Log.d(TAG, "转化正常----");
					} catch (Exception e) {
						Log.d(TAG, "转化异常----");
					}
					onClickTopSort();
					setCircleFromHomePage();
				}else{
					Util.getContentValidate(R.string.no_sort_shop);
				}
			}
	
		}).execute(mCitysName);
	}
	
	/**
	 * 根据首页传过来的modulevalue与content确定 商圈显示
	 */
	public void setCircleFromHomePage(){
		Log.d(TAG, "mModuleValue=="+mModuleValue+",mContent=="+mContent);
		if("0".equals(mModuleValue)||Util.isEmpty(mModuleValue)){
			mCircleTextView.setText("区域");
			mCircleTextView.setTextColor(Color.BLACK);
		}else if("-2".equals(mModuleValue)&&!Util.isEmpty(mContent)){  //此时mCotent为   **区 就是显示内容
			mCircleTextView.setText(mContent);
			mCircleTextView.setTextColor(Color.RED);
		}else if("-1".equals(mModuleValue)){ //此时根据  附近 ---距离来搜索商圈  content = 200
			mCircleTextView.setText(mContent+"米");
			mCircleTextView.setTextColor(Color.RED);
		}else if("3".equals(mModuleValue)){ //此时根据 热门商圈---商圈名称 查询
			List<SubShopSortType> list = mShopSort.getCircle().getList();
			
			Flag1:
			for(SubShopSortType sortType:list){
				String moduleValue = sortType.getModuleValue();
				if("3".equals(moduleValue)){
					List<CircleItem> subList = sortType.getSubList();
					if(null ==subList || subList.size()==0 ){
						return;
					}
					for(CircleItem item : subList){
						if(item.getValue().equals(mContent)){
							mCircleTextView.setText(item.getName());
							mCircleTextView.setTextColor(Color.RED);
							break Flag1;//跳出外层循环
						}
					}
				}
			}
		}
	
	}
	
	/**
	 * 点击顶部的排序  商圈  行业 智能排序 筛选
	 */
	public void onClickTopSort(){
		mCircleLinearLayout.setOnClickListener(mListener);
		mTypeLinearLayout.setOnClickListener(mListener);
		mIntelligentLinearLayout.setOnClickListener(mListener);
		mFilterLinearLayout.setOnClickListener(mListener);
	}

	/**
	 * 点击 商圈---0     行业---1   智能排序---2     筛选的监听---3   
	 */
	private OnClickListener  mListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_shop_circle: //商圈
				if (mClickPosition != 0) {
					setGrayArrow(false, true, true, true);
					mCurrentFlag = false;
				}else{
					//TODO
				}
				mCurrentFlag = !mCurrentFlag;
				
				mCircleArrow.setBackgroundResource(R.drawable.up_red);
				
				mClickPosition = 0;
				showPopWindowBefore(0);
				
				break;
				
			case R.id.ll_shop_industry: //行业
				if(mClickPosition !=1){
					setGrayArrow(true, false, true, true);
					mCurrentFlag = false;
				}else{
					//TODO
				}
				mCurrentFlag = !mCurrentFlag;
				
				mTypeArrow.setBackgroundResource(R.drawable.up_red);
				
				mClickPosition=1;
				
				showPopWindowBefore(1);
				
				break; 
				
			case R.id.ll_shop_intelligent: //智能
				if(mClickPosition!=2){
					setGrayArrow(true, true, false, true);
					mCurrentFlag = false;
				}else{
					//TODO
				}
				mCurrentFlag = !mCurrentFlag;
				
				
				mIntelligentArrow.setBackgroundResource(R.drawable.up_red);
				
				
				mClickPosition=2;
				
				showPopWindowBefore(2);
				
				break;
				
			case R.id.ll_shop_filter: //筛选
				if(mClickPosition!=3){
					setGrayArrow(true, true, true, false);
					mCurrentFlag = false;
				}else{
					//TODO
				}
				mCurrentFlag = !mCurrentFlag;
				/*if(mCurrentFlag){*/
				mFilterArrow.setBackgroundResource(R.drawable.up_red);
				/*}else{
					mFilterArrow.setBackgroundResource(R.drawable.down_gray);
				}*/
				mClickPosition=3;
				showPopWindowBefore(3);
				break;
			default:
				break;
			}
		}
	}  ;
	
	
	/**
	 * 点击顶部不同的排序类别  弹出不同的popWindow的前置处理(置空popwindow与contentview);
	 * @param type 0---商圈
	 * 		  	   1---行业	
	 * 		       2---智能排序
	 * 			   3---筛选
	 */
	private void showPopWindowBefore(int type) {
		if(mPopupWindow!=null){
			//每次点击都需要重新置空
			mPopupWindow.dismiss();
			mPopupWindow = null;
			mPopWindowView = null;
			/*//popwindow消失的时候  屏幕变亮
			setBackScreenBackgroud(false);*/
			//判断是否点击的是同一个类别   如果不同显示另一个类别    
			if(popWindowType != type){
				showPopWindow(type);
			}
			
		}else /*if(mPopupWindow==null)*/{
			showPopWindow(type);
		}
	}
	
	
	/**
	 * 置灰其他标签监听  
	 * @param downCircleArrow ---商圈箭头
	 * @param downTypeArrow ---行业箭头
	 * @param downIntelligentArrow ---智能排序箭头
	 * @param downFilterArrow ---筛选箭头
	 */
	protected void setGrayArrow(boolean downCircleArrow, boolean downTypeArrow, boolean downIntelligentArrow, boolean downFilterArrow) {
		if(downCircleArrow){
			mCircleArrow.setBackgroundResource(R.drawable.down_gray);
		}

		if(downTypeArrow){
			mTypeArrow.setBackgroundResource(R.drawable.down_gray);
		}
		
		if(downIntelligentArrow){
			mIntelligentArrow.setBackgroundResource(R.drawable.down_gray);
		}
		
		if(downFilterArrow){
			mFilterArrow.setBackgroundResource(R.drawable.down_gray);
		}
	}

	/**
	 * 显示不同类型的popwindow; 
	 * @param type 0---商圈 
	 * 			   1---行业
	 * 			   2---智能
	 * 		       3---筛选
	 * 
	 */
	protected void showPopWindow(int type) {
		
		popWindowType = type;
		switch (type) {
		case 0: //商圈
			circlePopWindow();
			break;
		case 1: //行业
			typePopWindow();
			break;
		case 2: //智能
			intelligentPopwindow();
			break;
		case 3: //筛选
			filterPopwindow();
			break;

		default:
			break;
		}
		/*setBackScreenBackgroud(true);*/
	}
	
	/**
	 * 显示筛选的popwindow
	 */
	private void filterPopwindow() {
		mPopWindowView = View.inflate(getActivity(), R.layout.popwindow_1listview, null);
		
		mPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setPopWindow();
		
		ListView lv = (ListView) mPopWindowView.findViewById(R.id.lv_1);
		
		//获取智能排序的数据
		final List<SubShopSortType> filterData = mShopSort.getFilter().getList();
		if(null == filterData||filterData.size()==0){
			return;
		}
		final FilterAdapter adapter = new FilterAdapter(filterData, getActivity());
		lv.setAdapter(adapter);

		onListViewItemClick(lv, filterData, adapter);
		
		mPopupWindow.showAsDropDown(mSortLinearLayout);
	}
	
	/**
	 * 显示智能排序的popwindow
	 */
	private void intelligentPopwindow() {
		mPopWindowView = View.inflate(getActivity(), R.layout.popwindow_1listview, null);
		
		mPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setPopWindow();
		
		ListView lv = (ListView) mPopWindowView.findViewById(R.id.lv_1);
		
		//获取智能排序的数据
		final List<SubShopSortType> intelligentData = mShopSort.getIntelligentSorting().getList();
		
		if(null == intelligentData||intelligentData.size()==0){
			return;
		}
		final IntelligentAdapter adapter = new IntelligentAdapter(intelligentData, getActivity());
		lv.setAdapter(adapter);
		
		onListViewItemClick(lv, intelligentData, adapter);
		
		mPopupWindow.showAsDropDown(mSortLinearLayout);
	}
	/**
	 * 显示行业的popwindow
	 */
	private void typePopWindow() {
		mPopWindowView = View.inflate(getActivity(), R.layout.popwindow_1listview, null);
	
		mPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setPopWindow();
		
		ListView lv = (ListView) mPopWindowView.findViewById(R.id.lv_1);
		
		//获取行业的数据
		final List<SubShopSortType> fileterData = mShopSort.getType().getList();
		if(null == fileterData||fileterData.size()==0){
			return;
		}
		final TypeAdapter adapter = new TypeAdapter(fileterData, getActivity());
		lv.setAdapter(adapter);
		
		onListViewItemClick(lv, fileterData, adapter);
		
		mPopupWindow.showAsDropDown(mSortLinearLayout);
	}

	/**
	 * 行业 智能排序 筛选的listview的itemclick点击
	 */
	private void onListViewItemClick(ListView lv, final List<SubShopSortType> data, final BaseAdapter adapter) {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				for(int i=0;i<data.size();i++){
					data.get(i).setCheck(i == position);
				}
				adapter.notifyDataSetChanged();
				int sortType = -1;
				if(adapter instanceof TypeAdapter){
					sortType = 1;
					if(position==0){ //置空行业查询条件
						mType = "0";
						mTypeTextView.setText(getResources().getString(R.string.type));
						mTypeTextView.setTextColor(Color.BLACK);
					}else{
						mTypeTextView.setText(data.get(position).getQueryName());
						mTypeTextView.setTextColor(Color.RED);
					}
				}else if(adapter instanceof IntelligentAdapter){
					sortType = 2;
					if(position==0){ //置空智能排序的查询条件
						mOrder = "0";
						mIntelligentTextView.setText(getResources().getString(R.string.order));
						mIntelligentTextView.setTextColor(Color.BLACK);
					}else{
						mIntelligentTextView.setText(data.get(position).getQueryName());
						mIntelligentTextView.setTextColor(Color.RED);
					}
				}else if(adapter instanceof FilterAdapter){
					if(position==0){ //置空筛选的查询条件
						mFilter = "0";
						mFilterTextView.setText(getResources().getString(R.string.filter));
						mFilterTextView.setTextColor(Color.BLACK);
					}else{
						mFilterTextView.setText(data.get(position).getQueryName());
						mFilterTextView.setTextColor(Color.RED);
					}
					sortType = 3;
				}
				
				Log.d(TAG, "sortType==="+sortType);
				toSearch(sortType,data.get(position).getValue());
			}
		});
	}
	
	/**
	 * 显示商圈的popwindow
	 */
	private void circlePopWindow() {
		mPopWindowView = View.inflate(getActivity(), R.layout.popwindow_2listview, null);
	
		mPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setPopWindow();
		
		ListView lv1 = (ListView) mPopWindowView.findViewById(R.id.lv_1);
		ListView lv2 = (ListView) mPopWindowView.findViewById(R.id.lv_2);
		
		//获取到商圈的数据
		final List<SubShopSortType> list = mShopSort.getCircle().getList();
		
		//左侧列表
		final CircleLeftAdapter adapter1 = new CircleLeftAdapter(list, getActivity());
		lv1.setAdapter(adapter1);
		adapter1.updataRedIndex(0);
		
		//右侧默认显示一个空的集合数据
		/*//final List<CircleItem> subList = list.get(1).getSubList();
		mChangeSubList = list.get(1).getSubList();*/
		//mChangeSubList = new ArrayList<CircleItem>();
		mChangeSubList = list.get(0).getSubList();
		if(null==mChangeSubList){
			mChangeSubList = new ArrayList<CircleItem>();
		}
		final CircleRightAdapter adapter2 = new CircleRightAdapter(mChangeSubList, getActivity());
		lv2.setAdapter(adapter2);
	
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(position==0){ 
					mCircleTextView.setText("区域");
					mCircleTextView.setTextColor(Color.BLACK);
					//置空商圈查询条件
					toSearch(String.valueOf(Util.NUM_ZERO),Util.NUM_ZERO,String.valueOf(Util.NUM_ZERO));
				}else{
					if(list.get(position).getSubList()!=null){
						mChangeSubList = list.get(position).getSubList();
						adapter2.updataData(mChangeSubList);
						adapter1.updataRedIndex(position);
					}else{  //点击左侧列表的选项去查询
						mCircleTextView.setTextColor(Color.RED);
						mCircleTextView.setText(list.get(position).getQueryName());
						toSearch(list.get(position).getModuleValue(),0,list.get(position).getQueryName());
					}
					
				}
				
			}
		});
		
		//点击右侧列表的选项去查询
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mCircleTextView.setTextColor(Color.RED);
				mCircleTextView.setText(mChangeSubList.get(position).getName());
				adapter2.updataRedIndex(position);
				toSearch(mChangeSubList.get(position).getModuleValue(),0,mChangeSubList.get(position).getValue());
			}
		});
			
		mPopupWindow.showAsDropDown(mSortLinearLayout);
	}
	
	
	private void setPopWindow(){
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setGrayArrow(true, true, true, true);
				mPopupWindow = null;
				mPopWindowView = null;
			}
		});
	}
	
	
	/**
	 * 按照条件去查询  更新商户列表(多项查询)
	 * sortType 
	 * 	0---商圈  		查询条件  moduleValue + content
	 * 	1---行业  		查询条件  content
	 * 	2---智能排序	查询条件  content
	 *  3---筛选		查询条件  content
	 * @param describle
	 */
	private void toSearch(String moduleValue,int sortType,String content){
		mPopupWindow.dismiss();
		mPopupWindow=null;
		mPopWindowView = null;
		//置灰所有的按钮
		setGrayArrow(true, true, true, true);
		mCurrentFlag = false;
		mClickPosition = -1;
		mSearchWord = "";
		mPage = 1;
		switch (sortType) { 
		case 0://商圈排序请求API
		/*	mType = "0";
			mOrder = "0";
			mFilter = "0";*/
			mModuleValue = moduleValue;
			mContent = content;
			//getShopList();
			break;
		case 1: //行业排序请求API
			mType = content;
			/*mOrder = "0";
			mFilter = "0";
			mModuleValue = "0";
			mContent = "0";*/
			//getShopList();
			break;
		case 2://智能排序请求API
			/*mType = "0";*/
			mOrder = content;
			/*mFilter = "0";
			mModuleValue = "0";
			mContent = "0";*/
			//getShopList();
			break;
		case 3:
			/*mType = "0";
			mOrder = "0";*/
			mFilter = content;
			/*mModuleValue = "0";
			mContent = "0";*/
			//getShopList();
			break;
		
		default:
			break;
				
		}
		
		Log.d(TAG, "^^^^^^moduleValue==="+mModuleValue);
		Log.d(TAG, "^^^^^^content==="+mContent);
		Log.d(TAG, "^^^^^^type==="+mType);
		Log.d(TAG, "^^^^^^order==="+mOrder);
		Log.d(TAG, "^^^^^^filter==="+mFilter);
		
		getShopList();
	}
	
	private void toSearch(int sortType,String content){
		toSearch("", sortType, content);
	}
	/**
	 * 获取控件
	 * @param v 视图
	 */
	private void findView(View v) {
		// 设置返回图标
		TextView tvBack = (TextView) v.findViewById(R.id.tv_area);
		tvBack.setBackgroundResource(R.drawable.backup);
		mFrameLayout = (FrameLayout) v.findViewById(R.id.fl_shop);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mLvShop = (XListView) v.findViewById(R.id.listView);
		mKeywordEdt = (AutoCompleteTextView) v.findViewById(R.id.edt_search);
		
		mCircleLinearLayout = (LinearLayout) v.findViewById(R.id.ll_shop_circle);
		mTypeLinearLayout = (LinearLayout) v.findViewById(R.id.ll_shop_industry);
		mIntelligentLinearLayout = (LinearLayout) v.findViewById(R.id.ll_shop_intelligent);
		mFilterLinearLayout = (LinearLayout) v.findViewById(R.id.ll_shop_filter);
		mSortLinearLayout = (LinearLayout) v.findViewById(R.id.ll_sort);
		
		mCircleArrow = (ImageView) v.findViewById(R.id.icon_shop_circle);
		mTypeArrow = (ImageView) v.findViewById(R.id.icon_shop_industry);
		mIntelligentArrow = (ImageView) v.findViewById(R.id.icon_shop_intelligent);
		mFilterArrow = (ImageView) v.findViewById(R.id.icon_shop_filter);
		
		mCircleTextView = (TextView) v.findViewById(R.id.tv_shop_circle);
		mTypeTextView = (TextView) v.findViewById(R.id.tv_shop_industry);
		mIntelligentTextView = (TextView) v.findViewById(R.id.tv_shop_intelligent);
		mFilterTextView = (TextView) v.findViewById(R.id.tv_shop_filter);
	}
	
	/**
	 * 初始化数据
	 * @param v 视图
	 */
	private void init(View v) {
		mFlagData = true;
		mFlagUpData = true;
		mOrder = "0"; // 默认值
		mFilter = "0"; // 默认值
		// 获取值
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d(TAG, "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			mCitysName = "";
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
			mCitysName = cityName;
		}
		// 获取商店类型
		if (Util.isEmpty(getMyActivity().getIntent().getStringExtra(TYPE))) {
			mType = "0";
		} else {
			mType = StringUtils.stripToEmpty(getMyActivity().getIntent().getStringExtra(TYPE));
		}
		
		// 首页模板号
		int moduleValue = 0;
		if (!Util.isEmpty(getMyActivity().getIntent().getStringExtra(MODULE_VALUE))) {
			moduleValue = Integer.parseInt(getMyActivity().getIntent().getStringExtra(MODULE_VALUE));
			Log.d(TAG, "mModuleValue :" + moduleValue + ", mContent: " + mContent);
			// 首页模板号
			if (Util.isEmpty(mType) || NO_MODULE.equals(mType) 
					|| (CustConst.Home.SHOPCIRCLE_VALUE != moduleValue && CustConst.Home.SHOPBRAND_VALUE != moduleValue)) {
				mModuleValue = NO_MODULE;
				mContent = "";
			} else {
				mModuleValue = moduleValue+"";
				mContent = getMyActivity().getIntent().getStringExtra(TYPE);
				mType = "0";
				Log.d(TAG, "mModuleValue :" + mModuleValue + ", mContent: " + mContent);
			}
		} else {
			mModuleValue = NO_MODULE;
			mContent = "";
		}
		mKeywordEdt.findFocus();
		mKeywordEdt.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 友盟统计
					MobclickAgent.onEvent(getMyActivity(), "shop_search_shop");
					mPage = 1;
					getShopList();
				}
				return false;
			}
		});
	
		//从首页进入  行业标签显示对应的类别
		if(!TYPE_ALL.equals(mType)){
			mTypeTextView.setTextColor(Color.RED);
		}else{
			mTypeTextView.setTextColor(Color.BLACK);
		}
		
		if(TYPE_FOOD.equals(mType)){
			mTypeTextView.setText("美食");
		}else if(TYPE_COFFEE.equals(mType)){
			mTypeTextView.setText("丽人");
		}else if(TYPE_EXERCISE.equals(mType)){
			mTypeTextView.setText("健身");
		}else if(TYPE_ENTERTAINMENT.equals(mType)){
			mTypeTextView.setText("娱乐");
		}else if(TYPE_OTHER.equals(mType)){
			mTypeTextView.setText("其他");
		}
		
		// 上拉刷新
		mLvShop.setPullLoadEnable(true);
		mLvShop.setXListViewListener(this);
		mHandler = new Handler();
		mKeywordEdt.addTextChangedListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}

	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {

			if (mFlagUpData) {
				mFlagUpData = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						getShopList();
					}
				}, 5000);
			}
		}
	};

	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 列表
	 */
	public void getShopList() {
		mSearchWord = mKeywordEdt.getText().toString();
		String params[] = {mSearchWord, mType, mLongitude, mLatitude, mCitysName, String.valueOf(mPage),mModuleValue,mContent,mOrder,mFilter};
		if (mFirstRunFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		// 获得一个用户信息对象
		new ShopListTask(getMyActivity(), new Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFirstRunFlag = false;
				mFlagData = true;
				mFlagUpData = true;
				mLvShop.stopLoadMore();
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (result == null) {
					mLvShop.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
				} else {
					Log.d(TAG, "result shopInfo >>> " + result.toString());
					ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvShop.setPullLoadEnable(true);
					PageData page = new PageData(result, "shopList", new TypeToken<List<Shop>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvShop.setPullLoadEnable(false);
					} else {
						mLvShop.setPullLoadEnable(true);
					}
					List<Shop> list = (List<Shop>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					}

					if (mHomeShopListAdapter == null) {
						mHomeShopListAdapter = new HomeShopListAdapter(getMyActivity(), list);
						mLvShop.setAdapter(mHomeShopListAdapter);
					} else {
						if (page.getPage() == 1) {
							mHomeShopListAdapter.setItems(list);
						} else {
							mHomeShopListAdapter.addItems(list);
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
						Log.d(TAG, "Home >>> mPage ," + mPage);
					} catch (Exception e) {
						Log.e(TAG, "mPage >>> error ," + e.getMessage());
					}
				}

			}
		}).execute(params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 点击返回查看到活动列表
	 */
	@OnClick(R.id.tv_area)
	public void tvBackClick(View view) {
		getMyActivity().finish();
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ShopFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ShopFragment.class.getSimpleName()); // 统计页面
		mPage = 1;
		getShopList();
	}

	@Override
	public void afterTextChanged(Editable s) {
		mType = "0"; // 所有类型
		mPage = 1;
		Log.d(TAG, "输入  >>>>>>>>>>>>" + mKeywordEdt.getText().toString());
		getShopList();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	/**
	 * 上拉
	 */
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getShopList();
				}
			}, 2000);
		}
	}
}