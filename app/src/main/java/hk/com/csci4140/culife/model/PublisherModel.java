package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by maoyuxuan(Michael Mao) on 11/04/2018.
 */

public class PublisherModel implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("joined_days")
    private String joinedDays;

    @SerializedName("rate")
    private int rate;

    @SerializedName("post_mission_total")
    private int totalMission;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("icon_link")
    private String iconLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinedDays() {
        return joinedDays;
    }

    public void setJoinedDays(String joinedDays) {
        this.joinedDays = joinedDays;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTotalMission() {
        return totalMission;
    }

    public void setTotalMission(int totalMission) {
        this.totalMission = totalMission;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }
}
