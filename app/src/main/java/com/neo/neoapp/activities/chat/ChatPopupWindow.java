package com.neo.neoapp.activities.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.neo.neoapp.NeoBasicPopupWindow;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;

public class ChatPopupWindow extends NeoBasicPopupWindow implements OnClickListener {
	private NeoBasicTextView mHtvVoiceMode;
	private NeoBasicTextView mHtvCreate;
	private NeoBasicTextView mHtvSynchronous;

	private onChatPopupItemClickListener mOnChatPopupItemClickListener;

	public ChatPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(
				R.layout.include_dialog_chat, null), width, height);
		setAnimationStyle(R.style.Popup_Animation_Alpha);
	}

	@Override
	public void initViews() {
		mHtvVoiceMode = (NeoBasicTextView) findViewById(R.id.dialog_chat_htv_voicemode);
		mHtvCreate = (NeoBasicTextView) findViewById(R.id.dialog_chat_htv_create);
		mHtvSynchronous = (NeoBasicTextView) findViewById(R.id.dialog_chat_htv_synchronous);
	}

	@Override
	public void initEvents() {
		mHtvVoiceMode.setOnClickListener(this);
		mHtvCreate.setOnClickListener(this);
		mHtvSynchronous.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_chat_htv_voicemode:
			if (mOnChatPopupItemClickListener != null) {
				mOnChatPopupItemClickListener.onVoiceModeClick();
			}
			break;

		case R.id.dialog_chat_htv_create:
			if (mOnChatPopupItemClickListener != null) {
				mOnChatPopupItemClickListener.onCreateClick();
			}
			break;

		case R.id.dialog_chat_htv_synchronous:
			if (mOnChatPopupItemClickListener != null) {
				mOnChatPopupItemClickListener.onSynchronousClick();
			}
			break;
		}
		dismiss();
	}

	public void setOnChatPopupItemClickListener(
			onChatPopupItemClickListener listener) {
		mOnChatPopupItemClickListener = listener;
	}

	public interface onChatPopupItemClickListener {
		void onVoiceModeClick();

		void onCreateClick();

		void onSynchronousClick();
	}
}
