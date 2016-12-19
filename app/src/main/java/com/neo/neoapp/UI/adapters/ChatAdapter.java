package com.neo.neoapp.UI.adapters;

import java.util.List;

import com.neo.neoapp.BaseObjectListAdapter;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.activities.chat.MessageItem;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.Message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class ChatAdapter extends BaseObjectListAdapter {

	public ChatAdapter(NeoBasicApplication application, Context context,
			List<? extends Entity> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message msg = (Message) getItem(position);
		MessageItem messageItem = MessageItem.getInstance(msg, mContext);
		if(msg.getMessageType()==Message.MESSAGE_TYPE.SEND){
			messageItem.fillContentForSend(msg);
		}else{
			messageItem.fillContentForReceive();
		}
		View view = messageItem.getRootView();
		return view;
	}
}
