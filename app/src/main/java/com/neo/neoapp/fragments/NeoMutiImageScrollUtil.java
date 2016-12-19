package com.neo.neoapp.fragments;



import com.neo.neoapp.R;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NeoMutiImageScrollUtil {
	
	private Context mContext;
	private NeoBasicFragment mFrag;
    
    private LinearLayout mGallery;
    private int[] imgMutiIdArray =new int[]{R.drawable.pic_adv1, R.drawable.pic_adv2, R.drawable.pic_adv3,
			R.drawable.pic_adv4,R.drawable.pic_adv5,R.drawable.pic_adv6};
	
	NeoMutiImageScrollUtil(Context ctx,NeoBasicFragment frag)
	{
		mContext = ctx;
		mFrag = frag;
		
		if (mContext!=null && mFrag!=null){
			InitView();
			InitEvent();
		}	
	}
	
	public NeoMutiImageScrollUtil() {
		// TODO Auto-generated constructor stub
	}

	public void InitView()
	{  
		mGallery = (LinearLayout) mFrag.findViewById(R.id.id_gallery);  
		for (int i = 0; i < imgMutiIdArray.length; i++)  
        {  
              
            View view = ((NeoDisPlayFragment)mFrag).getmInflater().inflate(R.layout.scroll_item_of_image_text, mGallery, false);  
            ImageView img = (ImageView) view  
                    .findViewById(R.id.muti_scroll_image);  
            img.setImageResource(imgMutiIdArray[i]);  
            TextView txt = (TextView) view  
                    .findViewById(R.id.muti_scroll_title);  
            txt.setText("");  
            mGallery.addView(view);  
        }
	}
	
	public void InitEvent()
	{
		return;
	}
	
}
