package cn.suanzi.baomi.cust.fragment;

import java.util.List;

import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.NewShopInfoData;
import cn.suanzi.baomi.base.pojo.NewShopTime;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.Teacher;
import cn.suanzi.baomi.cust.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 教育行业的店铺首页
 * @author yingchen
 *
 */
public class NewEduShopHomeFragment extends Fragment implements OnClickListener{

	private NewShopInfoData shopInfoData;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewEduShopHomeFragment newInstance(Bundle args) {
		NewEduShopHomeFragment fragment = new NewEduShopHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edu_shop_home, null);
		shopInfoData = (NewShopInfoData) getArguments().getSerializable("shoInfoData");
		
		showView(view);
		return view;
	}

	/**
	 * 显示视图
	 * @param view
	 */
	private void showView(View view) {
		//营业时间
		TextView time = (TextView) view.findViewById(R.id.tv_shop_time);
		List<NewShopTime> businessHours = shopInfoData.getShopInfo().getBusinessHours();
		if(null != businessHours && businessHours.size()>0){
			StringBuffer buffer = new StringBuffer();
			for (NewShopTime shopTime:businessHours) {
				buffer.append(shopTime.getOpen()+"-"+shopTime.getClose()+" ");
			}
			time.setText(buffer.toString());
		}else{
			time.setText("暂无营业时间");
		}
		
		//人气
		TextView populartiy = (TextView) view.findViewById(R.id.tv_shop_popularity);
		populartiy.setText("人气:"+shopInfoData.getShopInfo().getPopularity());
		
		//地址
		TextView address = (TextView) view.findViewById(R.id.address);
		Shop shopInfo = shopInfoData.getShopInfo();
		StringBuffer buffer = new StringBuffer();
		if(!Util.isEmpty(shopInfo.getCity())){
			buffer.append(shopInfo.getCity());
		}
		if(!Util.isEmpty(shopInfo.getDistrict())){
			buffer.append(shopInfo.getDistrict());
		}
		if(!Util.isEmpty(shopInfo.getStreet())){
			buffer.append(shopInfo.getStreet());
		}
		address.setText(buffer.toString());
		
		//最近访问
		LinearLayout recentVisit = (LinearLayout) view.findViewById(R.id.ll_recentvisit);
		recentVisit.setOnClickListener(this);
		
		ImageView visit1 = (ImageView) view.findViewById(R.id.visit_user1);
		ImageView visit2 = (ImageView) view.findViewById(R.id.visit_user2);
		ImageView visit3 = (ImageView) view.findViewById(R.id.visit_user3);
		//如果访问人数小于3
		if(shopInfoData.getRecentVisitor().size()==0){
			visit1.setVisibility(View.GONE);
			visit2.setVisibility(View.GONE);
			visit3.setVisibility(View.GONE);
		}else if(shopInfoData.getRecentVisitor().size()==1){
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(0).getAvatarUrl(), visit1);
			visit2.setVisibility(View.GONE);
			visit3.setVisibility(View.GONE);
		}else if(shopInfoData.getRecentVisitor().size()==2){
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(0).getAvatarUrl(), visit1);
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(1).getAvatarUrl(), visit2);
			visit3.setVisibility(View.GONE);
		}else{
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(0).getAvatarUrl(), visit1);
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(1).getAvatarUrl(), visit2);
			Util.showFirstImages(getActivity(), shopInfoData.getRecentVisitor().get(2).getAvatarUrl(), visit3);
		}
		
		//名师堂
		LinearLayout teachers = (LinearLayout) view.findViewById(R.id.teadcher);
		LinearLayout teLayout = (LinearLayout) view.findViewById(R.id.teadcher_line);
		teLayout.setOnClickListener(this);
		
		ImageView teaerHead1 = (ImageView) view.findViewById(R.id.iv_teacher_logo1);
		teaerHead1.setOnClickListener(this);
		TextView teacherName1 = (TextView) view.findViewById(R.id.name_teacher_logo1);
		TextView teacherType1 = (TextView) view.findViewById(R.id.type_teacher_logo1);
		
		ImageView teaerHead2 = (ImageView) view.findViewById(R.id.iv_teacher_logo2);
		teaerHead2.setOnClickListener(this);
		TextView teacherName2 = (TextView) view.findViewById(R.id.name_teacher_logo2);
		TextView teacherType2 = (TextView) view.findViewById(R.id.type_teacher_logo2);
		
		ImageView teaerHead3 = (ImageView) view.findViewById(R.id.iv_teacher_logo3);
		teaerHead2.setOnClickListener(this);
		TextView teacherName3 = (TextView) view.findViewById(R.id.name_teacher_logo3);
		TextView teacherType3 = (TextView) view.findViewById(R.id.type_teacher_logo3);
		
		if(null == shopInfoData.getShopTeacher() || shopInfoData.getShopTeacher().size()==0){
			teachers.setVisibility(View.GONE);
		}else if(shopInfoData.getShopTeacher().size()==1){
			setTeacherShow(true, teaerHead1, teacherName1, teacherType1, shopInfoData.getShopTeacher().get(0));
			setTeacherShow(false, teaerHead2, teacherName2, teacherType2, null);
			setTeacherShow(false, teaerHead3, teacherName3, teacherType3, null);
		}else if(shopInfoData.getShopTeacher().size()==2){
			setTeacherShow(true, teaerHead1, teacherName1, teacherType1, shopInfoData.getShopTeacher().get(0));
			setTeacherShow(true, teaerHead2, teacherName2, teacherType2, shopInfoData.getShopTeacher().get(1));
			setTeacherShow(false, teaerHead3, teacherName3, teacherType3, null);
		}else if(shopInfoData.getShopTeacher().size()==3){
			setTeacherShow(true, teaerHead1, teacherName1, teacherType1, shopInfoData.getShopTeacher().get(0));
			setTeacherShow(true, teaerHead2, teacherName2, teacherType2, shopInfoData.getShopTeacher().get(1));
			setTeacherShow(true, teaerHead3, teacherName3, teacherType3, shopInfoData.getShopTeacher().get(2));
		}
		//每日/周/月 之星
		LinearLayout everyStart = (LinearLayout) view.findViewById(R.id.ll_edu_every_star);
		everyStart.setOnClickListener(this);
		ImageView everyStarHead = (ImageView) view.findViewById(R.id.iv_edu_every_start_head);
		TextView everyStartIntroduce = (TextView) view.findViewById(R.id.tv_edu_every_start_introduce);
		if(null == shopInfoData.getStudentStar() || Util.isEmpty(shopInfoData.getStudentStar().getStarCode())){
			everyStart.setVisibility(View.GONE);
		}else{
			Util.showBannnerImage(getActivity(), shopInfoData.getStudentStar().getStarUrl(), everyStarHead);
			everyStartIntroduce.setText(shopInfoData.getStudentStar().getStarInfo());
		}
		
		//招生启示
		LinearLayout recruit = (LinearLayout) view.findViewById(R.id.ll_edu_enrollment_plan);
		recruit.setOnClickListener(this);
		ImageView recruitIv= (ImageView) view.findViewById(R.id.iv_edu_enrollment_plan);
		
		if(null == shopInfoData.getShopRecruitInfo()||Util.isEmpty(shopInfoData.getShopRecruitInfo().getRecruitCode())
				||Util.isEmpty(shopInfoData.getShopRecruitInfo().getRecruitUrl()) ){
			recruit.setVisibility(View.GONE);
		}else{
			recruit.setVisibility(View.VISIBLE);
			Util.showBannnerImage(getActivity(), shopInfoData.getShopRecruitInfo().getRecruitUrl(), recruitIv);
		}
	}
	
	/**
	 * 设置名师显示
	 */
	private void setTeacherShow(boolean show,ImageView head,TextView name,TextView type,Teacher teacher){
		if(show){
			Util.showFirstImages(getActivity(), teacher.getTeacherImgUrl(), head);
			name.setText(teacher.getTeacherName());
			type.setText(teacher.getTeachCourse()+"("+teacher.getTeacherTitle()+")");
		}else{
			head.setVisibility(View.INVISIBLE);
			name.setVisibility(View.INVISIBLE);
			type.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_recentvisit: //最近访客
			Util.showToastZH("最近访客");
			break;
		
		case R.id.teadcher_line: //名师堂
			Util.showToastZH("名师堂");
			break;
			
		case R.id.iv_teacher_logo1: //教师1
			Util.showToastZH("教师1");
			break;
			
		case R.id.iv_teacher_logo2: //教师2
			Util.showToastZH("教师2");
			break;
			
		case R.id.iv_teacher_logo3: //教师3
			Util.showToastZH("教师3");
			break;
			
		case R.id.ll_edu_every_star: //每 之星
			Util.showToastZH("每   之星");
			break;
		
		case R.id.ll_edu_enrollment_plan: //招生启示
			Util.showToastZH("招生启示");
			break;

		default:
			break;
		}
	}
	
}
