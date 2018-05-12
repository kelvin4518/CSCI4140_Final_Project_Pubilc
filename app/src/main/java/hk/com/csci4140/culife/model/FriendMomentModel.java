package hk.com.csci4140.culife.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class FriendMomentModel {

    private String content;//内容

    private int id;//该条数据的id

    private String title;

    private String image;

    private Integer habits_count;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void initFriendMoment(JSONObject response){
        try {
            Integer habitsCount = response.getInt("habitsCount");
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
            habits_count = habitsCount;
            title = name;
            image = author_image;
            content = Tdescription;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }
}
