package com.neo.neoapp.dialog;

import android.content.Context;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.FlippingImageView;
import com.neo.neoapp.UI.views.NeoBasicTextView;


public class NeoFlippingLoadingDialog extends BaseDialog {

	private FlippingImageView mFivIcon;
	private NeoBasicTextView mHtvText;
	private String mText;

	public NeoFlippingLoadingDialog(Context context, String text) {
		super(context);
		mText = text;
		init();
	}

	private void init() {
		setContentView(R.layout.common_flipping_loading_diloag);
		mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (NeoBasicTextView) findViewById(R.id.loadingdialog_htv_text);
		mFivIcon.startAnimation();
		mHtvText.setText(mText);
	}

	public void setText(String text) {
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
