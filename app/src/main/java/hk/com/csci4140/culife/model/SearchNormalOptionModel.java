package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 30/04/2018.
 */

public class SearchNormalOptionModel implements Serializable {

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

        @SerializedName("salary_range")
        private List<SalaryRange> salaryRangeList;

        @SerializedName("mission_type")
        private List<MissionType> missionTypeList;

        @SerializedName("district_list")
        private List<District> districtList;

        @SerializedName("industry_lists")
        private List<Industry> industryList;

        private List<String> errors;

        public List<SalaryRange> getSalaryRangeList() {
            return salaryRangeList;
        }

        public void setSalaryRangeList(List<SalaryRange> salaryRangeList) {
            this.salaryRangeList = salaryRangeList;
        }

        public List<MissionType> getMissionTypeList() {
            return missionTypeList;
        }

        public void setMissionTypeList(List<MissionType> missionTypeList) {
            this.missionTypeList = missionTypeList;
        }

        public List<District> getDistrictList() {
            return districtList;
        }

        public void setDistrictList(List<District> districtList) {
            this.districtList = districtList;
        }

        public List<Industry> getIndustryList() {
            return industryList;
        }

        public void setIndustryList(List<Industry> industryList) {
            this.industryList = industryList;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public class SalaryRange implements Serializable{

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

        public class District implements Serializable{

            private int id;

            @SerializedName(value = "district", alternate = "district_sc")
            private String district;

            private int region;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public int getRegion() {
                return region;
            }

            public void setRegion(int region) {
                this.region = region;
            }
        }

        public class Industry implements Serializable{
            private int id;

            @SerializedName(value = "industry", alternate = "industry_sc")
            private String industry;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIndustry() {
                return industry;
            }

            public void setIndustry(String industry) {
                this.industry = industry;
            }
        }
    }
}
