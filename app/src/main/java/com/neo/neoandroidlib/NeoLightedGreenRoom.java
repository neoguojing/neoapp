package com.neo.neoandroidlib;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class NeoLightedGreenRoom {
	
	private static String tag = "NeoLightedCreenRoom";
	
	private int count;
	private Context ctx =null;
	
	PowerManager.WakeLock wl = null;
	
	private int clientCount = 0;
	
	private NeoLightedGreenRoom(Context inCtx){
		ctx = inCtx;
		wl = this.createWakeLock(inCtx);
		turnOnLights();
	}
	
	private static NeoLightedGreenRoom mInstance = null;
	
	synchronized public static NeoLightedGreenRoom getInstance(Context inCtx){
		
		if (mInstance == null){
			mInstance = new NeoLightedGreenRoom(inCtx);
		}
		return mInstance;
	}

	private WakeLock createWakeLock(Context inCtx) {
		// TODO Auto-generated method stub
		PowerManager pm  =
				(PowerManager)inCtx.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
		return wl;
	}
	
	synchronized public int registerClient(){
		this.clientCount++;
		Log.d(tag,"register a new client clientCount:"+clientCount);
		return clientCount;
	}
	
	synchronized public int unRegisterClient(){
		
		Log.d(tag,"unregister a client clientCount:"+clientCount);
		if(clientCount==0){
			return 0;
		}
		this.clientCount--;
		if(clientCount==0){
			emptyTheRoom();
		}
		return clientCount;
	}
	
	synchronized public void emptyTheRoom() {
		// TODO Auto-generated method stub
		Log.d(tag,"empty the room");
		count = 0;
		this.turnOffLights();
	}
	
	public static boolean isSetup(){
		return (mInstance != null)? true:false;
	}
	
	synchronized public int enter(){
		
		count++;
		Log.d(tag,"a new visitor count:"+count);
		return count;
	}
	
	synchronized public int leave(){
		
		if(count == 0){
			
			Log.d(tag,"count:"+count);
			return count;
		}
		count--;
		if(count == 0){
			
			turnOffLights();
		}
		
		Log.d(tag,"a visitor out count:"+count);
		
		return count;
	}
	
	synchronized public int getCount(){
		return count;
	}
	
	private void turnOffLights() {
		// TODO Auto-generated method stub
		
		if(this.wl.isHeld()){
			Log.d(tag,"release wake lock. no more visitoirs");
			this.wl.release();
		}
	}

	private void turnOnLights() {
		// TODO Auto-generated method stub
		Log.d(tag,"turn on lights count:"+count);
		this.wl.acquire();
	}
}
