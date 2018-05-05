package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gechen(Ge Chen) on 30/04/2018.
 */

public class MissionDetailModel implements Serializable {

    private int id;
    private String title;
    private String salary;
    private String address;
    private String icon;

    @SerializedName("passed_date")
    private String passedDate;

    private double lat;
    private double lng;

    @SerializedName("is_fav")
    private String isFav;

    @SerializedName("mission_id")
    private String missionId;

    @SerializedName("owner_id")
    private int ownerId;

    @SerializedName("type_id")
    private int typeId;

    @SerializedName("no_spec_date")
    private int NoSpecificDate;

    @SerializedName("date")
    private String date;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("num_of_needed")
    private int numOfNeeded;

    @SerializedName("current_needed")
    private int currentNeeded;

    @SerializedName("post_start")
    private String postStart;

    @SerializedName("post_end")
    private String postEnd;

    @SerializedName("need_face")
    private int needFace;

    @SerializedName("have_address")
    private int haveAddress;

    @SerializedName("need_photo")
    private int needPhoto;

    @SerializedName("need_video")
    private int needVideo;

    @SerializedName("public_media")
    private int publicMedia;

    @SerializedName("content")
    private String content;

    @SerializedName("requirement")
    private String requirement;

    @SerializedName("mission_type")
    private String missionType;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPassedDate() {
        return passedDate;
    }

    public void setPassedDate(String passedDate) {
        this.passedDate = passedDate;
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

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getNoSpecificDate() {
        return NoSpecificDate;
    }

    public void setNoSpecificDate(int noSpecificDate) {
        NoSpecificDate = noSpecificDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNumOfNeeded() {
        return numOfNeeded;
    }

    public void setNumOfNeeded(int numOfNeeded) {
        this.numOfNeeded = numOfNeeded;
    }

    public int getCurrentNeeded() {
        return currentNeeded;
    }

    public void setCurrentNeeded(int currentNeeded) {
        this.currentNeeded = currentNeeded;
    }

    public String getPostStart() {
        return postStart;
    }

    public void setPostStart(String postStart) {
        this.postStart = postStart;
    }

    public String getPostEnd() {
        return postEnd;
    }

    public void setPostEnd(String postEnd) {
        this.postEnd = postEnd;
    }

    public int getNeedFace() {
        return needFace;
    }

    public void setNeedFace(int needFace) {
        this.needFace = needFace;
    }

    public int getHaveAddress() {
        return haveAddress;
    }

    public void setHaveAddress(int haveAddress) {
        this.haveAddress = haveAddress;
    }

    public int getNeedPhoto() {
        return needPhoto;
    }

    public void setNeedPhoto(int needPhoto) {
        this.needPhoto = needPhoto;
    }

    public int getNeedVideo() {
        return needVideo;
    }

    public void setNeedVideo(int needVideo) {
        this.needVideo = needVideo;
    }

    public int getPublicMedia() {
        return publicMedia;
    }

    public void setPublicMedia(int publicMedia) {
        this.publicMedia = publicMedia;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }


}
