package com.neo.neoapp.activities.chat;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.EmoticonsTextView;
import com.neo.neoapp.entity.Message;


public class TextMessageItem extends MessageItem implements OnLongClickListener {

	private EmoticonsTextView mEtvContent;

	public TextMessageItem(Message msg, Context context) {
		super(msg, context);
	}
	

	@Override
	protected void onInitViews() {
		View view = mInflater.inflate(R.layout.message_text, null);
		mLayoutMessageContainer.addView(view);
		mEtvContent = (EmoticonsTextView) view
				.findViewById(R.id.message_etv_msgtext);
		mEtvContent.setText(mMsg.getContent());
		mEtvContent.setOnLongClickListener(this);
		mLayoutMessageContainer.setOnLongClickListener(this);
	}

	@Override
	protected void onFillMessage() {
		//mEtvContent.setText(mMsg.getContent());
	}

	@Override
	public boolean onLongClick(View v) {
		System.out.println("长按");
		return true;
	}

}
