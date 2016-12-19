package com.neo.neoapp.tasks;

import android.util.Log;

public class ServicieWorker implements Runnable {
	
	private int mCounter = -1;
	private String TAG = "ServicieWorker";

	public ServicieWorker(int counter) {
		// TODO Auto-generated constructor stub
		mCounter = counter;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			Log.v(TAG+Thread.currentThread().getId(),"counter = "+mCounter);
			Thread.sleep(10000);
		}catch(InterruptedException e){
			Log.v(TAG+Thread.currentThread().getId(),"Interrupt");
		}
	}

}
