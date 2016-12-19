package com.neo.neoandroidlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.EditText;


public class NetWorkUtils {
	
	private Context mContext;
	public static State wifiState = null;
	public static State mobileState = null;

	public NetWorkUtils(Context context) {
		mContext = context;
	}

	public enum NetWorkState {
		WIFI, MOBILE, NONE;

	}
	
	public static String getWifiIpAddress(Context context){
		//获取wifi服务  
        WifiManager wifiManager =
        		(WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        	//wifiManager.setWifiEnabled(true);    
        	return null;
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        String ip = intToIp(ipAddress);   
		return ip;
	}
	
	private static String intToIp(int i) {       
         
         return (i & 0xFF ) + "." +       
       ((i >> 8 ) & 0xFF) + "." +       
       ((i >> 16 ) & 0xFF) + "." +       
       ( i >> 24 & 0xFF) ;  
    }   
	
	/*匹配IP
	 * ((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.)
	 * {3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))
	 * 192.168.0.0 - 192.168.255.255
		172.16.0.0 - 172.31.255.255
		10.0.0.0 - 10.255.255.255
	 * */
	public static  boolean isLocalIpSegment(String ip){
		
		String ip196Regex = "((192\\.)(168\\.)"
				+ "(?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.)"
				+ "(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		String ip10Regex = "((10\\.)"
				+ "(?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){2}"
				+ "(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		String ip172Regex = "((172\\.)"
				+ "(?:(?:(1[6-9])|(2\\d)|(3[0-1]))\\.)"
				+ "(?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.)"
				+ "(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))))";
		if (Pattern.compile(ip196Regex).matcher(ip)
				.matches()) {
			return true;
		}
		
		if (Pattern.compile(ip10Regex).matcher(ip)
				.matches()) {
			return true;
		}
		
		if (Pattern.compile(ip172Regex).matcher(ip)
				.matches()) {
			return true;
		}
		
		return false;
	}
	
	public static String getGPRSIpAddress()  
    {  
        try  
        {  
            for (Enumeration<NetworkInterface> en = 
            		NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
            {  
               NetworkInterface intf = en.nextElement();  
               for (Enumeration<InetAddress> enumIpAddr =
            		   intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
               {  
                   InetAddress inetAddress = enumIpAddr.nextElement();  
                   if (!inetAddress.isLoopbackAddress())  
                   {  
                       return inetAddress.getHostAddress().toString();  
                   }  
               }  
           }  
        }  
        catch (SocketException ex)  
        {  
            Log.e("getGPRSIpAddress IpAddress", ex.toString());  
        }  
        return null;  
    } 
	
	public NetWorkState getConnectState() {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		//manager.getActiveNetworkInfo();
		NetworkInfo ni =null;
		ni = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (ni!=null)
			wifiState = ni.getState();
		ni = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (ni!=null)
			mobileState = ni.getState();
		
		if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED == mobileState) {
			return NetWorkState.MOBILE;
		} else if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			return NetWorkState.NONE;
		} else if (wifiState != null && State.CONNECTED == wifiState) {
			return NetWorkState.WIFI;
		}
		return NetWorkState.NONE;
	}
	
	public static NetWorkState getConnectState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		//NetworkInfo ni = manager.getActiveNetworkInfo();
		NetworkInfo ni =null;
		ni = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (ni!=null)
			wifiState = ni.getState();
		ni = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (ni!=null)
			mobileState = ni.getState();

		if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED == mobileState) {
			return NetWorkState.MOBILE;
		} else if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			return NetWorkState.NONE;
		} else if (wifiState != null && State.CONNECTED == wifiState) {
			return NetWorkState.WIFI;
		}
		return NetWorkState.NONE;
	}
	
	public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) 
        		context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
	
	public static String neoPing(String hostname){
		Process p = null;
		int status = 0;
		try {
			p = Runtime.getRuntime().exec("ping -c "+
			"1" + " " + hostname);
			status = p.waitFor(); 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 10.83.50.111  m_strForNetAddress
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        if (status != 0) {  
            return null; 
        }    
 
        BufferedReader buf = new BufferedReader(
        		new InputStreamReader(p.getInputStream()));
        
        String str = new String();  
        StringBuffer output = new StringBuffer();
        //读出所有信息并显示                    
        try {
			while((str=buf.readLine())!=null){  
				output.append(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (buf!=null)
					buf.close();
				if (p!=null)
					p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return output.toString();
	}
	
}
