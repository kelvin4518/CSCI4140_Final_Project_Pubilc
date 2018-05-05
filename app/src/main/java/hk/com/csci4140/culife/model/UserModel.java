package hk.com.csci4140.culife.model;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.utility.SessionManager;

/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */

/**
 * This class is to save the user data.
 */
public class UserModel {

    public static boolean isLogin;
    public static boolean isRemember;
    public static String token;
    public static String iconUrl;
    public static int region;
    public static int showLocation;


    public static void initModel(Context mContext){
        isLogin = SessionManager.getBoolean(mContext, Constant.IS_LOGIN);
        isRemember = SessionManager.getBoolean(mContext, Constant.IS_REMEMBER);
        token = SessionManager.getString(mContext, Constant.TOKEN);
        iconUrl = SessionManager.getString(mContext, Constant.ICON_URL);
        if(SessionManager.getInt(mContext, Constant.REGION) != 1 &&
                SessionManager.getInt(mContext, Constant.REGION) != 2) {
            int language = SessionManager.getInt(mContext, Constant.LANGUAGE_SETTING);
            if(language == 0){
                Resources resources = mContext.getResources();
                Configuration configuration = resources.getConfiguration();
                if(configuration.locale.getCountry().equals("CN")){
                    region = 2;
                }
                else {
                    region = 1;
                }
            }
        }
        else {
            region = SessionManager.getInt(mContext, Constant.REGION);
        }
        showLocation = SessionManager.getInt(mContext, Constant.SHOW_LOCATION);
    }


    public static void login(Context mContext, boolean isRemember, String token,@Nullable String iconUrl, int regionNum, int showLocation){

        SessionManager.putBoolean(mContext, Constant.IS_LOGIN, true);
        SessionManager.putBoolean(mContext, Constant.IS_REMEMBER, isRemember);
        SessionManager.putString(mContext, Constant.TOKEN, token);
        SessionManager.putString(mContext, Constant.ICON_URL, (iconUrl == null ? "" : iconUrl));
        SessionManager.putInt(mContext, Constant.REGION, regionNum);
        SessionManager.putInt(mContext, Constant.SHOW_LOCATION, showLocation);
        initModel(mContext);
    }


    public static void updateIconUrl(Context mContext, @Nullable String iconUrl){
        SessionManager.putString(mContext, Constant.ICON_URL, (iconUrl == null ? "" : iconUrl));
        initModel(mContext);
    }


    public static void updateRegion(Context mContext, int regionNum){
        if(SessionManager.getInt(mContext, Constant.REGION) != 1 &&
                SessionManager.getInt(mContext, Constant.REGION) != 2) {
            int language = SessionManager.getInt(mContext, Constant.LANGUAGE_SETTING);
            if(language == 0){
                Resources resources = mContext.getResources();
                Configuration configuration = resources.getConfiguration();
                if(configuration.locale.getCountry().equals("CN")){
                    SessionManager.putInt(mContext, Constant.REGION, 2);
                }
                else {
                    SessionManager.putInt(mContext, Constant.REGION, 1);
                }
            }
        }
        else {
            SessionManager.putInt(mContext, Constant.REGION, regionNum);
        }
        initModel(mContext);
    }


    public static void updateShowLocation(Context mContext, int showLocation){
        SessionManager.putInt(mContext, Constant.SHOW_LOCATION, showLocation);
        initModel(mContext);
    }


    public static void logout(Context mContext){
        SessionManager.putBoolean(mContext, Constant.IS_LOGIN, false);
        SessionManager.putBoolean(mContext, Constant.IS_REMEMBER, false);
        SessionManager.putString(mContext, Constant.TOKEN, "");
        SessionManager.putString(mContext, Constant.ICON_URL, "");
        SessionManager.putInt(mContext, Constant.REGION, 0);
        SessionManager.putInt(mContext, Constant.SHOW_LOCATION, 0);
        initModel(mContext);
    }

}
