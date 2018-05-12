package hk.com.csci4140.culife.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class HomeFragmentModel {

    private String title;//内容

    private String time;

    private String identity;

    private int habit_id;

    private int id;//该条数据的id

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
