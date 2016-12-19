package com.neo.neoapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoNoScrollGridView;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;


public class NeoGridViewUtil implements OnItemClickListener {
	
	private Context mContext;
	private NeoBasicFragment mFrag;
	
	private NeoNoScrollGridView gview;
    private List<Map<String, Object>> data_list;  
    private SimpleAdapter  mAdapter;
    int[] icon = new int[]{R.drawable.ic_amuse, R.drawable.ic_food, R.drawable.ic_hotel,
			R.drawable.ic_movie,R.drawable.ic_sight,R.drawable.ic_takeout,
			R.drawable.ic_sight,R.drawable.ic_takeout}; 
    String[] iconName = { "娱乐", "餐饮", "酒店", "电影", "景观", "外卖","景观", "外卖"};
	
	NeoGridViewUtil(Context ctx,NeoBasicFragment frag)
	{
		mContext = ctx;
		mFrag = frag;
		
		if (mContext!=null && mFrag!=null){
			InitView();
			InitEvent();
		}	
	}
	
	public NeoGridViewUtil() {
		// TODO Auto-generated constructor stub
	}

	public void InitView()
	{
		gview = (NeoNoScrollGridView) mFrag.findViewById(R.id.grid_for_display);
        data_list = new ArrayList<Map<String, Object>>();
		
        if (data_list.isEmpty()) {
        	mFrag.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					//showLoadingDialog("正在加载,请稍后...");
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					for(int i=0;i<icon.length;i++){
			            Map<String, Object> map = new HashMap<String, Object>();
			            map.put("image", icon[i]);
			            map.put("text", iconName[i]);
			            data_list.add(map);
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
						String [] from ={"image","text"};
				        int [] to = {R.id.grid_image,R.id.grid_title};
				        mAdapter = new SimpleAdapter(mContext, data_list, R.layout.grid_item_of_image_text, from, to);
				        //配置适配器
				        gview.setAdapter(mAdapter);
						
					}

				}
			});

        }else
        {
        	String [] from ={"image","text"};
	        int [] to = {R.id.grid_image,R.id.grid_title};
	        mAdapter = new SimpleAdapter(mContext, data_list, R.layout.grid_item_of_image_text, from, to);
	        //配置适配器
	        gview.setAdapter(mAdapter);
        }
	}
	
	public void InitEvent()
	{
		gview.setOnItemClickListener(this);
		return;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
