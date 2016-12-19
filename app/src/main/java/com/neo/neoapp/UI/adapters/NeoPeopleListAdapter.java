package com.neo.neoapp.UI.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoandroidlib.PhotoUtils;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.People;

public class NeoPeopleListAdapter extends NeoBasicListAdapter {
	private HashMap<String,String> unReadMessageCount = new HashMap<String,String>();
	
	public NeoPeopleListAdapter(NeoBasicApplication application,
			Context context, List<? extends Entity> datas) {
		super(application, context, datas);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_of_peoples, null);
			holder = new ViewHolder();

			holder.mIvAvatar = (ImageView) convertView
					.findViewById(R.id.user_item_iv_avatar);
			holder.mIvVip = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_vip);
			holder.mIvGroupRole = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_group_role);
			holder.mIvIndustry = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_industry);
			holder.mIvWeibo = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_weibo);
			holder.mIvTxWeibo = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_txweibo);
			holder.mIvDevice = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_device);
			holder.mIvRelation = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_relation);
			holder.mIvMultipic = (ImageView) convertView
					.findViewById(R.id.user_item_iv_icon_multipic);

			holder.mHtvName = (NeoBasicTextView) convertView
					.findViewById(R.id.user_item_htv_name);
			holder.mLayoutGender = (LinearLayout) convertView
					.findViewById(R.id.user_item_layout_gender);
			holder.mIvGender = (ImageView) convertView
					.findViewById(R.id.user_item_iv_gender);
			holder.mHtvAge = (NeoBasicTextView) convertView
					.findViewById(R.id.user_item_htv_age);
			holder.mHtvDistance = (NeoBasicTextView) convertView
					.findViewById(R.id.user_item_htv_distance);
			holder.mHtvTime = (NeoBasicTextView) convertView
					.findViewById(R.id.user_item_htv_time);
			holder.mHtvSign = (NeoBasicTextView) convertView
					.findViewById(R.id.user_item_htv_sign);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		People people = (People) getItem(position);
		
		Bitmap headpic = mApplication.getUserHeadPic(people.getAvatar());
		if (headpic==null){
			headpic = mApplication.getAvatar(people
					.getAvatar());
		}else{
			headpic = NeoImageUtil.compressTheImageToDestSize(headpic,
	        		NeoImageUtil.dip2px(mContext,77f),
	        		NeoImageUtil.dip2px(mContext,77f));
			
		}
		
		String countStr = getUnreadMessageCount(people.getName());
		if (countStr.equals("0"))
			holder.mIvAvatar.setImageBitmap(headpic);
		else{
			holder.mIvAvatar.setImageBitmap(
					NeoImageUtil.drawTextOntBitMap(headpic,
							getUnreadMessageCount(people.getName())));
		}
		
		holder.mHtvName.setText(people.getName());
		holder.mLayoutGender.setBackgroundResource(people.getGenderBgId());
		holder.mIvGender.setImageResource(people.getGenderId());
		holder.mHtvAge.setText(people.getAge() + "");
		holder.mHtvDistance.setText(people.getDistance());
		holder.mHtvTime.setText(people.getTime());
		holder.mHtvSign.setText(people.getSign());
		if (people.getIsVip() != 0) {
			holder.mIvVip.setVisibility(View.VISIBLE);
		} else {
			holder.mIvVip.setVisibility(View.GONE);
		}
		if (people.getIsGroupRole() != 0) {
			holder.mIvGroupRole.setVisibility(View.VISIBLE);
			if (people.getIsGroupRole() == 1) {
				holder.mIvGroupRole
						.setImageResource(R.drawable.ic_userinfo_groupowner);
			}
		} else {
			holder.mIvIndustry.setVisibility(View.GONE);
		}
		if (!android.text.TextUtils.isEmpty(people.getIndustry())) {
			holder.mIvIndustry.setVisibility(View.VISIBLE);
			holder.mIvIndustry.setImageBitmap(PhotoUtils.getIndustry(mContext,
					people.getIndustry()));
		} else {
			holder.mIvIndustry.setVisibility(View.GONE);
		}
		if (people.getIsbindWeibo() != 0) {
			holder.mIvWeibo.setVisibility(View.VISIBLE);
			if (people.getIsbindWeibo() == 1) {
				holder.mIvWeibo.setImageResource(R.drawable.ic_userinfo_weibov);
			}
		} else {
			holder.mIvWeibo.setVisibility(View.GONE);
		}
		if (people.getIsbindTxWeibo() != 0) {
			holder.mIvTxWeibo.setVisibility(View.VISIBLE);
			if (people.getIsbindTxWeibo() == 1) {
				holder.mIvTxWeibo
						.setImageResource(R.drawable.ic_userinfo_tweibov);
			}
		} else {
			holder.mIvTxWeibo.setVisibility(View.GONE);
		}

		if (people.getDevice() != 0) {
			holder.mIvDevice.setVisibility(View.VISIBLE);
			if (people.getDevice() == 1) {
				holder.mIvDevice
						.setImageResource(R.drawable.ic_userinfo_android);
			}
			if (people.getDevice() == 2) {
				holder.mIvDevice.setImageResource(R.drawable.ic_userinfo_apple);
			}
		} else {
			holder.mIvDevice.setVisibility(View.GONE);
		}
		if (people.getIsRelation() != 0) {
			holder.mIvRelation.setVisibility(View.VISIBLE);
		} else {
			holder.mIvRelation.setVisibility(View.GONE);
		}
		if (people.getIsMultipic() != 0) {
			holder.mIvMultipic.setVisibility(View.VISIBLE);
		} else {
			holder.mIvMultipic.setVisibility(View.GONE);
		}
		return convertView;
	}

	public String getUnreadMessageCount(String name) {
		if (!unReadMessageCount.containsKey(name))
			return "0";
		return unReadMessageCount.get(name);
	}

	public void setUnreadMessageCount(String name,int count) {
		this.unReadMessageCount.put(name, String.valueOf(count));
	}

	class ViewHolder {

		ImageView mIvAvatar;
		ImageView mIvVip;
		ImageView mIvGroupRole;
		ImageView mIvIndustry;
		ImageView mIvWeibo;
		ImageView mIvTxWeibo;
		ImageView mIvDevice;
		ImageView mIvRelation;
		ImageView mIvMultipic;
		NeoBasicTextView mHtvName;
		LinearLayout mLayoutGender;
		ImageView mIvGender;
		NeoBasicTextView mHtvAge;
		NeoBasicTextView mHtvDistance;
		NeoBasicTextView mHtvTime;
		NeoBasicTextView mHtvSign;
	}

}
