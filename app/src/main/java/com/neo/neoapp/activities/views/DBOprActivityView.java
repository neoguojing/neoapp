package com.neo.neoapp.activities.views;

import java.util.Iterator;

import com.neo.neoapp.R;
import com.neo.neoapp.providers.rawdata.NeoBasicMetaData;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class DBOprActivityView {
	
	private TextView tvDBName;
	private TextView tvDBVersion;
	private TextView tvDBUri;
	private TableRow trDBTableRow;

	
	public void setDataBaseInfoView(View view){
		
		 Log.d("DBOprActivityView",String.valueOf(view));
		 if(null != view){
		
			tvDBName = (TextView)view.findViewById(R.id.db_name);
			tvDBVersion = (TextView)view.findViewById(R.id.db_version);
			tvDBUri = (TextView)view.findViewById(R.id.db_uri);
			
			tvDBName.setText(NeoBasicMetaData.DATABASE_NAME);
			tvDBName.setBackgroundColor(0xffffff);
			tvDBVersion.setText(String.valueOf(NeoBasicMetaData.DATABASE_VERSION));
			tvDBUri.setText(NeoBasicMetaData.AUTHORITY);
			
			trDBTableRow = (TableRow)view.findViewById(R.id.db_table_row); 
			
			Iterator<String> it = NeoBasicMetaData.mDBTables.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				String tableName = NeoBasicMetaData.mDBTables.get(key);
				TextView tvTableName = new TextView(view.getContext());
				tvTableName.setText(tableName);
				trDBTableRow.addView(tvTableName);
			}
			
			Log.d("DBOprActivityView",(String) tvDBName.getText());
			Log.d("DBOprActivityView",(String) tvDBVersion.getText());
			Log.d("DBOprActivityView",(String) tvDBUri.getText());
		 }
	}
	
	public void setDataBaseInfoView(Activity activity){
		
		 Log.d("DBOprActivityView",String.valueOf(activity));
		 if(null != activity){
		
			tvDBName = (TextView)activity.findViewById(R.id.db_name);
			tvDBVersion = (TextView)activity.findViewById(R.id.db_version);
			tvDBUri = (TextView)activity.findViewById(R.id.db_uri);
			
			//if(tvDBName!=null)
				tvDBName.setText(NeoBasicMetaData.DATABASE_NAME);
				tvDBName.setBackgroundColor(0xffffff);
			//if(tvDBVersion!=null)
				tvDBVersion.setText(String.valueOf(NeoBasicMetaData.DATABASE_VERSION));
			//if(tvDBUri!=null)
				tvDBUri.setText(NeoBasicMetaData.AUTHORITY);
		 }
	}
}
