package com.neo.neoapp.socket.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;

import cz.msebera.android.httpclient.Header;

public class NeoServerIpUpdateTask implements Runnable {
	private String Tag = "NeoServerIpUpdateTask";
	private NeoBasicApplication mApplication;
	private Context mContext;
	public NeoServerIpUpdateTask(NeoBasicApplication application,
			Context context){
		mApplication = application;
		mContext = context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*if (mApplication.netWorkState == NetWorkState.WIFI||
				mApplication.netWorkState == NetWorkState.NONE)
			return;*/
		NeoAppBroadCastMessages.sendStaticBroadCastTestMsg(mContext,
				"WelcomeActivity.class","update ip success");
		updateServerIp();
	}
	
	private void updateServerIp(){
		
        NeoAsyncHttpUtil.get(mContext, NeoAppSetings.DestIpUpdateUrlPrefix+mApplication.mMe.getName(),
        		new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray arg0) {
                Log.i(Tag, new StringBuilder(String.valueOf(arg0.length())).toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }

            public void onFinish() {
                Log.i(Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
					Log.i(Tag, "onSuccess"+":"+response.getString("info").toString());
					NeoAppBroadCastMessages.sendStaticBroadCastTestMsg(mContext,
							"WelcomeActivity.class","update ip success");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }
        });
	}
	
}
