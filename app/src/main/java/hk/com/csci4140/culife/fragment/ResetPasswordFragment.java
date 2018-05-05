package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cazaea.sweetalert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.R;

/**
 * Created by zhenghao(Kelvin Zheng) on 08/04/2018.
 */

public class ResetPasswordFragment extends BaseFragment {

    private static final String TAG = "ResetPasswordFrag";

    @BindView(R.id.reset_password_password)
    EditText mPassword;

    @BindView(R.id.reset_password_retype)
    EditText mRetype;

    private String mTitle;
    private String mPrevTitle;
    private String errorText = "";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.reset_password_title);
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    private boolean isValidInput(){
        if(mPassword.getText() == null || mPassword.getText().toString().equals("")){
            errorText = getString(R.string.reset_password_warning_input_password);
            return false;
        }
        else if(mRetype.getText() == null || mRetype.getText().toString().equals("")){
            errorText = getString(R.string.reset_password_warning_retype_password);
            return false;
        }
        else if(!mPassword.getText().toString().equals( mRetype.getText().toString())){
            errorText = getString(R.string.reset_password_warning_not_the_same);
            return false;
        }
        else {
            return true;
        }
    }

    @OnClick(R.id.reset_password_continue_button)
    void onClickContinue(){
        if(isValidInput()){
            //TODO: call api to change password
            replaceFragment(new ResetPasswordFinishedFragment(), null);
        }
        else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }


}
