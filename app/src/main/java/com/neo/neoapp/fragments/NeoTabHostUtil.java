package com.neo.neoapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neo.neoapp.R;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;


public class NeoTabHostUtil implements OnItemClickListener {
	
	private Context mContext;
	private NeoBasicFragment mFrag;
	
	private TabHost tabhost;
	
	NeoTabHostUtil(Context ctx,NeoBasicFragment frag)
	{
		mContext = ctx;
		mFrag = frag;
		
		if (mContext!=null && mFrag!=null){
			InitView();
			InitEvent();
		}	
	}
	
	public NeoTabHostUtil() {
		// TODO Auto-generated constructor stub
	}

	public void InitView()
	{
		tabhost =(TabHost) mFrag.findViewById(R.id.neo_tabhost);
		tabhost.setup();
		
		TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
		tab1.setIndicator("red");
		tab1.setContent(R.id.widget_layout_red);
		tabhost.addTab(tab1);
		
		TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
		tab2.setIndicator("yellow");
		tab2.setContent(R.id.widget_layout_yellow);
		tabhost.addTab(tab2);
	}
	
	public void InitEvent()
	{

		return;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
