package hk.com.csci4140.culife.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gechen(Ge Chen) on 30/04/2018.
 */

public class NearbyListModel implements Serializable{

    private String status;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_result")
    private int totalResult;

    private Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable {

        @SerializedName("missions_list")
        private List<MissionList> missionLists;

        private List<String> errors;

        public List<MissionList> getMissionLists() {
            return missionLists;
        }

        public void setMissionLists(List<MissionList> missionLists) {
            this.missionLists = missionLists;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
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
    }

}
