package com.neo.neoapp.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.neo.neoandroidlib.FileUtils;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.NeoBasicApplication;
import com.neo.neoapp.R;

import cz.msebera.httpclient.android.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PeopleProfile extends Entity implements Parcelable {
    public static final String AGE = "age";
    public static final String AVATAR = "avatar";
    public static final String CONSTELLATION = "constellation";
    public static final String DISTANCE = "distance";
    public static final String GENDER = "gender";
    public static final String NAME = "name";
    public static final String PHOTOS = "photos";
    public static final String SIGN = "sign";
    public static final String SIGNATURE = "signature";
    public static final String SIGN_DIS = "sign_dis";
    public static final String SIGN_PIC = "sign_pic";
    public static final String TIME = "time";
    public static final String UID = "uid";
    private int age;
    private String avatar;
    private String constellation;
    private String distance;
    private int gender;
    private int genderBgId;
    private int genderId;
    private boolean isHasSign;
    private String name;
    private List<String> photos;
    private String sign;
    private String signDistance;
    private String signPicture;
    private String time;
    private String uid;

    public PeopleProfile(JSONObject object) {
        if (object != null) {
            try {
                this.uid = object.getString(UID);
                this.avatar = object.getString(AVATAR);
                this.name = object.getString(NAME);
                this.gender = object.getInt(GENDER);
                if (this.gender == 0) {
                }
                this.age = object.getInt(AGE);
                this.constellation = object.getString(CONSTELLATION);
                this.distance = object.getString(DISTANCE);
                this.time = object.getString(TIME);
                if (object.has(SIGNATURE)) {
                    this.isHasSign = true;
                    JSONObject signObject = object.getJSONObject(SIGNATURE);
                    this.sign = signObject.getString(SIGN);
                    if (signObject.has(SIGN_PIC)) {
                        this.signPicture = signObject.getString(SIGN_PIC);
                    }
                    this.signDistance = signObject.getString(SIGN_DIS);
                }
                this.photos = new ArrayList<String>();
                JSONArray photosArray = object.getJSONArray(PHOTOS);
                for (int i = 0; i < photosArray.length(); i++) {
                    this.photos.add(photosArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public PeopleProfile() {
		// TODO Auto-generated constructor stub
	}

	public PeopleProfile(PeopleProfile mMyProfile) {
		// TODO Auto-generated constructor stub
		this.age = mMyProfile.age;
	    this.avatar =  mMyProfile.avatar;
	    this.constellation= mMyProfile.constellation;
	    this.distance = mMyProfile.distance;
	    this.gender = mMyProfile.gender;
	    this.genderBgId = mMyProfile.genderBgId;
	    this.genderId = mMyProfile.genderId;
	    this.isHasSign = mMyProfile.isHasSign;
	    this.name = mMyProfile.name;
	    this.photos = new ArrayList<String>();
	    this.photos.addAll(mMyProfile.photos);
	    this.sign = mMyProfile.sign;
	    this.signDistance = mMyProfile.signDistance;
	    this.signPicture = mMyProfile.signPicture;
	    this.time = mMyProfile.time;
	    this.uid = mMyProfile.uid; 
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

    public String getConstellation() {
        return this.constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
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

    public boolean isHasSign() {
        return this.isHasSign;
    }

    public void setHasSign(boolean isHasSign) {
        this.isHasSign = isHasSign;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignPicture() {
        return this.signPicture;
    }

    public void setSignPicture(String signPicture) {
        this.signPicture = signPicture;
    }

    public String getSignDistance() {
        return this.signDistance;
    }

    public void setSignDistance(String signDistance) {
        this.signDistance = signDistance;
    }

    public static Creator<PeopleProfile> getCreator() {
        return CREATOR;
    }

    public List<String> getPhotos() {
        return this.photos;
    }

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

    public static boolean resolveMyProfile(NeoBasicApplication application, Context context) {
    	
    	
        String json = FileUtils.getJson(context, NeoAppSetings.MyProfileFile);
        if (json == null) {
            return false;
        }
        try {
            int genderId;
            int genderBgId;
            JSONObject object = new JSONObject(json);
            String userId = object.getString(UID);
            String avatar = object.getString(AVATAR);
            String name = object.getString(NAME);
            int gender = object.getInt(GENDER);
            if (gender == 0) {
                genderId = R.drawable.ic_user_famale;
                genderBgId = R.drawable.bg_gender_famal;
            } else {
                genderId = R.drawable.ic_user_male;
                genderBgId = R.drawable.bg_gender_male;
            }
            int age = object.getInt(AGE);
            String constellation = object.getString(CONSTELLATION);
            String distance = object.getString(DISTANCE);
            String time = object.getString(TIME);
            boolean isHasSign = false;
            String sign = BuildConfig.VERSION_NAME;
            String signPic = BuildConfig.VERSION_NAME;
            String signDis = BuildConfig.VERSION_NAME;
            if (object.has(SIGNATURE)) {
                isHasSign = true;
                JSONObject signObject = object.getJSONObject(SIGNATURE);
                sign = signObject.getString(SIGN);
                if (signObject.has(SIGN_PIC)) {
                    signPic = signObject.getString(SIGN_PIC);
                }
                signDis = signObject.getString(SIGN_DIS);
            }
            JSONArray photosArray = object.getJSONArray(PHOTOS);
            List<String> photos = new ArrayList();
            for (int i = 0; i < photosArray.length(); i++) {
                photos.add(photosArray.getString(i));
            }
            application.mMyProfile = new PeopleProfile();
            application.mMyProfile.setUid(userId);
            application.mMyProfile.setAvatar(avatar);
            application.mMyProfile.setName(name);
            application.mMyProfile.setGender(gender);
            application.mMyProfile.setGenderId(genderId);
            application.mMyProfile.setGenderBgId(genderBgId);
            application.mMyProfile.setAge(age);
            application.mMyProfile.setConstellation(constellation);
            application.mMyProfile.setDistance(distance);
            application.mMyProfile.setTime(time);
            application.mMyProfile.setHasSign(isHasSign);
            application.mMyProfile.setSign(sign);
            application.mMyProfile.setSignPicture(signPic);
            application.mMyProfile.setSignDistance(signDis);
            application.mMyProfile.setPhotos(photos);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            application.mMyProfile = null;
            return false;
        }
    }

    public JSONObject convertToJson() {
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put(AGE, getAge());
            jsondata.put(GENDER, getGender());
            jsondata.put(AVATAR, getAvatar());
            jsondata.put(DISTANCE, getDistance());
            jsondata.put(NAME, getName());
            jsondata.put(TIME, getTime());
            jsondata.put(UID, getUid());
            jsondata.put(CONSTELLATION, getConstellation());
            if (isHasSign()) {
                JSONObject signature = new JSONObject();
                signature.put(SIGN, getSign());
                signature.put(SIGN_DIS, getSignDistance());
                signature.put(SIGN_PIC, getSignPicture());
                jsondata.put(SIGNATURE, signature);
            }
            JSONArray picarray = new JSONArray();
            if (this.photos != null) {
                for (int i = 0; i < this.photos.size(); i++) {
                    picarray.put(this.photos.get(i));
                }
            }
            jsondata.put(PHOTOS, picarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsondata;
    }
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeInt(this.genderId);
        dest.writeInt(this.genderBgId);
        dest.writeInt(this.age);
        dest.writeString(this.constellation);
        dest.writeString(this.distance);
        dest.writeString(this.time);
        dest.writeInt(this.isHasSign ? 1 : 0);
        dest.writeString(this.sign);
        dest.writeString(this.signPicture);
        dest.writeString(this.signDistance);
        dest.writeList(this.photos);
	}

	public static final Parcelable.Creator<PeopleProfile> CREATOR = new Parcelable.Creator<PeopleProfile>() {

		@SuppressWarnings("unchecked")
		@Override
		public PeopleProfile createFromParcel(Parcel source) {
			PeopleProfile profile = new PeopleProfile();
			profile.setUid(source.readString());
			profile.setAvatar(source.readString());
			profile.setName(source.readString());
			profile.setGender(source.readInt());
			profile.setGenderId(source.readInt());
			profile.setGenderBgId(source.readInt());
			profile.setAge(source.readInt());
			profile.setConstellation(source.readString());
			profile.setDistance(source.readString());
			profile.setTime(source.readString());
			profile.setHasSign(source.readInt() == 1 ? true : false);
			profile.setSign(source.readString());
			profile.setSignPicture(source.readString());
			profile.setSignDistance(source.readString());
			profile.setPhotos(source.readArrayList(PeopleProfile.class
					.getClassLoader()));
			return profile;
		}

		@Override
		public PeopleProfile[] newArray(int size) {
			return new PeopleProfile[size];
		}
	};

}
