package cn.suanzi.baomi.cust.fragment;

import cn.suanzi.baomi.cust.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewEduShopHoronurFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewEduShopHoronurFragment newInstance(Bundle args) {
		NewEduShopHoronurFragment fragment = new NewEduShopHoronurFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edu_horounr, null);
		
		return view;
	}
}
