package com.neo.neoapp.UI.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class NeoBasicTextView extends TextView {
	public NeoBasicTextView(Context context) {
		super(context);
	}

	public NeoBasicTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NeoBasicTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (text == null) {
			text = "";
		}
		super.setText(text, type);
	}
}
