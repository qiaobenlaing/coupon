package cn.suanzi.baomi.shop.adapter;

import java.util.LinkedHashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.StaffShop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.model.EditStaffTask;

/**
 * 店员列表
 * @author wensi.yu
 *
 */
public class GetClerkAdminAdapter extends BaseAdapter {
	
	private final static String TAG = "GetClerkAdminAdapter";
	
	private Context context;
	private List<StaffShop> list;
	private Activity mActivity;
	private LayoutInflater mInflater;
	/**登录令牌*/
	private UserToken mUserToken;
	/** mTokenCode**/
	private String mTokenCode;
	/**员工编码*/
	private String mStaffCode;
	/** 父店员编码 */
	private String mParentCode;
	
	public GetClerkAdminAdapter(Context context, List<StaffShop> list) {
		super();
		this.mActivity = (Activity)context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}
	
	public void addItems(List<StaffShop> datas){
		if (datas == null) {
			return ;
		}
		if (list == null) {
			list = datas;
		}else{
			list.addAll(datas);
		}
		notifyDataSetChanged();
	}
	
	public void setItems(List<StaffShop> datas){
		list = datas;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(list == null){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(list==null){
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView ==  null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_mystaffmanager_list, null);
			
			holder.name = (TextView) convertView.findViewById(R.id.tv_staff_manager_name);
			holder.phone = (TextView) convertView.findViewById(R.id.tv_staff_manager_phone);
			holder.edit = (LinearLayout) convertView.findViewById(R.id.layout_staff_manager);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final StaffShop staffShop = list.get(position);
		holder.name.setText(staffShop.getRealName());
		holder.phone.setText(staffShop.getMobileNbr());
		mStaffCode = staffShop.getBrandId();
		DB.saveStr(ShopConst.STAFF_BTANDID, mStaffCode);
		
		if(staffShop.isShowEdit()){
			holder.edit.setVisibility(View.VISIBLE);
		}else{
			holder.edit.setVisibility(View.GONE);
		}
		
		/**
		 * 点击编辑
		 */
		holder.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStaffCode = staffShop.getStaffCode();
				Log.d(TAG, "mStaffCode=="+mStaffCode);
				String editName = staffShop.getRealName();
				String editPhone = staffShop.getMobileNbr();
				Log.d(TAG, "editName==="+editName);
				Log.d(TAG, "editPhone==="+editPhone);
				
				DialogUtils.showDialogEditorUpdate(mActivity,  mActivity.getString(R.string.staff_shop_title),editName,editPhone, mActivity.getString(R.string.ok), mActivity.getString(R.string.no),new DialogUtils().new OnResListener() {
					
					@Override
					public void onOk(String... params) {
						LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String , Object>() ;
						reqParams.get(params[0]);
						reqParams.get(params[1]);
						String mStaffName = params[0];
						String mStaffPhone = params[1];
						Log.d(TAG, "mStaffName11==="+params[0] + "mStaffPhone11=="+params[1]);
						editStaff(mStaffName,mStaffPhone,staffShop);
					}
					
				});
				
			}
		});
		
		return convertView;
	}

	public class ViewHolder{
		public TextView name;
		public TextView phone;     
		public LinearLayout edit;
	}
	
	/**
	 * 修改员工
	 */
	private void editStaff(final String tvName,final String tvPhone,final StaffShop staffShop){
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();
		mParentCode = mUserToken.getStaffCode();
		
		new EditStaffTask(mActivity, new EditStaffTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
					String staffCode = result.get("staffCode").toString();
					if (staffCode.equals(staffShop.getStaffCode())) {
						staffShop.setRealName(tvName);
						staffShop.setMobileNbr(tvPhone);
						notifyDataSetChanged();
					}
					Util.getContentValidate(R.string.toast_staffmanamer_update);
				} else {
					if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_MOBILENBR){
						Util.getContentValidate(R.string.num_inputtype);
					} else if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.MOBILENBR_ERROR){
						Util.getContentValidate(R.string.mobilenbr_error);
					} else if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.MOBILENBR_BEENUSED){
						Util.getContentValidate(R.string.mobilenbr_used);
					} else if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_STAFFNAME){
						Util.getContentValidate(R.string.name_inputtype);
					} else if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_STAFFTYPE){
						Util.getContentValidate(R.string.select_type);
					} else if(Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.PHONE_USERED){
						Util.getContentValidate(R.string.mobilenbr_used);
					}
				}
			}
		}).execute(mStaffCode,tvPhone,tvName,String.valueOf(Util.NUM_ONE),"",mParentCode,mTokenCode);
	}

}
