package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.GetVerificationCodeModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 *
 * code structure :
 * 1. Bind the views using ButterKnife
 * 2. Bind the onClick behavior using ButterKnife
 * 3. Handle what view to be displayed
 * 4. Handle the destroy view
 * 5. Register API Handling
 */


public class RegisterEmailFragment extends BaseFragment {


    // Bind the views using butterKnife
    private static final String TAG = "RegisterEmailFrag";

    @BindView(R.id.register_username)
    EditText mUsername;

    @BindView(R.id.register_via_email)
    EditText mEmailAddress;

    @BindView(R.id.register_email_password)
    EditText mPassword;

    @BindView(R.id.register_email_retype_password)
    EditText mRetypePassword;

    private String mTitle;
    private String errorText = "";
    CatLoadingView mView;








    // Bind the onclick using butterknife
    @OnClick(R.id.register_email_register_button)
    void onClickRegister(){
        if(isInfoValidate()){
            //Call Register Http, first put the information into bundle
//            Bundle bundle = new Bundle();
//            bundle.putString(Constant.REGISTER_PHONE_NUM, mEmailAddress.getText().toString());
//            bundle.putString(Constant.REGISTER_PASSWORD, mPassword.getText().toString());
//            bundle.putInt(Constant.REGISTER_REGION, mRegionSpinner.getSelectedItemPosition());
//
//            callRegisterHttp(mEmailAddress.getText().toString(),
//                    mRegionSpinner.getSelectedItemPosition(),
//                    bundle);

            //


            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
//                jsonParams.put("username", "michael_firebasechat_1");
                // String userNameString = mEmailAddress.getText().toString().split("@")[0];
                jsonParams.put("username", mUsername.getText().toString());
                jsonParams.put("email", mEmailAddress.getText().toString());
                jsonParams.put("password", mPassword.getText().toString());
                outerJsonParams.put("user",jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                callRegisterByEmailHttp(entity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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










    // before user see the page
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment
        mTitle = getString(R.string.register_email_title);
        setToolbarTitle(mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_register_email, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();

        //Set the Go back Icon
        setGoBackIcon();
    }









    // after user exit the page
    @Override
    public void onDestroy(){
        super.onDestroy();

        //Previous fragment has no toolbar
//        setToolbarVisibility(false);
    }










    //Check if the information that user input is valid
    private boolean isInfoValidate(){
        if(mEmailAddress.getText() == null || mEmailAddress.getText().toString().equals("")){
            errorText = getString(R.string.register_warning_input_email_address);
            return false;
        }

        if(!mEmailAddress.getText().toString().contains("@")){
            errorText = getString(R.string.register_warning_valid_email_address);
            return false;
        }

//        if(mRegionSpinner.getSelectedItemPosition() == Constant.NON_SELECT){
//            errorText = getString(R.string.register_warning_select_region);
//            return false;
//        }

//        if( (mRegionSpinner.getSelectedItemPosition() == Constant.HONG_KONG)
//                &&
//                (mEmailAddress.getText().toString().length() != Constant.HONGKONG_PHONE_LENGTH) ){
//
//            errorText = getString(R.string.register_warning_valid_phone_num);
//            return false;
//        }
//
//        if( (mRegionSpinner.getSelectedItemPosition() == Constant.CHINA)
//                &&
//                (mEmailAddress.getText().toString().length() != Constant.CHINA_PHONE_LENGTH) ){
//
//            errorText = getString(R.string.register_warning_valid_phone_num);
//            return false;
//        }

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

//        if(!mPolicyCheckBox.isChecked()){
//            errorText = getString(R.string.register_warning_check_agreement);
//            return false;
//        }

        return true;
    }

    private void callRegisterByEmailHttp(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        mView = new CatLoadingView();
        mView.show(getFragmentManager(), "");
        client.post(getContext(),Constant.API_BASE_URL+"users",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mView.dismiss();
                Log.d("API_REPORT", "onSuccess: register");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                showBottomSnackBar("Welcome to CULife !");
                UserModel.fromLoginJson(getContext(),true,response);
                callRegisterByEmailToGoogleFirebase();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mView.dismiss();
                Log.d("API_REPORT", "onFailure: register");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar("Register Fails. Please try again later");
            }
        });
    }



    private FirebaseAuth mFirebaseAuth;

    private void callRegisterByEmailToGoogleFirebase(){
        // initialize the Auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        String userEmail = mEmailAddress.getText().toString();
        String userPassword = mPassword.getText().toString();

        // create the user with email and password
        mFirebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showBottomSnackBar("Corresponding Firebase User Created");
                }else{
                    showBottomSnackBar("Firebase User Creation Fails");
                }

                replaceActivity(MainActivity.class, null);
            }
        });

    }

    // OLD CODE : NOT USE ANYMORE
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
