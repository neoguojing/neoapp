package com.neo.neoandroidlib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class NeoHttpOprs {
	private static HttpClient mHttpClient;
	private HttpGet mHttpGet;
	private HttpPost mHttpPost;
	private HttpResponse mHttpRep;
	
	public NeoHttpOprs(){
		getHttpClient();
	}
	
	public static synchronized HttpClient getHttpClient(){
		
		if (mHttpClient == null){
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, 
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			//HttpProtocolParams.setUserAgent(params, useragent);
			ConnManagerParams.setTimeout(params, 1000);
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http",
					SSLSocketFactory.getSocketFactory(), 80));
			ClientConnectionManager conMgr = new 
					ThreadSafeClientConnManager(params, schReg);
			
			mHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return mHttpClient;
		
	}
	
	protected HttpResponse doGet(String uri,  List<? extends NameValuePair> params){
		
		HttpResponse tHttpRep = null;
		String param = URLEncodedUtils.format(params, "UTF-8");
		try {
			mHttpGet = new HttpGet(uri+"?"+param);
			tHttpRep = mHttpClient.execute(mHttpGet);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tHttpRep;
		
	}
	
	protected HttpResponse doPost(String uri, List<? extends NameValuePair> params){
			
			HttpResponse tHttpRep = null;
			try {
				mHttpPost = new HttpPost(uri);
				mHttpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
				tHttpRep = mHttpClient.execute(mHttpPost);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tHttpRep;
		
	}

	protected HttpResponse doUpload(String uri,InputStream in){
		
		HttpResponse tHttpRep = null;
		try {
			byte[] data = IOUtils.toByteArray(in);
			InputStreamBody isb = new InputStreamBody(
					new ByteArrayInputStream(data), "uploadedFile");
			MultipartEntity multipart = new MultipartEntity();
			multipart.addPart("uploadedFile", isb);
			mHttpPost = new HttpPost(uri);
			mHttpPost.setEntity(multipart);
			tHttpRep = mHttpClient.execute(mHttpPost);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tHttpRep;
	
	}
	
	protected void saveDownloadFile(String filename, byte[] data){
		
		File file = new File(filename);
		OutputStream output = null;
		try {

			if (!file.exists())
				file.createNewFile();
			 output = new FileOutputStream(file);
			 IOUtils.write(data, output);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				output = null;
			}

		}
		
	}
}
