package com.neo.neoandroidlib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class NeoIntentFactiory {
	
	public static void getWebBrowser(Activity activity){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.baidu.com"));
		activity.startActivity(intent);
	}
	public static void getWebSearch(Activity activity){
		Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		intent.setData(Uri.parse("http://www.baidu.com"));
		activity.startActivity(intent);
	}
	public static void getDial(Activity activity){
		Intent intent = new Intent(Intent.ACTION_DIAL);
		activity.startActivity(intent);
	}
	public static void getMap(Activity activity){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("geo:100,100"));
		activity.startActivity(intent);
	}
	
	public static void playMp3(Activity activity){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("audio/mp3");
		activity.startActivity(intent);
	}
	
	public static void sendEmail(Activity activity){
		Uri email = Uri.parse("mailto:xxx@163.com");
		Intent intent = new Intent(Intent.ACTION_SENDTO, email);
		activity.startActivity(intent);
	}
	
	public static void sendText(Activity activity){
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.putExtra("sms_body", "the sms text");  
		intent.setType("vnd.android-dir/mms-sms");  

		activity.startActivity(intent);
	}
	
	public static void unInstallApp(Activity activity, String appname){
		Uri uninstallUri = Uri.fromParts("package", appname, null);  
		Intent intent = new Intent(Intent.ACTION_DELETE, uninstallUri); 

		activity.startActivity(intent);
	}
	
	public static void installApp(Activity activity){
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.setType("application/vnd.android.package-archive");  

		activity.startActivity(intent);
	}
	
	public static void findApp(Activity activity, String appname){
		Uri uri = Uri.parse("market://search?q="+appname);           
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
  
		activity.startActivity(intent);
	}
}
