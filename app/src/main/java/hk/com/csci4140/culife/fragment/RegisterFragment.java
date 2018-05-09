package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cazaea.sweetalert.SweetAlertDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.R;

/**
 * Created by zhenghao(Kelvin Zheng) on 01/04/2018.
 */

public class RegisterFragment extends BaseFragment {

    private static final String TAG = "RegisterFrag";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has no toolbar
        setToolbarVisibility(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @OnClick(R.id.register_phone_button)
    void onClickPhoneRegister(){
        //Go to Phone Register
        replaceFragment(new RegisterPhoneFragment(), null);
    }

    @OnClick(R.id.register_wechat_button)
    void onClickWechatRegister(){
        //TODO: WeChat register function
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText("Still need to implemented")
                .setConfirmText(getString(R.string.warning_confirm))
                .show();
    }

    @OnClick(R.id.register_login_button)
    void onClickLogin(){
        pressBack();
    }
}
