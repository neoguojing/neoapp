package com.neo.neoapp.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.neo.neoapp.R;
import com.neo.neoapp.NeoBasicApplication;

public class NeoBasicMapFragment extends NeoBasicFragment{
	
	MapView mMapView = null; 
	BaiduMap mBaiduMap = null;
	BitmapDescriptor mCurrentMarker;
	
	public NeoBasicMapFragment() {
		super();
	}

	public NeoBasicMapFragment(NeoBasicApplication application, Activity activity,
			Context context) {
		super(application, activity, context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_baidumap, container,
				false);  
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		mMapView = (MapView) findViewById(R.id.bmapView); 
	}

	@Override
	protected void initEvents() {

		
	}

	@Override
	protected void init() {
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		
		initLocation();
		showMyLocation();
	}

	
	public void onCancel() {
		clearAsyncTask();
		
	}
	
	protected void initLocation()
	{
		mBaiduMap.setMyLocationEnabled(true);
		MyLocationData locData = new MyLocationData.Builder()
		.accuracy(mApplication.mLocation.getRadius()) 
	    .direction(100).latitude(mApplication.mLocation.getLatitude())  
	    .longitude(mApplication.mLocation.getLongitude()).build();  
		
		mBaiduMap.setMyLocationData(locData);
		
		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）  
		/*mCurrentMarker = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_geo);  
		MyLocationConfiguration config = 
				new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);  
		mBaiduMap.setMyLocationConfiguration(); */
	}
	
	protected void showMyLocation(){
		LatLng centerPoint = new LatLng(mApplication.mLatitude,mApplication.mLongitude);
		
		//define map status
		MapStatus mapStatu = new MapStatus.Builder().target(centerPoint)
				.zoom(12).build();
		//update map status
		MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newMapStatus(mapStatu);
		mBaiduMap.setMapStatus(mapUpdate);
	}

	@Override
	public void onDestroy() {
		clearAsyncTask();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy(); 
		super.onDestroy();
	}
	
	@Override
	public void onResume() {  
        super.onResume();   
        mMapView.onResume();  
    }  
    @Override
	public void onPause() {  
        super.onPause();   
        mMapView.onPause();  
    } 
}
