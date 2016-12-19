package com.neo.neoandroidlib;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class NeoCookieListUtil {
	private static List<Cookie> cookies = null;
	
	public static List<Cookie> getCookies()
	{
		return (cookies==null)?new ArrayList<Cookie>():cookies;
	}
	
	public static void setCookies(List<Cookie> cookies) {  
		NeoCookieListUtil.cookies = cookies;  
    }  
}
