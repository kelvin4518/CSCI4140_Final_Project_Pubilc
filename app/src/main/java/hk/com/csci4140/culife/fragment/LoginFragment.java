package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFrag";

    @BindView(R.id.login_remember_me)
    CheckBox mCbRememberMe;

    @BindView(R.id.login_phone_num)
    EditText mPhone;

    @BindView(R.id.login_password)
    EditText mPassword;

    private String errorText = "";

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

    //Save the Parameter and call API
    private void saveParameter(){

        if(checkUserInput()){
            //Put the parameter and call API
            HashMap<String, String> mParameter = new HashMap<>();
            mParameter.put(Constant.LOGIN_PHONE_PHONE, mPhone.getText().toString());
            mParameter.put(Constant.LOGIN_PHONE_PASSWORD, mPassword.getText().toString());
            mParameter.put(Constant.LOGIN_PHONE_DEVICE_TYPE, Constant.DEVICE_TYPE);
            mParameter.put(Constant.LOGIN_PHONE_DEVICE_TOKEN, Utility.getDeviceToken());

            callLoginHttp(mParameter);
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
}
