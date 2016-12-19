package com.neo.neoapp.UI.adapters;

import java.util.List;

import com.neo.neoapp.BaseArrayListAdapter;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class SimpleListDialogAdapter extends BaseArrayListAdapter {

	public SimpleListDialogAdapter(Context context, List<String> datas) {
		super(context, datas);
	}

	public SimpleListDialogAdapter(Context context, String... datas) {
		super(context, datas);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.listitem_dialog, null);
		}
		((NeoBasicTextView) arg1.findViewById(R.id.listitem_dialog_text))
				.setText((CharSequence) getItem(arg0));
		return arg1;
	}
}
