package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gechen(Ge Chen) on 14/04/2018.
 */

public class LoginModel implements Serializable {

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
        private String token;

        @SerializedName("has_profile")
        private String hasProfile;

        @SerializedName("icon_link")
        private String iconUrl;

        @SerializedName("show_location")
        private int showLocation;

        private int region;

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHasProfile() {
            return hasProfile;
        }

        public void setHasProfile(String hasProfile) {
            this.hasProfile = hasProfile;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getShowLocation() {
            return showLocation;
        }

        public void setShowLocation(int showLocation) {
            this.showLocation = showLocation;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }
    }
}
