package com.neo.neoapp.activities.register;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.R;
import com.neo.neoapp.dialog.BaseDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.httpclient.android.BuildConfig;
import java.io.UnsupportedEncodingException;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

public class StepBaseInfo extends RegisterStep implements TextWatcher,
		OnCheckedChangeListener {

    private final String Tag;
    private BaseDialog mBaseDialog;
    private EditText mEtName;
    private int mGender;
    private boolean mIsChange;
    private boolean mIsGenderAlert;
    private RadioButton mRbFemale;
    private RadioButton mRbMale;
    private RadioGroup mRgGender;
    private String mUserName;

    public StepBaseInfo(RegisterActivity activity, View contentRootView) {
        super(activity, contentRootView);
        this.Tag = "StepBaseInfo";
        this.mIsChange = true;
        this.mUserName = BuildConfig.VERSION_NAME;
        this.mGender = 0;
    }

	@Override
	public void initViews() {
		mEtName = (EditText) findViewById(R.id.reg_baseinfo_et_name);
		mRgGender = (RadioGroup) findViewById(R.id.reg_baseinfo_rg_gender);
		mRbMale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_male);
		mRbFemale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_female);
	}

	@Override
	public void initEvents() {
		mEtName.addTextChangedListener(this);
		mRgGender.setOnCheckedChangeListener(this);
	}

    private void onlineNameVerify() {
        JSONException e1;
        UnsupportedEncodingException e;
        StringEntity stringEntity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("step", "4");
            if (getmUserName().equals(null) || getmUserName().equals(BuildConfig.VERSION_NAME)) {
                this.mActivity.showAlertDialog("NEO", "username is null or ''");
                return;
            }
            jsonObject.put("username", getmUserName());
            StringEntity stringEntity2 = new StringEntity(jsonObject.toString());
            stringEntity2.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
			stringEntity = stringEntity2;
            if (this.mActivity.registerUrl == null) {
                this.mActivity.showAlertDialog("NEO", "url is null");
            } else {
                NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i("StepBaseInfo", "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("StepBaseInfo", "onSuccess ");
                        try {
                            StepBaseInfo.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepBaseInfo.this.mActivity);
                            if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                                StepBaseInfo.this.mIsChange = false;
                                StepBaseInfo.this.mOnNextActionListener.next();
                                return;
                            }
                            StepBaseInfo.this.mActivity.showAlertDialog("NEO", response.getString("info"));
                            StepBaseInfo.this.mUserName = null;
                            StepBaseInfo.this.mEtName.requestFocus();
                            StepBaseInfo.this.mEtName.setText(BuildConfig.VERSION_NAME);
                        } catch (Exception e) {
                            Log.e("StepBaseInfo", e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("StepBaseInfo", " onFailure" + throwable.toString());
                        StepBaseInfo.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                    }
                });
            }
        } catch (JSONException e4) {
            e1 = e4;
            e1.printStackTrace();
            if (this.mActivity.registerUrl == null) {
                NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i("StepBaseInfo", "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("StepBaseInfo", "onSuccess ");
                        try {
                            StepBaseInfo.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepBaseInfo.this.mActivity);
                            if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                                StepBaseInfo.this.mIsChange = false;
                                StepBaseInfo.this.mOnNextActionListener.next();
                                return;
                            }
                            StepBaseInfo.this.mActivity.showAlertDialog("NEO", response.getString("info"));
                            StepBaseInfo.this.mUserName = null;
                            StepBaseInfo.this.mEtName.requestFocus();
                            StepBaseInfo.this.mEtName.setText(BuildConfig.VERSION_NAME);
                        } catch (Exception e) {
                            Log.e("StepBaseInfo", e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("StepBaseInfo", " onFailure" + throwable.toString());
                        StepBaseInfo.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                    }
                });
            } else {
                this.mActivity.showAlertDialog("NEO", "url is null");
            }
        } catch (UnsupportedEncodingException e5) {
            e = e5;
            e.printStackTrace();
            if (this.mActivity.registerUrl == null) {
                this.mActivity.showAlertDialog("NEO", "url is null");
            } else {
                NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i("StepBaseInfo", "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("StepBaseInfo", "onSuccess ");
                        try {
                            StepBaseInfo.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepBaseInfo.this.mActivity);
                            if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                                StepBaseInfo.this.mIsChange = false;
                                StepBaseInfo.this.mOnNextActionListener.next();
                                return;
                            }
                            StepBaseInfo.this.mActivity.showAlertDialog("NEO", response.getString("info"));
                            StepBaseInfo.this.mUserName = null;
                            StepBaseInfo.this.mEtName.requestFocus();
                            StepBaseInfo.this.mEtName.setText(BuildConfig.VERSION_NAME);
                        } catch (Exception e) {
                            Log.e("StepBaseInfo", e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("StepBaseInfo", " onFailure" + throwable.toString());
                        StepBaseInfo.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                    }
                });
            }
        }
    }
	@Override
	public void doNext() {
        onlineNameVerify();
	}

	@Override
	public boolean validate() {
		if (isNull(mEtName)) {
			showCustomToast("请输入用户名");
			mEtName.requestFocus();
			return false;
		}
        this.mUserName = this.mEtName.getText().toString().trim();
		if (mRgGender.getCheckedRadioButtonId() < 0) {
			showCustomToast("请选择性别");
			return false;
		}
		return true;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mIsChange = true;
		if (!mIsGenderAlert) {
			mIsGenderAlert = true;
			mBaseDialog = BaseDialog.getDialog(mContext, "提示", "注册成功后性别将不可更改",
					"确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			mBaseDialog.show();
		}
		switch (checkedId) {
		case R.id.reg_baseinfo_rb_male:
			mRbMale.setChecked(true);
                this.mGender = this.mRbMale.getId();
			break;

		case R.id.reg_baseinfo_rb_female:
			mRbFemale.setChecked(true);
                this.mGender = this.mRbFemale.getId();
			break;
		}
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
		mIsChange = true;
	}

    public String getmUserName() {
        return this.mUserName;
    }

    public int getmGender() {
        return this.mGender;
    }
}
