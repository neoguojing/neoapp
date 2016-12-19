package com.neo.neoapp.providers.rawdata;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class NeoBasicMetaData {
	
	public static final String AUTHORITY = "com.neo.providers.RecordProvider";
	
	public static final String DATABASE_NAME = "neoapp.db";
	public static final int DATABASE_VERSION = 1;
	//public static final String DBTABLE_NAME = "records";
	public static HashMap<String, String> mDBTables = new HashMap<String, String>();
	static {
		mDBTables.put("NeoBasicTableMetaData", "records");
	}
	
	private NeoBasicMetaData(){}
	
	public static final class NeoBasicTableMetaData implements BaseColumns{
		
		private NeoBasicTableMetaData(){}
		public static final String TABLE_NAME = "records";
		
		public static final Uri CONTENT_URI = 
				Uri.parse("content://"+ AUTHORITY + "/records");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.neoapp.record"; 
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.neoapp.record";
		
		public static final String DEFAULT_SORT_ORDER = "modified DESC";
		
		public static final String CREATE_SQL = "CREATE TABLE " + NeoBasicTableMetaData.TABLE_NAME+
				"("+NeoBasicTableMetaData._ID+" INTEGER PRIMARY KEY,"+
				NeoBasicTableMetaData.RECORD_NAME + " TEXT,"+
				NeoBasicTableMetaData.RECORD_TAG + " TEXT,"+
				NeoBasicTableMetaData.RECORD_CONTENT+" TEXT,"+
				NeoBasicTableMetaData.RECORD_CREATE_DATE+" INTEGER,"+
				NeoBasicTableMetaData.RECORD_MODIFIED_DATE+" INTEGER"+
				");";
		public static final String DROP_SQL = "DROP TABLE IF EXISTS" + NeoBasicTableMetaData.TABLE_NAME;
		
		public static final String RECORD_NAME = "name";
		public static final String RECORD_TAG = "tag";
		public static final String RECORD_CONTENT = "content";
		public static final String RECORD_CREATE_DATE = "created";
		public static final String RECORD_MODIFIED_DATE = "modified";
	}

}
