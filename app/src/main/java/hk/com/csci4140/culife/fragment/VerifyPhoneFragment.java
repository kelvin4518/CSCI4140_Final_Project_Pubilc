package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.GetVerificationCodeModel;
import hk.com.csci4140.culife.model.RegisterVerifyPhoneModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 07/04/2018.
 */

public class VerifyPhoneFragment extends BaseFragment {

    private static final String TAG = "RegisterVerifyPhoneFrag";

    @BindView(R.id.register_verify_phone_num)
    TextView mTvPhoneNum;

    @BindView(R.id.register_verify_count)
    TextView mTvCount;

    @BindView(R.id.register_verify_code)
    EditText mEtVerifyCode;

    @BindView(R.id.register_verify_resend_button)
    Button mResendButton;

    private PrevFragment prevFragment;

    private int countTime = 60;
    private String mTitle;
    private String mPrevTitle;


    private Bundle bundle;
    private String mPhone;
    private String mPassword;
    private int    mRegion;


    private Handler mHandler = new Handler();
    private Runnable mRun;

    private enum PrevFragment{
        FORGET_PASSWORD, REGISTER_BY_PHONE
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.register_verify_phone_title);
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
            prevFragment = mPrevTitle.equals(getString(R.string.forget_password_title)) ?
                    PrevFragment.FORGET_PASSWORD : PrevFragment.REGISTER_BY_PHONE;
        }
        setToolbarTitle(mTitle);
    }


    @Override
    public void onResume(){
        super.onResume();

        //Set the Go back Icon
        setGoBackIcon();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the previous fragment's title
        setToolbarTitle(mPrevTitle);

        //Stop the counting runnable
        mHandler.removeCallbacks(mRun);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_register_verify_phone, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        super.onStart();

        if(bundle == null){
            bundle = getArguments();
            //Set the variables
            mPhone = bundle.getString(Constant.REGISTER_PHONE_NUM);

            if(prevFragment == PrevFragment.REGISTER_BY_PHONE) {
                mPassword = bundle.getString(Constant.REGISTER_PASSWORD);
                mRegion = bundle.getInt(Constant.REGISTER_REGION);
            }
        }
        String areaCode = mPhone.length() == Constant.HONGKONG_PHONE_LENGTH ? Constant.HONG_KONG_AREA_CODE : Constant.CHINA_AREA_CODE;
        mTvPhoneNum.setText(String.format(getString(R.string.register_verify_phone_format), areaCode, mPhone));

        //Set the counting of 60 seconds
        mTvCount.setText(String.format(getString(R.string.register_verify_count_format), Integer.toString(countTime)));
        continueCount();
    }


    //Count the 60 seconds
    private void continueCount(){
        mRun = new Runnable(){
            @Override
            public void run() {
                countTime = countTime - 1;
                mTvCount.setText(String.format(getString(R.string.register_verify_count_format), Integer.toString(countTime)));
                if (countTime == 0){
                    mTvCount.setText(getString(R.string.register_verify_count_resend));
                    mResendButton.setVisibility(View.VISIBLE);
                    countTime = 60;
                }else {
                    continueCount();
                }
            }};
        mHandler.postDelayed(mRun
                ,1000);
    }


    @OnClick(R.id.register_verify_resend_button)
    void onClickResend(){
        //Resend Verification code
        callResendHttp(mPhone, mRegion);
    }


    @OnClick(R.id.register_verify_confirm_button)
    void onClickConfirm(){
        if(mEtVerifyCode.getText() == null || mEtVerifyCode.getText().toString().equals("")){
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(getString(R.string.register_verify_warning_input_verify_code))
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
        else {
            Utility.hideKeyboard(getContext());
            switch (prevFragment){
                case FORGET_PASSWORD:
                    //TODO: forget password confirm api
                    //Stop the counting runnable
                    mHandler.removeCallbacks(mRun);
                    setPrevTitle(mTitle);
                    replaceFragment(new ResetPasswordFragment(), null);
                    break;
                case REGISTER_BY_PHONE:
                    //Save the parameter
                    saveParameter();
                    break;
                default:
                    break;
            }
        }
    }


    //Save the data to Hash Map for calling the register verify api
    private void saveParameter(){
        HashMap<String, String> mParamaterMap = new HashMap<>();
        mParamaterMap.put(Constant.VERIFY_PHONE, mPhone);
        mParamaterMap.put(Constant.VERIFY_PASSWORD, mPassword);
        mParamaterMap.put(Constant.VERIFY_REGION, Integer.toString(mRegion));
        mParamaterMap.put(Constant.VERIFY_CODE, mEtVerifyCode.getText().toString());
        mParamaterMap.put(Constant.VERIFY_DEVICE_TOKEN, Utility.getDeviceToken());
        mParamaterMap.put(Constant.VERIFY_DEVICE_TYPE, Constant.DEVICE_TYPE);

        //Call the Verify api
        callRegisterVerifyHttp(mParamaterMap);
    }


    //Call the Resend verification code api
    private void callResendHttp(String phone, int region){
        ObserverOnNextListener<GetVerificationCodeModel> observer = new ObserverOnNextListener<GetVerificationCodeModel>() {
            @Override
            public void onNext(GetVerificationCodeModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    mResendButton.setVisibility(View.GONE);
                    mTvCount.setText(String.format(getString(R.string.register_verify_count_format), Integer.toString(countTime)));
                    continueCount();
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        switch (prevFragment){
            case FORGET_PASSWORD:
                HttpMethod.getInstance().forgetPassword(new ProgressObserver<GetVerificationCodeModel>(getContext(), observer), phone);
                break;
            case REGISTER_BY_PHONE:
                HttpMethod.getInstance().registerByPhone(new ProgressObserver<GetVerificationCodeModel>(getContext(), observer), phone, region);
                break;
            default:
                break;
        }
    }


    //Call the Register Verify API
    private void callRegisterVerifyHttp(HashMap<String, String> parameterMap){
        ObserverOnNextListener<RegisterVerifyPhoneModel> observer = new ObserverOnNextListener<RegisterVerifyPhoneModel>() {
            @Override
            public void onNext(RegisterVerifyPhoneModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //Stop the counting runnable
                    mHandler.removeCallbacks(mRun);
                    //Put the Token to next fragment
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TOKEN, model.getResult().getToken());
                    bundle.putInt(Constant.REGISTER_REGION, mRegion);
                    replaceFragment(new RegisterFinishedFragment(), bundle);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().verifyByRegisterPhone(new ProgressObserver<RegisterVerifyPhoneModel>(getContext(), observer), parameterMap);
    }

}
