package com.neo.neoapp.handlers;

import com.neo.neoapp.activities.MainTabActivity;
import com.neo.neoapp.definitions.ENeoAppWorkerThreadMessages;
import com.neo.neoapp.definitions.ENeoUIThreadMessges;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class NeoAppUIThreadHandler extends Handler {
	
	private String TAG = "NeoAppUIThreadHandler";
	private Context mContext = null;
	public NeoAppUIThreadHandler(Looper looper, Context context){
		super(looper);
		
		mContext = context;
	}
	
	public NeoAppUIThreadHandler(Looper mainLooper) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleMessage(Message msg) { 
      super.handleMessage(msg); 
      
      switch(ENeoUIThreadMessges.values()[msg.what]) 
      { 
      case UI_TEST:
    	  Log.v(TAG, "handleMessage:UI_TEST");
    	  msg.getData();
    	  if (mContext!=null)
    		  Toast.makeText(mContext,  msg.getData().getString("test")
    			  , Toast.LENGTH_LONG)
    			  .show();
    	  break;
    	  
      default:
      		break;
      } 
    } 
	
	public void sendMessage(long interval){
		
		Message msg = this.obtainMessage(ENeoAppWorkerThreadMessages.SERVICEWORKER_TEST.ordinal());
		prepareMessage(msg);
		this.sendMessageDelayed(msg, 1000*interval);
	}

	private void prepareMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle bd = new Bundle();
		bd.putString("test", "hello worker?");
		msg.setData(bd);
		return;
	}
}
