package com.neo.neoapp.providers;

import java.util.HashMap;

import com.neo.neoapp.providers.rawdata.NeoBasicMetaData;
import com.neo.neoapp.providers.rawdata.NeoBasicMetaData.NeoBasicTableMetaData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class NeoAppContentProvider extends ContentProvider {

	private static final String TAG = "NeoAppContentProvider";
	
	private static HashMap<String, String> mRecordsProjectionMap;
	static{
		mRecordsProjectionMap = new HashMap<String,String>();
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData._ID,
					NeoBasicMetaData.NeoBasicTableMetaData._ID);
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData.RECORD_NAME,
					NeoBasicMetaData.NeoBasicTableMetaData.RECORD_NAME);
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData.RECORD_TAG,
				NeoBasicMetaData.NeoBasicTableMetaData.RECORD_TAG);
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData.RECORD_CONTENT,
				NeoBasicMetaData.NeoBasicTableMetaData.RECORD_CONTENT);
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData.RECORD_CREATE_DATE,
				NeoBasicMetaData.NeoBasicTableMetaData.RECORD_CREATE_DATE);
		mRecordsProjectionMap.put(NeoBasicMetaData.NeoBasicTableMetaData.RECORD_MODIFIED_DATE,
				NeoBasicMetaData.NeoBasicTableMetaData.RECORD_MODIFIED_DATE);
	}
	
	private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int COLLECTION_URI_INDICATOR = 1;
	private static final int SINGLE_URI_INDICATOR = 2;
	
	static {
		mUriMatcher.addURI(NeoBasicMetaData.AUTHORITY, "records", COLLECTION_URI_INDICATOR);
		mUriMatcher.addURI(NeoBasicMetaData.AUTHORITY, "records/#", SINGLE_URI_INDICATOR);
	}
	
	//创建数据库
	private static class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context context) {
			super(context, NeoBasicTableMetaData.TABLE_NAME, null, 
					NeoBasicMetaData.DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.d(TAG, "inner onCreate called");
			db.execSQL(NeoBasicTableMetaData.CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
			// TODO Auto-generated method stub
			Log.d(TAG, "inner onUpgrade called");
			Log.w(TAG, "upgrade database from version "+ oldversion+" to"+ newversion);
			db.execSQL(NeoBasicTableMetaData.DROP_SQL);
			onCreate(db);
		}
		
	}
	
	private DataBaseHelper mDataBaseHelper;
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
		int count;
		switch(mUriMatcher.match(uri)){
		case COLLECTION_URI_INDICATOR:
			count = db.delete(NeoBasicTableMetaData.TABLE_NAME, whereClause, whereArgs);
			break;
		case SINGLE_URI_INDICATOR:
			String rowid = uri.getPathSegments().get(1);
			count = db.delete(NeoBasicTableMetaData.TABLE_NAME, 
					NeoBasicTableMetaData._ID+"="+rowid+
					(!TextUtils.isEmpty(whereClause)?" And ("+whereClause+")":"")
					, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri"+uri);	
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(mUriMatcher.match(uri)){
		case COLLECTION_URI_INDICATOR:
			return NeoBasicTableMetaData.CONTENT_TYPE;
		case SINGLE_URI_INDICATOR:
			return NeoBasicTableMetaData.CONTENT_ITEM_TYPE;
		
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (mUriMatcher.match(uri) != COLLECTION_URI_INDICATOR){
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		
		ContentValues values;
		if (initialValues != null){
			values = new ContentValues(initialValues);
		}else{
			values = new ContentValues();
		}
		
		Long now = Long.valueOf(System.currentTimeMillis());
		
		if (values.containsKey(NeoBasicTableMetaData.RECORD_CREATE_DATE) == false){
			values.put(NeoBasicTableMetaData.RECORD_CREATE_DATE, now);
		}
		if (values.containsKey(NeoBasicTableMetaData.RECORD_MODIFIED_DATE) == false){
			values.put(NeoBasicTableMetaData.RECORD_MODIFIED_DATE, now);
		}
		
		if (values.containsKey(NeoBasicTableMetaData.RECORD_NAME) == false){
			throw new SQLException("insert row failed because the record name is needed!");
		}
		
		if (values.containsKey(NeoBasicTableMetaData.RECORD_TAG) == false){
			values.put(NeoBasicTableMetaData.RECORD_TAG, "NULL");
		}
		if (values.containsKey(NeoBasicTableMetaData.RECORD_CONTENT) == false){
			values.put(NeoBasicTableMetaData.RECORD_CONTENT, "NULL");
		}
		
		SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
		long rowid = db.insert(NeoBasicTableMetaData.TABLE_NAME, 
				NeoBasicTableMetaData.RECORD_NAME, values);
		if(rowid > 0){
			Uri insertedRecordUri = ContentUris.withAppendedId(NeoBasicTableMetaData.CONTENT_URI,
					rowid);
			getContext().getContentResolver().notifyChange(insertedRecordUri, null);
			return insertedRecordUri;
		}
		throw new SQLException("Failed to insert row into" + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnCreate called");
		mDataBaseHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch(mUriMatcher.match(uri)){
		case COLLECTION_URI_INDICATOR:
			qb.setTables(NeoBasicTableMetaData.TABLE_NAME);
			qb.setProjectionMap(mRecordsProjectionMap);
			break;
		case SINGLE_URI_INDICATOR:
			qb.setTables(NeoBasicTableMetaData.TABLE_NAME);
			qb.setProjectionMap(mRecordsProjectionMap);
			qb.appendWhere(NeoBasicTableMetaData._ID + "=" + uri.getPathSegments().get(1));
			break;
		
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		String orderby;
		
		if(TextUtils.isEmpty(sortOrder)){
			orderby = NeoBasicTableMetaData.DEFAULT_SORT_ORDER;
		}else{
			orderby = sortOrder;
		}
		
		SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderby);
		
		//int i = c.getCount();
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
		int count;
		switch(mUriMatcher.match(uri)){
		case COLLECTION_URI_INDICATOR:
			count = db.update(NeoBasicTableMetaData.TABLE_NAME, values, whereClause, whereArgs);
			break;
		case SINGLE_URI_INDICATOR:
			String rowid = uri.getPathSegments().get(1);
			count = db.update(NeoBasicTableMetaData.TABLE_NAME, 
					values, NeoBasicTableMetaData._ID+"="+rowid+
					(!TextUtils.isEmpty(whereClause)?" And ("+whereClause+")":"")
					, whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
