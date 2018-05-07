package hk.com.apptech.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import hk.com.apptech.culife.R;

/**
 * Created by zhenghao(Kelvin Zheng) on 11/04/2018.
 */

public class UserProfileFragment extends BaseFragment {

    private static final String TAG = "UserProfileFrag";

    private String mTitle;
    private String mPrevTitle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(true);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }


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
        setToolbarTitle(mTitle);

        initialSetting();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the title of previous fragment
        setToolbarTitle(mPrevTitle);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    //Menu Item On Click
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




    @Override
    public void onStart(){
        initialSetting();
        super.onStart();


    }
}
