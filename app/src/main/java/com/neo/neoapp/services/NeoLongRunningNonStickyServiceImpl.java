package com.neo.neoapp.services;

import com.neo.neoandroidlib.NeoThreadUtils;

import android.content.Intent;
import android.util.Log;

public class NeoLongRunningNonStickyServiceImpl extends
		NeoLongRunningNonStickyService {
	
	private static String tag = "NeoLongRunningNonStickyServiceImpl";
	public NeoLongRunningNonStickyServiceImpl() {
		super("com.neo.neoapp.broadcasts.longrunningbroadcast");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleBroadCastIntent(Intent broadCastIntent) {
		// TODO Auto-generated method stub
		NeoThreadUtils.logThreadSignature("NeoLongRunningNonStickyServiceImpl");
		Log.d(tag,"handleBroadCastIntent");
		
	}

}
