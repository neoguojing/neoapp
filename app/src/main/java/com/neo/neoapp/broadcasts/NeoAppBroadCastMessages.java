package com.neo.neoapp.broadcasts;

import com.neo.neoapp.entity.Message;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class NeoAppBroadCastMessages {
	
	public static final String broadcastAction = "NEODYNAMICBRODCAST";
	
	public static void sendStaticBroadCastTestMsg(Context context,String clsStr, String msg ){
		
		String testIntent = "com.neo.neoapp.broadcasts.sendbroadcasts";
		Intent testBcintent = new Intent(testIntent);
		testBcintent.putExtra("fortest", msg);
		testBcintent.putExtra("cls", clsStr);
		
		context.sendBroadcast(testBcintent);
		
	}
	
	public static void sendDynamicBroadCastMsg(Context context,String msg){
		
		Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.putExtra("msg",msg);  
        intent.setAction(broadcastAction);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        context.sendBroadcast(intent);   //发送广播
		
	}
	
	public static void sendDynamicBroadCastMsg(Context context,Message msg){
		
		Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.putExtra("msg",msg);  
        intent.setAction(broadcastAction);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        context.sendBroadcast(intent);   //发送广播
		
	}
}
