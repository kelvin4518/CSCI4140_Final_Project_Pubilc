package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.cazaea.sweetalert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.GetVerificationCodeModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 07/04/2018.
 */

public class RegisterPhoneFragment extends BaseFragment {

    private static final String TAG = "RegisterPhoneFrag";


    @BindView(R.id.register_et_phone_num)
    EditText mPhoneNum;

    @BindView(R.id.register_et_password)
    EditText mPassword;

    @BindView(R.id.register_et_retype_password)
    EditText mRetypePassword;

    @BindView(R.id.register_region_spinner)
    Spinner mRegionSpinner;

    @BindView(R.id.register_policy_checkbox)
    CheckBox mPolicyCheckBox;

    private String mTitle;
    private String errorText = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment
        mTitle = getString(R.string.register_phone_title);
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

        //Previous fragment has no toolbar
        setToolbarVisibility(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_register_phone, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    //Check if the information that user input is valid
    private boolean isInfoValidate(){
        if(mPhoneNum.getText() == null || mPhoneNum.getText().toString().equals("")){
            errorText = getString(R.string.register_warning_input_phone_num);
            return false;
        }

        if(mRegionSpinner.getSelectedItemPosition() == Constant.NON_SELECT){
            errorText = getString(R.string.register_warning_select_region);
            return false;
        }

        if( (mRegionSpinner.getSelectedItemPosition() == Constant.HONG_KONG)
                &&
                (mPhoneNum.getText().toString().length() != Constant.HONGKONG_PHONE_LENGTH) ){

            errorText = getString(R.string.register_warning_valid_phone_num);
            return false;
        }

        if( (mRegionSpinner.getSelectedItemPosition() == Constant.CHINA)
                &&
                (mPhoneNum.getText().toString().length() != Constant.CHINA_PHONE_LENGTH) ){

            errorText = getString(R.string.register_warning_valid_phone_num);
            return false;
        }

        if(mPassword.getText() == null || mPassword.getText().toString().equals("")){
            errorText = getString(R.string.register_warning_input_password);
            return false;
        }

        if(mRetypePassword.getText() == null || mRetypePassword.getText().toString().equals("")){
            errorText = getString(R.string.register_warning_retype_password);
            return false;
        }


        if(!mPassword.getText().toString().equals(mRetypePassword.getText().toString())){
            errorText = getString(R.string.register_warning_not_the_same);
            return false;
        }

        if(!Utility.judgeContainsStr( mPassword.getText().toString())){
            errorText = getString(R.string.register_warning_contains_character);
            return false;
        }

        if(!mPolicyCheckBox.isChecked()){
            errorText = getString(R.string.register_warning_check_agreement);
            return false;
        }

        return true;
    }


    @OnClick(R.id.register_phone_register_button)
    void onClickRegister(){
        if(isInfoValidate()){
            //Call Register Http, first put the information into bundle
            Bundle bundle = new Bundle();
            bundle.putString(Constant.REGISTER_PHONE_NUM, mPhoneNum.getText().toString());
            bundle.putString(Constant.REGISTER_PASSWORD, mPassword.getText().toString());
            bundle.putInt(Constant.REGISTER_REGION, mRegionSpinner.getSelectedItemPosition());

            callRegisterHttp(mPhoneNum.getText().toString(),
                    mRegionSpinner.getSelectedItemPosition(),
                    bundle);
        }
        else {
            //Show the warning text
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }


    //Call the Register API
    private void callRegisterHttp(String phone, int region, final Bundle bundle){
        ObserverOnNextListener<GetVerificationCodeModel> observer = new ObserverOnNextListener<GetVerificationCodeModel>() {
            @Override
            public void onNext(GetVerificationCodeModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    setPrevTitle(mTitle);
                    replaceFragment(new VerifyPhoneFragment(), bundle);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().registerByPhone(new ProgressObserver<GetVerificationCodeModel>(getContext(), observer), phone, region);
    }
}
