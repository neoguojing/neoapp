package com.neo.neoapp.UI.views.popup;

import com.neo.neoapp.NeoBasicPopupWindow;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;


public class PeopleStateListPopupWindow extends NeoBasicPopupWindow implements
		OnClickListener {

	private NeoBasicTextView mHtvCopy;
	private NeoBasicTextView mHtvReport;
	private onOtherFeedListPopupItemClickListner mOnOtherFeedListPopupItemClickListner;

	public PeopleStateListPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(
				R.layout.include_dialog_otherfeedlist, null), width, height);
		setAnimationStyle(R.style.Popup_Animation_Alpha);
	}

	@Override
	public void initViews() {
		mHtvCopy = (NeoBasicTextView) findViewById(R.id.dialog_otherfeedlist_htv_copy);
		mHtvReport = (NeoBasicTextView) findViewById(R.id.dialog_otherfeedlist_htv_report);
	}

	@Override
	public void initEvents() {
		mHtvCopy.setOnClickListener(this);
		mHtvReport.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_otherfeedlist_htv_copy:
			if (mOnOtherFeedListPopupItemClickListner != null) {
				mOnOtherFeedListPopupItemClickListner.onCopy(v);
			}
			break;

		case R.id.dialog_otherfeedlist_htv_report:
			if (mOnOtherFeedListPopupItemClickListner != null) {
				mOnOtherFeedListPopupItemClickListner.onReport(v);
			}
			break;
		}
		dismiss();
	}

	public void setOnOtherFeedListPopupItemClickListner(
			onOtherFeedListPopupItemClickListner l) {
		mOnOtherFeedListPopupItemClickListner = l;
	}

	public interface onOtherFeedListPopupItemClickListner {
		void onCopy(View v);

		void onReport(View v);
	}
}
