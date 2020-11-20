package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.NewShopVisitAdapter;

public class NewShopRecentVisitFragment extends Fragment {
	
	private static final String TAG = NewShopRecentVisitFragment.class.getSimpleName();
	/**用户列表*/
	private ListView mUserListView;
	private TextView mTitleTextView;
	private ImageView mBackTextView;
	
	public static NewShopRecentVisitFragment newInstance() {
		Bundle args = new Bundle();
		NewShopRecentVisitFragment fragment = new NewShopRecentVisitFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_newshop_recent_visit, null);
		mUserListView = (ListView) view.findViewById(R.id.lv_recent_visit);
		mTitleTextView = (TextView) view.findViewById(R.id.tv_mid_content);
		mTitleTextView.setText("最近访客");
		mBackTextView = (ImageView) view.findViewById(R.id.iv_turn_in);
		mBackTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		List<User> datas = (ArrayList<User>) getActivity().getIntent().getSerializableExtra("recentVisit");
		
		//显示用户列表
		if(datas != null && datas.size()>0){
			Log.d(TAG, "最近访问用户数量==="+datas.size());
			mUserListView.setAdapter(new NewShopVisitAdapter(getActivity(), datas));
		}
		return view;
	}

}
