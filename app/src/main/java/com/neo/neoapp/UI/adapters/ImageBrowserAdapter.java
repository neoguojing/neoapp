package com.neo.neoapp.UI.adapters;

import java.util.ArrayList;
import java.util.List;

import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.UI.views.photo.PhotoView;
import com.neo.neoapp.activities.ImageBrowserActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


public class ImageBrowserAdapter extends PagerAdapter {

	private NeoBasicApplication mApplication;
	private List<String> mPhotos = new ArrayList<String>();
	private String mType;

	public ImageBrowserAdapter(NeoBasicApplication application,
			List<String> photos, String type) {
		mApplication = application;
		if (photos != null) {
			mPhotos = photos;
		}
		mType = type;
	}

	@Override
	public int getCount() {
		if (mPhotos.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		Bitmap bitmap = null;
		if (ImageBrowserActivity.TYPE_ALBUM.equals(mType)) {
			bitmap = mApplication.getPhotoOriginal(mPhotos.get(position
					% mPhotos.size()));
		} else if (ImageBrowserActivity.TYPE_PHOTO.equals(mType)) {
			bitmap = BitmapFactory.decodeFile(mPhotos.get(position));
		}
		photoView.setImageBitmap(bitmap);
		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
