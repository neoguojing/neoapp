package com.neo.neoapp.entity;

import com.neo.neoapp.R;
import com.neo.neoapp.entity.People;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.neo.neoandroidlib.FileUtils;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoBasicApplication;

import cz.msebera.httpclient.android.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class People extends Entity implements Parcelable {
    public static final String AGE = "age";
    public static final String AVATAR = "avatar";
    public static final String BIRTHDAY = "birthday";
    public static final String DEVICE = "device";
    public static final String DISTANCE = "distance";
    public static final String GENDER = "gender";
    public static final String GROUP_ROLE = "group_role";
    public static final String INDUSTRY = "industry";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String MULTIPIC = "multipic";
    public static final String NAME = "name";
    public static final String RELATION = "relation";
    public static final String RENREN = "renren";
    public static final String SIGN = "sign";
    public static final String TIME = "time";
    public static final String TX_WEIBO = "tx_weibo";
    public static final String UID = "uid";
    public static final String VIP = "vip";
    public static final String WEIBO = "weibo";
    public static final String IP = "ip";
    public static final String PORT = "port";
    private int age = 0;
    private String avatar = "";
    private String birthday = "";
    private int device = 0;
    private String distance = "";
    private int gender = 0;
    private int genderBgId;
    private int genderId;
    private String industry = "";
    private int isGroupRole = 0;
    private int isMultipic = 0;
    private int isRelation = 0;
    private int isVip = 0;
    private int isbindRenRen = 0;
    private int isbindTxWeibo = 0;
    private int isbindWeibo = 0;
    private double latitude = 0;
    private double longitude = 0;
    private String name = "";
    private String sign = "";
    private String time = "";
    private String uid = "";
    private String ip = "";
    private int port = 0;

    public People(String uid, String avatar, int isVip, int isGroupRole,
    		String industry, int isbindWeibo, int isbindTxWeibo,
    		int isbindRenRen, int device, int isRelation, int isMultipic, 
    		String name, int gender, int age, String distance, String time,
    		String sign, String birthday, double longitude, double latitude,
    		String ip,int port) {
        this.uid = uid;
        this.avatar = avatar;
        this.isVip = isVip;
        this.isGroupRole = isGroupRole;
        this.industry = industry;
        this.isbindWeibo = isbindWeibo;
        this.isbindTxWeibo = isbindTxWeibo;
        this.isbindRenRen = isbindRenRen;
        this.device = device;
        this.isRelation = isRelation;
        this.isMultipic = isMultipic;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.distance = distance;
        this.time = time;
        this.sign = sign;
        if (gender == 0) {
            setGenderId(R.drawable.ic_user_famale);
            setGenderBgId(R.drawable.bg_gender_famal);
        } else {
            setGenderId(R.drawable.ic_user_male);
            setGenderBgId(R.drawable.bg_gender_male);
        }
        this.birthday = birthday;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ip = ip;
        this.port = port;
    }

    public People(JSONObject object) {
        if (object != null) {
            try {
                this.uid = object.getString(UID);
                this.avatar = object.getString(AVATAR);
                this.isVip = object.getInt(VIP);
                this.isGroupRole = object.getInt(GROUP_ROLE);
                this.industry = object.getString(INDUSTRY);
                this.isbindWeibo = object.getInt(WEIBO);
                this.isbindTxWeibo = object.getInt(TX_WEIBO);
                this.isbindRenRen = object.getInt(RENREN);
                this.device = object.getInt(DEVICE);
                this.isRelation = object.getInt(RELATION);
                this.isMultipic = object.getInt(MULTIPIC);
                this.name = object.getString(NAME);
                this.gender = object.getInt(GENDER);
                if (gender == 0) {
                    setGenderId(R.drawable.ic_user_famale);
                    setGenderBgId(R.drawable.bg_gender_famal);
                } else {
                    setGenderId(R.drawable.ic_user_male);
                    setGenderBgId(R.drawable.bg_gender_male);
                }
                this.age = object.getInt(AGE);
                this.distance = object.getString(DISTANCE);
                this.time = object.getString(TIME);
                this.sign = object.getString(SIGN);
                this.birthday = object.getString(BIRTHDAY);
                this.latitude = object.getDouble(LATITUDE);
                this.longitude = object.getDouble(LONGITUDE);
                this.ip = "";
                this.port = 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    public People(People people) {
        this.uid = people.getUid();
        this.avatar = people.getAvatar();
        this.isVip = people.getIsVip();
        this.isGroupRole = people.getIsGroupRole();
        this.industry = people.getIndustry();
        this.isbindWeibo = people.getIsbindWeibo();
        this.isbindTxWeibo = people.getIsbindRenRen();
        this.isbindRenRen = people.getIsbindRenRen();
        this.device = people.getDevice();
        this.isRelation = people.getIsRelation();
        this.isMultipic = people.getIsMultipic();
        this.name = people.getName();
        this.gender = people.getGender();
        this.age = people.getAge();
        this.distance = people.getDistance();
        this.time = people.getTime();
        this.sign = people.getSign();
        if (gender == 0) {
            setGenderId(R.drawable.ic_user_famale);
            setGenderBgId(R.drawable.bg_gender_famal);
        } else {
            setGenderId(R.drawable.ic_user_male);
            setGenderBgId(R.drawable.bg_gender_male);
        }
        this.birthday = people.getBirthday();
        this.longitude = people.getLongitude();
        this.latitude = people.getLatitude();
        this.ip = people.getIp();
        this.port = people.getPort();
    }
    
    public People() {

	}

	public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIsVip() {
        return this.isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsGroupRole() {
        return this.isGroupRole;
    }

    public void setIsGroupRole(int isGroupRole) {
        this.isGroupRole = isGroupRole;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getIsbindWeibo() {
        return this.isbindWeibo;
    }

    public void setIsbindWeibo(int isbindWeibo) {
        this.isbindWeibo = isbindWeibo;
    }

    public int getIsbindTxWeibo() {
        return this.isbindTxWeibo;
    }

    public void setIsbindTxWeibo(int isbindTxWeibo) {
        this.isbindTxWeibo = isbindTxWeibo;
    }

    public int getIsbindRenRen() {
        return this.isbindRenRen;
    }

    public void setIsbindRenRen(int isbindRenRen) {
        this.isbindRenRen = isbindRenRen;
    }

    public int getDevice() {
        return this.device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public int getIsRelation() {
        return this.isRelation;
    }

    public void setIsRelation(int isRelation) {
        this.isRelation = isRelation;
    }

    public int getIsMultipic() {
        return this.isMultipic;
    }

    public void setIsMultipic(int isMultipic) {
        this.isMultipic = isMultipic;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGenderId() {
        return this.genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getGenderBgId() {
        return this.genderBgId;
    }

    public void setGenderBgId(int genderBgId) {
        this.genderBgId = genderBgId;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
	@Override
	public int describeContents() {
		return 0;
	}
    public static boolean resolveMe(NeoBasicApplication application, Context context) {
        String json = FileUtils.getJson(context, NeoAppSetings.MeFile);
        if (json == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(json);
            application.mMe = new People(jSONObject.getString(UID),
            		jSONObject.getString(AVATAR), jSONObject.getInt(VIP),
            		jSONObject.getInt(GROUP_ROLE), jSONObject.getString(INDUSTRY),
            		jSONObject.getInt(WEIBO), jSONObject.getInt(TX_WEIBO),
            		jSONObject.getInt(RENREN), jSONObject.getInt(DEVICE), 
            		jSONObject.getInt(RELATION), jSONObject.getInt(MULTIPIC), 
            		jSONObject.getString(NAME), jSONObject.getInt(GENDER),
            		jSONObject.getInt(AGE), jSONObject.getString(DISTANCE),
            		jSONObject.getString(TIME), jSONObject.getString(SIGN),
            		jSONObject.getString(BIRTHDAY), jSONObject.getDouble(LATITUDE),
            		jSONObject.getDouble(LONGITUDE),"",0);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject convertToJson() {
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put(AGE, getAge());
            jsondata.put(DEVICE, getDevice());
            jsondata.put(GENDER, getGender());
            jsondata.put(RENREN, getIsbindRenRen());
            jsondata.put(TX_WEIBO, getIsbindTxWeibo());
            jsondata.put(WEIBO, getIsbindWeibo());
            jsondata.put(GROUP_ROLE, getIsGroupRole());
            jsondata.put(MULTIPIC, getIsMultipic());
            jsondata.put(RELATION, getIsRelation());
            jsondata.put(VIP, getIsVip());
            jsondata.put(LATITUDE, getLatitude());
            jsondata.put(LONGITUDE, getLongitude());
            jsondata.put(AVATAR, getAvatar());
            jsondata.put(BIRTHDAY, getBirthday());
            jsondata.put(DISTANCE, getDistance());
            jsondata.put(INDUSTRY, getIndustry());
            jsondata.put(NAME, getName());
            jsondata.put(SIGN, getSign());
            jsondata.put(TIME, getTime());
            jsondata.put(UID, getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsondata;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeInt(this.isVip);
        dest.writeInt(this.isGroupRole);
        dest.writeString(this.industry);
        dest.writeInt(this.isbindWeibo);
        dest.writeInt(this.isbindTxWeibo);
        dest.writeInt(this.isbindRenRen);
        dest.writeInt(this.device);
        dest.writeInt(this.isRelation);
        dest.writeInt(this.isMultipic);
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeInt(this.age);
        dest.writeString(this.distance);
        dest.writeString(this.time);
        dest.writeString(this.sign);
        dest.writeString(this.birthday);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.ip);
        dest.writeInt(this.port);
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
	public static final Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>() {

		@Override
		public People createFromParcel(Parcel source) {
                return new People(source.readString(), 
				source.readString(), source.readInt(),
				 source.readInt(), source.readString(),
				  source.readInt(), source.readInt(),
				   source.readInt(), source.readInt(), 
				   source.readInt(), source.readInt(), 
				   source.readString(), source.readInt(),
				    source.readInt(), source.readString(), 
					source.readString(), source.readString(),
					source.readString(), 
					 source.readDouble(), source.readDouble(),
					 source.readString(),source.readInt());
            }

		@Override
		public People[] newArray(int size) {
			return new People[size];
		}
	};

}
