package com.neo.neoapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;

import com.neo.neoandroidlib.NetWorkUtils;
import com.neo.neoapp.entity.NeoConfig;

public class NeoAppSetings {
	public static final String Local_Server_HostName = "neo-Lenovo-G450";
	
    public static final String ConfigFile = "config.json";
    public static final String HeadPicDir = "headpic/";
    public static final String IpServerUrl = "http://mainapp.applinzi.com/nat/obtain/neo/";
    public static final String DestIpFetchUrlPrefix = "http://mainapp.applinzi.com/android/fetchip/";
    public static final String DestIpUpdateUrlPrefix = "http://mainapp.applinzi.com/android/updateip/";
    public static final String MeFile = "me.json";
    public static final String MyFriendsFile = "myfriends.json";
    public static final String MyHeadPic = "headpic";
    public static final String MyNearByFile = "mynearby.json";
    public static final String MyPhotosDir = "myphotos/";
    public static final String MyPhotosOriginalDir = "myphotos/original/";
    public static final String MyPhotosThumbnailDir = "myphotos/thumbnail/";
    public static final String MyProfileFile = "myprofile.json";
    public static final String MyStatusFile = "mystatus.json";
    public static final String ProfilesDir = "profile/";
    public static final String StatuPhotosDir = "statusphoto/";
    public static final String StatusDir = "status/";
    public static final String UserMsgDir = "messages/";
    private static String download_suffix = null;
    private static String getsvc_suffix = null;
    private static String headpic_suffix = null;
    private static final String login_check_suffix = "/site1/android/logincheck/";
    private static final String login_suffix = "/site1/android/login/";
    private static final String logout_suffix = "/site1/android/logout/";
    private static final String prefix = "http://";
    private static String register_suffix;
    
    //佛如 socket
    public static final int port = 7000;

    public enum LOGIN_STATE {
        LOGIN,
        NOLOGIN,
        OFFLINE,
        SRVNOREACH,
        HTTPERR
    }

    public enum NEO_ERRCODE {
        NOERROR(0),
        REGISTER_SUCCESS(1),
        REGISTER_USER_EXIST(2),
        USER_NOEXIST(3),
        LOGIN_SUCCESS(4),
        PASSWORD_INVALICE(5),
        PASSWORD_ISNONE(6),
        PHONE_NOEXIST(7),
        EMAIL_NOEXIST(8),
        INVALIDE_JSON(9),
        UERE_LOGOUT(10),
        UER_NOLOGIN(11),
        DATA_NOEXIST(12),
        PHONE_REGISTERED(13),
        PIC_UPLOAD_FAIED(14),
        PIC_DOWNLOAD_ERROR(15),
        
        
        USERIP_NOEXIST(100),
        USERIP_UPDATE_SUCCESS(101);
        
        
        private int nCode;

        private NEO_ERRCODE(int _nCode) {
            this.nCode = _nCode;
        }

        public String toString() {
            return String.valueOf(this.nCode);
        }
    }

    static {
        register_suffix = "/site1/android/register/";
        headpic_suffix = "/site1/android/headpic/";
        getsvc_suffix = "/site1/android/get/";
        download_suffix = "/site1/android/download/";
    }

    public static Uri getServerUrl(String ip, String port) {
        return Uri.parse(new StringBuilder(prefix).append(ip).append(port).toString());
    }

    private static String getServerUrlString(NeoConfig config) {
    	if (!config.getLocalip().equals("")){
    		return getLocalServerUrlString(config);
    	}
        return new StringBuilder(prefix).append(config.getIp()).append(":").append(config.getPort()).toString();
    }

    public static String getLoginUrl(NeoConfig config) {
        return getServerUrlString(config) + login_suffix;
    }
    
    public static String getLogOutUrl(NeoConfig config) {
        return getServerUrlString(config) + logout_suffix;
    }
    
    public static String getLoginCheckUrl(NeoConfig config) {
        return getServerUrlString(config) + login_check_suffix;
    }

    public static String getRegisterUrl(NeoConfig config) {
        return getServerUrlString(config) + register_suffix;
    }

    public static String getRegisterPicUrl(NeoConfig config) {
        return getServerUrlString(config) + headpic_suffix;
    }

    public static String getGetUrl(NeoConfig config, String name) {
        return getServerUrlString(config) + getsvc_suffix + name;
    }

    public static String getGetDownLoad(NeoConfig config, String name) {
        return getServerUrlString(config) + download_suffix + name;
    }
    
	public static String getLocalServerIp(){
		
		String patten = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.)"+
				 "{3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		String pingResult = NetWorkUtils.neoPing(Local_Server_HostName);
		if (pingResult==null){
			return "";
		}
		Matcher m = Pattern.compile(patten).matcher(pingResult);
		if (m.find())
			return m.group();
		return "";
	}
	
	private static String getLocalServerUrlString(NeoConfig config) {
        return new StringBuilder(prefix).append(config.getLocalip()).
        		append(":").append(config.getLocalport()).toString();
    }
}
