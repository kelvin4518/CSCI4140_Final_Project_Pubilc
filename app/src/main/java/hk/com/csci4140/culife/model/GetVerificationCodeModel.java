package hk.com.csci4140.culife.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 14/04/2018.
 */

public class GetVerificationCodeModel implements Serializable{

    private String status;
    private String responseValue;
    private StandardModel.Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }

    public StandardModel.Result getResult() {
        return result;
    }

    public void setResult(StandardModel.Result result) {
        this.result = result;
    }
}
