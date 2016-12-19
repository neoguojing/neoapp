package com.neo.neoapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.neo.neoandroidlib.FileUtils;
import com.neo.neoandroidlib.JsonResolveUtils;
import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.dialog.NeoFlippingLoadingDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.dialog.NeoFlippingLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class NeoBasicActivity extends FragmentActivity {
	protected NeoBasicApplication mApplication;
	protected NeoFlippingLoadingDialog mLoadingDialog;

	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (NeoBasicApplication) getApplication();
		mLoadingDialog = new NeoFlippingLoadingDialog(this, "请求提交中");

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
	}

	@Override
	protected void onDestroy() {
		clearAsyncTask();
		super.onDestroy();
	}

    public NeoBasicApplication getMyApplication() {
        return this.mApplication;
    }
    
    protected abstract void initViews();
	/** 初始化事件 **/
	protected abstract void initEvents();

	protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
		mAsyncTasks.add(asyncTask.execute());
	}

	protected void clearAsyncTask() {
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks
				.iterator();
		while (iterator.hasNext()) {
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}
		mAsyncTasks.clear();
	}

	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	protected void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	public void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

    public void showNeoJsoErrorCodeToast(JSONObject json) {
        try {
            showLongToast("errcode:" + json.getString("errcode") + ";info:" + json.getString("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void showCustomToast(int resId) {
        View toastRoot = LayoutInflater.from(this).inflate(R.layout.common_toast, null);
        ((NeoBasicTextView) toastRoot.findViewById(R.id.toast_text)).setText(getString(resId));
        Toast toast = new Toast(this);
        toast.setGravity(17, 0, 0);
        toast.setDuration(0);
        toast.setView(toastRoot);
        toast.show();
    }

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(NeoBasicActivity.this).inflate(
				R.layout.common_toast, null);
		((NeoBasicTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(NeoBasicActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** Debug输出Log日志 **/
	protected void showLogDebug(String tag, String msg) {
		Log.d(tag, msg);
	}

	/** Error输出Log日志 **/
	protected void showLogError(String tag, String msg) {
		Log.e(tag, msg);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 含有标题和内容的对话框 **/
	public AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 含有标题、内容、图标、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			int icon, String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
	}

    public boolean netWorkCheck(Context context) {
    	boolean rtn = true;
    	//mApplication.netWorkState =new  NetWorkUtils(context).getConnectState();
    	mApplication.netWorkState = NetWorkUtils.getConnectState(context);
        if (mApplication.netWorkState == NetWorkState.NONE) {
            showAlertDialog("NEO", "Please check your NetWork connection!");
            rtn = false;
        } else if (!this.mApplication.mNeoConfig.getIp().equals("")) {
        	rtn = true;
        } else if (FileUtils.isFileExist(getMyApplication().mAppDataPath+
        			NeoAppSetings.ConfigFile)){
    		rtn = JsonResolveUtils.resolveNeoConfig(mApplication,this);
    		if (!rtn)
    			showAlertDialog("NEO", "resolveNeoConfig faied");
        }
        
        if (mApplication.mNeoConfig.getIp().equals("")&&
        		mApplication.mNeoConfig.getLocalip().equals(""))
        	showAlertDialog("NEO", "The server info is empty");
        
        return rtn;
    }
    
    protected boolean initNetWorkCheck(Context context) {
    	mApplication.netWorkState = NetWorkUtils.getConnectState(context);
        if (mApplication.netWorkState == NetWorkState.NONE) {
            showAlertDialog("NEO", "Please check your NetWork connection!");
            return false;
        }
        return true;

    }
}
