package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import org.json.*;
import com.loopj.android.http.*;
import com.roger.catloadinglibrary.CatLoadingView;





/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 *
 * code structure :
 * 1. Bind the views using ButterKnife
 * 2. Bind the onClick behavior using ButterKnife
 * 3. Handle what view to be displayed
 * 4. Login API Handling
 */



public class LoginFragment extends BaseFragment {

    // class variables defined here
    private static final String TAG = "LoginFrag";

    // fields in the login form， 登录页面的几个input
    @BindView(R.id.login_remember_me)
    CheckBox mCbRememberMe;

    @BindView(R.id.login_phone_num)
    EditText mPhone;

    @BindView(R.id.login_password)
    EditText mPassword;

    private String errorText = "";

    CatLoadingView mView;

    // use butterknife to set the button behavior

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
        // replaceFragment(new RegisterFragment(), null);

        // 现在暂时直接转到电话／邮件注册页面
        replaceFragment(new RegisterEmailFragment(), null);
    }

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

    //Save the Parameter and call API
    private void saveParameter(){

        if(checkUserInputEmail()){
//            //Put the parameter and call API
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

    //Call Login by Email Http
    private void callLoginByEmailHttp(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();

        mView = new CatLoadingView();

        mView.show(getFragmentManager(), "");

        client.post(getContext(),Constant.API_BASE_URL+"users/login",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                showBottomSnackBar("Welcome to CULife !");
                UserModel.fromLoginJson(getContext(),mCbRememberMe.isChecked(),response);


                callRegisterByEmailToGoogleFirebase();


                replaceActivity(MainActivity.class, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.login_warning_wrong_password));
            }
        });
    }




    private FirebaseAuth mFirebaseAuth;
    private void callRegisterByEmailToGoogleFirebase(){
        // initialize the Auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        String userEmail = mPhone.getText().toString();
        String userPassword = mPassword.getText().toString();

        // create the user with email and password
        mFirebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    showBottomSnackBar("Corresponding Firebase User Created");
                }else{
//                    showBottomSnackBar("Firebase User Creation Fails");
                }

                replaceActivity(MainActivity.class, null);
            }
        });

    }


    // OLD CODE : NOT USE ANYMORE
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

}
