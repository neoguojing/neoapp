package com.neo.neoapp.fragments;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;


public class NeoDisPlayFragment extends NeoBasicFragment {

	private LayoutInflater mInflater; 
	private NeoDisPlayFragment pThis;
	
	public NeoDisPlayFragment getpThis() {
		return pThis;
	}

	public void setpThis(NeoDisPlayFragment pThis) {
		this.pThis = pThis;
	}

    private ScheduledExecutorService mScheduledExecutorService;
    
    /////////////////////////////////
    private NeoBrandScrollUtil mBrandScroll;
    private NeoMutiImageScrollUtil mMutiImageScroll;
    private NeoGridViewUtil mGridView;
    private NeoTabHostUtil mTabHost;
	
	public NeoDisPlayFragment(NeoBasicApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_display, container,
				false);
		pThis = this;
		mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mBrandScroll = new NeoBrandScrollUtil(mContext,pThis);
		mGridView = new NeoGridViewUtil(mContext,pThis);
		mMutiImageScroll = new NeoMutiImageScrollUtil(mContext,pThis);	
	    mTabHost = new NeoTabHostUtil(mContext,pThis);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onStart() { 
		mScheduledExecutorService = 
				Executors.newSingleThreadScheduledExecutor();
		mScheduledExecutorService.scheduleAtFixedRate(mBrandScroll, 1, 5, TimeUnit.SECONDS);
		super.onStart();
		
	}
	
	@Override
	public void onStop() { 
		mScheduledExecutorService.shutdown();
		super.onStop();
	}	


}
