package com.neo.neoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.NeoAsyncHttpUtil;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoandroidlib.TextUtils;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoAppSetings.NEO_ERRCODE;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.adapters.SimpleListDialogAdapter;
import com.neo.neoapp.UI.views.HeaderLayout;
import com.neo.neoapp.UI.views.HeaderLayout.HeaderStyle;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.activities.imageactivity.ImageFactoryCrop;
import com.neo.neoapp.dialog.SimpleListDialog;
import com.neo.neoapp.dialog.SimpleListDialog.onSimpleListItemClickListener;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.params.ConnPerRouteBean;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.http.entity.mime.MIME;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends NeoBasicActivity implements OnClickListener,
		onSimpleListItemClickListener {
	private final String Tag = "LoginActivity";
	private HeaderLayout mHeaderLayout;
	private EditText mEtAccount;
	private EditText mEtPwd;
	private NeoBasicTextView mHtvForgotPassword;
	private NeoBasicTextView mHtvSelectCountryCode;
	private Button mBtnBack;
	private Button mBtnLogin;

	private static final String[] DEFAULT_ACCOUNTS = new String[] {
			"+8612345678901", "805118680@qq.com", "12345678" };
	private static final String DEFAULT_PASSWORD = "123456";
	private String mAreaCode = "+86";
	private String mAccount;
	private String mPassword;

	private SimpleListDialog mSimpleListDialog;
    private enum AccountType {
        USERNAME,
        EMAIL,
        PHONE,
        USERID,
        INVALID
    }
	private String[] mCountryCodes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getActionBar().hide();
		setContentView(R.layout.activity_login);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.login_header);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle("登录", null);
		mEtAccount = (EditText) findViewById(R.id.login_et_account);
		mEtPwd = (EditText) findViewById(R.id.login_et_pwd);
		mHtvForgotPassword = (NeoBasicTextView) findViewById(R.id.login_htv_forgotpassword);
		TextUtils.addUnderlineText(this, mHtvForgotPassword, 0,
				mHtvForgotPassword.getText().length());
		mHtvSelectCountryCode = (NeoBasicTextView) findViewById(R.id.login_htv_selectcountrycode);
		TextUtils.addUnderlineText(this, mHtvSelectCountryCode, 0,
				mHtvSelectCountryCode.getText().length());
		mBtnBack = (Button) findViewById(R.id.login_btn_back);
		mBtnLogin = (Button) findViewById(R.id.login_btn_login);
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, WelcomeActivity.class));
		finish();
	}
	
	@Override
	protected void initEvents() {
		mHtvForgotPassword.setOnClickListener(this);
		mHtvSelectCountryCode.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_htv_forgotpassword:
			startActivity(FindPwdTabsActivity.class);
			break;

		case R.id.login_htv_selectcountrycode:
			mCountryCodes = getResources()
					.getStringArray(R.array.country_codes);
			mSimpleListDialog = new SimpleListDialog(LoginActivity.this);
			mSimpleListDialog.setTitle("选择国家区号");
			mSimpleListDialog.setTitleLineVisibility(View.GONE);
			mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(
					LoginActivity.this, mCountryCodes));
			mSimpleListDialog
					.setOnSimpleListItemClickListener(LoginActivity.this);
			mSimpleListDialog.show();
			break;

		case R.id.login_btn_back:
			finish();
			break;

		case R.id.login_btn_login:
			login();
			break;
		}
	}

	@Override
	public void onItemClick(int position) {
		mAccount = null;
		String text = TextUtils.getCountryCodeBracketsInfo(
				mCountryCodes[position], mAreaCode);
		mEtAccount.requestFocus();
		mEtAccount.setText(text);
		mEtAccount.setSelection(text.length());

	}

	private boolean matchEmail(String text) {
		if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
				.matches()) {
			return true;
		}
		return false;
	}

	private boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

    private boolean matchUid(String text) {
        if (Pattern.compile("\\d{1,9}").matcher(text).matches()) {
            return true;
        }
        return false;
    }

	private boolean isNull(EditText editText) {
		String text = editText.getText().toString().trim();
		if (text != null && text.length() > 0) {
			return false;
		}
		return true;
	}

    private AccountType validateAccount() {
        this.mAccount = null;
        if (isNull(this.mEtAccount)) {
            showCustomToast("\u8bf7\u8f93\u5165\u7528\u6237\u540d/\u7535\u8bdd/\u767b\u5f55\u90ae\u7bb1");
            this.mEtAccount.requestFocus();
            return AccountType.INVALID;
        }
        String account = this.mEtAccount.getText().toString().trim();
        if (matchPhone(account)) {
            if (account.length() < 3) {
                showCustomToast("\u8d26\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
                this.mEtAccount.requestFocus();
                return AccountType.INVALID;
            } else if (Pattern.compile("(\\d{3,})|(\\+\\d{3,})").matcher(account).matches()) {
                this.mAccount = account;
                return AccountType.PHONE;
            }
        }
        if (matchEmail(account)) {
            this.mAccount = account;
            return AccountType.EMAIL;
        } else if (matchUid(account)) {
            this.mAccount = account;
            return AccountType.USERID;
        } else {
            this.mAccount = account;
            return AccountType.USERNAME;
        }
    }

	private boolean validatePwd() {
		mPassword = null;
		String pwd = mEtPwd.getText().toString().trim();
		if (pwd.length() < 4) {
			showCustomToast("密码不能小于4位");
			mEtPwd.requestFocus();
			return false;
		}
		if (pwd.length() > 16) {
			showCustomToast("密码不能大于16位");
			mEtPwd.requestFocus();
			return false;
		}
		mPassword = pwd;
		return true;
	}

    private void onlineLogin(AccountType accounttype) {
        try {
            if (netWorkCheck(this)) {
                JSONObject jsonObject = new JSONObject();
                switch (accounttype) {
                    case USERNAME /*1*/:
                        jsonObject.put("username", this.mAccount);
                        break;
                    case EMAIL:
                        jsonObject.put("email", this.mAccount);
                        break;
                    case PHONE:
                        jsonObject.put("phone", this.mAccount);
                        break;
					default:
						break;
                }
                jsonObject.put("password", this.mPassword);
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                stringEntity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
                NeoAsyncHttpUtil.postJson(this, NeoAppSetings.getLoginUrl(this.mApplication.mNeoConfig), stringEntity, new JsonHttpResponseHandler() {
                    public void onFinish() {
                        Log.i(LoginActivity.this.Tag, "onFinish");
                    }

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(LoginActivity.this.Tag, "onSuccess ");
                        LoginActivity.this.showNeoJsoErrorCodeToast(response);
                        NeoAsyncHttpUtil.addPersistCookieToGlobaList(LoginActivity.this);
                        try {
                            if (response.getString("errcode").equals(NEO_ERRCODE.LOGIN_SUCCESS.toString())) {
                            	mApplication.mMe.setName(mAccount);
                                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                                LoginActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(LoginActivity.this.Tag, " onFailure" + throwable.toString());
                        LoginActivity.this.showAlertDialog("NEO", "Server is not avaliable"
                        + errorResponse.toString());
                        offlineLogin();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
    }
    
    private void offlineLogin(){
    	showAlertDialog("NEO","you are in offline state!");
    	if (mApplication.mMe.getName().equals("")){
    		showAlertDialog("NEO","you have not login before!");
    	}
    		
    	if (this.mAccount.equals(mApplication.mMe.getName())){
    		LoginActivity.this.startActivity(
    				new Intent(LoginActivity.this, MainTabActivity.class));
            LoginActivity.this.finish();
    	}else{
    		showAlertDialog("NEO","you have not login before!");
    	}
    }
    
    private void testLogin() {
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {
                super.onPreExecute();
                LoginActivity.this.showLoadingDialog("\u6b63\u5728\u767b\u5f55,\u8bf7\u7a0d\u540e...");
            }

            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                    if ((LoginActivity.DEFAULT_ACCOUNTS[0].equals(LoginActivity.this.mAccount) || LoginActivity.DEFAULT_ACCOUNTS[1].equals(LoginActivity.this.mAccount) || LoginActivity.DEFAULT_ACCOUNTS[2].equals(LoginActivity.this.mAccount)) && LoginActivity.DEFAULT_PASSWORD.equals(LoginActivity.this.mPassword)) {
                        return Boolean.valueOf(true);
                    }
                    return Boolean.valueOf(true);
                } catch (InterruptedException e) {
                    return Boolean.valueOf(false);
                }
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                LoginActivity.this.dismissLoadingDialog();
                if (result.booleanValue()) {
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                    LoginActivity.this.finish();
                    return;
                }
                LoginActivity.this.showCustomToast("\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef,\u8bf7\u68c0\u67e5\u662f\u5426\u8f93\u5165\u6b63\u786e");
            }
        });
    }

    private void login() {
        AccountType accountType = validateAccount();
        if (accountType != AccountType.INVALID && validatePwd()) {
            if (netWorkCheck(this)) {
            	onlineLogin(accountType);
            }
        }
    }
}
