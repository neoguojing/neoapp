package com.neo.neoapp.UI.adapters;

import java.util.List;

import com.neo.neoapp.BaseObjectListAdapter;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.EmoticonsTextView;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.entity.Entity;
import com.neo.neoapp.entity.FeedComment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class PeopleStateProfileCommentsAdapter extends BaseObjectListAdapter {

	public PeopleStateProfileCommentsAdapter(NeoBasicApplication application,
			Context context, List<? extends Entity> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.listitem_feedcomment, null);
			holder = new ViewHolder();
			holder.mIvAvatar = (ImageView) convertView
					.findViewById(R.id.feedcomment_item_iv_avatar);
			holder.mEtvName = (EmoticonsTextView) convertView
					.findViewById(R.id.feedcomment_item_etv_name);
			holder.mEtvContent = (EmoticonsTextView) convertView
					.findViewById(R.id.feedcomment_item_etv_content);
			holder.mHtvTime = (NeoBasicTextView) convertView
					.findViewById(R.id.feedcomment_item_htv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FeedComment comment = (FeedComment) getItem(position);
		holder.mIvAvatar.setImageBitmap(mApplication.getAvatar(comment
				.getAvatar()));
		holder.mEtvName.setText(comment.getName());
		holder.mEtvContent.setText(comment.getContent());
		holder.mHtvTime.setText(comment.getTime());
		return convertView;
	}

	class ViewHolder {
		ImageView mIvAvatar;
		EmoticonsTextView mEtvName;
		EmoticonsTextView mEtvContent;
		NeoBasicTextView mHtvTime;
	}
}
