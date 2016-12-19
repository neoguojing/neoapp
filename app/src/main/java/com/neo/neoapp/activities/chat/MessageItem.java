package com.neo.neoapp.activities.chat;

import com.neo.neoandroidlib.DateUtils;
import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.R;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.entity.Message;
import com.neo.neoapp.entity.Message.MESSAGE_STATUS;
import com.neo.neoapp.entity.Message.MESSAGE_TYPE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public abstract class MessageItem {

	protected Context mContext;
	protected View mRootView;

	/**
	 * TimeStampContainer
	 */
	private RelativeLayout mLayoutTimeStampContainer;
	private NeoBasicTextView mHtvTimeStampTime;
	private NeoBasicTextView mHtvTimeStampDistance;

	/**
	 * LeftContainer
	 */
	private RelativeLayout mLayoutLeftContainer;
	private LinearLayout mLayoutStatus;
	private NeoBasicTextView mHtvStatus;

	/**
	 * MessageContainer
	 */
	protected LinearLayout mLayoutMessageContainer;

	/**
	 * RightContainer
	 */
	private LinearLayout mLayoutRightContainer;
	private ImageView mIvPhotoView;

	protected LayoutInflater mInflater;
	protected Message mMsg;

	protected int mBackground;

	public MessageItem(Message msg, Context context) {
		mMsg = msg;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public static MessageItem getInstance(Message msg, Context context) {
		MessageItem messageItem = null;
		switch (msg.getContentType()) {
		case TEXT:
			messageItem = new TextMessageItem(msg, context);
			break;

		case IMAGE:
			messageItem = new ImageMessageItem(msg, context);
			break;

		case MAP:
			messageItem = new MapMessageItem(msg, context);
			break;

		case VOICE:
			messageItem = new VoiceMessageItem(msg, context);
			break;

		}
		messageItem.init(msg.getMessageType());
		return messageItem;
	}

	private void init(MESSAGE_TYPE messageType) {
		switch (messageType) {
		case RECEIVER:
			mRootView = mInflater.inflate(R.layout.message_group_receive_template,
					null);
			mBackground = R.drawable.bg_message_box_receive;
			break;

		case SEND:
			mRootView = mInflater.inflate(R.layout.message_group_send_template,
					null);
			mBackground = R.drawable.bg_message_box_send;
			break;
		}
		if (mRootView != null) {
			initViews(mRootView);
		}
	}

	protected void initViews(View view) {
		mLayoutTimeStampContainer = (RelativeLayout) view
				.findViewById(R.id.message_layout_timecontainer);
		mHtvTimeStampTime = (NeoBasicTextView) view
				.findViewById(R.id.message_timestamp_htv_time);
		mHtvTimeStampDistance = (NeoBasicTextView) view
				.findViewById(R.id.message_timestamp_htv_distance);

		mLayoutLeftContainer = (RelativeLayout) view
				.findViewById(R.id.message_layout_leftcontainer);
		mLayoutStatus = (LinearLayout) view
				.findViewById(R.id.message_layout_status);
		mHtvStatus = (NeoBasicTextView) view.findViewById(R.id.message_htv_status);

		mLayoutMessageContainer = (LinearLayout) view
				.findViewById(R.id.message_layout_messagecontainer);
		mLayoutMessageContainer.setBackgroundResource(mBackground);

		mLayoutRightContainer = (LinearLayout) view
				.findViewById(R.id.message_layout_rightcontainer);
		mIvPhotoView = (ImageView) view.findViewById(R.id.message_iv_userphoto);
		onInitViews();
	}

	public void fillContentForSend(Message msg) {
		fillTimeStamp();
		fillStatus(msg);
		fillMessage();
		fillPhotoViewForSend();
	}
	
	public void fillContentForReceive() {
		fillTimeStamp();
		//fillStatus();
		fillMessage();
		fillPhotoViewForReceive();
	}

	protected void fillMessage() {
		onFillMessage();
	}

	protected void fillTimeStamp() {
		mLayoutTimeStampContainer.setVisibility(View.VISIBLE);
		if (mMsg.getTime() != 0) {
			mHtvTimeStampTime.setText(DateUtils.formatDate(mContext,
					mMsg.getTime()));
		}
		if (mMsg.getDistance() != null) {
			mHtvTimeStampDistance.setText(mMsg.getDistance());
		} else {
			mHtvTimeStampDistance.setText("未知");
		}
	}

	protected void fillStatus(Message msg) {
		mLayoutLeftContainer.setVisibility(View.VISIBLE);
		mLayoutStatus
				.setBackgroundResource(R.drawable.bg_message_status_sended);
		if (msg.getMessageStatu()==MESSAGE_STATUS.SENDSUCCESS){
			mHtvStatus.setText("送达");
		}else{
			mHtvStatus.setText("失败");
		}
	}
	
	protected void fillPhotoViewForSend() {
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		Bitmap bm = ((NeoBasicApplication) ((Activity) mContext)
				.getApplication()).getMyHeadPic();
		bm = NeoImageUtil.compressTheImageToDestSize(bm,
        		NeoImageUtil.dip2px(mContext,40f),
        		NeoImageUtil.dip2px(mContext,40f));
		mIvPhotoView.setImageBitmap(bm);
	}
	
	protected void fillPhotoViewForReceive() {
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		Bitmap bm = ((NeoBasicApplication) ((Activity) mContext)
				.getApplication()).getUserHeadPic(mMsg.getAvatar());
		
		if (bm==null){
			bm = ((NeoBasicApplication) ((Activity) mContext)
					.getApplication()).getAvatar(mMsg.getAvatar());
			mIvPhotoView.setImageBitmap(bm);
		}else{
			bm = NeoImageUtil.compressTheImageToDestSize(bm,
	        		NeoImageUtil.dip2px(mContext,40f),
	        		NeoImageUtil.dip2px(mContext,40f));
			mIvPhotoView.setImageBitmap(bm);
		}
	}

	protected void refreshAdapter() {
		((ChatActivity) mContext).refreshAdapter();
	}

	public View getRootView() {
		return mRootView;
	}

	protected abstract void onInitViews();

	protected abstract void onFillMessage();
}
