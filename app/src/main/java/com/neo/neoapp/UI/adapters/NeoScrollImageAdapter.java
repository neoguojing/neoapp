package com.neo.neoapp.UI.adapters;

import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.neo.neoapp.UI.NeoViewPageAdpterForView;

public class NeoScrollImageAdapter extends NeoViewPageAdpterForView {

	public NeoScrollImageAdapter(List<?> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}
	
	/*@Override  
    public void destroyItem(View container, int position, Object object) {  
        ((ViewPager)container).removeView(viewList.get(position % viewList.size()));  
          
    }  


    @Override  
    public Object instantiateItem(View container, int position) {  
        ((ViewPager)container).addView(viewList.get(position % viewList.size()), 0);  
        return viewList.get(position % viewList.size());  
    }*/
}
