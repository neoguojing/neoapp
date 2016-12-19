package com.neo.neoapp.definitions;

public enum ENeoUIThreadMessges {
	UI_MSG_START(0),
	UI_TEST(1);
	
	private int mMsgId;
	
	private ENeoUIThreadMessges(int msgid){
		mMsgId = msgid;
	}
	
	public int getMsgId(){
		return mMsgId;
	}
	
	public int setMsgId(int msgid){
		return mMsgId = msgid;
	}
}
