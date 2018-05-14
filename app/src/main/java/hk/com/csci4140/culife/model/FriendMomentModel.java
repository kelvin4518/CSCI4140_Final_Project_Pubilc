package hk.com.csci4140.culife.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class FriendMomentModel {

    private int id;//该条数据的id

    private String title;

    private int author_id;

    private String author_bio;

    private String author_image;

    private Boolean author_following;

    private String body;

    private String createdAt;

    private String description;

    private Integer habits_count;

    private String author_username;

    private String start_time;

    private Boolean favorited;

    private String start_date;

    private String end_date;

    private String end_time;

    private int is_public;

    private int send_notification;

    private int favoritesCount;

    private String slug;

    private String updateAt;

    private JSONArray tagList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void initFriendMoment(JSONObject response){
        try {
//            Integer habitsCount = response.getInt("habitsCount");
            //String body = response.getString("body");
            //JSONArray tagList = response.getJSONArray("tagList");

            id = response.getInt("id");
            JSONObject author = response.getJSONObject("author");
            author_id = author.getInt("id");
            author_username = author.getString("username");
            author_bio = author.getString("bio");
            author_image = author.getString("image");
            author_following = author.getBoolean("following");
            body = response.getString("body");
            createdAt = response.getString("createdAt");
            description = response.getString("description");
            start_date = response.getString("start_date");
            end_date = response.getString("end_date");
            start_time = response.getString("start_time");
            end_time = response.getString("end_time");
            is_public = response.getInt("is_public");
            send_notification = response.getInt("send_notification");
            favorited = response.getBoolean("favorited");
            favoritesCount = response.getInt("favoritesCount");
            slug = response.getString("slug");
            tagList = response.getJSONArray("tagList");
            title = response.getString("title");
            updateAt = response.getString("updatedAt");

            //Boolean privacy = response.getBoolean("namewhat")//TODO: A private habit or not
//            habits_count = habitsCount;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getHabits_count() {
        return habits_count;
    }

    public void setHabits_count(Integer habits_count) {
        this.habits_count = habits_count;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public void setAuthor_username(String author_username) {
        this.author_username = author_username;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_bio() {
        return author_bio;
    }

    public void setAuthor_bio(String author_bio) {
        this.author_bio = author_bio;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public Boolean getAuthor_following() {
        return author_following;
    }

    public void setAuthor_following(Boolean author_following) {
        this.author_following = author_following;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }

    public int getSend_notification() {
        return send_notification;
    }

    public void setSend_notification(int send_notification) {
        this.send_notification = send_notification;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public JSONArray getTagList() {
        return tagList;
    }

    public void setTagList(JSONArray tagList) {
        this.tagList = tagList;
    }
}
