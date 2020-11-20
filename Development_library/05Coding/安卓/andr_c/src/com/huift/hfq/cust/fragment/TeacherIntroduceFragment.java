package com.huift.hfq.cust.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TeacherIntroduceFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static TeacherIntroduceFragment newInstance() {
		Bundle args = new Bundle();
		TeacherIntroduceFragment fragment = new TeacherIntroduceFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
