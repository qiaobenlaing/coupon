package com.huift.hfq.cust.fragment;

import com.huift.hfq.cust.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewEduShopMasterFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewEduShopMasterFragment newInstance(Bundle args) {
		NewEduShopMasterFragment fragment = new NewEduShopMasterFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edu_master, null);
		
		return view;
	}
	
}
