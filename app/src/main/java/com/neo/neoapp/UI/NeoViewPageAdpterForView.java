package com.neo.neoapp.UI;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class NeoViewPageAdpterForView extends PagerAdapter {

	protected List<View> viewList;
	protected List<String> titleList; 
	
	public NeoViewPageAdpterForView(List<?> list)
	{
		viewList = (List<View>) list;
		titleList = null;
	}
	
	public NeoViewPageAdpterForView(List<?> viewList, List<String> titleList)
	{
		this.viewList = (List<View>) viewList;
		this.titleList = titleList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView(viewList.get(position)); 
    }  


    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  
         container.addView(viewList.get(position), 0); 
         return viewList.get(position);  
    }  
    
    @Override  
    public CharSequence getPageTitle(int position) {  
        // TODO Auto-generated method stub 
    	if (titleList==null || titleList.isEmpty())
    		return null;
        return titleList.get(position);  
    }  

}
