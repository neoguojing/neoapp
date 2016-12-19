package com.neo.neoapp.fragments;

import java.util.ArrayList;
import java.util.List;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.NeoViewPageAdpterForView;
import com.neo.neoapp.UI.adapters.NeoScrollImageAdapter;
import com.neo.neoapp.handlers.NeoAppUIThreadHandler;

import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class NeoBrandScrollUtil implements OnPageChangeListener, Runnable{
	
	private Context mContext;
	private NeoBasicFragment mFrag;
    
	private ViewPager mViewPager;
	private NeoViewPageAdpterForView mfPagerAdapter;
	private ViewGroup group;

    private List<ImageView> tips;  
    private List<ImageView> mImageViews;  
    private int[] imgIdArray;  
    
    public int currentItemOfBrandScroll = 0;
    
    private Handler handler = new Handler(Looper.getMainLooper()) {  
        public void handleMessage(android.os.Message msg) {  
        	mViewPager.setCurrentItem(currentItemOfBrandScroll);// 切换当前显示的图片  
        };  
    };  
	
	NeoBrandScrollUtil(Context ctx,NeoBasicFragment frag)
	{
		mContext = ctx;
		mFrag = frag;
		
		if (mContext!=null && mFrag!=null){
			InitView();
			InitEvent();
		}	
	}
	
	public NeoBrandScrollUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public void InitView()
	{
		 
		mViewPager = (ViewPager)mFrag.findViewById(R.id.Brand_Scroll);
		group = (ViewGroup)mFrag.findViewById(R.id.scroll_image_Group);  
		imgIdArray = new int[]{R.drawable.pic_brand1, R.drawable.pic_brand2, 
				R.drawable.pic_brand3};  		
		tips = new ArrayList<ImageView>();
		mImageViews = new ArrayList<ImageView>(); 
		
		if (mImageViews.isEmpty()) {
			for(int i =0; i<imgIdArray.length;++i)
			{
				
				ImageView tIV = new ImageView(mContext);
				tIV.setLayoutParams(new LayoutParams(10,10));
				tips.add(tIV);
				
				if (i == 0)
				{
					tips.get(i).setBackgroundResource(R.drawable.scrollwhite);				
				}else
				{
					tips.get(i).setBackgroundResource(R.drawable.scrollblackl);	
				}
				
				LinearLayout.LayoutParams layoutparm = new LinearLayout.LayoutParams(
						new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT));
				layoutparm.leftMargin = 5;
				layoutparm.rightMargin = 5;
				group.addView(tIV,layoutparm);
			}
			
			 
	        for(int i=0; i<imgIdArray.length; i++){  
	            ImageView imageView = new ImageView(mContext); 
	            imageView.setImageResource(imgIdArray[i]); 
	            imageView.setScaleType(ScaleType.FIT_XY);
	            mImageViews.add(imageView); 
	        }  
			
		}
			
		 mfPagerAdapter = new NeoScrollImageAdapter(mImageViews);
		 mViewPager.setAdapter(mfPagerAdapter);
		 mViewPager.setCurrentItem((mImageViews.size()) * 100);   
		 //mViewPager.setOnPageChangeListener(this); 
	}
	
	private void initViewInBackground(){
		mFrag.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				//showLoadingDialog("正在加载,请稍后...");
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				for(int i =0; i<imgIdArray.length;++i)
				{
					
					ImageView tIV = new ImageView(mContext);
					tIV.setLayoutParams(new LayoutParams(10,10));
					tips.add(tIV);
					
					if (i == 0)
					{
						tips.get(i).setBackgroundResource(R.drawable.scrollwhite);				
					}else
					{
						tips.get(i).setBackgroundResource(R.drawable.scrollblackl);	
					}
					
					LinearLayout.LayoutParams layoutparm = new LinearLayout.LayoutParams(
							new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, 
									LayoutParams.WRAP_CONTENT));
					layoutparm.leftMargin = 5;
					layoutparm.rightMargin = 5;
					group.addView(tIV,layoutparm);
				}
				
				 
		        for(int i=0; i<imgIdArray.length; i++){  
		            ImageView imageView = new ImageView(mContext); 
		            imageView.setImageResource(imgIdArray[i]); 
		            imageView.setScaleType(ScaleType.FIT_XY);
		            mImageViews.add(imageView); 
		        }  
		      						 
		        
		        return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				//dismissLoadingDialog();
				if (!result) {
					mFrag.showCustomToast("数据加载失败...");
				} else{
					mfPagerAdapter = new NeoScrollImageAdapter(mImageViews);
					mViewPager.setAdapter(mfPagerAdapter);
					mViewPager.setCurrentItem((mImageViews.size()) * 100);  
					//mViewPager.setOnPageChangeListener(this); 
					
				}

			}
		});
	}
	public void InitEvent()
	{
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0);
	}
	
	private void setImageBackground(int selectItems){  
        for(int i=0; i<tips.size(); i++){  
            if(i == selectItems){  
                tips.get(i).setBackgroundResource(R.drawable.scrollwhite);  
            }else{  
                tips.get(i).setBackgroundResource(R.drawable.scrollblackl);  
            }  
        }  
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		currentItemOfBrandScroll = (currentItemOfBrandScroll+1)%(mImageViews.size());
		handler.obtainMessage().sendToTarget();
	}
	
}
