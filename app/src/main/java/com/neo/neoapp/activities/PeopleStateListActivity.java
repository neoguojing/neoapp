package com.neo.neoapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;

import com.neo.neoandroidlib.JsonResolveUtils;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.PeopleStateListAdapter;
import com.neo.neoapp.UI.views.HeaderLayout;
import com.neo.neoapp.UI.views.HeaderLayout.HeaderStyle;
import com.neo.neoapp.UI.views.list.NeoRefreshListView;
import com.neo.neoapp.UI.views.list.NeoRefreshListView.OnCancelListener;
import com.neo.neoapp.UI.views.list.NeoRefreshListView.OnRefreshListener;
import com.neo.neoapp.entity.Feed;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;

public class PeopleStateListActivity extends NeoBasicActivity implements
		OnRefreshListener, OnCancelListener {

	private HeaderLayout mHeaderLayout;
	private NeoRefreshListView mMmrlvList;
	private PeopleStateListAdapter mAdapter;
	private People mPeople;
	private PeopleProfile mProfile;

	private List<Feed> mFeeds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_otherfeedlist);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.otherfeedlist_header);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mMmrlvList = (NeoRefreshListView) findViewById(R.id.otherfeedlist_mmrlv_list);
	}

	@Override
	protected void initEvents() {
		mMmrlvList.setOnRefreshListener(this);
		mMmrlvList.setOnCancelListener(this);
	}

	private void init() {
		mMmrlvList.setItemsCanFocus(false);
		mProfile = getIntent().getParcelableExtra("entity_profile");
		mPeople = getIntent().getParcelableExtra("entity_people");
		mHeaderLayout.setDefaultTitle(mProfile.getName() + "的动态", null);
		getStatus();
	}

	private void getStatus() {
		if (mFeeds == null) {
			putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showLoadingDialog("正在加载,请稍后...");
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					mFeeds = new ArrayList<Feed>();
					return JsonResolveUtils.resolveNearbyStatus(
							PeopleStateListActivity.this, mFeeds,
							mProfile.getUid());
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					dismissLoadingDialog();
					if (!result) {
						showCustomToast("数据加载失败...");
					} else {
						mAdapter = new PeopleStateListAdapter(mProfile, mPeople,
								mApplication, PeopleStateListActivity.this,
								mFeeds);
						mMmrlvList.setAdapter(mAdapter);
					}
				}

			});
		}
	}

	@Override
	public void onCancel() {
		clearAsyncTask();
		mMmrlvList.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				mMmrlvList.onRefreshComplete();
			}
		});
	}
}
