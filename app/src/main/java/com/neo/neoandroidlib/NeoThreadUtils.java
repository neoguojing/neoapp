package com.neo.neoandroidlib;

import android.os.Bundle;
import android.util.Log;

public class NeoThreadUtils {
	
	public static long getThreadId(){
		
		Thread t = Thread.currentThread();
		return t.getId();
	}
	
	public static String getThreadSignature(){
		Thread t = Thread.currentThread();
		long id = t.getId();
		String name = t.getName();
		long pri = t.getPriority();
		String gname = t.getThreadGroup().getName();
		return (name+":(id)"+id+":(priority)"+
				pri+":(group)"+gname);
	}
	
	public static void logThreadSignature(String name){
		Log.d(name,getThreadSignature());
	}
	
	public static void sleepForInSecs(int secs){
		try{
			Thread.sleep(secs*1000);
		}catch(InterruptedException x){
			throw new RuntimeException("interrupted",x);
		}
	}
	
	public static Bundle getStringAsBundle(String message){
		Bundle b = new Bundle();
		b.putString("message", message);
		return b;
	}
	
	public static String getStringFromBundle(Bundle b){
		
		return b.getString("message");
	}
}
