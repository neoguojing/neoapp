package com.neo.neoapp.activities.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NeoSocketMessageCacheUtil;
import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoandroidlib.PhotoUtils;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.ChatAdapter;
import com.neo.neoapp.UI.adapters.CheckListDialogAdapter;
import com.neo.neoapp.UI.views.HeaderLayout.HeaderStyle;
import com.neo.neoapp.activities.WelcomeActivity;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.dialog.SimpleListDialog;
import com.neo.neoapp.entity.Message;
import com.neo.neoapp.entity.NeoConfig;
import com.neo.neoapp.entity.Message.CONTENT_TYPE;
import com.neo.neoapp.entity.Message.MESSAGE_STATUS;
import com.neo.neoapp.entity.Message.MESSAGE_TYPE;
import com.neo.neoapp.socket.client.NeoAyncSocketClient;
import com.neo.neoapp.socket.server.NeoAyncSocketServer;
import com.neo.neoapp.UI.views.HeaderLayout;
import com.neo.neoapp.UI.views.ChatListView;
import com.neo.neoapp.UI.views.ScrollLayout;
import com.neo.neoapp.UI.views.EmoteInputView;
import com.neo.neoapp.UI.views.EmoticonsEditText;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.ClientCookie;

public class ChatActivity extends BaseMessageActivity {
	private final String Tag = "ChatActivity";
	NeoAyncSocketClient socketClient = null;
	MsgReceiver msgBroadCastReceiver = null;
	private int mPosition = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		init();
	}

	@Override
	public void onBackPressed() {
		String msgFile = mApplication.mAppDataPath+NeoAppSetings.UserMsgDir+mPeople.getName();
		if(!FileUtils.writeObjectToFile(msgFile, mMessages)){
			showAlertDialog("NEO", "save message faild ");
		}
		if (mLayoutMessagePlusBar.isShown()) {
			hidePlusBar();
		} else if (mInputView.isShown()) {
			mIbTextDitorKeyBoard.setVisibility(View.GONE);
			mIbTextDitorEmote.setVisibility(View.VISIBLE);
			mInputView.setVisibility(View.GONE);
		} else if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
			mIbTextDitorKeyBoard.setVisibility(View.VISIBLE);
			mIbTextDitorEmote.setVisibility(View.GONE);
			hideKeyBoard();
		} else if (mLayoutScroll.getCurScreen() == 1) {
			mLayoutScroll.snapToScreen(0);
		} else {
			//super.onBackPressed();
			if (mPosition!=0 && !mPeople.getIp().equals("")){
				Intent intent = new Intent();
				intent.putExtra("position", mPosition);
				intent.putExtra("ip", mPeople.getIp());
				this.setResult(RESULT_OK, intent);
			}
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		PhotoUtils.deleteImageFile();
		//socketClient.close();
		super.onDestroy();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		msgBroadCastReceiver = new MsgReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(NeoAppBroadCastMessages.broadcastAction);
		registerReceiver(msgBroadCastReceiver, filter);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		unregisterReceiver(msgBroadCastReceiver);
	}
	
	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.chat_header);
		mHeaderLayout.init(HeaderStyle.TITLE_CHAT);
		mClvList = (ChatListView)findViewById(R.id.chat_clv_list);
		mLayoutScroll = (ScrollLayout) findViewById(R.id.chat_slayout_scroll);
		mLayoutRounds = (LinearLayout) findViewById(R.id.chat_layout_rounds);
		mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);

		mIbTextDitorPlus = (ImageButton) findViewById(R.id.chat_textditor_ib_plus);
		mIbTextDitorKeyBoard = (ImageButton) findViewById(R.id.chat_textditor_ib_keyboard);
		mIbTextDitorEmote = (ImageButton) findViewById(R.id.chat_textditor_ib_emote);
		mIvTextDitorAudio = (ImageView) findViewById(R.id.chat_textditor_iv_audio);
		mBtnTextDitorSend = (Button) findViewById(R.id.chat_textditor_btn_send);
		mEetTextDitorEditer = (EmoticonsEditText) findViewById(R.id.chat_textditor_eet_editer);

		mIbAudioDitorPlus = (ImageButton) findViewById(R.id.chat_audioditor_ib_plus);
		mIbAudioDitorKeyBoard = (ImageButton) findViewById(R.id.chat_audioditor_ib_keyboard);
		mIvAudioDitorAudioBtn = (ImageView) findViewById(R.id.chat_audioditor_iv_audiobtn);

		mLayoutFullScreenMask = (LinearLayout) findViewById(R.id.fullscreen_mask);
		mLayoutMessagePlusBar = (LinearLayout) findViewById(R.id.message_plus_layout_bar);
		mLayoutMessagePlusPicture = (LinearLayout) findViewById(R.id.message_plus_layout_picture);
		mLayoutMessagePlusCamera = (LinearLayout) findViewById(R.id.message_plus_layout_camera);
		mLayoutMessagePlusLocation = (LinearLayout) findViewById(R.id.message_plus_layout_location);
		mLayoutMessagePlusGift = (LinearLayout) findViewById(R.id.message_plus_layout_gift);

	}

	@Override
	protected void initEvents() {
		mLayoutScroll.setOnScrollToScreen(this);
		mIbTextDitorPlus.setOnClickListener(this);
		mIbTextDitorEmote.setOnClickListener(this);
		mIbTextDitorKeyBoard.setOnClickListener(this);
		mBtnTextDitorSend.setOnClickListener(this);
		mIvTextDitorAudio.setOnClickListener(this);
		mEetTextDitorEditer.addTextChangedListener(this);
		mEetTextDitorEditer.setOnTouchListener(this);
		mIbAudioDitorPlus.setOnClickListener(this);
		mIbAudioDitorKeyBoard.setOnClickListener(this);

		mLayoutFullScreenMask.setOnTouchListener(this);
		mLayoutMessagePlusPicture.setOnClickListener(this);
		mLayoutMessagePlusCamera.setOnClickListener(this);
		mLayoutMessagePlusLocation.setOnClickListener(this);
		mLayoutMessagePlusGift.setOnClickListener(this);

	}

	@SuppressWarnings("unchecked")
	private void init() {
		//mProfile = getIntent().getParcelableExtra("entity_profile");
		mPeople = getIntent().getParcelableExtra("entity_people");
		mPosition = getIntent().getIntExtra("position", 0);
		
		mHeaderLayout.setTitleChat(R.drawable.ic_chat_dis_1,
				R.drawable.bg_chat_dis_active, "与" + mPeople.getName() + "对话",
				mPeople.getDistance() + " " + mPeople.getTime(),
				R.drawable.ic_topbar_profile,
				new OnMiddleImageButtonClickListener(),
				R.drawable.ic_topbar_more,
				new OnRightImageButtonClickListener());
		mInputView.setEditText(mEetTextDitorEditer);
		initRounds();
		initPopupWindow();
		initSynchronousDialog();
		
		loadMessage();
		//socket
	    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads().detectDiskWrites().detectNetwork()  
        .penaltyLog().build());  
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  
        .penaltyLog().penaltyDeath().build()); 
	    
	    initClientSocket();
	    
	}
	
	private void loadMessage(){
		 putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
	            protected void onPreExecute() {
	                super.onPreExecute();
	                showLoadingDialog("\u8bf7\u7a0d\u540e,\u6b63\u5728\u521d\u59cb\u5316...");
	            }

	            protected Boolean doInBackground(Void... params) {
	                try {
	                    Boolean rtn = Boolean.valueOf(true);
	                    String msgFile = mApplication.mAppDataPath+NeoAppSetings.UserMsgDir+mPeople.getName();
	            		if (FileUtils.isFileExist(msgFile)){
	            			List<Message> msgList = null;
	            			//msgList = (List<Message>) FileUtils.readMessageListFromFile(msgFile);
	            			msgList = FileUtils.readObjectFromFile(msgFile,msgList);
	            			if (msgList!=null){
	            				mMessages.addAll(msgList);
	            			}
	            			else{
	            				showAlertDialog("NEO", "load message faild ");
	            				rtn = false;
	            			}
	            		}
	            		
	            		//add the message in cache and clear it.
	            		mMessages.addAll(NeoSocketMessageCacheUtil.getIntance().
	            				getMessageList(mPeople.getName()));
	            		NeoSocketMessageCacheUtil.getIntance().clearMessageList(mPeople.getName());
	                    return rtn;
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return Boolean.valueOf(false);
	                }
	            }

	            protected void onPostExecute(Boolean result) {
	                super.onPostExecute(result);
	                dismissLoadingDialog();
	                if (!result.booleanValue()) {
	                    showAlertDialog("NEO", "load message faied");
	                }else{
	                	mAdapter = new ChatAdapter(mApplication, ChatActivity.this, mMessages);
	            		mClvList.setAdapter(mAdapter);
	                }
	            }
	        });
	}
	private void initClientSocket(){
		//test
		//socketClient = new NeoAyncSocketClient(this);
	    //normal
	    if(!NeoAyncSocketServer.socketMap.containsKey(mPeople.getName())){
	    	//get the server ip from kunkunsae
	    	//showAlertDialog("NEO",mPeople.getIp());	    	
	    	if (mPeople.getIp().equals("")){
	    		getDestIpAddress(mPeople.getName());
	    	}
	    	else{
	    		socketClient = new NeoAyncSocketClient(this,mPeople.getIp());
	    		//socketClient = new NeoAyncSocketClient(this);
	    	}
	    }
	}
	
	private void initSocketTask()
	{
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    Boolean rtn = Boolean.valueOf(true);
                    if (mPeople.getIp()!=null&&!mPeople.getIp().isEmpty()){
            			ChatActivity.this.showAlertDialog("NEO", mPeople.getIp());
        	    		socketClient = new NeoAyncSocketClient(ChatActivity.this,mPeople.getIp());
                    }
                    return rtn;
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
            }
        });

	}
	
	private void getDestIpAddress(String name) {
		/*
		 * {
			    "name":"erjung",
			    "uid":0,
			    "ip":"101.80.178.57",
			    "port":7000,
			    "localip":"",
			    "netstate":0
			}
		 * */
		
        if (netWorkCheck(this)) {
            NeoAsyncHttpUtil.get((Context) this, NeoAppSetings.DestIpFetchUrlPrefix+name,
            		new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONArray arg0) {
                    Log.i(ChatActivity.this.Tag, new StringBuilder(String.valueOf(arg0.length())).toString());
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.e(ChatActivity.this.Tag, " onFailure" + throwable.toString());
                    ChatActivity.this.showAlertDialog("NEO", "Get Server Address failed" + throwable.toString());
                }

                public void onFinish() {
                    Log.i(ChatActivity.this.Tag, "onFinish");
                }

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i(ChatActivity.this.Tag, "onSuccess ");
                    try {
                    	if (response.has("errcode")){
                    		 //ChatActivity.this.showLongToast(response.getString("info"));
                    		 ChatActivity.this.showAlertDialog("NEO", "user is offline or unreachable! ");
                    	}else{
                    		ChatActivity.this.showLongToast("ip:" + response.getString(NeoConfig.IP)
                            		+ ";port:" + response.getString(NeoConfig.PORT));
                    		if (response.getInt("netstate")==0)
                    			mPeople.setIp(response.getString("localip"));
                    		else
                    			mPeople.setIp(response.getString("ip"));
                    		
                    		if (mPeople.getIp()!=null&&!mPeople.getIp().isEmpty()){
                    			ChatActivity.this.showAlertDialog("NEO", mPeople.getIp());
                    			initSocketTask();
                	    		/*socketClient = new NeoAyncSocketClient(ChatActivity.this,mPeople.getIp());
                	    		//socketClient = new NeoAyncSocketClient(this);
                	    		if (socketClient==null)
                	    			ChatActivity.this.showAlertDialog("NEO", "user is offline or unreachable!");*/
                    		}
                    	}
                        
                        
                    } catch (Exception e) {
                        Log.e(ChatActivity.this.Tag, e.toString());
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(ChatActivity.this.Tag, " onFailure" + throwable.toString());
                }
            });
	     }
	 }
	 
	@Override
	public void doAction(int whichScreen) {
		switch (whichScreen) {
		case 0:
			((ImageView) mLayoutRounds.getChildAt(0))
					.setImageBitmap(mRoundsSelected);
			((ImageView) mLayoutRounds.getChildAt(1))
					.setImageBitmap(mRoundsNormal);
			break;

		case 1:
			((ImageView) mLayoutRounds.getChildAt(1))
					.setImageBitmap(mRoundsSelected);
			((ImageView) mLayoutRounds.getChildAt(0))
					.setImageBitmap(mRoundsNormal);
			mIbTextDitorKeyBoard.setVisibility(View.GONE);
			mIbTextDitorEmote.setVisibility(View.VISIBLE);
			if (mInputView.isShown()) {
				mInputView.setVisibility(View.GONE);
			}
			hideKeyBoard();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.chat_textditor_ib_plus:
			if (!mLayoutMessagePlusBar.isShown()) {
				showPlusBar();
			}
			break;

		case R.id.chat_textditor_ib_emote:
			mIbTextDitorKeyBoard.setVisibility(View.VISIBLE);
			mIbTextDitorEmote.setVisibility(View.GONE);
			mEetTextDitorEditer.requestFocus();
			if (mInputView.isShown()) {
				hideKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.chat_textditor_ib_keyboard:
			mIbTextDitorKeyBoard.setVisibility(View.GONE);
			mIbTextDitorEmote.setVisibility(View.VISIBLE);
			showKeyBoard();
			break;

		case R.id.chat_textditor_btn_send:
			String content = mEetTextDitorEditer.getText().toString().trim();
			if (!TextUtils.isEmpty(content)) {
				mEetTextDitorEditer.setText(null);
				Message sendmsg = new Message(mApplication.mMe.getName(),mApplication.mMe.getAvatar(), System
						.currentTimeMillis(), "0.12km", content,
						CONTENT_TYPE.TEXT, MESSAGE_TYPE.SEND);
				//for test the client receiver
				/*Message sendmsg = new Message("test1",mApplication.mMe.getAvatar(), System
						.currentTimeMillis(), "0.12km", content,
						CONTENT_TYPE.TEXT, MESSAGE_TYPE.SEND);*/
				if (NeoAyncSocketServer.socketMap
						.containsKey(mPeople.getName())){
					if(!NeoAyncSocketServer.send((SocketChannel) NeoAyncSocketServer.
							socketMap.get(mPeople.getName()),
							sendmsg)){
						sendmsg.setMessageStatu(MESSAGE_STATUS.SENDFAILED);
					}
					
				}else if(socketClient!=null){
					if (!socketClient.send(sendmsg)){
						sendmsg.setMessageStatu(MESSAGE_STATUS.SENDFAILED);
					}
				}else{
					
					showLongToast("user is offline of unreachable");
					sendmsg.setMessageStatu(MESSAGE_STATUS.SENDFAILED);
				}
				
				mMessages.add(sendmsg);
				mAdapter.notifyDataSetChanged();
				mClvList.setSelection(mMessages.size());
			}
			break;

		case R.id.chat_textditor_iv_audio:
			mLayoutScroll.snapToScreen(1);
			break;

		case R.id.chat_audioditor_ib_plus:
			if (!mLayoutMessagePlusBar.isShown()) {
				showPlusBar();
			}
			break;

		case R.id.chat_audioditor_ib_keyboard:
			mLayoutScroll.snapToScreen(0);
			break;

		case R.id.message_plus_layout_picture:
			PhotoUtils.selectPhoto(ChatActivity.this);
			hidePlusBar();
			break;

		case R.id.message_plus_layout_camera:
			mCameraImagePath = PhotoUtils.takePicture(ChatActivity.this);
			hidePlusBar();
			break;

		case R.id.message_plus_layout_location:
			mMessages.add(new Message(mApplication.mMe.getName(),"nearby_people_other", System
					.currentTimeMillis(), "0.12km", null, CONTENT_TYPE.MAP,
					MESSAGE_TYPE.SEND));
			mAdapter.notifyDataSetChanged();
			mClvList.setSelection(mMessages.size());
			hidePlusBar();
			break;

		case R.id.message_plus_layout_gift:
			hidePlusBar();
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.chat_textditor_eet_editer) {
			mIbTextDitorKeyBoard.setVisibility(View.GONE);
			mIbTextDitorEmote.setVisibility(View.VISIBLE);
			showKeyBoard();
		}
		if (v.getId() == R.id.fullscreen_mask) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				hidePlusBar();
			}
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {
			mIvTextDitorAudio.setVisibility(View.VISIBLE);
			mBtnTextDitorSend.setVisibility(View.GONE);
		} else {
			mIvTextDitorAudio.setVisibility(View.GONE);
			mBtnTextDitorSend.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onVoiceModeClick() {
		String[] modes = getResources().getStringArray(R.array.chat_audio_type);
		mDialog = new SimpleListDialog(this);
		mDialog.setTitle("语音收听方式");
		mDialog.setTitleLineVisibility(View.GONE);
		mDialog.setAdapter(new CheckListDialogAdapter(mCheckId, this, modes));
		mDialog.setOnSimpleListItemClickListener(new OnVoiceModeDialogItemClickListener());
		mDialog.show();
	}

	@Override
	public void onCreateClick() {

	}

	@Override
	public void onSynchronousClick() {
		mSynchronousDialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				if (!FileUtils.isSdcardExist()) {
					showCustomToast("SD卡不可用,请检查");
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = PhotoUtils.getBitmapFromFile(path);
						if (PhotoUtils.bitmapIsLarge(bitmap)) {
							PhotoUtils.cropPhoto(this, this, path);
						} else {
							if (path != null) {
								mMessages.add(new Message(mApplication.mMe.getName(),
										"nearby_people_other", System
												.currentTimeMillis(), "0.12km",
										path, CONTENT_TYPE.IMAGE,
										MESSAGE_TYPE.SEND));
								mAdapter.notifyDataSetChanged();
								mClvList.setSelection(mMessages.size());
							}
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				if (mCameraImagePath != null) {
					mCameraImagePath = PhotoUtils
							.savePhotoToSDCard(PhotoUtils.CompressionPhoto(
									mScreenWidth, mCameraImagePath, 2));
					PhotoUtils.fliterPhoto(this, this, mCameraImagePath);
				}
			}
			mCameraImagePath = null;
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					mMessages.add(new Message(mApplication.mMe.getName(),"nearby_people_other", System
							.currentTimeMillis(), "0.12km", path,
							CONTENT_TYPE.IMAGE, MESSAGE_TYPE.SEND));
					mAdapter.notifyDataSetChanged();
					mClvList.setSelection(mMessages.size());
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_FLITER:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					mMessages.add(new Message(mApplication.mMe.getName(),"nearby_people_other", System
							.currentTimeMillis(), "0.12km", path,
							CONTENT_TYPE.IMAGE, MESSAGE_TYPE.SEND));
					mAdapter.notifyDataSetChanged();
					mClvList.setSelection(mMessages.size());
				}
			}
			break;
		}
	}

	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
	}
	
	private void displayReceiveMsg(String content){
		Message receiveMsg = new Message(mApplication.mMe.getName(),"nearby_people_other",
				System.currentTimeMillis(), "0.12km", content,
				CONTENT_TYPE.TEXT, MESSAGE_TYPE.RECEIVER);
		
		mMessages.add(receiveMsg);
		mAdapter.notifyDataSetChanged();
		mClvList.setSelection(mMessages.size());
	}
	
	private void displayReceiveMsg(Message msg){
		mMessages.add(msg);
		mAdapter.notifyDataSetChanged();
		mClvList.setSelection(mMessages.size());
	}
	
	public byte[] MessageToByteArray(Message obj){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] bytes =null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bytes;		
	}
	
	private class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(NeoAppBroadCastMessages.broadcastAction)) {
				Message msg = (Message) intent.getExtras().getSerializable("msg");
				if (msg==null)
					return;
				displayReceiveMsg(msg);
			}
		
		}
	}
}
