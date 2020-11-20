package cn.suanzi.baomi.cust.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XGridView;
import cn.suanzi.baomi.base.view.XGridView.IXGridViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyPhotoAlbumActivity;
import cn.suanzi.baomi.cust.activity.ProductPhotoActivity;
import cn.suanzi.baomi.cust.adapter.MyPhotoProductsAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.cGetShopProductAlbumTask;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 商家相册 (产品)
 * @author yanfang.li
 */
public class MyPhotoProductFragment extends Fragment implements IXGridViewListener{
	private static final String TAG = MyPhotoProductFragment.class.getSimpleName();
	
	private XGridView mGvProduct;
	/** 相册列表适配器*/
	private MyPhotoProductsAdapter mPhotoProductAdapter;
	private int mPage = 1;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 上拉加载的线程*/
	private Handler mHandler;
	/** 上拉加载数据加载接收记载*/
	private boolean mDataFlag = false;
	
	public static MyPhotoProductFragment newInstance() {
		Bundle args = new Bundle();
		MyPhotoProductFragment fragment = new MyPhotoProductFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_product, container, false);
		init(view);
		Util.addLoginActivity(getMyActivity());
		return view;
	}

	/**
	 * 初始化方法
	 * @param v
	 */
	private void init(View v) {
		mDataFlag = true;
		mGvProduct = (XGridView) v.findViewById(R.id.gv_photo);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mGvProduct.setPullLoadEnable(true);
		mGvProduct.setXGridViewListener(this);
		cGetShopProductAlbum(); //调用产品子相册的列表
	}
	
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
	 * 查询产品子相册列表
	 */
	public void cGetShopProductAlbum(){
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		String shopCode = getMyActivity().getIntent().getStringExtra(MyPhotoAlbumActivity.SHOP_CODE);
		new cGetShopProductAlbumTask(getMyActivity(), new cGetShopProductAlbumTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				if (result == null) {
					ViewSolveUtils.morePageOne(mGvProduct, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 正在加载
					mGvProduct.setPullLoadEnable(true);
					PageData page = new PageData(result, "subAlbumList", new TypeToken<List<ShopDecoration>>(){}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mGvProduct.setPullLoadEnable(false);
					} else {
						mGvProduct.setPullLoadEnable(true);
					}
					List<ShopDecoration> list = (List<ShopDecoration>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mGvProduct, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mGvProduct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 正在加载
					}
					if (mPhotoProductAdapter == null) {
						mPhotoProductAdapter = new MyPhotoProductsAdapter(getMyActivity(), list);
						mGvProduct.setAdapter(mPhotoProductAdapter);
					} else {
						if (page.getPage() == 1) {
							mPhotoProductAdapter.setItems(list);
						} else {
							mPhotoProductAdapter.addItems(list);
						}
					}
					mGvProduct.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
							ShopDecoration shopDecoration = (ShopDecoration) mGvProduct.getItemAtPosition(position);
							Intent intent = new Intent(getMyActivity(), ProductPhotoActivity.class);
							intent.putExtra(ProductPhotoActivity.DECORATION, shopDecoration);
							getMyActivity().startActivity(intent);
						}
					});
				}
			}
		}).execute(shopCode,mPage+"");
	}
	
	@Override
	public void onLoadMore() {
		if (mDataFlag) {
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					cGetShopProductAlbum();
					mGvProduct.stopLoadMore();
				}
			}, 2000);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyPhotoProductFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyPhotoProductFragment.class.getSimpleName()); //统计页面
	}
}
