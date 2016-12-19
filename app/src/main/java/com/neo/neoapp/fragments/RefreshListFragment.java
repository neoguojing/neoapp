package com.neo.neoapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.neo.neoandroidlib.JsonResolveUtils;
import com.neo.neoandroidlib.NeoSocketMessageCacheUtil;
import com.neo.neoapp.R;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.UI.adapters.NeoPeopleListAdapter;
import com.neo.neoapp.UI.views.list.NeoRefreshListView;
import com.neo.neoapp.UI.views.list.NeoRefreshListView.OnCancelListener;
import com.neo.neoapp.UI.views.list.NeoRefreshListView.OnRefreshListener;
import com.neo.neoapp.activities.chat.ChatActivity;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.entity.Message;
import com.neo.neoapp.entity.People;

public class RefreshListFragment extends NeoBasicFragment implements
OnItemClickListener, OnRefreshListener, OnCancelListener{
	private String Tag = "RefreshListFragment"; 
	static final int RefreshListFragment_ChatActivity_Msgid = 200;
	
	private NeoRefreshListView refreshList;
	private NeoPeopleListAdapter peopleListAdpt;
	private MsgReceiver msgBroadCastReceiver = null;
	
	public RefreshListFragment() {
		super();
	}

	public RefreshListFragment(NeoBasicApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_peoples, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	 @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        	getPeoples();
        	
        } else {
            //相当于Fragment的onPause
        	clearMypeopleAndFriend();
        }
    }
	
	 @Override
	public void onResume(){
		super.onResume();
		//init the msg receiver
    	msgBroadCastReceiver = new MsgReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(NeoAppBroadCastMessages.broadcastAction);
		mContext.registerReceiver(msgBroadCastReceiver, filter);
		
		updateAllPeopleMsgState();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if (msgBroadCastReceiver!=null)
    		mContext.unregisterReceiver(msgBroadCastReceiver);
	}
	 
	@Override
	protected void initViews() {
		refreshList = (NeoRefreshListView) findViewById(R.id.people_list);
	}

	@Override
	protected void initEvents() {
		refreshList.setOnItemClickListener(this);
		refreshList.setOnRefreshListener(this);
		refreshList.setOnCancelListener(this);
	}

	@Override
	protected void init() {
		getPeoples();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = (int) arg3;
		People people = mApplication.mNearByPeoples.get(position);
		//PeopleProfile profile = mApplication..get(position);
		/*String uid = null;
		String name = null;
		String avatar = null;
		if (position > 3) {
			uid = "momo_p_other";
		} else {
			uid = people.getUid();
		}
		name = people.getName();
		avatar = people.getAvatar();
		Intent intent = new Intent(mContext, OtherProfileActivity.class);
		intent.putExtra("uid", uid);
		intent.putExtra("name", name);
		intent.putExtra("avatar", avatar);
		intent.putExtra("entity_people", people);*/
		updateMsgFlag(people.getName(), 0);
		//peopleListAdpt.setUnreadMessageCount(people.getName(), 0);
		//peopleListAdpt.notifyDataSetChanged();
		
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.putExtra("entity_people", people);
		intent.putExtra("position", position);
		//intent.putExtra("entity_profile", mProfile);
		//startActivity(intent);
		startActivityForResult(intent,RefreshListFragment_ChatActivity_Msgid);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case RefreshListFragment_ChatActivity_Msgid:
			if (resultCode==mActivity.RESULT_OK){
				mApplication.mNearByPeoples.get(
						data.getExtras().getInt("position"))
						.setIp(data.getExtras().getString("ip"));	
			}
			break;
		default:
			break;
		}
	}
	 
	private void getPeoples() {
		
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("正在加载,请稍后...");
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				Boolean rtn = true;
				rtn = JsonResolveUtils.resolveNearbyPeople(mApplication);
				rtn &= getMypeopleAndFriend();
				return rtn;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (!result) {
					showCustomToast("数据加载失败...");
				} 
				peopleListAdpt = new NeoPeopleListAdapter(mApplication,
						mContext, mApplication.mNearByPeoples);
				refreshList.setAdapter(peopleListAdpt);
			}

		});
		
	}

	@Override
	public void onCancel() {
		clearAsyncTask();
		refreshList.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Boolean rtn = true;
				rtn = getMypeopleAndFriend();
				return rtn;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				refreshList.onRefreshComplete();
				peopleListAdpt.notifyDataSetChanged();
			}
		});
	}

	public void onManualRefresh() {
		refreshList.onManualRefresh();
	}
	
	private boolean getMypeopleAndFriend(){
		boolean rtn =true;
		if (mApplication.mMyFriends.isEmpty()){
			rtn = JsonResolveUtils.resolveMyFriends(mApplication, mContext);
			if (rtn&&!mApplication.mNearByPeoples.containsAll(
					mApplication.mMyFriends)){
				mApplication.mNearByPeoples.addAll(mApplication.mMyFriends);
			}
		}
		
		if (mApplication.mMyNearByPeoples.isEmpty()){
			rtn = JsonResolveUtils.resolveMyNearbyPeople(mApplication, mContext);
			if (rtn&&!mApplication.mNearByPeoples.containsAll(
					mApplication.mMyNearByPeoples)){
				mApplication.mNearByPeoples.addAll(mApplication.mMyNearByPeoples);
			}
		}
		return rtn;
	}
	
	private void clearMypeopleAndFriend(){
		if (!mApplication.mNearByPeoples.containsAll(
				mApplication.mMyFriends))
			mApplication.mMyFriends.clear();
		if (!mApplication.mNearByPeoples.containsAll(
				mApplication.mMyNearByPeoples))
			mApplication.mMyNearByPeoples.clear();
	}
	
	public void updateMsgFlag(String name, int count){
		peopleListAdpt.setUnreadMessageCount(name,count);
		peopleListAdpt.notifyDataSetChanged();
	}
	
	private void updateAllPeopleMsgState(){
		
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Boolean rtn = true;
				
				if (peopleListAdpt==null)
					peopleListAdpt = new NeoPeopleListAdapter(mApplication,
							mContext, mApplication.mNearByPeoples);
				
				for (People p:mApplication.mNearByPeoples){
					peopleListAdpt.setUnreadMessageCount(p.getName(),
							NeoSocketMessageCacheUtil.getIntance().
							getMessageCount(p.getName()));
				}
				return rtn;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result)
					refreshList.setAdapter(peopleListAdpt);
					//peopleListAdpt.notifyDataSetChanged();
			}
		});
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
				NeoSocketMessageCacheUtil.getIntance().addMessage(msg.getName(), msg);
				updateMsgFlag(msg.getName(),
						NeoSocketMessageCacheUtil.getIntance().
						getMessageCount(msg.getName()));
			}
		
		}
	}
}
