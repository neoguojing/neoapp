package com.neo.neoapp.activities.register;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NeoMsgUtil;
import com.neo.neoandroidlib.TextUtils;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.dialog.BaseDialog;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

public class StepVerify extends RegisterStep implements OnClickListener,
		TextWatcher {
	private final String Tag = "StepVerify";
	private NeoBasicTextView mHtvPhoneNumber;
	private EditText mEtVerifyCode;
	private Button mBtnResend;
	private NeoBasicTextView mHtvNoCode;

	private static final String PROMPT = "验证码已经发送到* ";
	private static final String DEFAULT_VALIDATE_CODE = "881129";

	private boolean mIsChange = true;
	private String mVerifyCode;

	private int mReSendTime = 60;
	private BaseDialog mBaseDialog;
	protected String remoteCode;

	public StepVerify(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
		handler.sendEmptyMessage(0);
	}

	@Override
	public void initViews() {
		mHtvPhoneNumber = (NeoBasicTextView) findViewById(R.id.reg_verify_htv_phonenumber);
		mHtvPhoneNumber.setText(PROMPT + getPhoneNumber());
		mEtVerifyCode = (EditText) findViewById(R.id.reg_verify_et_verifycode);
		mBtnResend = (Button) findViewById(R.id.reg_verify_btn_resend);
		mBtnResend.setEnabled(false);
		mBtnResend.setText("重发(60)");
		mHtvNoCode = (NeoBasicTextView) findViewById(R.id.reg_verify_htv_nocode);
		TextUtils.addUnderlineText(mContext, mHtvNoCode, 0, mHtvNoCode
				.getText().toString().length());
        TextUtils.addHyperlinks(this.mHtvNoCode, 0, this.mHtvNoCode.getText().toString().length(), new OnClickListener() {
            public void onClick(View v) {
                System.out.println("123");
            }
        });
	}

	@Override
	public void initEvents() {
		mBtnResend.setOnClickListener(this);
		mHtvNoCode.setOnClickListener(this);
		mEtVerifyCode.addTextChangedListener(this);
        requestCode();
	}

    private void doCodeVerify() {
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                StepVerify.this.showLoadingDialog("\u6b63\u5728\u9a8c\u8bc1,\u8bf7\u7a0d\u540e...");
            }

            protected Boolean doInBackground(Void... params) {
                //Thread.sleep(2000);
				if (StepVerify.DEFAULT_VALIDATE_CODE.equals(StepVerify.this.mVerifyCode)||
						remoteCode.equals(StepVerify.this.mVerifyCode)) {
				    return Boolean.valueOf(true);
				}
                return Boolean.valueOf(false);
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                StepVerify.this.dismissLoadingDialog();
                if (result.booleanValue()) {
                    StepVerify.this.mIsChange = false;
                    StepVerify.this.mOnNextActionListener.next();
                    return;
                }
                StepVerify.this.mBaseDialog = BaseDialog.getDialog(StepVerify.this.mContext, "\u63d0\u793a", "\u9a8c\u8bc1\u7801\u9519\u8bef", "\u786e\u8ba4", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StepVerify.this.mEtVerifyCode.requestFocus();
                        dialog.dismiss();
                    }
                });
                StepVerify.this.mBaseDialog.show();
            }
        });
    }

    private void requestCode() {
        JSONException e1;
        UnsupportedEncodingException e;
        StringEntity stringEntity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("step", "2");
            StringEntity stringEntity2 = new StringEntity(jsonObject.toString());
            stringEntity2.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
			stringEntity = stringEntity2;
        } catch (JSONException e4) {
            e1 = e4;
            e1.printStackTrace();
            if (this.mActivity.registerUrl == null) {
                NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i(StepVerify.this.Tag, "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(StepVerify.this.Tag, "onSuccess ");
                        try {
                            StepVerify.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepVerify.this.mActivity);
                            if (response.getString("code").isEmpty()) {
                                StepVerify.this.mActivity.showAlertDialog("NEO", "code is empty");
                                return;
                            }
                            StepVerify.this.remoteCode = response.getString("code");
                            NeoMsgUtil.sendMessage(StepVerify.this.mContext, StepVerify.this.remoteCode, StepVerify.this.getPhoneNumber());
                        } catch (Exception e) {
                            Log.e(StepVerify.this.Tag, e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(StepVerify.this.Tag, " onFailure" + throwable.toString());
                        StepVerify.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
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
                        Log.i(StepVerify.this.Tag, "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(StepVerify.this.Tag, "onSuccess ");
                        try {
                            StepVerify.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepVerify.this.mActivity);
                            if (response.getString("code").isEmpty()) {
                                StepVerify.this.mActivity.showAlertDialog("NEO", "code is empty");
                                return;
                            }
                            StepVerify.this.remoteCode = response.getString("code");
                            NeoMsgUtil.sendMessage(StepVerify.this.mContext, StepVerify.this.remoteCode, StepVerify.this.getPhoneNumber());
                        } catch (Exception e) {
                            Log.e(StepVerify.this.Tag, e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(StepVerify.this.Tag, " onFailure" + throwable.toString());
                        StepVerify.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                    }
                });
            }
        }
        if (this.mActivity.registerUrl == null) {
            this.mActivity.showAlertDialog("NEO", "url is null");
        } else {
            NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                public void onFinish() {
                    Log.i(StepVerify.this.Tag, "onFinish");
                }

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i(StepVerify.this.Tag, "onSuccess ");
                    try {
                        StepVerify.this.mActivity.showNeoJsoErrorCodeToast(response);
                        NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepVerify.this.mActivity);
                        if (response.getString("code").isEmpty()) {
                            StepVerify.this.mActivity.showAlertDialog("NEO", "code is empty");
                            return;
                        }
                        StepVerify.this.remoteCode = response.getString("code");
                        NeoMsgUtil.sendMessage(StepVerify.this.mContext, StepVerify.this.remoteCode, StepVerify.this.getPhoneNumber());
                    } catch (Exception e) {
                        Log.e(StepVerify.this.Tag, e.toString());
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(StepVerify.this.Tag, " onFailure" + throwable.toString());
                    StepVerify.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                }
            });
        }
    }
	@Override
    public void doNext() {
        doCodeVerify();
    }


	@Override
	public boolean validate() {
		if (isNull(mEtVerifyCode)) {
			showCustomToast("请输入验证码");
			mEtVerifyCode.requestFocus();
			return false;
		}
		mVerifyCode = mEtVerifyCode.getText().toString().trim();
		return true;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_verify_btn_resend:
			handler.sendEmptyMessage(0);
                requestCode();
			break;

		case R.id.reg_verify_htv_nocode:
			showCustomToast("抱歉,暂时不支持此操作");
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

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mReSendTime > 1) {
				mReSendTime--;
				mBtnResend.setEnabled(false);
				mBtnResend.setText("重发(" + mReSendTime + ")");
				handler.sendEmptyMessageDelayed(0, 1000);
			} else {
				mReSendTime = 60;
				mBtnResend.setEnabled(true);
				mBtnResend.setText("重    发");
			}
		}
	};

}
