package hk.com.csci4140.culife.fragment;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.adapter.IndustryListAdapter;
import hk.com.csci4140.culife.adapter.ProfileSettingAdapter;

/**
 * Created by zhenghao(Kelvin Zheng) on 11/04/2018.
 */

public class UserProfileFragment extends BaseFragment {

    private static final String TAG = "UserProfileFrag";

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

    private  RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;



    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        // setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(false);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }

    //Set the Setting Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //In case the duplicate menu item
        try {
            for(int i = 0; i < menu.size(); i ++){
                menu.getItem(i).setVisible(false);
            }
        }catch (Exception e){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, mView);

        mUserName.setText("michael");
        mUserPhone.setText("12345678");
        mUserEmail.setText("michael251902523@gmail.com");

        // whether the change in content will change the layout size
        mOptionRecyclerView.setHasFixedSize(false);
        mOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mOptionRecyclerView.setLayoutManager(mLayoutManager);

        // use an Adapter
        ArrayList<String> settingList = new ArrayList<String>();
        settingList.add("Habit Performance Analysis");
        settingList.add("More Setting");
        ArrayList<Integer> settingLogoList = new ArrayList<Integer>();
        settingLogoList.add(R.drawable.ic_menu_camera);
        settingLogoList.add(R.mipmap.ic_launcher_round);
        ProfileSettingAdapter adapter = new ProfileSettingAdapter(getContext(), settingList, settingLogoList);
        mOptionRecyclerView.setAdapter(adapter);



        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }



    // when user is interacting with the pages
    // Menu Item On Click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setPrevTitle(mTitle);
        switch (item.getItemId()){
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
    public void onDestroy(){
        super.onDestroy();

        //Set the title of previous fragment
        setToolbarTitle(mPrevTitle);
    }



}
