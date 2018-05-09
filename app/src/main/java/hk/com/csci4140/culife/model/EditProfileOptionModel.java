package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 14/04/2018.
 */

public class EditProfileOptionModel implements Serializable {

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

        @SerializedName("user_info")
        private UserInfo userInfo;

        @SerializedName("age_ranges")
        private List<AgeRange> ageRangeList;

        @SerializedName("industry_lists")
        private List<Industry> industryList;

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public List<AgeRange> getAgeRangeList() {
            return ageRangeList;
        }

        public void setAgeRangeList(List<AgeRange> ageRangeList) {
            this.ageRangeList = ageRangeList;
        }

        public List<Industry> getIndustryList() {
            return industryList;
        }

        public void setIndustryList(List<Industry> industryList) {
            this.industryList = industryList;
        }

        public class UserInfo implements Serializable{

            private String phone;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

        public class AgeRange implements Serializable{

            private int id;

            @SerializedName("age_range")
            private String ageRange;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAgeRange() {
                return ageRange;
            }

            public void setAgeRange(String ageRange) {
                this.ageRange = ageRange;
            }
        }

        public class Industry implements Serializable{

            private int id;

            @SerializedName(value = "industry", alternate = "industry_sc")
            private String industryName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIndustryName() {
                return industryName;
            }

            public void setIndustryName(String industryName) {
                this.industryName = industryName;
            }
        }
    }
}
