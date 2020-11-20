package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ShopPayBillFragment;

public class ShopPayBillActivity extends SingleFragmentActivity{

	private static final String TAG = ShopPayBillActivity.class.getSimpleName();
	private ShopPayBillFragment mFragment; 
	@Override
	protected Fragment createFragment() {
		mFragment = ShopPayBillFragment.newInstance();
		return mFragment;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("TAG", "aaaaaaaaaaaaaaaaaaaa");
		if (mFragment != null) {
			mFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
}
