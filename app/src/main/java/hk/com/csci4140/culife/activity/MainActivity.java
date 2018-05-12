package hk.com.csci4140.culife.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.fragment.ChatListFragment;
import hk.com.csci4140.culife.fragment.HabitDetailFragment;
import hk.com.csci4140.culife.fragment.HomeFragment;
import hk.com.csci4140.culife.fragment.PolicyFragment;
import hk.com.csci4140.culife.fragment.PostMissionStepOneFragment;
import hk.com.csci4140.culife.fragment.UserProfileFragment;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private boolean hasInitNav = false;

    private int previousItem = 0;

    private CatLoadingView mCatLoadingView;












    // before user see the page, set which fragment is going to be displayed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Set the app language
        Utility.setLanguage(MainActivity.this);

        ButterKnife.bind(this);

        Log.d(TAG, "onCreate: user token : "+UserModel.token);
    }

    @Override
    public void onStart() {
        super.onStart();
        //MainActivity needs tool bar
        setToolbarVisibility(true);

        //If the bottom navigation hasn't been initialized, we should initialize it
        if(!hasInitNav) {
            setBottomNav();

            //Check if the user has login, if not, show the policy fragment
            Fragment initFragment = UserModel.isLogin ? new HomeFragment() : new PolicyFragment();
            // michael added, to disable the terms and conditions page when starting the app


            initFragment = new HomeFragment();

            boolean hasBottomNav = UserModel.isLogin;
            //Set if the fragment has bottom navigation bar
            setBottomNavFragment(hasBottomNav);
//            setFragment(initFragment, null);



            JSONObject jsonParams = new JSONObject();
            callGetHabitListAPI();
        }
    }



    private void callGetHabitListAPI(){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
//

        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        client.get(this,Constant.API_BASE_URL+"habits/created",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("API_REPORT", "onSuccess: get habit list");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                showBottomSnackBar("Success get habit list");
                HomeFragment destFragment = new HomeFragment();
                destFragment.initHomePageDetail(response);
                setFragment(destFragment, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("API_REPORT", "onFailure: get habit list");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar("Fail get habit list");
                HomeFragment destFragment = new HomeFragment();
                setFragment(destFragment, null);
            }
        });

    }










    // Bottom Navigation Bar

    //Set the bottom navigation bar
    private void setBottomNav(){
        hasInitNav = true;
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_home, R.drawable.ic_menu_camera, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_range, R.drawable.ic_menu_send, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_add, R.drawable.ic_action_add, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_message, R.drawable.ic_menu_gallery, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.nav_other_mission, R.drawable.ic_menu_manage, R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Disable thpre-initializede translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

        // Set listeners
        // TODO : rewrite the check login logic
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                setTabSelectFragment(position);
                return true;

            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

    }

    //Set the Tab selection
    private void setTabSelectFragment(int position){
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new HomeFragment();
                //fragment = new FriendMomentFragment();
                fragment = new ChatListFragment();
                break;
            case 2:
                 fragment = new PostMissionStepOneFragment();
                //fragment = new ();
                break;
            case 3:
                fragment = new HabitDetailFragment();
                break;
            case 4:
                fragment = new UserProfileFragment();
                break;
            default:
                fragment = null;
                break;
        }
        setFragment(fragment, null);
    }












    //Get the previous selected item in bottom navigation bar
    public int getPreviousItem() {
        return previousItem;
    }
}
