package com.neo.neoandroidlib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

public class ResourceOpr {
	public static String getContentsFromAXML(Activity activity, int rid)
	throws XmlPullParserException, IOException{
		StringBuffer sb = new StringBuffer();
		Resources rc = activity.getResources();
		XmlResourceParser xrp = rc.getXml(rid);
		
		xrp.next();
		int eventtype = xrp.getEventType();
		while(eventtype != XmlPullParser.END_DOCUMENT){
			if(eventtype == XmlPullParser.START_DOCUMENT){
				sb.append("/*document start*/");
			}
			else if(eventtype == XmlPullParser.START_TAG){
				sb.append("\nstart tag "+ xrp.getName());
			}
			else if(eventtype == XmlPullParser.END_TAG){
				sb.append("\nend tag "+ xrp.getName());
			}
			else if(eventtype == XmlPullParser.TEXT){
				sb.append("\nText "+ xrp.getText());
			}
			
			eventtype = xrp.next();
		}
		
		sb.append("\n end document");
		return sb.toString();
	}
	
	public static String getStringFromRawFile(Activity activity, int rid)
	throws IOException{
		
		Resources res = activity.getResources();
		InputStream  is = res.openRawResource(rid);
		String result = convertSteamToString(is);
		is.close();
		return result;
		
	}

	public static String convertSteamToString(InputStream is) 
			throws IOException{
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = is.read();
		while(i!=-1){
			baos.write(i);
			i = is.read();
		}
		
		return baos.toString();
	}
	
	public static String getStringFromAsset(Activity activity, String name)
			throws IOException{
			
			AssetManager am = activity.getAssets();
			InputStream  is = am.open(name);
			String result = convertSteamToString(is);
			is.close();
			return result;
			
		}
}
