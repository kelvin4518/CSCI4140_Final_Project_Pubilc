package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 23/04/2018.
 */

public class CreateMissionOptionModel implements Serializable {

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

        private List<String> errors;

        @SerializedName("mission_type")
        private List<MissionType> missionTypes;

        @SerializedName("post_period")
        private List<PostPeriod> postPeriods;

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public List<MissionType> getMissionTypes() {
            return missionTypes;
        }

        public void setMissionTypes(List<MissionType> missionTypes) {
            this.missionTypes = missionTypes;
        }

        public List<PostPeriod> getPostPeriods() {
            return postPeriods;
        }

        public void setPostPeriods(List<PostPeriod> postPeriods) {
            this.postPeriods = postPeriods;
        }

        public class MissionType implements Serializable{

            private int id;

            @SerializedName(value = "mission_type", alternate = "mission_type_sc")
            private String type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public class PostPeriod implements Serializable{

            private int id;

            @SerializedName(value = "post_period", alternate = "post_period_sc")
            private String period;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }
        }
    }
}
