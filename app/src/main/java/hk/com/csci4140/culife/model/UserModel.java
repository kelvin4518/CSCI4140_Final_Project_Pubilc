package hk.com.csci4140.culife.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.activity.LoginActivity;
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

    // Mihchael added :

    public static String myChatName;
    public static JSONArray myChatList;



    public static void initModel(Context mContext){
        myChatName = SessionManager.getString(mContext,Constant.USERNAME);
        formatMyChatListFromStringList(SessionManager.getArrayList(Constant.USER_CHAT_LIST));

        Log.d("USERMODEL", "initModel: list : "+myChatList);



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



    // this part use session manager to set the necessary state
    public static void login(Context mContext, boolean isRemember, String token,@Nullable String iconUrl, int regionNum, int showLocation){

        SessionManager.putBoolean(mContext, Constant.IS_LOGIN, true);
        SessionManager.putBoolean(mContext, Constant.IS_REMEMBER, isRemember);
        SessionManager.putString(mContext, Constant.TOKEN, token);
        SessionManager.putString(mContext, Constant.ICON_URL, (iconUrl == null ? "" : iconUrl));
        SessionManager.putInt(mContext, Constant.REGION, regionNum);
        SessionManager.putInt(mContext, Constant.SHOW_LOCATION, showLocation);
        initModel(mContext);
    }

    public static void fromLoginJson(Context mContext,boolean isRemember,JSONObject jsonObject) {
        try {
            JSONObject responseObject = jsonObject.getJSONObject("user");
            String token = responseObject.getString("token");


            String username = responseObject.getString("username");




//            Log.d(Constant.API_REPORT_TAG, "fromLoginJson: response : "+jsonObject);
//            Log.d(Constant.API_REPORT_TAG, "fromLoginJson: user : "+responseObject);
//            Log.d(Constant.API_REPORT_TAG, "fromLoginJson: token : "+token);
            SessionManager.putBoolean(mContext, Constant.IS_LOGIN, true);
            SessionManager.putBoolean(mContext, Constant.IS_REMEMBER, isRemember);
            SessionManager.putString(mContext, Constant.TOKEN, token);
            SessionManager.putString(mContext, Constant.USERNAME, username);

            isLogin = SessionManager.getBoolean(mContext, Constant.IS_LOGIN);
            isRemember = SessionManager.getBoolean(mContext, Constant.IS_REMEMBER);
            token = SessionManager.getString(mContext, Constant.TOKEN);
            username = SessionManager.getString(mContext, Constant.USERNAME);
            initModel(mContext);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        SessionManager.putString(mContext, Constant.USERNAME, "");
        initModel(mContext);
    }

    public static String getMyChatName() {
        if(myChatName == null || myChatName == ""){
            return "Anonymous";
        }else{
            return myChatName;
        }
    }
























//            JSONObject object = new JSONObject();
//            object.put("otherUserID","10");
//            object.put("iconLink","https://avatars0.githubusercontent.com/u/9919?s=280&v=4");
//            object.put("name","username!");
//            object.put("lastMessage","some message");
//            object.put("lastDate","04/05/2018");






    public static void addNewChatToChatList(JSONObject chatInfo){
        ArrayList<String> chatInfoList = SessionManager.getArrayList(Constant.USER_CHAT_LIST);
        try{
            String mapString = chatInfo.toString();
            chatInfoList.add(mapString);
        }catch (Exception e){

        }
        SessionManager.putArrayList(chatInfoList,Constant.USER_CHAT_LIST);
    }

    public static void formatMyChatListFromStringList(ArrayList<String> stringArrayList){

        myChatList = new JSONArray();
        for(int i=0;i<stringArrayList.size();i++){
            String jsonString = stringArrayList.get(i);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                myChatList.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONArray getMyChatList() {
        if(myChatList == null){
            myChatList = new JSONArray();
            return myChatList;
        }else{
            return myChatList;
        }
    }
}
