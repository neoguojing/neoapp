package com.neo.neoapp.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neo.neoandroidlib.JsonResolveUtils;
import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.R;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.UI.adapters.NeoCommonListAdapter;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.UI.views.list.NeoCommonListView;
import com.neo.neoapp.activities.NeoAppSettingsActivity;
import com.neo.neoapp.activities.NeoMeProfileSettingsActivity;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.Setings;

public class NeoSetingListFragment extends NeoBasicFragment implements
OnItemClickListener, OnClickListener{
	
	private LinearLayout meGroup = null;
	private NeoCommonListView commonList;
	private NeoCommonListAdapter commonListAdpt;
    private ImageView headpic;
    private NeoBasicTextView myName;
	public static List<Setings> mSetingList = new ArrayList<Setings>();
	
	public NeoSetingListFragment() {
		super();
	}

	public NeoSetingListFragment(NeoBasicApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_setings, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		meGroup = (LinearLayout)findViewById(R.id.meViewGroup);
		commonList  = (NeoCommonListView) findViewById(R.id.setings_list);
        this.headpic = (ImageView) findViewById(R.id.userheader);
        this.headpic.setImageBitmap(NeoImageUtil.compressTheImageToDestSize(
        		mApplication.mAppDataPath+mApplication.mMe.getName(),
        		NeoImageUtil.dip2px(mContext,80f),
        		NeoImageUtil.dip2px(mContext,80f)));
        
        this.myName = (NeoBasicTextView) findViewById(R.id.myname);
        myName.setText(mApplication.mMe.getName());
	}

	@Override
	protected void initEvents() {
		meGroup.setOnClickListener(this);
		commonList .setOnItemClickListener(this);

	}

	@Override
	protected void init() {
		getListItems();	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = (int) arg3;
		/*People people = mApplication.mNearByPeoples.get(position);
		String uid = null;
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
		intent.putExtra("entity_people", people);
		startActivity(intent);8*/
		
		if (position==mSetingList.size()-1){
			Intent intent = new Intent(mContext, NeoAppSettingsActivity.class);
			startActivity(intent);
		}
	}

	private void getListItems() {
		
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				//showLoadingDialog("正在加载,请稍后...");
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				return JsonResolveUtils.resolveSetings(mApplication, mSetingList);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				//dismissLoadingDialog();
				if (!result) {
					showCustomToast("数据加载失败...");
				} else {
					commonListAdpt = new NeoCommonListAdapter(mApplication,
							mContext, mSetingList);
					commonListAdpt.setLayOutId(R.layout.list_item_of_setings);
					commonList.setAdapter(commonListAdpt);
				}
			}

		});
		
		

	}

	public void onCancel() {
		clearAsyncTask();
	}

	public void onRefresh() {
		
	}

	public void onManualRefresh() {

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		
		case R.id.meViewGroup:
			Intent intent = new Intent(mContext,NeoMeProfileSettingsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
