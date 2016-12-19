package com.neo.neoapp.UI.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;

public class NeoAppSettingsListAdapter extends NeoBasicListAdapter{
	
	private final int VIEW_TYPE_COUNT = 4;
	private List<ViewItem> viewList = new ArrayList<ViewItem>();
	private OnClickCallBack mCallback = null;
	
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
	
	public interface OnClickCallBack{
		public void buttonClick(View v, int position);
	}
	
	public NeoAppSettingsListAdapter(NeoBasicApplication application,
			Context context, OnClickCallBack callback) {
		super(application, context);
		// TODO Auto-generated constructor stub
		initViewList();
		mCallback = callback;
	}
	
	private void initViewList(){
		viewList.add(new ViewItem("切换账号","",
				ViewType.BUTTON));
		viewList.add(new ViewItem("退出","",
				ViewType.BUTTON));
		
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
		final int  pos = position;
		ButtonViewHolder buttonholder = null;
		if (convertView == null) {
			switch(type){
			case STRING_STRING:
				break;
			case STRING_IMAGE:

				break;
			case STRING_CHECKBOX:
				break;
			case BUTTON:
				convertView = mInflater.inflate(R.layout.list_item_of_button, null);
				buttonholder = new ButtonViewHolder(convertView);
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				buttonholder.button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mCallback.buttonClick(arg0,pos);
					}
					
				});
				convertView.setTag(buttonholder);
				break;
			default:
				break;
			}

		} else {
			switch(type){
			case STRING_STRING:

				break;
			case STRING_IMAGE:
				break;
			case STRING_CHECKBOX:
				break;
			case BUTTON:
				buttonholder = (ButtonViewHolder) convertView.getTag();
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				buttonholder.button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mCallback.buttonClick(arg0,pos);
					}
					
				});
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


}
