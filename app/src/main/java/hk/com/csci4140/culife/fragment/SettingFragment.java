package hk.com.csci4140.culife.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.LoginModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 12/04/2018.
 */

public class SettingFragment extends BaseFragment {

    private static final String TAG = "SettingFrag";

    @BindView(R.id.show_location_switch_button)
    SwitchButton showLocationButton;

    @BindView(R.id.setting_region_spinner)
    Spinner regionSpinner;

    @BindView(R.id.setting_language_spinner)
    Spinner languageSpinner;

    private String mTitle;
    private String mPrevTitle;

    private boolean firstSelectLanguage = true;

    private void initialSetting(){
        //Set the Go Back
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.setting_title);
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
        }
        setToolbarTitle(mTitle);

        initialSetting();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set previous Fragment's title
        setToolbarTitle(mPrevTitle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Initialize the show location, 0 = default, 1 = true, 2 = false;
        int isShowLocation = UserModel.showLocation;

        showLocationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    callSetIsShowLocation(1);
                }
                else {
                    callSetIsShowLocation(0);
                }
            }
        });

        switch (isShowLocation){
            case 0:
                showLocationButton.setCheckedNoEvent(false);
                break;
            case 1:
                showLocationButton.setCheckedNoEvent(true);
                break;
            default:
                break;
        }


        //Initialize the region
        int regionId = UserModel.region;
        switch (regionId){
            case 1:
                regionSpinner.setSelection(0, false);
                break;
            case 2:
                regionSpinner.setSelection(1, false);
                break;
            default:
                break;
        }
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callSetRegionApi(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Initialize the language
        int languageId = Utility.getLanguageId(getContext());
        switch (languageId){
            case 1:
                languageSpinner.setSelection(0, false);
                break;
            case 2:
                languageSpinner.setSelection(1, false);
                break;
            default:
                break;
        }
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SessionManager.putInt(getContext(),Constant.LANGUAGE_SETTING, (i + 1) );

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //Call Set is Show Location Api
    private void callSetIsShowLocation(final int isShown){
        ObserverOnNextListener<StandardModel> observer = new ObserverOnNextListener<StandardModel>() {
            @Override
            public void onNext(StandardModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    UserModel.updateShowLocation(getContext(), isShown);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    boolean checked = showLocationButton.isChecked();
                    showLocationButton.setCheckedNoEvent(!checked);
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().setIsShowLocation(new ProgressObserver<StandardModel>(getContext(), observer), UserModel.token, isShown);
    }


    //Call Set region Api
    private void callSetRegionApi(final int region){
        ObserverOnNextListener<StandardModel> observer = new ObserverOnNextListener<StandardModel>() {
            @Override
            public void onNext(StandardModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    UserModel.updateRegion(getContext(), region);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().setRegion(new ProgressObserver<StandardModel>(getContext(), observer), UserModel.token, region);
    }


    @OnClick(R.id.setting_return_record_container)
    void onClickReturnRecord(){
        //TODO: return record
        showBottomSnackBar("click return record");
    }


    @OnClick(R.id.setting_notification_setting_container)
    void onClickNotification(){
        //TODO: notification setting
        showBottomSnackBar("click notification setting");
    }


    @OnClick(R.id.setting_change_password_container)
    void onClickChangePassword(){
        //TODO: click change password
        showBottomSnackBar("click change password");
    }


    @OnClick(R.id.setting_change_phone_container)
    void onClickChangePhone(){
        //TODO: click change phone
        showBottomSnackBar("click change phone");
    }


    @OnClick(R.id.setting_common_problem_container)
    void onClickCommonProblem(){
        //TODO: click common problem
        showBottomSnackBar("click common problem");
    }


    @OnClick(R.id.setting_privacy_policy_container)
    void onClickPrivacyPolicy(){
        //TODO: click privacy policy
        showBottomSnackBar("click privacy policy");
    }


    @OnClick(R.id.setting_usage_policy_container)
    void onClickUsagePolicy(){
        //TODO: click usage policy
        showBottomSnackBar("click usage policy");
    }


    @OnClick(R.id.setting_contact_us_container)
    void onClickContactUs(){
        //TODO: click contact us
        showBottomSnackBar("click contact us");
    }


    @OnClick(R.id.setting_log_out_container)
    void onClickLogout(){
        //Show Alert Dialog
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText(getString(R.string.setting_log_out_warning))
                .setCancelText(getString(R.string.warning_cancel))
                .setConfirmText(getString(R.string.warning_confirm))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        UserModel.logout(getContext());
                        sweetAlertDialog.cancel();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        replaceActivity(LoginActivity.class, null);
                    }
                })
                .show();
    }
}
