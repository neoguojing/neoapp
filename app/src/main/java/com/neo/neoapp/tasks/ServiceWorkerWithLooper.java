package com.neo.neoapp.tasks;

import com.neo.neoapp.handlers.NeoAppUIThreadHandler;

import android.content.Context;
import android.os.Looper;

public class ServiceWorkerWithLooper implements Runnable {

	private final Object mLock = new Object();
	private Looper mLooper;
	private NeoAppUIThreadHandler mUIHandler;
	
	public ServiceWorkerWithLooper(String tname,Context context){
		
		mUIHandler = new NeoAppUIThreadHandler(Looper.getMainLooper(), context);
		mUIHandler.sendMessage(1);
		Thread t = new Thread(null,this,tname);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
		synchronized(mLock){
			while(mLooper==null){
				try{
					mLock.wait();
				}catch(InterruptedException ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(mLock){
			Looper.prepare();
			mLooper = Looper.myLooper();
			mLock.notify();
		}
		Looper.loop();
	}
	
	public Looper getLooper(){
		return mLooper;
	}
	
	public void quit(){
		mLooper.quit();
	}
}
