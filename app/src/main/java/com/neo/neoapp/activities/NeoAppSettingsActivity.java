package com.neo.neoapp.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.NeoAppSettingsListAdapter;
import com.neo.neoapp.UI.adapters.NeoAppSettingsListAdapter.OnClickCallBack;
import com.neo.neoapp.UI.views.list.NeoCommonListView;

import cz.msebera.android.httpclient.Header;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NeoAppSettingsActivity extends NeoBasicActivity 
implements OnItemClickListener,OnClickCallBack {
	private final String Tag = "NeoAppSettingsActivity";
	private NeoCommonListView commonList;
	private NeoAppSettingsListAdapter commonListAdpt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neoapp_settings);
		initViews();
		initEvents();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		int position = (int) arg3;
		int  end = commonListAdpt.getCount()-1;
		//切换张哈
		if (position==end-1){
			
		}
		
		//退出
		if (position==end){
			//doLogout();
		}
	}
	
	private void doLogout(){
		 NeoAsyncHttpUtil.get(this,NeoAppSetings.getLogOutUrl(mApplication.mNeoConfig),
	         						new JsonHttpResponseHandler() {
	    		 @Override
	             public void onFinish() {
	                 Log.i(NeoAppSettingsActivity.this.Tag, "onFinish");
	             }
	             @Override
	             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
	                 super.onSuccess(statusCode, headers, response);
	                 Log.i(NeoAppSettingsActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
	                 
	                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(NeoAppSettingsActivity.this);

	             }
	             @Override
	             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
	                 Log.e(NeoAppSettingsActivity.this.Tag, " onFailure" + throwable.toString());
	                 NeoAppSettingsActivity.this.showAlertDialog("NEO", throwable.toString());
	             }
	             @Override
	             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
	                 super.onSuccess(statusCode, headers, response);
	                 Log.i(NeoAppSettingsActivity.this.Tag, "onSuccess ");
	                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(NeoAppSettingsActivity.this);
	                 if (response.has("errcode")){
	 					try {
	 						if (response.getString("errcode").equals(
	 								NEO_ERRCODE.UERE_LOGOUT.toString())){
	 							NeoAppSettingsActivity.this.startActivity(new Intent(NeoAppSettingsActivity.this, LoginActivity.class));
	 							NeoAppSettingsActivity.this.finish();
	 						}
	 					} catch (JSONException e) {
	 						// TODO Auto-generated catch block
	 						e.printStackTrace();
	 					}
	                 }
	             }
	             @Override
	             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
	            	 NeoAppSettingsActivity.this.showAlertDialog("NEO", throwable.toString());
	             }
	         });
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		commonListAdpt = new NeoAppSettingsListAdapter(mApplication,this,this);
		commonList = (NeoCommonListView)findViewById(R.id.app_settings_list);
		commonList.setAdapter(commonListAdpt);
		commonList.setOnItemClickListener(this);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonClick(View v,int position) {
		// TODO Auto-generated method stub
		if (position==(commonListAdpt.getCount()-1)){
			DialogInterface.OnClickListener positiveCallback = new DialogInterface.OnClickListener(){
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					doLogout();
				}
				
			};
			
			DialogInterface.OnClickListener negtiveCallback = new DialogInterface.OnClickListener(){
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
				
			};
			
			showAlertDialog("NEO","Really want to logout?","退出",
					positiveCallback,"取消",negtiveCallback);
		}
		
	}

}
