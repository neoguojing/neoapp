package com.neo.neoapp.broadcasts;

import com.neo.neoandroidlib.NeoLightedGreenRoom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class NeoLongRunningReceiver extends BroadcastReceiver {

	private static final String tag = "NeoLongRunningReceiver";
	NeoLightedGreenRoom mLightedRoom;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(tag, "onReceive");
		
		mLightedRoom = NeoLightedGreenRoom.getInstance(context);
		startService(context, intent);
	}
	
	private void startService(Context context, Intent intent){
		
		Intent serviceIntent = new Intent(
				context,getLongRunningBroadCastClass());
		context.startService(serviceIntent);
	}
	
	public abstract Class getLongRunningBroadCastClass();
}
