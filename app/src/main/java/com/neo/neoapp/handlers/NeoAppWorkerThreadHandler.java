package com.neo.neoapp.handlers;

import com.neo.neoapp.definitions.ENeoAppWorkerThreadMessages;
import com.neo.neoapp.definitions.ENeoUIThreadMessges;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class NeoAppWorkerThreadHandler extends Handler {
	
	private String TAG = "NeoAppWorkerThreadHandler";
	public NeoAppWorkerThreadHandler(Looper looper){
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) { 
      super.handleMessage(msg); 
      
      switch(ENeoAppWorkerThreadMessages.values()[msg.what]) 
      { 
      case SERVICEWORKER_TEST:
    	  Log.v(TAG, "handleMessage:SERVICEWORKER_TEST");
    	  break;
    	  
      default:
      		break;
      } 
    } 
	
	public void sendMessage(long interval){
		
		Message msg = this.obtainMessage(ENeoUIThreadMessges.UI_TEST.getMsgId());
		prepareMessage(msg);
		this.sendMessageDelayed(msg, 1000*interval);
	}

	private void prepareMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle bd = new Bundle();
		bd.putString("test", "hello UI");
		msg.setData(bd);
		return;
	}
}
