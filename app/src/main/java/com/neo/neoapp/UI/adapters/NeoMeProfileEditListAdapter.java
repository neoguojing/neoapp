package com.neo.neoapp.UI.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.Setings;

public class NeoMeProfileEditListAdapter extends NeoBasicListAdapter {
	
	private final int VIEW_TYPE_COUNT = 4;
	private List<ViewItem> viewList = new ArrayList<ViewItem>();
	
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
	
	private void resolveMeToList(){
		viewList.add(new ViewItem(People.NAME,mApplication.mMe.getName(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.SIGN,mApplication.mMe.getSign(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.AGE,mApplication.mMe.getAge(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.BIRTHDAY,mApplication.mMe.getBirthday(),
				ViewType.STRING_STRING));
		viewList.add(new ViewItem(People.GENDER,mApplication.mMe.getGender(),
				ViewType.STRING_CHECKBOX));
		viewList.add(new ViewItem(People.INDUSTRY,mApplication.mMe.getIndustry(),
				ViewType.STRING_STRING));
		
		viewList.add(new ViewItem("地址","beijing",
				ViewType.STRING_STRING));
		viewList.add(new ViewItem("提交","",
				ViewType.BUTTON));
		
	}
	
	public NeoMeProfileEditListAdapter(NeoBasicApplication application,
			Context context) {
		super(application, context);
		// TODO Auto-generated constructor stub
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
		
		StringEditViewHolder stringholder = null;
		ButtonViewHolder buttonholder = null;
		CheckBoxViewHolder checkboxholder = null;
		if (convertView == null) {
			switch(type){
			case STRING_STRING:
				convertView = mInflater.inflate(R.layout.list_item_of_stringedit, null);
				stringholder = new StringEditViewHolder(convertView);
				stringholder.name.setText(viewList.get(position).title);
				stringholder.value.setText(viewList.get(position).value);
				convertView.setTag(stringholder);
				break;
			case STRING_IMAGE:

				break;
			case STRING_CHECKBOX:
				convertView = mInflater.inflate(R.layout.list_item_of_gender, null);
				checkboxholder = new CheckBoxViewHolder(convertView);
				checkboxholder.name.setText(viewList.get(position).title);
				if (viewList.get(position).value.equals("0"))
					checkboxholder.female.setChecked(true);
				else
					checkboxholder.male.setChecked(true);
				convertView.setTag(buttonholder);
				break;
			case BUTTON:
				convertView = mInflater.inflate(R.layout.list_item_of_button, null);
				buttonholder = new ButtonViewHolder(convertView);
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				convertView.setTag(buttonholder);
				break;
			default:
				break;
			}

		} else {
			switch(type){
			case STRING_STRING:
				stringholder = (StringEditViewHolder) convertView.getTag();
				stringholder.name.setText(viewList.get(position).title);
				stringholder.value.setText(viewList.get(position).value);
				break;
			case STRING_IMAGE:
				break;
			case STRING_CHECKBOX:
				checkboxholder = (CheckBoxViewHolder) convertView.getTag();
				checkboxholder.name.setText(viewList.get(position).title);
				if (viewList.get(position).value.equals("0"))
					checkboxholder.female.setChecked(true);
				else
					checkboxholder.male.setChecked(true);
				break;
			case BUTTON:
				buttonholder = (ButtonViewHolder) convertView.getTag();
				buttonholder.button.setText(viewList.get(position).title);
				buttonholder.button.setBackgroundColor(Color.parseColor("#DC143C"));
				break;
			default:
				break;
			}
		}
		
		return convertView;
	}
	

	
	class StringEditViewHolder {
		
		NeoBasicTextView name;
		EditText value;
		
		StringEditViewHolder(View view){
			name = (NeoBasicTextView)view.findViewById(R.id.string_name);
			value = (EditText)view.findViewById(R.id.string_edit);
			value.setOnFocusChangeListener(new OnFocusChangeListener(){

				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if (arg1==false){
						String text = value.getText().toString().trim();
					}
				}
				
			});
		}

	}
	
	class CheckBoxViewHolder {
		
		NeoBasicTextView name;
		RadioGroup radio;
		RadioButton female;
		RadioButton male;
		
		CheckBoxViewHolder(View view){
			name = (NeoBasicTextView)view.findViewById(R.id.string_name_for_checkbox_list);
			radio = (RadioGroup)view.findViewById(R.id.radioGroup_list);
			female = (RadioButton) radio.findViewById(R.id.radioFemale_list);
			male = (RadioButton) radio.findViewById(R.id.radioMale_list);
			radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					int id = arg0.getCheckedRadioButtonId();
					mApplication.mMe.setGender(id);
				}
				
			});
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
