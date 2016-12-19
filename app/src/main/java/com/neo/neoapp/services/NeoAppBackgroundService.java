package com.neo.neoapp.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.handlers.NeoAppUIThreadHandler;
import com.neo.neoapp.socket.server.NeoAyncSocketServer;
import com.neo.neoapp.socket.server.NeoServerIpUpdateTask;
import com.neo.neoapp.tasks.ServicieWorker;

import android.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class NeoAppBackgroundService extends Service {
	
	private static final String TAG = "NeoAppBackgroundService";
	private NotificationManager notifyMgr;
	//private ThreadGroup mThreadGroup = new ThreadGroup("ServiceWorker");
	private NeoAyncSocketServer aServer = null;
	private NeoAppUIThreadHandler mUIHandler = null;
	private NeoServerIpUpdateTask tIpUpdateTask = null;
	ScheduledExecutorService service = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		Log.v(TAG, "in onCreate");
		notifyMgr = (NotificationManager)getSystemService(
				NOTIFICATION_SERVICE);
		
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		super.onStartCommand(intent, flags, startId);
		
		//int counter = intent.getExtras().getInt("counter");
		//Log.v(TAG,"in onStartCommand,counter = " + counter +
		//		",startId = "+ startId);
		//new Thread(mThreadGroup,new ServicieWorker (counter),
				//		TAG).start();
		//displayNotifyMsg("WelcomeActivity.class",TAG+" is running");
		aServer = new NeoAyncSocketServer(this);
		
		//start ip update task
		tIpUpdateTask = new NeoServerIpUpdateTask((NeoBasicApplication) getApplication(),
				this);
		service = Executors  
	                .newSingleThreadScheduledExecutor();  
	    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
	    service.scheduleAtFixedRate(tIpUpdateTask, 10, 5*60, TimeUnit.SECONDS); 
		return START_STICKY;
	}
	
	@Override
	public void onDestroy(){
		Log.v(TAG,"in onDestroy");
		
		//mThreadGroup.interrupt(); 
		if (service!=null){
			service.shutdown();
		}
		aServer.close();
		notifyMgr.cancelAll();
		super.onDestroy();
	}
	
	
	private void displayNotifyMsg(String clsStr, String message) {
		// TODO Auto-generated method stub
		NeoAppBroadCastMessages.sendStaticBroadCastTestMsg(this,clsStr,message);
	}
}
