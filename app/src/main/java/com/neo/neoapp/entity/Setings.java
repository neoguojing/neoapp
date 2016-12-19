package com.neo.neoapp.entity;

import com.neo.neoapp.R;
import com.neo.neoapp.entity.Setings;

import android.os.Parcel;
import android.os.Parcelable;

public class Setings extends Entity implements Parcelable {

	public static final String UID = "uid";
	public static final String IMAGE = "image";
	public static final String NAME = "name";

	private String uid;// ID
	private String mImame;// 头像
	private String mName;// 头像

	public Setings(String uid, String mImame, String mName) {
		super();
		this.uid = uid;
		this.mImame = mImame;
		this.mName = mName;

	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getmImame() {
		return mImame;
	}

	public void setmImame(String mImame) {
		this.mImame = mImame;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uid);
		dest.writeString(mImame);
		dest.writeString(mName);

	}

	public static final Parcelable.Creator<Setings> CREATOR = new Parcelable.Creator<Setings>() {

		@Override
		public Setings createFromParcel(Parcel source) {
			String uid = source.readString();
			String mImame = source.readString();
			String mName = source.readString();
			Setings setting = new Setings(uid, mImame, mName);
			return setting;
		}

		@Override
		public Setings[] newArray(int size) {
			return new Setings[size];
		}
	};

}
