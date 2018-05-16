package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

/**
 * Created by liujie(Jerry Liu) on 08/04/2018.
 */

public class ForgetPasswordFragment extends BaseFragment {

    private static final String TAG = "ForgetPasswordFrag";

    @BindView(R.id.forget_password_phone)
    EditText mPhone;

    private String mTitle;
    private String mPrevTitle;
    private String errorText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = "CULife";
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
        }
        setToolbarTitle(mTitle);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
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
        View mView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    //Check if the information that usre input is valid
    private boolean isValidPhoneNum(){
        if(mPhone.getText() == null || mPhone.getText().toString().equals("")){
            errorText = getString(R.string.forget_password_warning_text);
            return false;
        }

        if(mPhone.getText().toString().length() != Constant.HONGKONG_PHONE_LENGTH
                &&
                mPhone.getText().toString().length() != Constant.CHINA_PHONE_LENGTH){
            errorText = getString(R.string.forget_password_wrong_phone);
            return false;
        }

        return true;
    }


    @OnClick(R.id.forget_password_continue_button)
    void onClickContinue(){
        if(isValidPhoneNum()){
            //call API to send SMS
            String phone = mPhone.getText().toString();
            callForgetPasswordHttp(phone);

        }
        else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }


    //Call API to get verification message
    private void callForgetPasswordHttp(final String phone){
        ObserverOnNextListener<GetVerificationCodeModel> observer = new ObserverOnNextListener<GetVerificationCodeModel>() {
            @Override
            public void onNext(GetVerificationCodeModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //Go to Verify Fragment
                    setPrevTitle(mTitle);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.REGISTER_PHONE_NUM, phone);
                    replaceFragment(new VerifyPhoneFragment(), bundle);

                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    if(model.getResult().getErrors().get(0).contains("can not find user")){
                        showBottomSnackBar(getString(R.string.forget_password_can_not_find_phone));
                    }
                    else {
                        showBottomSnackBar(getString(R.string.network_connect_errors));
                    }
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().forgetPassword(new ProgressObserver<GetVerificationCodeModel>(getContext(), observer), phone);
    }
}
