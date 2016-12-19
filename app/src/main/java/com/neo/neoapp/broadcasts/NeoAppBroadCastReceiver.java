package com.neo.neoapp.broadcasts;

import java.util.HashMap;

import com.neo.neoapp.activities.MainTabActivity;
import com.neo.neoapp.activities.WelcomeActivity;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NeoAppBroadCastReceiver extends BroadcastReceiver {

	private static String TAG = "NeoAppBroadCastReceiver";
	NotificationManager mNotificationManager;
	NotificationCompat.Builder mBuilder;
	private HashMap<String,Class<?>> clsMap =  new HashMap<String,Class<?>>(){
		{
			put("MainTabActivity.class",MainTabActivity.class);
			put("WelcomeActivity.class",WelcomeActivity.class);
		}
	};
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onReceive");
		String message = intent.getStringExtra("fortest");
		String cls = intent.getStringExtra("cls");
		
		NeoNotificationParams params = new NeoNotificationParams(message);
		this.sendNotificationMsg(ctx,clsMap.get(cls),params);
	}

	public class NeoNotificationParams{
		
		public int getmId() {
			return mId;
		}

		public void setmId(int mId) {
			this.mId = mId;
		}

		public String getMtitle() {
			return mtitle;
		}

		public void setMtitle(String mtitle) {
			this.mtitle = mtitle;
		}

		public String getMcontent() {
			return mcontent;
		}

		public void setMcontent(String mcontent) {
			this.mcontent = mcontent;
		}

		public String getMticker() {
			return mticker;
		}

		public void setMticker(String mticker) {
			this.mticker = mticker;
		}

		public int getMpriority() {
			return mpriority;
		}

		public void setMpriority(int mpriority) {
			this.mpriority = mpriority;
		}

		public boolean isMongoing() {
			return mongoing;
		}

		public void setMongoing(boolean mongoing) {
			this.mongoing = mongoing;
		}

		public int getmMax() {
			return mMax;
		}

		public void setmMax(int mMax) {
			this.mMax = mMax;
		}

		public int getmCurrent() {
			return mCurrent;
		}

		public void setmCurrent(int mCurrent) {
			this.mCurrent = mCurrent;
		}

		public boolean isIndeterminate() {
			return indeterminate;
		}

		public void setIndeterminate(boolean indeterminate) {
			this.indeterminate = indeterminate;
		}

		public int getMdefault() {
			return mdefault;
		}

		public void setMdefault(int mdefault) {
			this.mdefault = mdefault;
		}

		public int getmSmallIcon() {
			return mSmallIcon;
		}

		public void setmSmallIcon(int mSmallIcon) {
			this.mSmallIcon = mSmallIcon;
		}

		NeoNotificationParams(String content){
			mtitle = "Notification";
			mcontent = content;
			mticker = "Notification Coming";
			mpriority = Notification.PRIORITY_DEFAULT;
			mongoing = false;
			mMax = 0;
			mCurrent = 0;
			indeterminate = false;
			mdefault = Notification.DEFAULT_ALL;
			mSmallIcon = R.drawable.ic_notification_overlay;
			mId = 1;
		}
		
		NeoNotificationParams(String title,
				String content,
				String ticker,
				int priority,
				boolean ongoing,
				int Max,
				int Current,
				boolean oindeterminate,
				int odefault,
				int SmallIcon,
				int id){
			mtitle = title;
			mcontent = content;
			mticker = ticker;
			mpriority = priority;
			mongoing = ongoing;
			mMax = Max;
			mCurrent = Current;
			indeterminate = oindeterminate;
			mdefault = odefault;
			mSmallIcon = SmallIcon;
			mId = id;
		}
		
		NeoNotificationParams(
				String content,
				boolean ongoing,
				int Max,
				int Current,
				boolean oindeterminate,
				int id){
			mtitle = "Notification";
			mcontent = content;
			mticker = "Notification Coming";
			mpriority = Notification.PRIORITY_DEFAULT;
			mongoing = ongoing;
			if (mongoing){
				mMax = Max;
				mCurrent = Current;
				indeterminate = oindeterminate;
			}else{
				mMax = 0;
				mCurrent = 0;
				indeterminate = false;
			}
			mdefault = Notification.DEFAULT_ALL;
			mSmallIcon = R.drawable.ic_notification_overlay;
			mId = id;
		}
		
		private String mtitle;
		private String mcontent;
		private String mticker;
		private int mpriority;
		private boolean mongoing;
		private int mMax;
		private int mCurrent;
		private boolean indeterminate;
		private int mdefault;
		private int mSmallIcon;
		private int mId;
	}
	
	public void sendNotificationMsg(Context ctx, Class<?> cls,
			NeoNotificationParams params){
		
		mNotificationManager = (NotificationManager)ctx.getSystemService(
				Context.NOTIFICATION_SERVICE);
		
		mBuilder = new NotificationCompat.Builder(ctx);
		mBuilder.setContentText(params.getMcontent())
		.setContentTitle(params.getMtitle())
		.setTicker(params.getMticker())
		.setWhen(System.currentTimeMillis())
		.setPriority(params.getMpriority())
		.setOngoing(params.isMongoing())
		.setDefaults(params.getMdefault())
		.setSmallIcon(params.getmSmallIcon());
		
		if(params.isMongoing()){
			mBuilder.setProgress(params.getmMax(),params.getmCurrent(),
					params.isIndeterminate());
		}
		
		//Intent intent = new Intent(ctx,MainTabActivity.class);  
		Intent intent = new Intent(ctx,cls);  
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);  
		mBuilder.setContentIntent(pendingIntent); 
		
		mNotificationManager.notify(params.getmId(), mBuilder.build());
	}
}
