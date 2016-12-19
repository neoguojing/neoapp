package com.neo.neoapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NeoIntentFactiory;
import com.neo.neoandroidlib.NeoSocketMessageCacheUtil;
import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoandroidlib.PhotoUtils;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.ChangeColorIconWithTextView;
import com.neo.neoapp.UI.NeoViewPagerAdapter;
import com.neo.neoapp.UI.views.photo.PhotoViewAttacher;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.entity.Message;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;
import com.neo.neoapp.entity.Setings;
import com.neo.neoapp.fragments.NeoBasicFragment;
import com.neo.neoapp.fragments.NeoBasicMapFragment;
import com.neo.neoapp.fragments.NeoDisPlayFragment;
import com.neo.neoapp.fragments.NeoSetingListFragment;
import com.neo.neoapp.fragments.RefreshListFragment;
import com.neo.neoapp.handlers.NeoAppWorkerThreadHandler;
import com.neo.neoapp.services.NeoAppBackgroundService;
import com.neo.neoapp.tasks.ServiceWorkerWithLooper;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.httpclient.android.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.mime.MIME;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainTabActivity extends NeoBasicActivity implements OnClickListener,
	OnPageChangeListener{
	
	private final String Tag = "MainTabActivity";
	private NeoBasicApplication mApplication;
	
	private int counter = 1;
	
	private ServiceWorkerWithLooper mtMainActivityWorker;
	private NeoAppWorkerThreadHandler mWorkerThreadHandler;
	
	//for vies
	private ViewPager mViewPager;
	private NeoViewPagerAdapter mfPagerAdapter;
	private List<NeoBasicFragment> mListFragments = new ArrayList<NeoBasicFragment>();
	//private List<RefreshListFragment> mListFragments = new ArrayList<RefreshListFragment>();
	
	private List<ChangeColorIconWithTextView> mListViews = 
			new ArrayList<ChangeColorIconWithTextView>();
	
	private ChangeColorIconWithTextView one = null;
	private MsgReceiver msgBroadCastReceiver = null;
	RefreshListFragment refreshList = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mApplication = (NeoBasicApplication) getApplication();
		//����������
		setOverflowButtonAlways();
		getActionBar().setDisplayShowHomeEnabled(false);
		
		//��ʼ������
		initViews();
		//initService();
		//initWorkerThread();
		//sendNotifications();
        initNetWorkData();
	}
    private void test() {
        if (this.mApplication.mMe != null) {
            showAlertDialog("NEO", new StringBuilder(String.valueOf(String.valueOf(this.mApplication.mMe.getGender()))).append(this.mApplication.mMe.getAvatar()).toString());
        }
    }
	public void onDestroy(){
		
		//stopService();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.action_settings:
			return true;
		case R.id.menu_webbrowser:
			NeoIntentFactiory.getWebBrowser(this);
			return true;
		case R.id.menu_websearch:
			NeoIntentFactiory.getWebSearch(this);
			return true;
		case R.id.menu_dial:
			NeoIntentFactiory.getDial(this);
			return true;
		case R.id.menu_map:
			NeoIntentFactiory.getMap(this);
			return true;
		case R.id.menu_db:
			Intent db_activity = new Intent();
			db_activity.setClass(MainTabActivity.this, DBOprActivity.class);
			startActivity(db_activity);
			MainTabActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	
		
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
			if (menu.getClass().getSimpleName().equals("MenuBuilder")){
				try {
					//利用反射
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible",Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	@Override
	protected void initViews(){
		
		mViewPager = (ViewPager)findViewById(R.id.id_viewpager);
		
		one=(ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two=(ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three=(ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
		ChangeColorIconWithTextView four=(ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);
	
		mListViews.add(one);
		mListViews.add(two);
		mListViews.add(three);
		mListViews.add(four);
		
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		one.setIconAlpha(1.0f);
		
		initDatas();
		initEvents();
	}
	
	private void initDatas(){
		
		refreshList = 
				new RefreshListFragment(mApplication,this,this);
		mListFragments.add(refreshList);
		
		NeoDisPlayFragment displayView = 
				new NeoDisPlayFragment(mApplication,this,this);
		mListFragments.add(displayView);
		
		NeoBasicMapFragment neoMap = 
				new NeoBasicMapFragment(mApplication,this,this);
		mListFragments.add(neoMap);
		
		NeoSetingListFragment setingList = 
				new NeoSetingListFragment(mApplication,this,this);
		mListFragments.add(setingList);
		
		/*for(String title : titles){
			
			TabFragment tabFragment = new TabFragment();
			Bundle bd = new Bundle();
			bd.putString(TabFragment.TITLE,title);
			tabFragment.setArguments(bd);
			mListFragments.add(tabFragment);
		}*/
		
		mfPagerAdapter = new NeoViewPagerAdapter(this.getSupportFragmentManager()
				,mListFragments);
		mViewPager.setAdapter(mfPagerAdapter);
		
	}
	
	protected void initEvents(){
		//mViewPager.setOnPageChangeListener(this);
		mViewPager.addOnPageChangeListener(this);
	}
    private void initNetWorkData() {
        if (!netWorkCheck(this)) {
            return;
        }
        
        if (FileUtils.isDirEmpty(NeoAppSetings.ProfilesDir)) {
            showAlertDialog("NEO", "ni meimei");
            return;
        }
        
        getMe();
        getMyProfile();
        getMyHeadpic();
        getNearbyData();
        getFriendData();
        getMyStatusData();
        //updateServerIp();
        updateServerIpWithPost();
    }
    
    private void getMe() {
    	 NeoAsyncHttpUtil.get(this, 
         		new StringBuilder(String.valueOf(
         				NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig,
         						this.mApplication.mMe.getName()))).append("/").append("5/").toString(),
         						new JsonHttpResponseHandler() {
    		 @Override
             public void onFinish() {
                 Log.i(MainTabActivity.this.Tag, "onFinish");
             }
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 super.onSuccess(statusCode, headers, response);
                 Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                 
                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);

             }
             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                 Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                 MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
             }
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 Log.i(MainTabActivity.this.Tag, "onSuccess ");
                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                 if (response.has("errcode")){
 					try {
 						showLongToast(response.get("info").toString());
 					} catch (JSONException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
                 }else{
                	 //showAlertDialog("NEO", response.toString());
                	 MainTabActivity.this.mApplication.mMe = new People(response);
                     FileUtils.overrideContent(new StringBuilder(String.valueOf(
                     		FileUtils.getAppDataPath(MainTabActivity.this))).
                     		append(NeoAppSetings.MeFile).toString(), 
                     		response.toString());
                 }
             }
             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
             }
         });
    }
    
    private void getMyProfile() {
    	NeoAsyncHttpUtil.get(this, 
         		new StringBuilder(String.valueOf(
         				NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig,
         						this.mApplication.mMe.getName()))).append("/").append("1/").toString(),
         						new JsonHttpResponseHandler() {
    		@Override
             public void onFinish() {
                 Log.i(MainTabActivity.this.Tag, "onFinish");
             }
    		@Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 super.onSuccess(statusCode, headers, response);
                 Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                 
                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);

             }
    		@Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                 Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                 MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
             }
    		@Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 Log.i(MainTabActivity.this.Tag, "onSuccess ");
                 NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                 if (response.has("errcode")){
 					try {
 						showLongToast(response.get("info").toString());
 					} catch (JSONException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
                 }else{
                	 //showAlertDialog("NEO", response.toString());
                	 MainTabActivity.this.mApplication.mMyProfile = 
                			 new PeopleProfile(response);
                     FileUtils.overrideContent(new StringBuilder(String.valueOf(
                     		FileUtils.getAppDataPath(MainTabActivity.this))).
                     		append(NeoAppSetings.MyProfileFile).toString(), 
                     		response.toString());
                 }
             }
    		@Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
             }
         });
    }
    
    private void getMyHeadpic() {
    	if (FileUtils.isFileExist(getMyApplication().mAppDataPath+
				getMyApplication().mMe.getName())){
    		showLongToast("head pic is exist");
    		return;
    	}
    	NeoAsyncHttpUtil.get((Context) this,
    			NeoAppSetings.getGetDownLoad(
    					this.mApplication.mNeoConfig, 
    					this.mApplication.mMe.getName()+"/"+"1/"),
    					new BinaryHttpResponseHandler(NeoAsyncHttpUtil.Image_MIME)
    	 {
            public void onFinish() {
                Log.i(MainTabActivity.this.Tag, "onFinish");
            }

            public void onFailure(int arg0, Header[] arg1, byte[] response, Throwable throwable) {
                Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }

            public void onSuccess(int arg0, Header[] arg1, byte[] response) {
                Log.i(MainTabActivity.this.Tag, "onSuccess" + response.length);
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                String filename = "";
                for (int i = 0; i < arg1.length; i++) {
                    if (arg1[i].getName().equals(MIME.CONTENT_DISPOSITION)) {
                        filename = arg1[i].getValue();
                    }
                    filename = filename.substring(filename.lastIndexOf("=") + 1);
                }
                Bitmap bmp = BitmapFactory.decodeByteArray(response, 0, response.length);
                //MainTabActivity.this.showAlertDialog("NEO", filename);
                PhotoUtils.savePhotoToSDCard(bmp, 
                		FileUtils.getAppDataPath(MainTabActivity.this)
                		+mApplication.mMe.getName());
            }
        });
    }
    
    private void getNearbyData() {
    	
    	if (FileUtils.isFileExist(getMyApplication().mAppDataPath+
    			NeoAppSetings.MyNearByFile)){
    		showLongToast("My nearby file is exsit");
    		return;
    	}
    	
        NeoAsyncHttpUtil.get(this, 
        		new StringBuilder(String.valueOf(
        				NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig,
        						this.mApplication.mMe.getName()))).append("/").append("3/").toString(),
        						new JsonHttpResponseHandler() {
            public void onFinish() {
                Log.i(MainTabActivity.this.Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                //showAlertDialog("NEO", response.toString());
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                int i = 0;
                while (i < response.length()) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        MainTabActivity.this.getNearbyAndFriendProfile(object.getString(People.NAME));
                        MainTabActivity.this.mApplication.mMyNearByPeoples.add(new People(object));
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                FileUtils.overrideContent(new StringBuilder(String.valueOf(
                		FileUtils.getAppDataPath(MainTabActivity.this))).
                		append(NeoAppSetings.MyNearByFile).toString(), 
                		response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, "onSuccess ");
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                if (response.has("errcode"))
					try {
						showLongToast(response.get("info").toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }
        });
    }

    private void getFriendData() {
    	if (FileUtils.isFileExist(getMyApplication().mAppDataPath+
    			NeoAppSetings.MyFriendsFile)){
    		showLongToast("My nearby file is exsit");
    		return;
    	}
    	
        NeoAsyncHttpUtil.get((Context) this, new StringBuilder(String.valueOf(NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig, this.mApplication.mMe.getName()))).append("/").append("2/").toString(), new JsonHttpResponseHandler() {
            public void onFinish() {
                Log.i(MainTabActivity.this.Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                int i = 0;
                while (i < response.length()) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        MainTabActivity.this.getNearbyAndFriendProfile(object.getString(Setings.NAME));
                        MainTabActivity.this.mApplication.mMyFriends.
                        add(new People(object));
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                FileUtils.overrideContent(new StringBuilder(
                		String.valueOf(FileUtils.getAppDataPath(MainTabActivity.this))).
                		append(NeoAppSetings.MyFriendsFile).toString(),
                		response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, "onSuccess ");
                //MainTabActivity.this.showNeoJsoErrorCodeToast(response);
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                if ((response.has("errcode"))) {
				    MainTabActivity.this.showLongToast(response.toString());
				}
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }
        });
    }

    private void getNearbyAndFriendProfile(String name) {
        NeoAsyncHttpUtil.get((Context) this, new StringBuilder(String.valueOf(NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig, name))).append("/").append("1/").toString(), new JsonHttpResponseHandler() {
            public void onFinish() {
                Log.i(MainTabActivity.this.Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                MainTabActivity.this.showAlertDialog("NEO", "JSONArray:" + response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, "onSuccess ");
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                try {
                    if (!response.has("errcode")) {
                        MainTabActivity.this.getOtherHeadImages(response.getString(People.NAME));
                        FileUtils.overrideContent(new StringBuilder(
                        		String.valueOf(FileUtils.getAppDataPath(MainTabActivity.this)))
                        .append(NeoAppSetings.ProfilesDir).
                        append(response.getString(Setings.NAME)).append(".json").toString(),
                        response.toString());
                    } else if (response.getString("errcode").equals(NEO_ERRCODE.DATA_NOEXIST.toString())) {
                        MainTabActivity.this.showAlertDialog("NEO", response.getString("info"));
                    } else if (response.getString("errcode").equals(NEO_ERRCODE.UER_NOLOGIN.toString())) {
                        MainTabActivity.this.showAlertDialog("NEO", response.getString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }
        });
    }

    private void getMyStatusData() {
        NeoAsyncHttpUtil.get((Context) this, new StringBuilder(String.valueOf(NeoAppSetings.getGetUrl(this.mApplication.mNeoConfig, this.mApplication.mMe.getName()))).append("/").append("4/").toString(), new JsonHttpResponseHandler() {
            public void onFinish() {
                Log.i(MainTabActivity.this.Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, new StringBuilder(String.valueOf(response.length())).toString());
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                FileUtils.overrideContent(new StringBuilder(String.valueOf(FileUtils.getAppDataPath(MainTabActivity.this))).append(NeoAppSetings.MyStatusFile).toString(), response.toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(MainTabActivity.this.Tag, "onSuccess ");
                //MainTabActivity.this.showNeoJsoErrorCodeToast(response);
                NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
                try {
                    if (!response.getString("errcode").equals(NEO_ERRCODE.LOGIN_SUCCESS.toString())) {
                        //MainTabActivity.this.showAlertDialog("NEO", response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
            }
        });
    }

    private void getOtherHeadImages(String name) {
        NeoAsyncHttpUtil.get((Context) this, 
        		NeoAppSetings.getGetDownLoad(this.mApplication.mNeoConfig, name)+"/"+"1/",
        		new BinaryHttpResponseHandler(NeoAsyncHttpUtil.Image_MIME) {

	        public void onFinish() {
	            Log.i(MainTabActivity.this.Tag, "onFinish");
	        }

	        public void onFailure(int arg0, Header[] arg1, byte[] response, Throwable throwable) {
	            Log.e(MainTabActivity.this.Tag, " onFailure" + throwable.toString());
	            MainTabActivity.this.showAlertDialog("NEO", throwable.toString());
	        }

	        public void onSuccess(int arg0, Header[] arg1, byte[] response) {
	            Log.i(MainTabActivity.this.Tag, "onSuccess" + response.length);
	            NeoAsyncHttpUtil.addPersistCookieToGlobaList(MainTabActivity.this);
	            String filename = BuildConfig.VERSION_NAME;
	            for (int i = 0; i < arg1.length; i++) {
	                if (arg1[i].getName().equals(MIME.CONTENT_DISPOSITION)) {
	                    filename = arg1[i].getValue();
	                }
	                filename = filename.substring(filename.lastIndexOf("=") + 1);
	            }
	            Bitmap bmp = BitmapFactory.decodeByteArray(response, 0, response.length);
	            //MainTabActivity.this.showAlertDialog("NEO", filename);
	            PhotoUtils.savePhotoToSDCard(bmp, new StringBuilder(String.valueOf(
	            		FileUtils.getAppDataPath(MainTabActivity.this))).append(NeoAppSetings.HeadPicDir).append(filename).toString());

	        }
	    });
    }
    
    /*{
	    "name":"erjung",
	    "uid":0,
	    "ip":"101.80.178.57",
	    "port":7000,
	    "localip":"",
	    "netstate":0
	}*/
    
    private void updateServerIpWithPost(){
    	if (!netWorkCheck(this)) {
    		return;
    	}
        JSONObject jsonObject = new JSONObject();
        StringEntity stringEntity = null;

        try {
			jsonObject.put("name", mApplication.mMe.getName());
			jsonObject.put("uid",  mApplication.mMe.getUid());
			if (mApplication.netWorkState==NetWorkState.WIFI){
				jsonObject.put("ip", "");
				jsonObject.put("localip", NetWorkUtils.getWifiIpAddress(this));
				jsonObject.put("netstate", 0);
			}else if (mApplication.netWorkState==NetWorkState.MOBILE){
				jsonObject.put("ip", NetWorkUtils.getGPRSIpAddress());
				jsonObject.put("localip", "");
				jsonObject.put("netstate", 1);
			}
			jsonObject.put("port", 7000);
			stringEntity = new StringEntity(jsonObject.toString());
	        stringEntity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        NeoAsyncHttpUtil.postJson(this, NeoAppSetings.DestIpUpdateUrlPrefix,stringEntity,
        		new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray arg0) {
                Log.i(Tag, new StringBuilder(String.valueOf(arg0.length())).toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }

            public void onFinish() {
                Log.i(Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
					Log.i(Tag, "onSuccess"+":"+response.getString("info").toString());
					MainTabActivity.this.showLongToast(response.getString("info").toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }
        });
	}
	
    private void updateServerIp(){
		
        NeoAsyncHttpUtil.get(this, NeoAppSetings.DestIpUpdateUrlPrefix+mApplication.mMe.getName(),
        		new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray arg0) {
                Log.i(Tag, new StringBuilder(String.valueOf(arg0.length())).toString());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }

            public void onFinish() {
                Log.i(Tag, "onFinish");
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
					Log.i(Tag, "onSuccess"+":"+response.getString("info").toString());
					MainTabActivity.this.showLongToast(response.getString("info").toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(Tag, " onFailure" + throwable.toString());
            }
        });
	}

    private void initService(){
		//�������ط���
		Intent bgsvc = new Intent(MainTabActivity.this,
				NeoAppBackgroundService.class);
		bgsvc.putExtra("counter", counter++);
		startService(bgsvc);
	}
	
	private void stopService(){
		Intent bgsvc = new Intent(MainTabActivity.this,
				NeoAppBackgroundService.class);
		stopService(bgsvc);
	}
	
	private void initWorkerThread(){
		//�����������߳�
		mtMainActivityWorker = new ServiceWorkerWithLooper("MainActivity Worker thread",
				MainTabActivity.this);
		mWorkerThreadHandler = new NeoAppWorkerThreadHandler
				(mtMainActivityWorker.getLooper());
		mWorkerThreadHandler.sendMessage(1);
	}
	
	private void sendNotifications(){
		//send notifiation
		NeoAppBroadCastMessages.sendStaticBroadCastTestMsg(this,"MainTabActivity.class","MainTabActivity onCreate");
	}
	
	private void setOverflowButtonAlways(){
		try {
			ViewConfiguration viewconfig=ViewConfiguration.get(this);
			//利用反射
			Field menuKey=ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(viewconfig, false);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		onTabClick(view);
	}
	
	private void onTabClick(View view){
		resetOthersTab();
		updateOneMsgFlag();
		switch(view.getId()){
		case R.id.id_indicator_one:
			mListViews.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0,false);
			break;
		case R.id.id_indicator_two:
			mListViews.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1,false);
			break;
		case R.id.id_indicator_three:
			mListViews.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2,false);
			break;
		case R.id.id_indicator_four:
			mListViews.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3,false);
			break;
		}
	}

	private void resetOthersTab() {
		// TODO Auto-generated method stub
		for (int i=0;i<mListViews.size();i++){
			mListViews.get(i).setIconAlpha(0.0f);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		if(positionOffset>0){
			ChangeColorIconWithTextView left = mListViews.get(position);
			ChangeColorIconWithTextView right = mListViews.get(position+1);
			left.setIconAlpha(1-positionOffset);
			right.setIconAlpha(positionOffset);
		}
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//init the msg receiver
    	msgBroadCastReceiver = new MsgReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(NeoAppBroadCastMessages.broadcastAction);
		registerReceiver(msgBroadCastReceiver, filter);
		
		updateOneMsgFlag();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if (msgBroadCastReceiver!=null)
    		unregisterReceiver(msgBroadCastReceiver);
	}
	
	public void updateOneMsgFlag(){
		one.setMsgCount(NeoSocketMessageCacheUtil.getIntance().getAllMessageCount());
		one.invalidate();
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
				if (mViewPager.getCurrentItem()!=0)
					NeoSocketMessageCacheUtil.getIntance().
					addMessage(msg.getName(), msg);
				updateOneMsgFlag();
			}
		
		}
	}
}
