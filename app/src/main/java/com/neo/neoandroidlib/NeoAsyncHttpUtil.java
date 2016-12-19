package com.neo.neoandroidlib;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;
import cz.msebera.android.httpclient.protocol.HTTP;

public class NeoAsyncHttpUtil {
	private static String TAG = "NeoAsyncHttpUtil";
    private static  AsyncHttpClient client =new AsyncHttpClient();    //实例话对象
    
    public static final String[] Image_MIME = 
    	{"application/x-jpg", "image/png", "image/jpeg","application/octet-stream"};
    public static AsyncHttpClient getClientInstance()
    {
    	if (null==client){
    		client =new AsyncHttpClient();
        	client.setTimeout(AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        	
    	}
    	
    	if (NeoCookieListUtil.getCookies() != null) {//每次请求都要带上cookie  
            BasicCookieStore bcs = new BasicCookieStore();  
            bcs.addCookies(NeoCookieListUtil.getCookies().toArray(  
                    new Cookie[NeoCookieListUtil.getCookies().size()]));  
            client.setCookieStore(bcs); 
        } 
    	return client;
    }
    
    public static AsyncHttpClient getClientInstance(Context context)
    {
    	client = getClientInstance();
    	//将context对应的cookie，持久化
    	persistCookies(context);
    	return client;
    }
    
    //在activity和fragment中调用，
    public static void persistCookies(Context context)    //用一个完整url获取一个string对象
    {
    	if (null!=client){
	    	PersistentCookieStore cookieStore = new PersistentCookieStore(context);  
	    	client.setCookieStore(cookieStore); 
    	}
    }
    
    public static List<Cookie> addPersistCookieToGlobaList(Context context){  
    	List<Cookie> cookies = null;
    	if (null!=client){
	        PersistentCookieStore cookieStore = new PersistentCookieStore(context);  
	        cookies = cookieStore.getCookies();  
    	}
    	NeoCookieListUtil.setCookies(cookies);
        return cookies;  
    }  
      
    public static void clearCookie(Context context){  
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);  
        cookieStore.clear();  
    }  
    
    public static String getCookieText(Context context) {  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);  
        List<Cookie> cookies = myCookieStore.getCookies();  
        NeoCookieListUtil.setCookies(cookies);  
        for (Cookie cookie : cookies) {  
            Log.d(TAG, cookie.getName() + " = " + cookie.getValue());  
        }  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < cookies.size(); i++) {  
             Cookie cookie = cookies.get(i);  
             String cookieName = cookie.getName();  
             String cookieValue = cookie.getValue();  
            if (!cookieName.isEmpty() && !cookieValue.isEmpty()) {  
                sb.append(cookieName + "=");  
                sb.append(cookieValue + ";");  
            }  
        }  
        return sb.toString();  
    }  
    
    public static String getListUtilCookieText() {
        Cookie cookie;
        List<Cookie> cookies = NeoCookieListUtil.getCookies();
        NeoCookieListUtil.setCookies(cookies);
        for (Cookie cookie2 : cookies) {
            Log.d(TAG, cookie2.getName() + " = " + cookie2.getValue());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie2 = (Cookie) cookies.get(i);
            String cookieName = cookie2.getName();
            String cookieValue = cookie2.getValue();
            if (!(cookieName.isEmpty() || cookieValue.isEmpty())) {
                sb.append(new StringBuilder(String.valueOf(cookieName)).append("=").toString());
                sb.append(new StringBuilder(String.valueOf(cookieValue)).append(";").toString());
            }
        }
        return sb.toString();
    }
    public static void get(Context context,String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
    	getClientInstance(context).get(urlString, res);
    }
    
    public static void get(String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
    	getClientInstance().get(urlString, res);
    }
    
    public static void get(Context context,String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
    	getClientInstance(context).get(urlString, params,res);
    }
    
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
    	getClientInstance().get(urlString, params,res);
    }
    
    public static void get(Context context,String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
    	getClientInstance(context).get(urlString, res);
    }
    
    public static void get(String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
    	getClientInstance().get(urlString, res);
    }
    
    public static void get(Context context,String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
    	getClientInstance(context).get(urlString, params,res);
    }
    
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
    	getClientInstance().get(urlString, params,res);
    }
    
    public static void get(Context context,String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
    	getClientInstance(context).get(uString, bHandler);
    }
    
    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
    	getClientInstance().get(uString, bHandler);
    }

    public static void postJson(Context context, String urlString, HttpEntity json, JsonHttpResponseHandler res) {
        getClientInstance(context).post(context, urlString, json, RequestParams.APPLICATION_JSON, res);
    }

    public static void post(Context context, String urlString, RequestParams param, JsonHttpResponseHandler res) {
        getClientInstance(context).post(urlString, param, res);
    }
}
