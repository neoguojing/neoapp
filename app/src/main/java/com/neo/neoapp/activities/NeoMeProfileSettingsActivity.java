package com.neo.neoapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.NeoMeProfileListAdapter;
import com.neo.neoapp.UI.adapters.NeoMeProfileListAdapter.CallBack;
import com.neo.neoapp.UI.views.list.NeoCommonListView;

public class NeoMeProfileSettingsActivity extends NeoBasicActivity 
	implements OnItemClickListener,CallBack{
	private NeoCommonListView commonList;
	private NeoMeProfileListAdapter commonListAdpt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_profile_settings);
		initViews();
		initEvents();
	}
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		commonListAdpt = new NeoMeProfileListAdapter(mApplication,this,this);
		commonList = (NeoCommonListView)findViewById(R.id.me_profile_list);
		commonList.setAdapter(commonListAdpt);
		commonList.setOnItemClickListener(this);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		int position = (int) arg3;
		int  end = commonListAdpt.getCount()-1;
		if (position==0){
			
		}
		
		if (position==end){
			startActivity(new Intent(this, NeoMeProfileEditActivity.class));
	        finish();
		}
		
	}
	@Override
	public void buttonClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, NeoMeProfileEditActivity.class));
        finish();
	}

}
