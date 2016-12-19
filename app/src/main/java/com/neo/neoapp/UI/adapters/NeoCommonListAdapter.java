package com.neo.neoapp.UI.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.Setings;


public class NeoCommonListAdapter extends NeoBasicListAdapter {
	private int layoutId = 0;
	public NeoCommonListAdapter(NeoBasicApplication application,
			Context context, List<? extends Entity> datas) {
		super(application, context, datas);
		// TODO Auto-generated constructor stub
	}
	
	public void setLayOutId(int id){
		this.layoutId = id;
	}
	
	protected int getLayOutId(){
		return this.layoutId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			//convertView = mInflater.inflate(R.layout.list_item_of_setings, null);
			convertView = mInflater.inflate(getLayOutId(), null);
			holder = new ViewHolder();

			holder.mSetingsIv = (ImageView) convertView
					.findViewById(R.id.seting_image);		

			holder.mSetingName = (NeoBasicTextView) convertView
					.findViewById(R.id.seting_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Setings seting = (Setings) getItem(position);
		
		holder.mSetingName.setText(seting.getmName());
		holder.mSetingsIv.setImageBitmap(NeoImageUtil.getImageFromRes(mContext, seting.getmImame())); 
		
		return convertView;
	}

	class ViewHolder {

		ImageView mSetingsIv;
		NeoBasicTextView mSetingName;

	}

}
