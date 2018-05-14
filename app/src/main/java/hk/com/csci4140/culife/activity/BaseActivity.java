package hk.com.csci4140.culife.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.fragment.HomeFragment;
import hk.com.csci4140.culife.fragment.LoginFragment;
import hk.com.csci4140.culife.fragment.MessageFragment;
import hk.com.csci4140.culife.fragment.OtherMissionFragment;
import hk.com.csci4140.culife.fragment.PostMissionFinishedFragment;
import hk.com.csci4140.culife.fragment.PostMissionStepOneFragment;
import hk.com.csci4140.culife.fragment.RangeFragment;
import hk.com.csci4140.culife.fragment.RegisterFinishedFragment;
import hk.com.csci4140.culife.fragment.ResetPasswordFinishedFragment;
import hk.com.csci4140.culife.receiver.NetWorkStateReceiver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 25/04/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private Toolbar toolbar;
    private boolean backButtonPressedOnce = false;
    private boolean isBottomNavFragment = false;
    private boolean isPrevBottomNavFragment = false;
    private long lastTime = 0;
    private FragmentManager mFragmentManager;
    private NetWorkStateReceiver netWorkStateReceiver;
    private String prevTitle = "";

    //My Latitude and Longitude
    private double myLatitude;
    private double myLongitude;



    @Override
    public void onStart(){
        super.onStart();
        //Set the app language
        Utility.setLanguage(BaseActivity.this);

        //Set the toolbar
        if(toolbar == null){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            try {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }catch (Exception e){
                Log.i(TAG, "OnStart: " + e.toString());
            }
        }
    }


    //Register net work state receiver in onResume()
    @Override
    protected void onResume() {
        super.onResume();

        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

    }


    //Unregister net work state receiver in onPause()
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(netWorkStateReceiver);
    }


    //Get toolbar
    public Toolbar getToolbar(){
        if(toolbar != null){
            return toolbar;
        }
        else {
            return (Toolbar) findViewById(R.id.toolbar);
        }
    }


    //Set the visibility of toolbar
    public void setToolbarVisibility(boolean visible){
        if(visible){
            try {
                (findViewById(R.id.toolbar)).setVisibility(View.VISIBLE);
            }catch (Exception e){
                Log.i(TAG, e.toString() );
            }
        }
        else {
            try {
                (findViewById(R.id.toolbar)).setVisibility(View.GONE);
            }catch (Exception e){
                Log.i(TAG, e.toString() );
            }
        }
    }


    //Set the title of toolbar
    public void setToolbarTitle(String title){
        if(toolbar != null) {
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText(title);
        }
    }


    //Replace current activity with new one (With No Stack)
    public void replaceActivity(Class activity, @Nullable Bundle bundle){
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constant.ACTIVITY_BUNDLE, bundle);
        startActivity(intent);
        this.finish();
    }


    //Add a new activity (with press back)
    public void addActivity(Class activity, @Nullable Bundle bundle){
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACTIVITY_BUNDLE, bundle);
        startActivity(intent);
    }


    //Replace current fragment (With Back)
    public void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        long currentTime = new Date().getTime();

        if (currentTime - lastTime > 300) {
            lastTime = currentTime;
            String backStateName = fragment.getClass().getName();

            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }

            fragment.setArguments(bundle);
            boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.content, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }

        }else {
            lastTime = currentTime;
        }
    }


    //Set fragment (With No Back)
    public void setFragment(Fragment fragment, @Nullable Bundle bundle){
        long currentTime = new Date().getTime();

        if (currentTime - lastTime > 300) {
            lastTime = currentTime;
            String backStateName = fragment.getClass().getName();

            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }

            fragment.setArguments(bundle);
            boolean fragmentPopped = mFragmentManager.popBackStackImmediate(null, 0);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment);
            ft.commit();

        }else {
            lastTime = currentTime;
        }
    }


    //Add a new fragment
    public void addFragment(Fragment fragment, @Nullable Bundle bundle){
        long currentTime = new Date().getTime();

        if (currentTime - lastTime > 300) {
            lastTime = currentTime;
            String backStateName = fragment.getClass().getName();

            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }

            fragment.setArguments(bundle);
            boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.add(R.id.content, fragment);
                ft.addToBackStack(backStateName);
                ft.commit();
            }

        }else {
            lastTime = currentTime;
        }
    }


    //Clear the fragment manager stack
    public void clearFragmentCallbackStack() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    //Override the onBackPressed
    @Override
    public void onBackPressed() {



        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        //Press Back of Post Mission Step One Fragment
        if(f instanceof PostMissionStepOneFragment){
            getBottomNav().setCurrentItem(((MainActivity)this).getPreviousItem());
//            clearFragmentCallbackStack();
            return;
        }
        else if (f instanceof RegisterFinishedFragment){
            //Press Back of Register Finished
            replaceActivity(MainActivity.class, null);
            return;
        }
        else if(f instanceof ResetPasswordFinishedFragment){
            //Press Back of Reset Password Finished Fragment
            clearFragmentCallbackStack();
            return;
        }else if(f instanceof PostMissionFinishedFragment){
            //Home page current item is 0;
            getBottomNav().setCurrentItem(0);
        }
//        else if(f instanceof LoginFragment){
//            replaceActivity(MainActivity.class, null);
//            return;
//        }

        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
            return;
        }




        ActivityManager activityManager = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);

        if(!(taskList.get(0).numActivities == 1 ) ||
                !taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            super.onBackPressed();
            return;
        }

        if (backButtonPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        backButtonPressedOnce = true;
        showBottomSnackBar(getString(R.string.exit_app_warning));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButtonPressedOnce = false;
            }
        }, 2000);

    }


    //Show Bottom Snack Bar
    public void showBottomSnackBar(String text){
        // make snackbar
        Snackbar mSnackbar = Snackbar.make(findViewById(R.id.content), text, Snackbar.LENGTH_SHORT);
        // get snackbar view
        View mView = mSnackbar.getView();
        // get textview inside snackbar view
        TextView mTextView = mView.findViewById(android.support.design.R.id.snackbar_text);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        // show the snackbar
        mSnackbar.show();
    }


    //Get Bottom Navigation bar
    public AHBottomNavigation getBottomNav(){
        return (AHBottomNavigation)findViewById(R.id.bottom_navigation);
    }


    //Get Profile Image
    public CircleImageView getProfileImage(){
        return (CircleImageView)findViewById(R.id.profile_image);
    }


    //Get Fragment frame layout
    public CoordinatorLayout getFragmentContentLayout(){
        return (CoordinatorLayout)findViewById(R.id.content);
    }


    //Bottom Nav boolean, if the boolean is true, it means current fragment needs bottom navigation bar
    public boolean isBottomNavFragment() {
        return isBottomNavFragment;
    }

    public void setBottomNavFragment(boolean bottomNavFragment) {
        isBottomNavFragment = bottomNavFragment;
    }


    //Previous bottom nav boolean, if the boolean is true, it means the previous fragment needs bottom navigation bar
    public boolean isPrevBottomNavFragment() {
        return isPrevBottomNavFragment;
    }

    public void setPrevBottomNavFragment(boolean prevBottomNavFragment) {
        isPrevBottomNavFragment = prevBottomNavFragment;
    }


    //Get and Set prev Title
    public String getPrevTitle() {
        return prevTitle;
    }

    public void setPrevTitle(String prevTitle) {
        this.prevTitle = prevTitle;
    }


    //Get and Set my latidude and my longitude

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }
}
