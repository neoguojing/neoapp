package com.neo.neoapp.UI.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.activities.NeoMeProfileEditActivity;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.Setings;

public class NeoMeProfileListAdapter extends NeoBasicListAdapter 
implements OnClickListener{
	
	private final int VIEW_TYPE_COUNT = 4;
	private List<ViewItem> viewList = new ArrayList<ViewItem>();
	private CallBack mCallBack = null;
	enum ViewType {
		STRING_STRING(0),
		STRING_IMAGE(1),
		STRING_CHECKBOX(2),
		BUTTON(3);
		
		private int id;
		ViewType(int id){
			this.id = id;
		}
		
		public int getId(){
			return this.id;
		}
	}
	
	public interface CallBack {
		public void buttonClick(View v);
	}
	
	private void resolveMeToList(){
		//mApplication.mMe.
		viewList.add(new ViewItem(People.AVATAR,mApplication.mMe.getAvatar(),
				ViewType.STRING_IMAGE));
		viewList.add(new ViewItem(People.NAME,mApplication.mMe.getName(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.SIGN,mApplication.mMe.getSign(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.AGE,mApplication.mMe.getAge(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.BIRTHDAY,mApplication.mMe.getBirthday(),
				ViewType.STRING_STRING));
		if (mApplication.mMe.getGender()==0)
			viewList.add(new ViewItem(People.GENDER,"female",
				ViewType.STRING_STRING));
		else
			viewList.add(new ViewItem(People.GENDER,"male",
					ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.INDUSTRY,mApplication.mMe.getIndustry(),
				ViewType.STRING_STRING));
		
		viewList.add(new ViewItem("地址","beijing",
				ViewType.STRING_STRING));
		viewList.add(new ViewItem("修改","",
				ViewType.BUTTON));
		
	}
	
	public NeoMeProfileListAdapter(NeoBasicApplication application,
			Context context,CallBack callbak) {
		super(application, context);
		// TODO Auto-generated constructor stub
		mCallBack = callbak;
		resolveMeToList();
	}
	
	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public Object getItem(int position) {
		return viewList.get(position);
	}
	
	@Override
	public int getItemViewType(int position) {
	  // TODO Auto-generated method stub
	  return viewList.get(position).type.getId();
	}
	
	public ViewType getViewType(int position) {
		  // TODO Auto-generated method stub
		  return viewList.get(position).type;
		}
	
	@Override
	public int getViewTypeCount() {
	    // TODO Auto-generated method stub
		return VIEW_TYPE_COUNT;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewType type = getViewType(position);
		
		StringImageViewHolder imageholder = null;
		StringStringViewHolder stringholder = null;
		ButtonViewHolder buttonholder = null;
		if (convertView == null) {
			switch(type){
			case STRING_STRING:
				convertView = mInflater.inflate(R.layout.list_item_of_stringstring, null);
				stringholder = new StringStringViewHolder(convertView);
				stringholder.name.setText(viewList.get(position).title);
				stringholder.value.setText(viewList.get(position).value);
				convertView.setTag(stringholder);
				break;
			case STRING_IMAGE:
				convertView = mInflater.inflate(R.layout.list_item_of_stringimage, null);
				imageholder = new StringImageViewHolder(convertView);
				imageholder.text.setText(viewList.get(position).title);
				imageholder.image.setImageBitmap(NeoImageUtil.compressTheImageToDestSize(
		        		mApplication.mAppDataPath+viewList.get(position).value,
		        		NeoImageUtil.dip2px(mContext,80f),
		        		NeoImageUtil.dip2px(mContext,80f)));
				convertView.setTag(imageholder);
				break;
			case STRING_CHECKBOX:
				break;
			case BUTTON:
				convertView = mInflater.inflate(R.layout.list_item_of_button, null);
				buttonholder = new ButtonViewHolder(convertView);
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				buttonholder.button.setOnClickListener(this);
				convertView.setTag(buttonholder);
				break;
			default:
				break;
			}

		} else {
			switch(type){
			case STRING_STRING:
				stringholder = (StringStringViewHolder) convertView.getTag();
				stringholder.name.setText(viewList.get(position).title);
				stringholder.value.setText(viewList.get(position).value);
				break;
			case STRING_IMAGE:
				imageholder = (StringImageViewHolder) convertView.getTag();
				imageholder.text.setText(viewList.get(position).title);
				imageholder.image.setImageBitmap(NeoImageUtil.compressTheImageToDestSize(
		        		mApplication.mAppDataPath+viewList.get(position).value,
		        		NeoImageUtil.dip2px(mContext,80f),
		        		NeoImageUtil.dip2px(mContext,80f)));
				break;
			case STRING_CHECKBOX:
				break;
			case BUTTON:
				buttonholder = (ButtonViewHolder) convertView.getTag();
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				buttonholder.button.setOnClickListener(this);
				break;
			default:
				break;
			}
		}
		
		return convertView;
	}
	
	class StringImageViewHolder {

		ImageView image;
		NeoBasicTextView text;
		
		StringImageViewHolder(View view){
			text = (NeoBasicTextView)view.findViewById(R.id.stringimage_name);
			image = (ImageView)view.findViewById(R.id.stringimage_pic);
		}

	}
	
	class StringStringViewHolder {
		
		NeoBasicTextView name;
		NeoBasicTextView value;
		
		StringStringViewHolder(View view){
			name = (NeoBasicTextView)view.findViewById(R.id.string_name);
			value = (NeoBasicTextView)view.findViewById(R.id.string_value);
		}

	}
	
	class ButtonViewHolder {
		
		Button button;
		
		ButtonViewHolder(View view){
			button = (Button)view.findViewById(R.id.list_item_button);
		}

	}
	
	class ViewItem{
		public String title;
		public String value;
		public ViewType type;
		
		ViewItem(String title,String value,ViewType type){
			this.title = title;
			this.value = value;
			this.type = type;
		}
		
		ViewItem(String title,int value,ViewType type){
			this.title = title;
			this.value = String.valueOf(value);
			this.type = type;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		mCallBack.buttonClick(arg0);
	}

}
