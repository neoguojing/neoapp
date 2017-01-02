package com.neo.neoapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class NeoConfig extends Entity implements Parcelable {
	
	public static final String IP = "ip";
	public static final String PORT = "port";
	public static final String NAME = "name";
	public static final String LOCALIP = "localip";
	public static final String LOCALPORT = "localport";
	
	
	private String ip;
	private String port;
	private String name;
	private String localip = "";
	private String localport = "8080";
	
	public NeoConfig(String ip, String port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;
	}
	
	public NeoConfig() {
		this.ip = "115.159.84.57";
		this.port = "80";
		this.name = "neo";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(ip);
		dest.writeString(port);
		dest.writeString(name);
	}
	
	public String getLocalip() {
		return localip;
	}

	public void setLocalip(String localip) {
		this.localip = localip;
	}

	public String getLocalport() {
		return localport;
	}

	public void setLocalport(String localport) {
		this.localport = localport;
	}

	public static final Parcelable.Creator<NeoConfig> CREATOR = new Parcelable.Creator<NeoConfig>() {

		@Override
		public NeoConfig createFromParcel(Parcel source) {
			String ip = source.readString();
			String port = source.readString();
			String name = source.readString();
			NeoConfig config = new NeoConfig(ip, port, name);
			return config;
		}

		@Override
		public NeoConfig[] newArray(int size) {
			return new NeoConfig[size];
		}
	};

}
