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
import android.widget.Button;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

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
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.HomeFragmentModel;
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

public class ChangeNameFragment extends BaseFragment {
    CatLoadingView mCatLoadingView;

    private static final String TAG = "SettingFrag";

    @BindView(R.id.change_username)
    TextView mUsernameChange;

    @BindView(R.id.change_bio)
    TextView mBioChange;

    private String mTitle;
    private String mPrevTitle;

   View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
                jsonParams.put("username", mUsernameChange.getText().toString());
                jsonParams.put("bio",mBioChange.getText().toString());

                outerJsonParams.put("user", jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                //Log.d(TAG,"bodyentity"+jsonParams);
                callAPItoChangeProfile(entity);
            }
            catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

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

    Button mConfirmCompleteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_setting_profiles, container, false);
        ButterKnife.bind(this, mView);

        mConfirmCompleteBtn = mView.findViewById(R.id.confirm_change_profile);
        mView.findViewById(R.id.confirm_change_profile).setOnClickListener(mOnClickListener);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

    }

    public void callAPItoChangeProfile(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getFragmentManager(), "");

        client.put(getContext(), Constant.API_BASE_URL+"user",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                replaceFragment(new UserProfileFragment(),null);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar("Fail to update!");
            }
        });
    }

}
