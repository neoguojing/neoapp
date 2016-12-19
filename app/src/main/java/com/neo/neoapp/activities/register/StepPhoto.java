package com.neo.neoapp.activities.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.PhotoUtils;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.activities.LoginActivity;
import com.neo.neoapp.dialog.EditTextDialog;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.httpclient.android.BuildConfig;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

public class StepPhoto extends RegisterStep implements OnClickListener {
    private String Tag;
    private String[] mAvatars;
    private String[] mDistances;
    private EditTextDialog mEditTextDialog;
    private NeoBasicTextView mHtvRecommendation;
    private ImageView mIvUserPhoto;
    private LinearLayout mLayoutAvatars;
    private LinearLayout mLayoutSelectPhoto;
    private LinearLayout mLayoutTakePicture;
    private View[] mMemberBlocks;
    private String mTakePicturePath;
    private Bitmap mUserPhoto;


	public StepPhoto(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
        this.Tag = "StepPhoto";
        this.mAvatars = new String[]{"welcome_0", "welcome_1", "welcome_2", "welcome_3", "welcome_4", "welcome_5"};
        this.mDistances = new String[]{"0.84km", "1.02km", "1.34km", "1.88km", "2.50km", "2.78km"};
	}


	public void setUserPhoto(Bitmap bitmap) {
		if (bitmap != null) {
			mUserPhoto = bitmap;
			mIvUserPhoto.setImageBitmap(mUserPhoto);
			return;
		}
		showCustomToast("未获取到图片");
		mUserPhoto = null;
		mIvUserPhoto.setImageResource(R.drawable.ic_common_def_header);
	}

	public String getTakePicturePath() {
		return mTakePicturePath;
	}

    public void setTakePicturePath(String path) {
        this.mTakePicturePath = path;
    }
	@Override
	public void initViews() {
		mHtvRecommendation = (NeoBasicTextView) findViewById(R.id.reg_photo_htv_recommendation);
		mIvUserPhoto = (ImageView) findViewById(R.id.reg_photo_iv_userphoto);
		mLayoutSelectPhoto = (LinearLayout) findViewById(R.id.reg_photo_layout_selectphoto);
		mLayoutTakePicture = (LinearLayout) findViewById(R.id.reg_photo_layout_takepicture);
	}

	@Override
	public void initEvents() {
		mHtvRecommendation.setOnClickListener(this);
		mLayoutSelectPhoto.setOnClickListener(this);
		mLayoutTakePicture.setOnClickListener(this);
	}

	@Override
	public boolean validate() {
		if (mUserPhoto == null) {
			showCustomToast("请添加头像");
			return false;
		}
		return true;
	}

    private void initMe() {
        this.mActivity.getMyApplication().mMe = new People();
        this.mActivity.getMyApplication().mMe.setAge(Integer.parseInt(this.mActivity.getAge()));
        this.mActivity.getMyApplication().mMe.setBirthday(this.mActivity.getBirthday());
        this.mActivity.getMyApplication().mMe.setDevice(0);
        this.mActivity.getMyApplication().mMe.setDistance("0km");
        this.mActivity.getMyApplication().mMe.setGender(this.mActivity.getGender());
        this.mActivity.getMyApplication().mMe.setIndustry(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMe.setIsbindRenRen(0);
        this.mActivity.getMyApplication().mMe.setIsbindTxWeibo(0);
        this.mActivity.getMyApplication().mMe.setIsbindWeibo(0);
        this.mActivity.getMyApplication().mMe.setIsGroupRole(0);
        this.mActivity.getMyApplication().mMe.setIsMultipic(0);
        this.mActivity.getMyApplication().mMe.setIsRelation(1);
        this.mActivity.getMyApplication().mMe.setIsVip(0);
        this.mActivity.getMyApplication().mMe.setLongitude(this.mActivity.getMyApplication().mLongitude);
        this.mActivity.getMyApplication().mMe.setLatitude(this.mActivity.getMyApplication().mLatitude);
        this.mActivity.getMyApplication().mMe.setName(this.mActivity.getUsername());
        this.mActivity.getMyApplication().mMe.setSign(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMe.setTime(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMe.setUid(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMe.setAvatar(this.mTakePicturePath.substring(this.mTakePicturePath.lastIndexOf("/") + 1));
    }

    private void initMyProfile() {
        this.mActivity.getMyApplication().mMyProfile = new PeopleProfile();
        this.mActivity.getMyApplication().mMyProfile.setAge(Integer.parseInt(this.mActivity.getAge()));
        this.mActivity.getMyApplication().mMyProfile.setConstellation(this.mActivity.getConstellation());
        this.mActivity.getMyApplication().mMyProfile.setDistance("0km");
        this.mActivity.getMyApplication().mMyProfile.setGender(this.mActivity.getGender());
        this.mActivity.getMyApplication().mMyProfile.setHasSign(false);
        this.mActivity.getMyApplication().mMyProfile.setSign(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMyProfile.setName(this.mActivity.getUsername());
        this.mActivity.getMyApplication().mMyProfile.setPhotos(null);
        this.mActivity.getMyApplication().mMyProfile.setSignDistance("0.0km");
        this.mActivity.getMyApplication().mMyProfile.setSignPicture(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMyProfile.setTime(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMyProfile.setUid(BuildConfig.VERSION_NAME);
        this.mActivity.getMyApplication().mMyProfile.setAvatar(this.mTakePicturePath.substring(this.mTakePicturePath.lastIndexOf("/") + 1));
    }

    private void writeDataToLocal() {
    	
        FileUtils.overrideContent(new StringBuilder(
        		String.valueOf(FileUtils.getAppDataPath(this.mActivity))).
        		append(NeoAppSetings.MeFile).toString(), 
        		this.mActivity.getMyApplication().mMe.convertToJson().toString());
        FileUtils.overrideContent(new StringBuilder(
        		String.valueOf(FileUtils.getAppDataPath(this.mActivity))).
        		append(NeoAppSetings.MyProfileFile).toString(), 
        		this.mActivity.getMyApplication().mMyProfile.convertToJson().toString());
        FileUtils.moveFile(this.mTakePicturePath, 
        		FileUtils.getAppDataPath(this.mActivity)+
        		mActivity.getMyApplication().mMe.getName());
    }

    private void doDataRegister() {
        JSONException e1;
        StringEntity stringEntity = null;
        StringEntity stringEntity2 = null;
		JSONObject jSONObject;
		JSONObject jsonObject = new JSONObject();
		try {
		    jsonObject.put("step", "6");
		    jsonObject.put("username", this.mActivity.getUsername());
		    jsonObject.put("first_name", BuildConfig.VERSION_NAME);
		    jsonObject.put("last_name", BuildConfig.VERSION_NAME);
		    jsonObject.put("email", BuildConfig.VERSION_NAME);
		    jsonObject.put("password", this.mActivity.getPassword());
		    jsonObject.put("is_staff", "0");
		    jsonObject.put("is_active", "1");
		    jsonObject.put("phone", this.mActivity.getPhoneNumber());
		    jsonObject.put(PeopleProfile.GENDER, this.mActivity.getGender());
		    jsonObject.put(People.BIRTHDAY, this.mActivity.getBirthday());
		    jsonObject.put(PeopleProfile.CONSTELLATION, this.mActivity.getConstellation());
		    jsonObject.put(People.VIP, "0");
		    jsonObject.put("grouprole", "0");
		    jsonObject.put(People.INDUSTRY, "0");
		    jsonObject.put(People.DEVICE, "0");
		    jsonObject.put(People.MULTIPIC, "0");
		    stringEntity2 = new StringEntity(jsonObject.toString(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
		} catch (JSONException e) {
		    e1 = e;
		    jSONObject = jsonObject;
		    e1.printStackTrace();
		    if (this.mActivity.registerUrl == BuildConfig.VERSION_NAME) {
		    }
		    NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
		        public void onFinish() {
		            Log.i(StepPhoto.this.Tag, "onFinish");
		        }

		        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		            super.onSuccess(statusCode, headers, response);
		            Log.i(StepPhoto.this.Tag, "onSuccess ");
		            try {
		                //StepPhoto.this.mActivity.showNeoJsoErrorCodeToast(response);
		                NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepPhoto.this.mActivity);
		                if (response.getString("errcode").equals(NEO_ERRCODE.REGISTER_SUCCESS.toString())) {
		                    StepPhoto.this.mActivity.showLongToast("注册成功f");
		                } else {
		                    StepPhoto.this.mActivity.showLongToast(response.getString("info"));
		                }
		            } catch (Exception e) {
		                Log.e(StepPhoto.this.Tag, e.toString());
		            }
		        }

		        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		            Log.e(StepPhoto.this.Tag, " onFailure" + throwable.toString());
		            StepPhoto.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
		        }
		    });
		}
		stringEntity2.setContentEncoding(AsyncHttpResponseHandler.DEFAULT_CHARSET);
		stringEntity2.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
		jSONObject = jsonObject;
		stringEntity = stringEntity2;
        if (this.mActivity.registerUrl == BuildConfig.VERSION_NAME || this.mActivity.picUploadUrl != BuildConfig.VERSION_NAME) {
            NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                public void onFinish() {
                    Log.i(StepPhoto.this.Tag, "onFinish");
                }

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i(StepPhoto.this.Tag, "onSuccess ");
                    try {
                        //StepPhoto.this.mActivity.showNeoJsoErrorCodeToast(response);
                        NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepPhoto.this.mActivity);
                        if (response.getString("errcode").equals(NEO_ERRCODE.REGISTER_SUCCESS.toString())) {
                            StepPhoto.this.mActivity.showLongToast("注册成功");
                        } else {
                            StepPhoto.this.mActivity.showLongToast(response.getString("info"));
                        }
                    } catch (Exception e) {
                        Log.e(StepPhoto.this.Tag, e.toString());
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(StepPhoto.this.Tag, " onFailure" + throwable.toString());
                    StepPhoto.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                }
            });
        } else {
            this.mActivity.showAlertDialog("NEO", "url is null");
        }
    }

    private void doHeadPicRegister() {
        //this.mActivity.showAlertDialog("NEO", this.mTakePicturePath);
        File file = new File(this.mTakePicturePath);
        if (!file.exists() || file.length() <= 0) {
            this.mActivity.showAlertDialog("NEO", "\u95ee\u4ef6\u4e0d\u5b58\u5728");
            return;
        }
        RequestParams params = new RequestParams();
        try {
            params.put("username", this.mActivity.getUsername());
            params.put(NeoAppSetings.MyHeadPic, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        NeoAsyncHttpUtil.post(this.mActivity, this.mActivity.picUploadUrl, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                        StepPhoto.this.mActivity.startActivity(new Intent(StepPhoto.this.mActivity, LoginActivity.class));
                        StepPhoto.this.mActivity.finish();
                        return;
                    }
                    StepPhoto.this.mActivity.showLongToast(response.getString("info"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                StepPhoto.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
            }
        });
    }

    private void doAppDataInit() {
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                StepPhoto.this.showLoadingDialog("\u8bf7\u7a0d\u540e,\u6b63\u5728\u63d0\u4ea4...");
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    StepPhoto.this.initMe();
                    StepPhoto.this.initMyProfile();
                    StepPhoto.this.writeDataToLocal();
                    return Boolean.valueOf(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                StepPhoto.this.dismissLoadingDialog();
            }
        });
    }

    private void doRegister() {
        doDataRegister();
        doHeadPicRegister();
    }
    
	@Override
    public void doNext() {
        doAppDataInit();
        doRegister();
    }
		

	@Override
	public boolean isChange() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_photo_htv_recommendation:
			mEditTextDialog = new EditTextDialog(mContext);
			mEditTextDialog.setTitle("填写推荐人");
			mEditTextDialog.setButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mEditTextDialog.cancel();
						}
					}, "确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String text = mEditTextDialog.getText();
							if (text == null) {
								mEditTextDialog.requestFocus();
								showCustomToast("请输入推荐人号码");
							} else {
								mEditTextDialog.dismiss();
								showCustomToast("您输入的推荐人号码为:" + text);
							}
						}
					});
			mEditTextDialog.show();
			break;

		case R.id.reg_photo_layout_selectphoto:
			PhotoUtils.selectPhoto(mActivity);
			break;

		case R.id.reg_photo_layout_takepicture:
			mTakePicturePath = PhotoUtils.takePicture(mActivity);
			break;
		}
	}

}
