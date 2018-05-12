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
 * Created by maoyuxuan(Michael Mao) on 13/05/2018.
 */

/**
 * This class is for better process other user's information.
 *
 */

public class OtherUserModel {

    public String id;
    public String username;
    public String iconLink;
    public String selfBio;

    public boolean heIsFollowingMe;  // 他是否在关注我
    public boolean iAmFollowingHim;  // 我是否在关注他
}
