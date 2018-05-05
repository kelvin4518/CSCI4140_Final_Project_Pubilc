package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 11/04/2018.
 */

public class GetMissionDetailModel implements Serializable {

    private String status;
    private Result result;


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

    public class Result implements Serializable{

        @SerializedName("mission_detail")
        private MissionDetailModel missionDetail;

        @SerializedName("giver_info")
        private PublisherModel publisherInfo;

        @SerializedName("is_owner")
        private int isOwner;

        private List<String> errors;


        public MissionDetailModel getMissionDetail() {
            return missionDetail;
        }

        public void setMissionDetail(MissionDetailModel missionDetail) {
            this.missionDetail = missionDetail;
        }

        public PublisherModel getPublisherInfo() {
            return publisherInfo;
        }

        public void setPublisherInfo(PublisherModel publisherInfo) {
            this.publisherInfo = publisherInfo;
        }

        public int getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(int isOwner) {
            this.isOwner = isOwner;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }
}
