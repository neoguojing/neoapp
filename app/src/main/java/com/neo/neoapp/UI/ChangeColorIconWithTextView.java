package com.neo.neoapp.UI;

import com.neo.neoandroidlib.NeoImageUtil;
import com.neo.neoapp.R; 

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithTextView extends View {

	private int color=0xFF45C01A;
	private Bitmap iconBitmap;
	private String text="微信";
	private int textSize=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
			12, getResources().getDisplayMetrics());
	
	private Canvas mcanvas;//画布
	private Bitmap bitmap;//图形
	private Paint paint;//画笔
	
	private float maplha;//透明�?
	private Rect iconRect;//图形矩形
	private Rect textBound;//文字矩形�?
	private Paint textPaint;//画文字的画笔
	
	private int msgCount = 0;
	
	public ChangeColorIconWithTextView(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
		this(context,null);
	}

	public ChangeColorIconWithTextView(Context context, AttributeSet attrs) {
		//super(context, attrs);
		// TODO Auto-generated constructor stub
		this(context,attrs,0);
	}

	/**
	 * 获取自定义属性的�?
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ChangeColorIconWithTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		//获得自定义的属�?�的数组
		TypedArray typeArray=context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconWithText);
		//获得数组的长�?
		int n=typeArray.getIndexCount();
		for(int i=0;i<n;i++){
			//得到当前属�?�的index
			int attr=typeArray.getIndex(i);
			switch(attr){
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable bitmapDrawable=(BitmapDrawable) typeArray.getDrawable(attr);
				iconBitmap=bitmapDrawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				color=typeArray.getColor(attr, 0xFF45C01A);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				text=typeArray.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				textSize=(int) typeArray.getDimension(attr, 
						TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 
						12, getResources().getDisplayMetrics()));
				break;
			}
			
		}
		typeArray.recycle();
		//初始画图工具
		textBound=new Rect();
		textPaint=new Paint();
		textPaint.setTextSize(textSize);
		textPaint.setColor(0Xff555555);
		textPaint.getTextBounds(text, 0, text.length(), textBound);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth=Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(), 
				getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-textBound.height());
		//�?术左边的位置
		int left=getMeasuredWidth()/2-iconWidth/2;
		//计算距离顶部的位�?
		int top=(getMeasuredHeight()-textBound.height())/2-iconWidth/2;
		//创建图形的矩形框
		iconRect=new Rect(left,top,left+iconWidth,top+iconWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(iconBitmap, null, iconRect, null);
		
		// 内存去准备bitmap , setAlpha , 纯色 ，xfermode �? 图标
		int alpha=(int) Math.ceil(maplha*255);
		setupTargetBitmap(alpha);
		
		// 1、绘制原文本 �? 2、绘制变色的文本
		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(bitmap, 0, 0, null);
		
		if (getMsgCount()!=0)
			drawMsgCount(canvas,bitmap,String.valueOf(getMsgCount()));
		super.onDraw(canvas);
	}
	
	public void setMsgCount(int msgcount){
		this.msgCount = msgcount;
	}
	
	public int getMsgCount(){
		return this.msgCount;
	}
	
	private void drawMsgCount(Canvas canvas,Bitmap bitmap,String text){
		
		//启用抗锯齿和使用设备的文本字距  
        Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.RED);  
		
        canvas.drawCircle(bitmap.getWidth()-50, 20, 20, countPaint);
        
        Paint textPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
        textPaint.setColor(Color.WHITE);  
        textPaint.setTextSize(20f);  
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); 
        
        canvas.drawText(text, bitmap.getWidth()-55, 25, textPaint);
	}
	
	/**
	 * 绘制变色的文�?
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		textPaint.setColor(color);
		textPaint.setAlpha(alpha);
		int x=getMeasuredWidth()/2-textBound.width()/2;
		int y=iconRect.bottom+textBound.height();
		canvas.drawText(text, x, y, textPaint);
	}

	/**
	 * 
	 * 绘制原文�?
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		textPaint.setColor(0xff333333);
		textPaint.setAlpha(255-alpha);
		int x=getMeasuredWidth()/2-textBound.width()/2;
		int y=iconRect.bottom+textBound.height();
		canvas.drawText(text, x, y, textPaint);
	}

	/*
	 * 在内存中绘制可变色的icon
	 */
	private void setupTargetBitmap(int alpha) {
		// TODO Auto-generated method stub
		bitmap=Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mcanvas=new Canvas(bitmap);
		paint=new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);//防锯�?
		paint.setDither(true);//防抖�?
		paint.setAlpha(alpha);
		mcanvas.drawRect(iconRect, paint);//在图形上绘制纯色
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		paint.setAlpha(255);
		mcanvas.drawBitmap(iconBitmap, null, iconRect, paint);
	}

	public void setIconAlpha(float alpha){
		this.maplha=alpha;
		invalidateView();
	}

	/*
	 * 重绘
	 * */
	private void invalidateView() {
		// TODO Auto-generated method stub
		if(Looper.getMainLooper()==Looper.myLooper()){
			invalidate();
		}else{
			postInvalidate();
		}
	}

	public static final String INSTANCE_STATUS="instance_status";
	public static final String STATUS_ALPHA="status_alpha";
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		if(state instanceof Bundle){
			Bundle bundle=(Bundle) state;
			maplha=bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return ;
		}
		super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, maplha);
		
		return bundle;
	}
	
	
}
