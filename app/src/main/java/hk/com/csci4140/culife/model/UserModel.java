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
import hk.com.csci4140.culife.activity.MainActivity;
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
    public static String myUserName;
    public static String myID;
    public static JSONArray myChatList;

    public static String TAG = "USERMODEL";



    public static void initModel(Context mContext){



        myChatName = SessionManager.getString(mContext,Constant.USERNAME);
        // TODO : Distinguish username and chat name, or it is ok with a demo purpose application
        myUserName = myChatName;
        myID = SessionManager.getString(mContext,Constant.USERID);
        formatMyChatListFromStringList(SessionManager.getArrayList(Constant.USER_CHAT_LIST));

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


        SessionManager.putString(mContext, Constant.USERID, "");
        ArrayList<String> emptyList = new ArrayList<String>();
        SessionManager.putArrayList(emptyList,Constant.USER_CHAT_LIST);
        try{
            SessionManager.putArrayList(emptyList,Constant.USER_GPS_INFO);
        }catch (Exception e){

        }
//        public static JSONArray myChatList;



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






    public static boolean isMovingExistingChatToTop(Context mContext,int otherUserID, String mostRecentMessage){
        /**
         * 思路：
         * 1. 找出是否存在这个user
         * 2. 如果存在，则移动
         * 3. 如果不存在，则创建
         *
         * */

        Log.d(TAG, "moveChatToTopForOtherUser: begin");

        ArrayList<String> chatInfoList = SessionManager.getArrayList(Constant.USER_CHAT_LIST);
        boolean alreadyExist = false;
        int position = 0;
        try{

            for (int i =0;i<chatInfoList.size();i++){

                // convert the string to json object
                try {
                    JSONObject alreadyHaveJSONObject = new JSONObject(chatInfoList.get(i));
                    if(alreadyHaveJSONObject.getString(Constant.USER_CHAT_LIST_OTHER_USER_ID).equals(String.valueOf(otherUserID))){
                        Log.d(TAG, "moveChatToTopForOtherUser: the id is the same");
                        alreadyExist = true;
                        position = i;
                         break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){

        }

        if(alreadyExist){
            Log.d(TAG, "moveChatToTopForOtherUser: already exist and move to top");
            moveChatToTopForPosition(mContext,position,mostRecentMessage,String.valueOf(otherUserID));
            return true;
        }else{
            Log.d(TAG, "moveChatToTopForOtherUser: create new chat");
            // get data and add
            return false;
        }

    }

    public static void moveChatToTopForPosition(Context mContext,int position, String mostRecentMessage, String otherUserID){
        /**
         * 思路：
         * 1. 拿出这个item，加到新的arraylist
         * 2. 遍历arraylist，加到新的arraylist，不要position的item
         * 3. 储存新的arraylist，init
         *
         * */

        // 1.
        ArrayList<String> chatInfoList = SessionManager.getArrayList(Constant.USER_CHAT_LIST);
        ArrayList<String> newChatInfoList = new ArrayList<String>();

        try{
            JSONObject jsonObject = new JSONObject(chatInfoList.get(position));
            jsonObject.put(Constant.USER_CHAT_LIST_LAST_MESSAGE,mostRecentMessage);
            String theIDOfUserThatIsChattingTo = SessionManager.getString(mContext,Constant.USER_CHATTING_DATABASE);
            if(!theIDOfUserThatIsChattingTo.equals(otherUserID)){
                jsonObject.put(Constant.USER_CHAT_IS_NOT_READ,"true");
            }
            newChatInfoList.add(jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }


        // 2.
        for (int i =0;i<chatInfoList.size();i++){
            if(i!=position){
                newChatInfoList.add(chatInfoList.get(i));
            }
        }

        // 3.
        SessionManager.putArrayList(newChatInfoList,Constant.USER_CHAT_LIST);
        initModel(mContext);

    }



    public static void addNewChatToChatList(Context mContext,JSONObject chatInfo){
        /**
         * 检查思路：
         * 1. 从sessionManager里面拿到现有的string list
         * 2. 对string list的每个string进行分析，将他们转成一个JSONObject, 方便与传输进来的JSONObject比较
         * 3. 检查相应的field,确认有没有重复
         *
         * 注意1：不要直接使用myChatList，因为如果多次点的是一个新的用户，则myChatList中尚无记录(现在只是存在session里)，所以会导致筛选无效
         * 注意2：在判定string是否相等时，使用equals,不要使用==
         *
         * */

        ArrayList<String> chatInfoList = SessionManager.getArrayList(Constant.USER_CHAT_LIST);
        ArrayList<String> newChatInfoList = new ArrayList<String>();
        newChatInfoList.add(chatInfo.toString());
        try{
            boolean alreadyExist = false;

            for (int i =0;i<chatInfoList.size();i++){
                newChatInfoList.add(chatInfoList.get(i));

                // convert the string to json object
                try {
                    JSONObject alreadyHaveJSONObject = new JSONObject(chatInfoList.get(i));
                    if(alreadyHaveJSONObject.getString(Constant.USER_CHAT_LIST_OTHER_USER_ID).equals(chatInfo.getString(Constant.USER_CHAT_LIST_OTHER_USER_ID))){
                        alreadyExist = true;
                        // break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Log.d("TEST ADD", "boolean : "+alreadyExist);
            if(!alreadyExist){
                SessionManager.putArrayList(newChatInfoList,Constant.USER_CHAT_LIST);
                initModel(mContext);
            }
        }catch (Exception e){

        }

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

    public static String getMyID() {
        if(myID == null || myID == ""){
            return "0";
        }else{
            return myID;
        }
    }


    public static void markReadForID(Context mContext,String otherUserID) {


        /**
         * 检查思路：
         * 1. 从sessionManager里面拿到现有的string list
         * 2. 对string list的每个string进行分析，将他们转成一个JSONObject, 方便与传输进来的JSONObject比较
         * 3. 检查相应的field,确认有没有重复
         *
         * 注意1：不要直接使用myChatList，因为如果多次点的是一个新的用户，则myChatList中尚无记录(现在只是存在session里)，所以会导致筛选无效
         * 注意2：在判定string是否相等时，使用equals,不要使用==
         *
         * */

        ArrayList<String> chatInfoList = SessionManager.getArrayList(Constant.USER_CHAT_LIST);
        ArrayList<String> newChatInfoList = new ArrayList<String>();
        try{
            for (int i =0;i<chatInfoList.size();i++){

                String stringToBeAdded = chatInfoList.get(i);

                try {
                    JSONObject alreadyHaveJSONObject = new JSONObject(chatInfoList.get(i));
                    Log.d(TAG, "markReadForID: IS Try "+otherUserID);
                    if(alreadyHaveJSONObject.getString(Constant.USER_CHAT_LIST_OTHER_USER_ID).equals(otherUserID)){
                        Log.d(TAG, "markReadForID: IS YES "+otherUserID);
                        alreadyHaveJSONObject.put(Constant.USER_CHAT_IS_NOT_READ,"false");
                        stringToBeAdded = alreadyHaveJSONObject.toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                newChatInfoList.add(stringToBeAdded);
            }
        }catch (Exception e){

        }

        SessionManager.putArrayList(newChatInfoList,Constant.USER_CHAT_LIST);
        initModel(mContext);

    }





}
