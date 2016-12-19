package com.neo.neoapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

public class NeoHttpTask extends AsyncTask<String, Integer, Object> {
		
	private Context mContext;
	
	private HttpDataProcessCallBack mCallBack;
	
	public NeoHttpTask(Context ctx){
		mContext = ctx;
	}
	/** 
	* 该方法由ui线程进行调用，用户可以在这里尽情的访问ui组件。 
	* 很多时候，我们会在这里显示一个进度条啥的，以示后台正在 
	* 执行某项功能。 
	*/
	@Override
	protected void onPreExecute() {
	super.onPreExecute();
	}

	/** 
	* 该方法由后台进程进行调用，进行主要的耗时的那些计算。 
	* 该方法在onPreExecute方法之后进行调用。当然在执行过程中 
	* 我们可以每隔多少秒就调用一次publishProgress方法，更新 
	* 进度信息 
	*/
	@Override
	protected Object doInBackground(String... params) {
	return null;
	}

	/** 
	* doInBackground中调用了publishProgress之后，ui线程就会 
	* 调用该方法。你可以在这里动态的改变进度条的进度，让用户知道 
	* 当前的进度。 
	*/
	@Override
	protected void onProgressUpdate(Integer... values) {
	super.onProgressUpdate(values);
	}

	/** 
	* 当doInBackground执行完毕之后，由ui线程调用。可以在这里 
	* 返回我们计算的最终结果给用户。 
	*/
	@Override
	protected void onPostExecute(Object result) {
	super.onPostExecute(result);
	}
	
	
	public static interface HttpDataProcessCallBack{
		
		void processHttpData();
	}

}
