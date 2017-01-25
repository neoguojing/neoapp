package com.neo.neoandroidlib;

import java.io.IOException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

public class NeoImageUtil {
	
	private static String TAG = "NeoImageUtil";
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
	public static Bitmap getImageFromSDCard(String name)
	{
		
		String SDCarePath=Environment.getExternalStorageDirectory().toString();
		String filePath=SDCarePath; 
		return BitmapFactory.decodeFile(filePath+"/"+name);
	}
	
	public static Bitmap getImageFromRes(Context ctx,String name)
	{
		ApplicationInfo appInfo = ctx.getApplicationInfo();
		int resID = ctx.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		Log.v(TAG,String.valueOf(resID));
		Log.v(TAG,appInfo.packageName);
		return BitmapFactory.decodeResource(ctx.getResources(), resID);
	}
	
	public static Bitmap getImageFromSrc(Context ctx,String packagePath)
	{
		
		return BitmapFactory.decodeStream(
				ctx.getClassLoader().getResourceAsStream(packagePath));
	}
	
	public static Bitmap getImageFromAsset(Context ctx,String name)
	{
		
		try {
			return BitmapFactory.decodeStream(
					ctx.getResources().getAssets().open(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap compressTheImageToDestSize(String filepath,int destWidth,
			int destHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = caculateInsamplesize(options,destWidth,destHeight);
		Bitmap src = BitmapFactory.decodeFile(filepath, options);
		return createScaledBitMap(src,destWidth,destHeight);
	}
	
	public static Bitmap compressTheImageToDestSize(Bitmap bitmap,int destWidth,
			int destHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = caculateInsamplesize(bitmap,destWidth,destHeight);
		return createScaledBitMap(bitmap,destWidth,destHeight);
	}

	private static int caculateInsamplesize(Bitmap bitmap, int destWidth,
			int destHeight) {
		final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        int inSampleSize = 1;
        if (height > destHeight || width > destWidth ) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > destHeight
                    && (halfWidth / inSampleSize) > destWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
	}

	private static Bitmap createScaledBitMap(Bitmap src, int destWidth,
			int destHeight) {
		// TODO Auto-generated method stub
		if (src==null){
			return null;
		}
		Bitmap dest = Bitmap.createScaledBitmap(src,destWidth,destHeight, false);
		if (dest!=src){
			src.recycle();
		}
		return dest;
	}

	private static int caculateInsamplesize(Options options,
			int destWidth, int destHeight) {
		// TODO Auto-generated method stub
		final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > destHeight || width > destWidth ) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > destHeight
                    && (halfWidth / inSampleSize) > destWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
	}
	
	public static Bitmap drawTextOntBitMap(Bitmap bitmap,String text){
		
		int textSize = 28;
		Bitmap rtn = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
		Canvas  canvas = new Canvas(rtn);
		
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);
		iconPaint.setFilterBitmap(true);
		
		Rect src = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		Rect dest = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		canvas.drawBitmap(bitmap, src, dest, iconPaint);
		
		//启用抗锯齿和使用设备的文本字距  
        Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.RED);  
		
        canvas.drawCircle(bitmap.getWidth()-20, 20, 20, countPaint);
        
        Paint textPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        textPaint.setColor(Color.WHITE);  
        textPaint.setTextSize(20f);  
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); 
        
        canvas.drawText(text, bitmap.getWidth()-25, 25, textPaint);
        return rtn;
	}
};
