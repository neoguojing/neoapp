package com.neo.neoapp.services;

import com.neo.neoandroidlib.NeoLightedGreenRoom;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

public abstract class NeoLongRunningNonStickyService extends IntentService {

	public static String tag = "NeoLongRunningNonStickyService";
	NeoLightedGreenRoom mLightedRoom;
	
	protected abstract void handleBroadCastIntent(Intent broadCastIntent);
	
	public NeoLongRunningNonStickyService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		mLightedRoom = 	NeoLightedGreenRoom.getInstance(getApplicationContext());
		mLightedRoom.registerClient();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		
		if(NeoLightedGreenRoom.isSetup())
			mLightedRoom.enter();
		return Service.START_NOT_STICKY;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try{
			Intent broadCasetIntent =
					intent.getParcelableExtra("original_intent");
			handleBroadCastIntent(broadCasetIntent);
			
		}
		finally{
			if(NeoLightedGreenRoom.isSetup())
				mLightedRoom.leave();
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(NeoLightedGreenRoom.isSetup())
			mLightedRoom.unRegisterClient();
	}
}
