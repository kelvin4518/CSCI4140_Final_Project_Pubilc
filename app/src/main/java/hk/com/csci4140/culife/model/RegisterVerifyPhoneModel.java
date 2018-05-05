package hk.com.csci4140.culife.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 14/04/2018.
 */

public class RegisterVerifyPhoneModel implements Serializable {

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
        private String       token;

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
    }
}
