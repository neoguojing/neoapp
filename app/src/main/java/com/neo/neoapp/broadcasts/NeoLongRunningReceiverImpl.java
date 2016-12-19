package com.neo.neoapp.broadcasts;

import com.neo.neoandroidlib.NeoThreadUtils;
import com.neo.neoapp.services.NeoLongRunningNonStickyServiceImpl;

public class NeoLongRunningReceiverImpl extends NeoLongRunningReceiver {

	@Override
	public Class getLongRunningBroadCastClass() {
		// TODO Auto-generated method stub
		NeoThreadUtils.logThreadSignature("NeoLongRunningReceiverImpl");
		
		return NeoLongRunningNonStickyServiceImpl.class;
	}

}
