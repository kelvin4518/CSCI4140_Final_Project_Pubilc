package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gechen(Ge Chen) on 12/04/2018.
 */

public class ShiftOptionModel implements Serializable {

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

        @SerializedName("salary_range")
        private List<SalaryRange> salaryRanges;


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

        public List<SalaryRange> getSalaryRanges() {
            return salaryRanges;
        }

        public void setSalaryRanges(List<SalaryRange> salaryRanges) {
            this.salaryRanges = salaryRanges;
        }

        public class MissionType implements Serializable{

            @SerializedName("id")
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

        public class SalaryRange implements Serializable{

            @SerializedName("id")
            private int id;

            @SerializedName(value = "salary_range", alternate = "salary_range_sc")
            private String range;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRange() {
                return range;
            }

            public void setRange(String range) {
                this.range = range;
            }
        }
    }
}
