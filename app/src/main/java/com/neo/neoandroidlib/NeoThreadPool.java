package com.neo.neoandroidlib;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NeoThreadPool {

    private static Executor service;
    private static NeoThreadPool pool = null;
    public static NeoThreadPool getThreadPool(){
    	if (pool==null)
    		return new NeoThreadPool();
    	return pool;
    }
    private NeoThreadPool(){
    	service = Executors.newCachedThreadPool() ;
    }
    public void  execute(Runnable thread){
    	service.execute(thread);
    }


}
