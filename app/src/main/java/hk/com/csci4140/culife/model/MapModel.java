package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 12/04/2018.
 */

public class MapModel implements Serializable {

    private String status;

    private Result result;

    @SerializedName("total_missions_list")
    private int totalMissionNum;

    @SerializedName("total_taker_list")
    private int totalTakerNum;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getTotalMissionNum() {
        return totalMissionNum;
    }

    public void setTotalMissionNum(int totalMissionNum) {
        this.totalMissionNum = totalMissionNum;
    }

    public int getTotalTakerNum() {
        return totalTakerNum;
    }

    public void setTotalTakerNum(int totalTakerNum) {
        this.totalTakerNum = totalTakerNum;
    }


    public class Result implements Serializable{

        private List<String> errors;

        @SerializedName("missions_list")
        private List<MissionList> missionLists;

        @SerializedName("taker_list")
        private List<TakerList> takerLists;


        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public List<MissionList> getMissionLists() {
            return missionLists;
        }

        public void setMissionLists(List<MissionList> missionLists) {
            this.missionLists = missionLists;
        }

        public List<TakerList> getTakerLists() {
            return takerLists;
        }

        public void setTakerLists(List<TakerList> takerLists) {
            this.takerLists = takerLists;
        }

        public class MissionList implements Serializable{

            @SerializedName("mission_detail")
            private MissionDetailModel missionDetail;

            public MissionDetailModel getMissionDetail() {
                return missionDetail;
            }

            public void setMissionDetail(MissionDetailModel missionDetail) {
                this.missionDetail = missionDetail;
            }
        }

        public class TakerList implements Serializable{

            @SerializedName("taker_detail")
            private TakerInfoModel takerInfo;

            public TakerInfoModel getTakerInfo() {
                return takerInfo;
            }

            public void setTakerInfo(TakerInfoModel takerInfo) {
                this.takerInfo = takerInfo;
            }
        }
    }
}
