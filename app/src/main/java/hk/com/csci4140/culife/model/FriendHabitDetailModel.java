package hk.com.csci4140.culife.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class FriendHabitDetailModel {

    private static final String TAG = "HabitModel";
    // about the habit : 关于这个habit的内容
    public Integer ID;
    public String name;    // get up early
    public String description;     // to keep healthy body TODO : Mr. Zheng : can use description to replace content
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
    public Boolean isPrivateHabit;     // habit is public and cannot have other to participate
    public Boolean isPrviateVisible;   // habit is public visible and others cannot see
    public Boolean ispublicHabit;     // habit is public and cannot have other to participate
    //public String Content;
    public String createTime;
    public Boolean isFavorited;
    public Integer countFavorited;
    public String isSlug;
    public String isUpdated;
    public String member;



    // about the user : 这个habit有关user的内容
    public String owner;
    public int ownerID;
    public int selfFinishNumber;
    public int selfFinishRatio;
    public int userParticipateNumber;
    public int userFinishNumber;

    public ArrayList<Map<String,String>> memberList; // has id, name, icon link,

    public String userImage;
    public String userBio;
    public Boolean userFollowing;



    public static void initModel(Context mContext){

    }


    public static void fromGetUserHabitListJson(Context mContext,JSONObject jsonObject) {
        try {
            JSONObject responseObject = jsonObject.getJSONObject("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMember(){return member;}

    public void initHabitWithModel(FriendMomentModel model){
        //Log.d(TAG,"initJSON"+start_time);

        //Boolean privacy = response.getBoolean("namewhat")//TODO: A private habit or not
        ID = model.getId();
        name = model.getTitle();
        description = model.getDescription();
        startDate = model.getStart_date();//
        endDate = model.getEnd_date();//
        startTime = model.getStart_time();
        endTime = model.getEnd_time();
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
        //Content = body;
        createTime = model.getCreatedAt();
        isFavorited = model.getFavorited();
        countFavorited = model.getFavoritesCount();
        isSlug = model.getSlug();
        isUpdated = model.getUpdateAt();
        //isPrivateHabit = privacy; TODO: The privacy part

        owner = model.getAuthor_username();
        ownerID = model.getAuthor_id();
        selfFinishNumber = 0;//
        selfFinishRatio = 0;//
        userParticipateNumber = 0;//
        userFinishNumber = 0;//
        userImage = model.getAuthor_image();
        userBio = model.getAuthor_bio();
        userFollowing = model.getAuthor_following();

    }

}
