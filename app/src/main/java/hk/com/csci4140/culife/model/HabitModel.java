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

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.utility.SessionManager;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */

/**
 * This class is to save the habit data.
 */

public class HabitModel {

    // about the habit : 关于这个habit的内容
    private int ID;
    private String name;    // get up early
    private String description;     // to keep healthy body
    private String startDate;   // 2018-05-04 TODO : need to confirm what kind of structure to use
    private String endDate;     // 2018-05-10
    private String startTime;   // 6:00
    private String endTime;     // 18:00
    private Boolean isFinishBefore;  // if it is true, then only need to set the task end time, need to finish this before the end time
    private Boolean needGPSVerify;  // if it is true, then use GPS to check the location
    private String lat;     // 33.123
    private String lng;     // 128.234
    private String location;        // university library
    private String GPSRequiredTime; // 30, count in minute, use GPS to check whether stay in lat and lng for 30 minutes
    private Boolean isComplete;     // user checking complete
    private Boolean isAutoComplete;     // GPS auto check complete
    private Boolean isPrivateHabit;     // habit is private and cannot have other to participate
    private Boolean isPrviateVisible;   // habit is private visible and others cannot see


    // about the user : 这个habit有关user的内容
    private String owner;
    private int ownerID;
    private int selfFinishNumber;
    private int selfFinishRatio;
    private int userParticipateNumber;
    private int userFinishNumber;
    private ArrayList<Map<String,String>> memberList; // has id, name, icon link,




    public static void initModel(Context mContext){

    }


    public static void fromGetUserHabitListJson(Context mContext,JSONObject jsonObject) {
        try {
            JSONObject responseObject = jsonObject.getJSONObject("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
