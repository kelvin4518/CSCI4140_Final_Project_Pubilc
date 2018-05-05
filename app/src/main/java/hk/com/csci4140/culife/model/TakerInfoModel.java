package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gechen(Ge Chen) on 12/04/2018.
 */

public class TakerInfoModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("name")
    private String name;

    @SerializedName("age_range")
    private int ageRangeId;

    @SerializedName("email")
    private String email;

    @SerializedName("self_intro")
    private String introduction;

    @SerializedName("industry")
    private List<String> industryList;

    @SerializedName("other_industry")
    private String otherIndustry;

    @SerializedName("icon_link")
    private String iconLink;

    @SerializedName("img_link")
    private String imgLink;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("phone")
    private String phone;

    @SerializedName("ability")
    private String ability;

    @SerializedName("rate")
    private int rateNum;

    @SerializedName("mission_total")
    private int totalMissionNum;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeRangeId() {
        return ageRangeId;
    }

    public void setAgeRangeId(int ageRangeId) {
        this.ageRangeId = ageRangeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<String> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<String> industryList) {
        this.industryList = industryList;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public int getRateNum() {
        return rateNum;
    }

    public void setRateNum(int rateNum) {
        this.rateNum = rateNum;
    }

    public int getTotalMissionNum() {
        return totalMissionNum;
    }

    public void setTotalMissionNum(int totalMissionNum) {
        this.totalMissionNum = totalMissionNum;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
