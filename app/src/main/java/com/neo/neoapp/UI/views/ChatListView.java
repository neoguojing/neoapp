package com.neo.neoapp.UI.views;

import com.neo.neoapp.UI.views.list.NeoRefreshListView;

import android.content.Context;
import android.util.AttributeSet;

public class ChatListView extends NeoRefreshListView {

	public ChatListView(Context context) {
		super(context);
		init();
	}

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setStackFromBottom(true);
		setFastScrollEnabled(true);
	}
}
