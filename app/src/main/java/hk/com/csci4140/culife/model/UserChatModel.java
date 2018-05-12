package hk.com.csci4140.culife.model;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.utility.SessionManager;

/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */

/**
 * This class is to save the user chatting data.
 *
 */

public class UserChatModel {

    public static String myChatName;
    public static ArrayList<Map<String,String>> myChatList;

    /**
     * myChatList Explained: in each map, we have the key:
     *
     * 1. otherUserID : the ID that the user I am talking to , if 0, then it is a group chat (not implemented yet)
     * 2. iconLink : the iconLink of user from the other side
     * 3. name : the name of user from the other side
     * 4. lastMessage : the last Message of the conversation
     * 5. lastDate : the last Date of the chat
     * */



    public static void initUserChatModelFromLocalStorage(Context mContext){
        myChatName  = SessionManager.getString(mContext, Constant.USERNAME);
//        myChatList = SessionManager.getArrayList(mContext, Constant.USER_CHAT_LIST);
    }

    public static void logout(Context mContext){
        SessionManager.putString(mContext, Constant.USERNAME, "");
        initUserChatModelFromLocalStorage(mContext);
    }

}
