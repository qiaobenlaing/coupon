package cn.suanzi.baomi.cust.fragment;

import cn.suanzi.baomi.cust.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 教育行业商户的课程表
 * @author yingchen
 *
 */
public class NewEduShopSheduleFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewEduShopSheduleFragment newInstance(Bundle args) {
		NewEduShopSheduleFragment fragment = new NewEduShopSheduleFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edu_shedule, null);
		return view;
	}
}
