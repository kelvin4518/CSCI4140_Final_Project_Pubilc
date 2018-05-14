package hk.com.csci4140.culife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.UserModel;

/**
 * Created by zhenghao(Kelvin Zheng) on 11/04/2018.
 */

public class UserProfileFragment extends BaseFragment {

    private static final String TAG = "UserProfileFrag";
    private Context mContext;
    private String mTitle;
    private String mPrevTitle;

    @BindView(R.id.user_profile_user_name)
    TextView mUserName;

    @BindView(R.id.user_profile_phone)
    TextView mUserPhone;

    @BindView(R.id.user_profile_email)
    TextView mUserEmail;

    @BindView(R.id.user_profile_recyclerView)
    RecyclerView mOptionRecyclerView;

    @BindView(R.id.user_profile_pic)
    ImageView mUserImage;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(false);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }

    public class MyClickListener implements View.OnClickListener{
        int position;

        public MyClickListener(int my_position){
            this.position = my_position;
        }

        @Override
        public void onClick(View v) {
            String message = new String();
            if (this.position == 0){
                //ChangeImageFragment ImageFragment = new ChangeImageFragment();
                //replaceFragment(ImageFragment,null);
            }
            else{
                ChangeNameFragment settingFragment = new ChangeNameFragment();
                replaceFragment(settingFragment,null);
            }
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    //Set the Setting Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //In case the duplicate menu item
        try {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreateOptionsMenu: " + e.toString());
        }

        inflater.inflate(R.menu.user_profile_menu, menu);
    }


    // before the user see the pages
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = getString(R.string.user_profile_fragment_title);
        }
        if (mPrevTitle == null) {
            mPrevTitle = getPrevTitle();
        }
        mTitle = "个人资料";
        setToolbarTitle(mTitle);

        initialSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, mView);

        callGetProfileAPIToGetID();
        Log.d(TAG, "Here?");

        // whether the change in content will change the layout size
        mOptionRecyclerView.setHasFixedSize(false);
        mOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mOptionRecyclerView.setLayoutManager(mLayoutManager);

        // use an Adapter
        ArrayList<String> settingList = new ArrayList<String>();
        settingList.add("Upload the icon");
        settingList.add("Modify the personal information");
        //ArrayList<Integer> settingLogoList = new ArrayList<Integer>();
        //settingLogoList.add(R.drawable.ic_menu_camera);
        //settingLogoList.add(R.mipmap.ic_launcher_round);
        mOptionRecyclerView.setHasFixedSize(false);
        mOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mOptionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mOptionRecyclerView.setAdapter(
                new CommonAdapter<String>(getContext(), R.layout.item_profile_setting, settingList) {
                    @Override
                    public void convert(ViewHolder holder, String s, int pos) {
                        Log.d(TAG,"settinglist"+s);
                        holder.setText(R.id.item_profile_setting_content, s);
                        holder.itemView.setOnClickListener(new UserProfileFragment.MyClickListener(pos));

                    }
                    @Override
                    public void onViewHolderCreated(ViewHolder holder, View itemView) {
                        super.onViewHolderCreated(holder, itemView);
                        ButterKnife.bind(this,itemView);
                    }
                });


        return mView;
    }

    @Override
    public void onStart() {
        initialSetting();
        super.onStart();
    }

    // when user is interacting with the pages
    // Menu Item On Click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setPrevTitle(mTitle);
        switch (item.getItemId()) {
            case R.id.action_taker_collection:
                showBottomSnackBar("like");
                break;
            case R.id.action_wallet:
                showBottomSnackBar("wallet");
                break;
            case R.id.action_setting:
                replaceFragment(new SettingFragment(), null);
                break;
            default:
                break;
        }

        return true;
    }


    // after the user won't see the pages
    @Override
    public void onDestroy() {
        super.onDestroy();

        //Set the title of previous fragment
        setToolbarTitle(mPrevTitle);
    }

    public void callGetProfileAPIToGetID() {

        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

//            mCatLoadingView = new CatLoadingView();

//            mCatLoadingView.show(getFragmentManager(), "");

        client.get(getContext(), Constant.API_BASE_URL + "profiles/", null, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: profiles");
                Log.d("API_REPORT", "onSuccess: status : " + statusCode);
                Log.d("API_REPORT", "onSuccess: response: " + response);

                String usename = new String();
                Integer id = 0;
                String bio = new String();
                String image = new String();

                try {
                    JSONObject json_profile = response.getJSONObject("profile");
                    usename = json_profile.getString("username");
                    id = json_profile.getInt("id");
                    bio = json_profile.getString("bio");
                    image = json_profile.getString("image");

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                mUserName.setText(usename);
                mUserPhone.setText(String.valueOf(id));
                mUserEmail.setText(bio);

                if (image != "null") {
                    Glide.with(getContext()).load(image).into(mUserImage);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                    mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: profiles");
                Log.d("API_REPORT", "onFailure: status : " + statusCode);
                Log.d("API_REPORT", "onFailure: response : " + response);
            }
        });
    }

}
