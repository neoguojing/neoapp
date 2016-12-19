package com.neo.neoapp.activities.register;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.TextUtils;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.SimpleListDialogAdapter;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.dialog.SimpleListDialog;
import com.neo.neoapp.dialog.SimpleListDialog.onSimpleListItemClickListener;
import com.neo.neoapp.dialog.WebDialog;
import com.neo.neoapp.dialog.WebDialog.OnWebDialogErrorListener;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import java.io.UnsupportedEncodingException;
import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

public class StepPhone extends RegisterStep implements OnClickListener,
		TextWatcher, onSimpleListItemClickListener, OnWebDialogErrorListener {
    private static final String DEFAULT_PHONE = "+8612345678901";
    private String Tag;
    private String mAreaCode;
    private String[] mCountryCodes;
    private EditText mEtPhone;
    private NeoBasicTextView mHtvAreaCode;
    private NeoBasicTextView mHtvNote;
    private NeoBasicTextView mHtvNotice;
    private boolean mIsChange;
    private String mPhone;
    private SimpleListDialog mSimpleListDialog;

	private WebDialog mWebDialog;

    public StepPhone(RegisterActivity activity, View contentRootView) {
        super(activity, contentRootView);
        this.Tag = "StepPhone";
        this.mAreaCode = "+86";
        this.mIsChange = true;
    }

	public String getPhoneNumber() {
        return this.mAreaCode + this.mPhone;
	}

	@Override
	public void initViews() {
		mHtvAreaCode = (NeoBasicTextView) findViewById(R.id.reg_phone_htv_areacode);
		mEtPhone = (EditText) findViewById(R.id.reg_phone_et_phone);
		mHtvNotice = (NeoBasicTextView) findViewById(R.id.reg_phone_htv_notice);
		mHtvNote = (NeoBasicTextView) findViewById(R.id.reg_phone_htv_note);
		TextUtils.addHyperlinks(mHtvNote, 8, 15, this);
	}

	@Override
	public void initEvents() {
		mHtvAreaCode.setOnClickListener(this);
		mEtPhone.addTextChangedListener(this);
	}

    private void testPhoneVerify() {
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                StepPhone.this.showLoadingDialog("\u6b63\u5728\u9a8c\u8bc1\u624b\u673a\u53f7,\u8bf7\u7a0d\u540e...");
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                    if (StepPhone.DEFAULT_PHONE.equals(new StringBuilder(String.valueOf(StepPhone.this.mAreaCode)).append(StepPhone.this.mPhone).toString())) {
                        return Boolean.valueOf(true);
                    }
                } catch (InterruptedException e) {
                }
                return Boolean.valueOf(false);
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                StepPhone.this.dismissLoadingDialog();
                if (result.booleanValue()) {
                    StepPhone.this.mIsChange = false;
                    StepPhone.this.mOnNextActionListener.next();
                    return;
                }
                StepPhone.this.showCustomToast("\u624b\u673a\u53f7\u7801\u4e0d\u53ef\u7528\u6216\u5df2\u88ab\u6ce8\u518c");
            }
        });
    }

    private void onlinePhoneVerify() {
        JSONException e1;
        UnsupportedEncodingException e;
        StringEntity stringEntity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("step", "1");
            jsonObject.put("phone", getPhoneNumber());
            StringEntity stringEntity2 = new StringEntity(jsonObject.toString());
            stringEntity2.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
			stringEntity = stringEntity2;
        } catch (JSONException e4) {
            e1 = e4;
            e1.printStackTrace();
            if (this.mActivity.registerUrl == null) {
                NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i(StepPhone.this.Tag, "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(StepPhone.this.Tag, "onSuccess ");
                        try {
                            StepPhone.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepPhone.this.mActivity);
                            if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                                StepPhone.this.mIsChange = false;
                                StepPhone.this.mOnNextActionListener.next();
                            } else if (response.getString("errcode").equals(NEO_ERRCODE.PHONE_REGISTERED.toString())) {
                                StepPhone.this.showCustomToast(response.getString("info"));
                            }
                        } catch (Exception e) {
                            Log.e(StepPhone.this.Tag, e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(StepPhone.this.Tag, " onFailure" + throwable.toString());
                        StepPhone.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
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
                        Log.i(StepPhone.this.Tag, "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(StepPhone.this.Tag, "onSuccess ");
                        try {
                            StepPhone.this.mActivity.showNeoJsoErrorCodeToast(response);
                            NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepPhone.this.mActivity);
                            if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                                StepPhone.this.mIsChange = false;
                                StepPhone.this.mOnNextActionListener.next();
                            } else if (response.getString("errcode").equals(NEO_ERRCODE.PHONE_REGISTERED.toString())) {
                                StepPhone.this.showCustomToast(response.getString("info"));
                            }
                        } catch (Exception e) {
                            Log.e(StepPhone.this.Tag, e.toString());
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(StepPhone.this.Tag, " onFailure" + throwable.toString());
                        StepPhone.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                    }
                });
            }
        }
        if (this.mActivity.registerUrl == null) {
            this.mActivity.showAlertDialog("NEO", "url is null");
        } else {
            NeoAsyncHttpUtil.postJson(this.mActivity, this.mActivity.registerUrl, stringEntity, new JsonHttpResponseHandler() {
                public void onFinish() {
                    Log.i(StepPhone.this.Tag, "onFinish");
                }

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i(StepPhone.this.Tag, "onSuccess ");
                    try {
                        StepPhone.this.mActivity.showNeoJsoErrorCodeToast(response);
                        NeoAsyncHttpUtil.addPersistCookieToGlobaList(StepPhone.this.mActivity);
                        if (response.getString("errcode").equals(NEO_ERRCODE.NOERROR.toString())) {
                            StepPhone.this.mIsChange = false;
                            StepPhone.this.mOnNextActionListener.next();
                        } else if (response.getString("errcode").equals(NEO_ERRCODE.PHONE_REGISTERED.toString())) {
                            StepPhone.this.showCustomToast(response.getString("info"));
                        }
                    } catch (Exception e) {
                        Log.e(StepPhone.this.Tag, e.toString());
                    }
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(StepPhone.this.Tag, " onFailure" + throwable.toString());
                    StepPhone.this.mActivity.showAlertDialog("NEO", "exception:" + throwable.toString());
                }
            });
        }
    }
	@Override
    public void doNext() {
        onlinePhoneVerify();
    }


	@Override
	public boolean validate() {
		mPhone = null;
		if (isNull(mEtPhone)) {
			showCustomToast("请填写手机号码");
			mEtPhone.requestFocus();
			return false;
		}
		String phone = mEtPhone.getText().toString().trim();
		if (matchPhone(mAreaCode + phone)) {
			mPhone = phone;
			return true;
		}
		showCustomToast("手机号码格式不正确");
		mEtPhone.requestFocus();
		return false;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
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
		if (s.toString().length() > 0) {
			mHtvNotice.setVisibility(View.VISIBLE);
			char[] chars = s.toString().toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				if (i % 4 == 2) {
					buffer.append(chars[i] + "  ");
				} else {
					buffer.append(chars[i]);
				}
			}
			mHtvNotice.setText(buffer.toString());
		} else {
			mHtvNotice.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_phone_htv_areacode:
			mCountryCodes = mContext.getResources().getStringArray(
					R.array.country_codes);
			mSimpleListDialog = new SimpleListDialog(mContext);
			mSimpleListDialog.setTitle("选择国家区号");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(mContext,
					mCountryCodes));
			mSimpleListDialog.setOnSimpleListItemClickListener(StepPhone.this);
			mSimpleListDialog.show();
			break;

		default:
			mWebDialog = new WebDialog(mContext);
			mWebDialog.init("用户协议", "确认",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mWebDialog.dismiss();
						}
					});
			mWebDialog.setOnWebDialogErrorListener(StepPhone.this);
			mWebDialog
					.loadUrl(JniManager.getInstance().getAgreementDialogUrl());
			mWebDialog.show();
			break;
		}
	}

	@Override
	public void onItemClick(int position) {
		String text = TextUtils.getCountryCodeBracketsInfo(
				mCountryCodes[position], mAreaCode);
		mAreaCode = text;
		mHtvAreaCode.setText(text);
	}

	@Override
	public void urlError() {
		showCustomToast("网页地址错误,请检查");
	}

	@Override
	public void networkError() {
		showCustomToast("当前网络不可用,请检查");
	}
}
