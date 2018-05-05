package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 12/04/2018.
 */

public class MapDistrictModel implements Serializable {

    private String status;

    private List<?> result;

    @SerializedName("mission_district")
    private List<MissionDistrict> missionDistricts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    public List<MissionDistrict> getMissionDistricts() {
        return missionDistricts;
    }

    public void setMissionDistricts(List<MissionDistrict> missionDistricts) {
        this.missionDistricts = missionDistricts;
    }

    public class MissionDistrict implements Serializable{
        @SerializedName("lat")
        private double lat;

        @SerializedName("lng")
        private double lng;

        @SerializedName("count")
        private int count;

        @SerializedName("title")
        private String title;


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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
