package hk.com.csci4140.culife.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.BaseActivity;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.SessionManager;

/**
 * Created by zhenghao(Kelvin Zheng) on 26/04/2018.
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFrag";

    private AlertDialog dialog;
    private FragmentManager mFragmentManager;

    @Override
    public void onStart(){
        super.onStart();
        //Use exception because LoginActivity doesn't have bottom navigation
        try {
            //Check if the current fragment needs bottom navigation, if it does, set the margin bottom of FrameLayout
            if(isBottomNavFragment()){
                getBottomNav().setVisibility(View.VISIBLE);
                setFragmentContentLayoutMarginBottom(50);
            }
            else {
                getBottomNav().setVisibility(View.GONE);
                setFragmentContentLayoutMarginBottom(0);
            }

            //If the user press go back in current fragment, this will be useful
            setBottomNavFragment(isPrevBottomNavFragment());
        }catch (Exception e){
            //For this situation, it means that fragment doesn't have bottomNavigation, just ignore the error message
            Log.i(TAG, "onStart: " + e.toString());
        }
    }


    //Returns the toolbar
    protected Toolbar getToolbar(){
        return ((BaseActivity)getActivity()).getToolbar();
    }

    //Set the toolbar's title
    protected void setToolbarTitle(String title){
        ((BaseActivity)getActivity()).setToolbarTitle(title);
    }

    //Set the visibility of toolbar
    protected void setToolbarVisibility(boolean visible){
        if(visible){
            ((BaseActivity)getActivity()).setToolbarVisibility(true);
        }
        else {
            ((BaseActivity)getActivity()).setToolbarVisibility(false);
        }
    }

    //Press Back function
    protected void pressBack(){

        ((BaseActivity)getActivity()).onBackPressed();
    }

    //Show the Snack Bar at bottom
    protected void showBottomSnackBar(String text){
        ((BaseActivity)getActivity()).showBottomSnackBar(text);
    }

    //Replace to a new activity, with no stack
    protected void replaceActivity(Class activity, @Nullable Bundle bundle){
        ((BaseActivity)getActivity()).replaceActivity(activity, bundle);
    }

    //Add a new activity, has stack
    protected void addActivity(Class activity, @Nullable Bundle bundle){
        ((BaseActivity)getActivity()).addActivity(activity, bundle);
    }

    //Replace to a fragment, with stack
    protected void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        ((BaseActivity)getActivity()).replaceFragment(fragment, bundle);
    }

    //Set a fragment, with no stack
    protected void setFragment(Fragment fragment, @Nullable Bundle bundle){
        ((BaseActivity)getActivity()).setFragment(fragment, bundle);
    }


    //Add a fragment, with stack
    protected void addFragment(Fragment fragment, @Nullable Bundle bundle){
        ((BaseActivity)getActivity()).addFragment(fragment, bundle);
    }

    //Clear the stack of fragment manager
    protected void clearFragmentManagerStack(){
        ((BaseActivity)getActivity()).clearFragmentCallbackStack();
    }

    //Get the bottom navigation bar
    protected AHBottomNavigation getBottomNav(){
        return ((BaseActivity)getActivity()).getBottomNav();
    }

    //Get the Circle Image View on the toolbar
    protected CircleImageView getProfileImage(){
        return ((BaseActivity)getActivity()).getProfileImage();
    }

    //This boolean is used to check if current fragment needs bottom navigation
    protected boolean isBottomNavFragment(){
        return ((BaseActivity)getActivity()).isBottomNavFragment();
    }

    protected void setBottomNavFragment(boolean isBottomNavFragment){
        ((BaseActivity)getActivity()).setBottomNavFragment(isBottomNavFragment);
    }

    //This boolean is used to check if previous fragment needs bottom navigation
    protected boolean isPrevBottomNavFragment(){
        return ((BaseActivity)getActivity()).isPrevBottomNavFragment();
    }

    protected void setPrevBottomNavFragment(boolean isPrevBottomNavFragment){
        ((BaseActivity)getActivity()).setPrevBottomNavFragment(isPrevBottomNavFragment);
    }

    //Get the FrameLayout that contains fragment
    protected CoordinatorLayout getFragmentContentLayout(){
        return ((BaseActivity)getActivity()).getFragmentContentLayout();
    }

    //Set the margin-bottom of previous FrameLayout
    protected void setFragmentContentLayoutMarginBottom(int dp){
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        Resources r = getContext().getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        int pxTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                50,
                r.getDisplayMetrics()
        );
        lp.setMargins(0, pxTop, 0, px);
        getFragmentContentLayout().setLayoutParams(lp);
    }

    //Set the Login Icon, if user hasn't login, it should show the login text, otherwise, show the Icon
    protected void setLoginIcon(){
        if(UserModel.isLogin){

            getToolbar().setNavigationIcon(null);
            getProfileImage().setVisibility(View.VISIBLE);
            if(UserModel.iconUrl != null && !UserModel.iconUrl.equals("")) {
                Glide.with(getContext()).load(UserModel.iconUrl).into(getProfileImage());
            }
            getProfileImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Goto user profile fragment
//                    setFragment(new UserProfileFragment(), null);
                    getBottomNav().setCurrentItem(3);
                }
            });
        }
        else {
            getProfileImage().setVisibility(View.GONE);
            getToolbar().setNavigationIcon(R.drawable.ic_action_login);
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addActivity(LoginActivity.class, null);
                }
            });
        }
    }

    //Set the Login Icon Invisibility
    protected void setLoginIconVisible(boolean visible){
        if(visible){
            getProfileImage().setVisibility(View.VISIBLE);
        }
        else {
            getProfileImage().setVisibility(View.GONE);
        }
    }

    //Set the go back Icon
    protected void setGoBackIcon(){
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"back");
                pressBack();
            }
        });
    }

    //Get and Set prev Title
    protected void setPrevTitle(String prevTitle){
        ((BaseActivity)getActivity()).setPrevTitle(prevTitle);
    }

    protected String getPrevTitle(){
        return ((BaseActivity)getActivity()).getPrevTitle();
    }


    //Get and Set my latitude and my longitude
    protected void setMyLatitude(double latitude){
        ((BaseActivity)getActivity()).setMyLatitude(latitude);
    }

    protected double getMyLatitude(){
        return ((BaseActivity)getActivity()).getMyLatitude();
    }

    protected void setMyLongitude(double longitude){
        ((BaseActivity)getActivity()).setMyLongitude(longitude);
    }

    protected double getMyLongitude(){
        return ((BaseActivity)getActivity()).getMyLongitude();
    }

}
