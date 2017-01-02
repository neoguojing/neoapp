package com.neo.neoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.JsonResolveUtils;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoAppSetings.LOGIN_STATE;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.activities.register.RegisterActivity;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.entity.NeoConfig;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;
import com.neo.neoapp.services.NeoAppBackgroundService;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.ClientCookie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends NeoBasicActivity implements OnClickListener {
    private String Tag;
    LOGIN_STATE loginstate;
    private String[] mAvatars;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private String[] mDistances;
    private ImageButton mIbtnAbout;
    private LinearLayout mLinearAvatars;
    private LinearLayout mLinearCtrlbar;
    private View[] mMemberBlocks;

    public WelcomeActivity() {
        this.loginstate = null;
        this.Tag = "WelcomeActivity";
        this.mAvatars = new String[]{"welcome_0", "welcome_1", "welcome_2", "welcome_3", "welcome_4", "welcome_5"};
        this.mDistances = new String[]{"0.84km", "1.02km", "1.34km", "1.88km", "2.50km", "2.78km"};
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_welcome);
        initViews();
        initEvents();
        initData();
        showWelcomeAnimation();
        
        initService();
	}

	private void initService(){
		Intent bgsvc = new Intent(WelcomeActivity.this,
				NeoAppBackgroundService.class);
		startService(bgsvc);
	}
	
	private void stopService(){
		Intent bgsvc = new Intent(WelcomeActivity.this,
				NeoAppBackgroundService.class);
		stopService(bgsvc);
	}
	
	@Override
	protected void initViews() {
		mLinearCtrlbar = (LinearLayout) findViewById(R.id.welcome_linear_ctrlbar);
		mLinearAvatars = (LinearLayout) findViewById(R.id.welcome_linear_avatars);
		mBtnRegister = (Button) findViewById(R.id.welcome_btn_register);
		mBtnLogin = (Button) findViewById(R.id.welcome_btn_login);
		mIbtnAbout = (ImageButton) findViewById(R.id.welcome_ibtn_about);
	}

	@Override
	protected void initEvents() {
		mBtnRegister.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mIbtnAbout.setOnClickListener(this);
	}
	
	private void initData() {
        initAppDirs();
        loadMyData();
	}


    private void initAppDirs() {
    	
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                WelcomeActivity.this.showLoadingDialog("\u8bf7\u7a0d\u540e,\u6b63\u5728\u521d\u59cb\u5316...");
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    Boolean rtn = Boolean.valueOf(true);
                    String prefix = FileUtils.getAppDataPath(WelcomeActivity.this);
                	getMyApplication().mAppDataPath = prefix;
                	if (getMyApplication().mAppDataPath.equals("")){
                		return false;
                	}
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.HeadPicDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.MyPhotosDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.MyPhotosOriginalDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.MyPhotosThumbnailDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.ProfilesDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.StatuPhotosDir).toString());
                	rtn &= FileUtils.createDirFile(new StringBuilder(String.valueOf(prefix)).append(NeoAppSetings.StatusDir).toString());
                	rtn &= FileUtils.createDirFile(prefix+NeoAppSetings.UserMsgDir);
                    return rtn;
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                WelcomeActivity.this.dismissLoadingDialog();
                if (!result.booleanValue()) {
                    WelcomeActivity.this.showAlertDialog("NEO", "create dir faied");
                }
            }
        });
    }

    private void loadMyData() {
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                WelcomeActivity.this.showLoadingDialog("\u8bf7\u7a0d\u540e,\u6b63\u5728\u521d\u59cb\u5316...");
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    if (People.resolveMe(WelcomeActivity.this.mApplication, WelcomeActivity.this) 
                    		&& PeopleProfile.resolveMyProfile(WelcomeActivity.this.mApplication, WelcomeActivity.this)) {
                        return Boolean.valueOf(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Boolean.valueOf(false);
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                WelcomeActivity.this.dismissLoadingDialog();
                if (!result.booleanValue()) {
                	showLongToast("json file does not exist");
                }
            }
        });
    }

    private LOGIN_STATE checkLoginState() {
        if (netWorkCheck(this)) {

            NeoAsyncHttpUtil.get(this, NeoAppSetings.getLoginCheckUrl(
            		this.mApplication.mNeoConfig), new JsonHttpResponseHandler() {
                public void onFinish() {
                    Log.i(WelcomeActivity.this.Tag, "onFinish");
                }

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i(WelcomeActivity.this.Tag, "onSuccess ");
                    try {
                        //WelcomeActivity.this.showNeoJsoErrorCodeToast(response);
                        NeoAsyncHttpUtil.addPersistCookieToGlobaList(WelcomeActivity.this);
                        if (response.getString("errcode").equals(NEO_ERRCODE.LOGIN_SUCCESS.toString())) {
                            WelcomeActivity.this.loginstate = LOGIN_STATE.LOGIN;
                            WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, MainTabActivity.class));
                            WelcomeActivity.this.finish();
                        } else if (response.getString("errcode").equals(NEO_ERRCODE.UER_NOLOGIN.toString())) {
                            WelcomeActivity.this.showLongToast(response.getString("info"));
                            WelcomeActivity.this.loginstate = LOGIN_STATE.NOLOGIN;
                            WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            WelcomeActivity.this.finish();
                        }
                    } catch (Exception e) {
                        Log.e(WelcomeActivity.this.Tag, e.toString());
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(WelcomeActivity.this.Tag, " onFailure" + throwable.toString());
                    WelcomeActivity.this.showAlertDialog("NEO", "exception:" + throwable.toString());
                    WelcomeActivity.this.loginstate = LOGIN_STATE.HTTPERR;
                }
            });
            return this.loginstate;
        }
        
        LOGIN_STATE login_state = LOGIN_STATE.OFFLINE;
        this.loginstate = login_state;
        return login_state;
    }
    
	private void initAvatarsItem() {
		initMemberBlocks();
		for (int i = 0; i < mMemberBlocks.length; i++) {
			((ImageView) mMemberBlocks[i]
					.findViewById(R.id.welcome_item_iv_avatar))
					.setImageBitmap(mApplication.getAvatar(mAvatars[i]));
			((NeoBasicTextView) mMemberBlocks[i]
					.findViewById(R.id.welcome_item_htv_distance))
					.setText(mDistances[i]);
		}
	}

	private void initMemberBlocks() {
		mMemberBlocks = new View[6];
		mMemberBlocks[0] = findViewById(R.id.welcome_include_member_avatar_block0);
		mMemberBlocks[1] = findViewById(R.id.welcome_include_member_avatar_block1);
		mMemberBlocks[2] = findViewById(R.id.welcome_include_member_avatar_block2);
		mMemberBlocks[3] = findViewById(R.id.welcome_include_member_avatar_block3);
		mMemberBlocks[4] = findViewById(R.id.welcome_include_member_avatar_block4);
		mMemberBlocks[5] = findViewById(R.id.welcome_include_member_avatar_block5);

		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		int widthAndHeight = (mScreenWidth - margin * 12) / 6;
		for (int i = 0; i < mMemberBlocks.length; i++) {
			ViewGroup.LayoutParams params = mMemberBlocks[i].findViewById(
					R.id.welcome_item_iv_avatar).getLayoutParams();
			params.width = widthAndHeight;
			params.height = widthAndHeight;
			mMemberBlocks[i].findViewById(R.id.welcome_item_iv_avatar)
					.setLayoutParams(params);
		}
		mLinearAvatars.invalidate();
	}

	private void showWelcomeAnimation() {
		Animation animation = AnimationUtils.loadAnimation(
				WelcomeActivity.this, R.anim.welcome_ctrlbar_slideup);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mLinearAvatars.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mLinearAvatars.setVisibility(View.VISIBLE);
					}
				}, 800);
			}
		});
		mLinearCtrlbar.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.welcome_btn_register:
			startActivity(RegisterActivity.class);
			break;

		case R.id.welcome_btn_login:
            if (checkLoginState() == LOGIN_STATE.NOLOGIN) {
                startActivity(LoginActivity.class);
            }
			break;

		case R.id.welcome_ibtn_about:
			//startActivity(AboutTabsActivity.class);
			break;
		}
	}
}
