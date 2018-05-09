package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;

import java.io.UnsupportedEncodingException;
import java.util.Dictionary;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.LoginModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 01/04/2018.
 */

import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import org.json.*;
import com.loopj.android.http.*;


public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFrag";

    @BindView(R.id.login_remember_me)
    CheckBox mCbRememberMe;

    @BindView(R.id.login_phone_num)
    EditText mPhone;

    @BindView(R.id.login_password)
    EditText mPassword;

    private String errorText = "";
    final String TARGET_URL = "http://ec2-54-251-167-117.ap-southeast-1.compute.amazonaws.com:8000/api/users/login";
//    private static AsyncHttpClient client;


    // before the user see the page
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has no toolbar
        setToolbarVisibility(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }




    // when user is interacting with the page
    @OnClick(R.id.login_wechat_button)
    void onClickWechatLogin(){
        //TODO: WeChat Login function
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText("Still need to implemented")
                .setConfirmText(getString(R.string.warning_confirm))
                .show();
    }

    @OnClick(R.id.login_normal_button)
    void onClickNormalLogin(){
        saveParameter();
    }

    @OnClick(R.id.login_forget_password)
    void onClickForgetPassword(){
        //Go to forget password fragment
        replaceFragment(new ForgetPasswordFragment(), null);
    }

    @OnClick(R.id.login_register_button)
    void onClickRegister(){
        replaceFragment(new RegisterFragment(), null);
    }

    //Check if the information that user input is valid
    private boolean checkUserInput(){

        if(mPhone.getText() == null || mPhone.getText().toString().equals("")){

            errorText = getString(R.string.login_warning_input_phone_num);
            return false;
        }

        if( (mPhone.getText().toString().length() != Constant.HONGKONG_PHONE_LENGTH)
                &&
                (mPhone.getText().toString().length() != Constant.CHINA_PHONE_LENGTH)){

            errorText = getString(R.string.login_warning_valid_phone_num);
            return false;
        }

        if(mPassword.getText() == null || mPassword.getText().toString().equals("")){

            errorText = getString(R.string.login_warning_input_password);
            return false;
        }

        return true;
    }

    private boolean checkUserInputEmail(){

        if(mPhone.getText() == null || mPhone.getText().toString().equals("")){
            // errorText = getString(R.string.login_warning_input_phone_num);
            errorText = "Please enter the email";
            return false;
        }

        if(!mPhone.getText().toString().contains("@")){
            errorText = "Please enter the valid email address";
            return false;
        }

        if(mPassword.getText() == null || mPassword.getText().toString().equals("")){

            errorText = getString(R.string.login_warning_input_password);
            return false;
        }

        return true;

    }

    //Save the Parameter and call API
    private void saveParameter(){

        if(checkUserInputEmail()){
//            //Put the parameter and call API
//            HashMap<String, String> mParameter = new HashMap<>();
//            mParameter.put(Constant.LOGIN_PHONE_PASSWORD, mPassword.getText().toString());
//            mParameter.put(Constant.LOGIN_PHONE_DEVICE_TYPE, Constant.DEVICE_TYPE);
//            mParameter.put(Constant.LOGIN_PHONE_DEVICE_TOKEN, Utility.getDeviceToken());
////            mParameter.put(Constant.LOGIN_PHONE_PHONE, mPhone.getText().toString());
////            callLoginHttp(mParameter);
//            mParameter.put("email", mPhone.getText().toString());


            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
                jsonParams.put("email", mPhone.getText().toString());
                jsonParams.put("password", mPassword.getText().toString());
                outerJsonParams.put("user",jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                callLoginByEmailHttp(entity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        else {

            //Show Alert Dialog
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }

    //Call Login by Phone Http
    private void callLoginHttp(HashMap<String, String> map){
        ObserverOnNextListener<LoginModel> observer = new ObserverOnNextListener<LoginModel>() {
            @Override
            public void onNext(LoginModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    UserModel.login(getContext(), mCbRememberMe.isChecked(),
                            model.getResult().getToken(),
                            model.getResult().getIconUrl(),
                            model.getResult().getRegion(),
                            model.getResult().getShowLocation());
                    replaceActivity(MainActivity.class, null);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.login_warning_wrong_password));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().loginByPhone(new ProgressObserver<LoginModel>(getContext(), observer), map);
    }


    //Call Login by Email Http
    private void callLoginByEmailHttp(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(getContext(),TARGET_URL,params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                showBottomSnackBar("Welcome to CULife !");
                UserModel.fromLoginJson(getContext(),mCbRememberMe.isChecked(),response);
                replaceActivity(MainActivity.class, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.login_warning_wrong_password));
            }
        });
    }
}
