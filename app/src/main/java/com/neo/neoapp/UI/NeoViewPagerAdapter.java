package com.neo.neoapp.UI;

import java.util.List;

import com.neo.neoapp.fragments.NeoBasicFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NeoViewPagerAdapter extends FragmentPagerAdapter {
	
	private List<NeoBasicFragment> mList;

	public NeoViewPagerAdapter(FragmentManager fm,List<NeoBasicFragment> inlist) {
		super(fm);
		// TODO Auto-generated constructor stub
		mList = inlist;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

}
