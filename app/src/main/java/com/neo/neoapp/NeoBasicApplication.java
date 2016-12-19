package com.neo.neoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.neo.neoandroidlib.NetWorkUtils.NetWorkState;
import com.neo.neoapp.R;
import com.neo.neoapp.entity.NeoConfig;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;

import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NeoBasicApplication extends Application {
    private static final String PHOTO_ORIGINAL_DIR = "photo/original/";
    private static final String PHOTO_THUMBNAIL_DIR = "photo/thumbnail/";
    private static final String PORTRAIT_DIR = "portrait/";
    private static final String STATUS_PHOTO_DIR = "statusphoto/";
    public static List<String> mEmoticons;
    public static Map<String, Integer> mEmoticonsId;
    public static List<String> mEmoticons_Zem;
    private String Tag;
    private Bitmap mDefaultPortrait;
    public double mLatitude;
    public BDLocation mLocation;
    public LocationClient mLocationClient;
    public double mLongitude;
    public People mMe;
    public PeopleProfile mMyProfile;
    public List<People> mNearByPeoples;
    public List<People> mMyNearByPeoples;
    public List<People> mMyFriends;
    public NeoConfig mNeoConfig;
    public String mAppDataPath;
    public NetWorkState netWorkState;
    public Map<String, SoftReference<Bitmap>> mPhotoOriginalCache;
    public Map<String, SoftReference<Bitmap>> mPhotoThumbnailCache;
    public Map<String, SoftReference<Bitmap>> mPortraitCache;
    public Map<String, SoftReference<Bitmap>> mStatusPhotoCache;

    public NeoBasicApplication() {
        this.Tag = "NeoBasicApplication";
        this.mPortraitCache = new HashMap();
        this.mPhotoOriginalCache = new HashMap();
        this.mPhotoThumbnailCache = new HashMap();
        this.mStatusPhotoCache = new HashMap();
        this.mNearByPeoples = new ArrayList();
        this.mMyNearByPeoples = new ArrayList();
        this.mMyFriends = new ArrayList();
        this.mNeoConfig = new NeoConfig();
        mAppDataPath = "";
        this.mMe = new People();
        this.mMyProfile = new PeopleProfile();
    }

    static {
        mEmoticons = new ArrayList();
        mEmoticonsId = new HashMap();
        mEmoticons_Zem = new ArrayList();
    }
	@Override
	public void onCreate() {
		super.onCreate();	
		
		SDKInitializer.initialize(getApplicationContext());
		
		mDefaultPortrait = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_common_def_header);
		
		for (int i = 1; i < 64; i++) {
			String emoticonsName = "[zem" + i + "]";
			int emoticonsId = getResources().getIdentifier("zem" + i,
					"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zem.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {
			
			public void onReceiveLocation(BDLocation arg0) {
				mLongitude = arg0.getLongitude();
				mLatitude = arg0.getLatitude();
				mLocation = arg0;
				Log.i("定位信息", "经度:" + mLongitude + ",纬度:" + mLatitude);
				mLocationClient.stop();
			}
		});
		mLocationClient.start();
		mLocationClient.requestOfflineLocation();
		System.out.println("开始加载。。");
	}

    protected void showLongToast(String text) {
        Toast.makeText(this, text, 1).show();
    }

    protected AlertDialog showAlertDialog(String title, String message) {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
    }
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.e("BaseApplication", "onLowMemory");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.e("BaseApplication", "onTerminate");
	}

	public Bitmap getUserHeadPic(String imagename){
		if (this.mPortraitCache.containsKey(imagename)) {
            Reference<Bitmap> reference = (Reference) this.mPortraitCache.get(imagename);
            if (reference.get() != null && !((Bitmap) reference.get()).isRecycled()) {
                return (Bitmap) reference.get();
            }
            this.mPortraitCache.remove(imagename);
        }
		
		
		if (mAppDataPath.equals(""))
			return null;
		
		String headPicPath = mAppDataPath+NeoAppSetings.HeadPicDir+imagename;
		
		Bitmap rtn = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(headPicPath));
			rtn = BitmapFactory.decodeStream(fis);
			if (rtn!=null)
				this.mPortraitCache.put(imagename, new SoftReference(rtn));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtn = null;
		}finally{
			if (fis != null) {
	            try {
	            	fis.close();
	            } catch (IOException e) {
	            }
	        }
		}
		
		return rtn;
		
	}
	
    public Bitmap getAvatar(String imageName) {
        if (this.mPortraitCache.containsKey(imageName)) {
            Reference<Bitmap> reference = (Reference) this.mPortraitCache.get(imageName);
            if (reference.get() != null && !((Bitmap) reference.get()).isRecycled()) {
                return (Bitmap) reference.get();
            }
            this.mPortraitCache.remove(imageName);
        }
        InputStream is = null;
        try {
            is = getAssets().open(new StringBuilder(PORTRAIT_DIR).append(imageName).toString());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
        		throw new FileNotFoundException(
            		new StringBuilder(String.valueOf(imageName))
            		.append("is not find").toString());
            }
            this.mPortraitCache.put(imageName, new SoftReference(bitmap));
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            return bitmap;
        } catch (Exception e2) {
            Bitmap bitmap2 = this.mDefaultPortrait;
            if (is == null) {
                return bitmap2;
            }
            try {
                is.close();
                return bitmap2;
            } catch (IOException e3) {
                return bitmap2;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }
		return mDefaultPortrait;
    }
    
    public Bitmap getMyHeadPic() {
    	if (mAppDataPath.equals(""))
			return null;
		
		String headPicPath = mAppDataPath+mMe.getAvatar();
		
		Bitmap rtn = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(headPicPath));
			rtn = BitmapFactory.decodeStream(fis);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtn = null;
		}finally{
			if (fis != null) {
	            try {
	            	fis.close();
	            } catch (IOException e) {
	            }
	        }
		}
		
		if (rtn==null)
			return mDefaultPortrait;
		return rtn;
    }
    
	public Bitmap getPhotoOriginal(String imageName) {
		if (mPhotoOriginalCache.containsKey(imageName)) {
			Reference<Bitmap> reference = mPhotoOriginalCache.get(imageName);
			if (reference.get() == null || reference.get().isRecycled()) {
				mPhotoOriginalCache.remove(imageName);
			} else {
				return reference.get();
			}
		}
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = getAssets().open(PHOTO_ORIGINAL_DIR + imageName);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null) {
				throw new FileNotFoundException(imageName + "is not find");
			}
			mPhotoOriginalCache.put(imageName,
					new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {

			}
		}
	}

	public Bitmap getPhotoThumbnail(String imageName) {
		if (mPhotoThumbnailCache.containsKey(imageName)) {
			Reference<Bitmap> reference = mPhotoThumbnailCache.get(imageName);
			if (reference.get() == null || reference.get().isRecycled()) {
				mPhotoThumbnailCache.remove(imageName);
			} else {
				return reference.get();
			}
		}
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = getAssets().open(PHOTO_THUMBNAIL_DIR + imageName);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null) {
				throw new FileNotFoundException(imageName + "is not find");
			}
			mPhotoThumbnailCache.put(imageName, new SoftReference<Bitmap>(
					bitmap));
			return bitmap;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {

			}
		}
	}
	
	public Bitmap getStatusPhoto(String imageName) {
		if (mStatusPhotoCache.containsKey(imageName)) {
			Reference<Bitmap> reference = mStatusPhotoCache.get(imageName);
			if (reference.get() == null || reference.get().isRecycled()) {
				mStatusPhotoCache.remove(imageName);
			} else {
				return reference.get();
			}
		}
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = getAssets().open(STATUS_PHOTO_DIR + imageName);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null) {
				throw new FileNotFoundException(imageName + "is not find");
			}
			mStatusPhotoCache.put(imageName, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {

			}
		}
	}
}
