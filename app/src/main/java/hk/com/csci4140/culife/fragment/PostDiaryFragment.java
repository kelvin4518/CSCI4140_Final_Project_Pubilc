package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.UserModel;

/**
 * Created by zhenghao(Kelvin Zheng) on 12/04/2018.
 */

public class PostDiaryFragment extends BaseFragment {
    CatLoadingView mCatLoadingView;
    public int habbitIDfordiary = 0;

    @BindView(R.id.diary_content)
    TextView mDairyContent;

    private static final String TAG = "SettingFrag";

    private String mTitle;
    private String mPrevTitle;

   View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
                jsonParams.put("habitid", habbitIDfordiary);
                jsonParams.put("blog",mDairyContent.getText().toString());

                outerJsonParams.put("blog", jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                //Log.d(TAG,"bodyentity");
                callAPItoChangeDiary(entity);
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
        View mView = inflater.inflate(R.layout.fragment_postdairy, container, false);
        ButterKnife.bind(this, mView);

        mConfirmCompleteBtn = mView.findViewById(R.id.confirm_upload_diary);
        mView.findViewById(R.id.confirm_upload_diary).setOnClickListener(mOnClickListener);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

    }

    public void callAPItoChangeDiary(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();
        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/blog_create",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
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
