package hk.com.csci4140.culife.model;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class HomeFragmentModel {

    public String title;//内容

    public String time;

    public String identity;

    public int habit_id;

    public int id;//该条数据的id

    public String getTitle() {
        return title;
    }

    public String getTime() { return time;}

    public String getIdentity() { return identity;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {this.time = time;}

    public void setIdentity(String identity) {this.identity = identity;}

    public int getId() {
        return id;
    }

    public int getHabitId(){ return habit_id;}

    public void setId(int id) {
        this.id = id;
    }

    public void initHomePageWithJSON(JSONObject response){
        try {
            Integer id = response.getInt("id");
            JSONObject author = response.getJSONObject("author");
            Integer author_id = author.getInt("id");
            String author_username = author.getString("username");
            String author_bio = author.getString("bio");
            String author_image = author.getString("image");
            Boolean author_following = author.getBoolean("following");
            //String body = response.getString("body");
            String createdAt = response.getString("createdAt");
            String Tdescription = response.getString("description");
            Boolean favorited = response.getBoolean("favorited");
            Integer favoritesCount = response.getInt("favoritesCount");
            String slug = response.getString("slug");
            //JSONArray tagList = response.getJSONArray("tagList");
            String name = response.getString("title");
            String updateAt = response.getString("updatedAt");
            String start_time = response.getString("start_time");
            String end_time = response.getString("end_time");

            //Boolean privacy = response.getBoolean("namewhat")//TODO: A private habit or not
            habit_id = id;
            title = name;
            time = start_time;
            identity = author_username;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }


}
