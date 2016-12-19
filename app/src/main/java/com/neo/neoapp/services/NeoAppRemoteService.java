package com.neo.neoapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class NeoAppRemoteService extends Service  {

	private static final String TAG = "NeoAppRemoteService";
	
	public class NeoAppRemoteServiceImpl 
		extends INeoAppRemoteServiceInterface.Stub{

		@Override
		public int getCount() throws RemoteException {
			// TODO Auto-generated method stub
			
			return 10;
		}
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "in onBind");
		return new NeoAppRemoteServiceImpl();
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		Log.v(TAG, "in onCreate");
	}
	
	@Override
	public void onDestroy(){
		Log.v(TAG,"in onDestroy");
		super.onDestroy();
	}

}
