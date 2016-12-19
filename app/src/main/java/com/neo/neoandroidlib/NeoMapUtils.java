package com.neo.neoandroidlib;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class NeoMapUtils {
	
	public static double calculateDistance(LatLng a,
			LatLng b){
		return DistanceUtil.getDistance(a, b);
	}
}
