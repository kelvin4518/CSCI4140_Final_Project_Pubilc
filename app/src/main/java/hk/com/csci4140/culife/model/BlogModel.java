package hk.com.csci4140.culife.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */

/**
 * This class is to save the habit data.
 */

public class BlogModel {
    public String username;
    public String blog;
    public Integer like;

    public void initBlogModel(JSONObject response){
        Integer id = 0;
        JSONObject au = new JSONObject();
        Integer userid = 0;
        try {
            id = response.getInt("id");
            au = response.getJSONObject("author");
            userid = au.getInt("id");
            username = au.getString("username");
            blog = response.getString("blog");
            like = response.getInt("like");
        }
        catch (JSONException e){

        }
    }

}
