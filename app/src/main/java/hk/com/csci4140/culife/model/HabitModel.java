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
    public int ID;
    public String name;    // get up early
    public String description;     // to keep healthy body
    public String startDate;   // 2018-05-04 TODO : need to confirm what kind of structure to use
    public String endDate;     // 2018-05-10
    public String startTime;   // 6:00
    public String endTime;     // 18:00
    public Boolean isFinishBefore;  // if it is true, then only need to set the task end time, need to finish this before the end time
    public Boolean needGPSVerify;  // if it is true, then use GPS to check the location
    public String lat;     // 33.123
    public String lng;     // 128.234
    public String location;        // university library
    public String GPSRequiredTime; // 30, count in minute, use GPS to check whether stay in lat and lng for 30 minutes
    public Boolean isComplete;     // user checking complete
    public Boolean isAutoComplete;     // GPS auto check complete
    public Boolean ispublicHabit;     // habit is public and cannot have other to participate
    public Boolean isPrviateVisible;   // habit is public visible and others cannot see
    public String Content;
    public String createTime;
    public Boolean isFavorited;
    public Integer countFavorited;
    public String isSlug;
    public String isUpdated;


    // about the user : 这个habit有关user的内容
    public String owner;
    public int ownerID;
    public int selfFinishNumber;
    public int selfFinishRatio;
    public int userParticipateNumber;
    public int userFinishNumber;
    public String userImage;
    public String userBio;
    public Boolean userFollowing;
    public ArrayList<Map<String,String>> memberList; // has id, name, icon link,


    public static void initModel(Context mContext){

    }


    public static void fromGetUserHabitListJson(Context mContext,JSONObject jsonObject) {
        try {
            JSONObject responseObject = jsonObject.getJSONObject("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initwithjson(JSONObject response){
        try {
            Integer id = response.getInt("id");
            JSONObject author = response.getJSONObject("author");
            Integer author_id = author.getInt("id");
            String author_username = author.getString("username");
            String author_bio = author.getString("bio");
            String author_image = author.getString("image");
            Boolean author_following = author.getBoolean("following");
            String body = response.getString("body");
            String createdAt = response.getString("createdAt");
            String Tdescription = response.getString("description");
            Boolean favorited = response.getBoolean("favorited");
            Integer favoritesCount = response.getInt("favoritesCount");
            String slug = response.getString("slug");
            //JSONArray tagList = response.getJSONArray("tagList");
            String title = response.getString("title");
            String updateAt = response.getString("updatedAt");

            ID = id;
            name = title;
            description = Tdescription;
            startDate = "NA";//
            endDate = "NA";//
            startTime = "NA"; //
            endTime = "NA";//
            isFinishBefore = false;//
            needGPSVerify = false;//
            lat = "NA";//
            lng = "NA";//
            location = "NA";      //
            GPSRequiredTime = "NA";//
            isComplete = false; //
            isAutoComplete = false;//
            ispublicHabit = false; //
            isPrviateVisible = false;//
            Content = body;
            createTime = createdAt;
            isFavorited = favorited;
            countFavorited = favoritesCount;
            isSlug = slug;
            isUpdated = updateAt;

            owner = author_username;
            ownerID = author_id;
            selfFinishNumber = 0;//
            selfFinishRatio = 0;//
            userParticipateNumber = 0;//
            userFinishNumber = 0;//
            userImage = author_image;
            userBio = author_bio;
            userFollowing = author_following;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }

}
