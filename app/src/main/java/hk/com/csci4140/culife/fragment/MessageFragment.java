package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;

/**
 * Created by zhenghao(Kelvin Zheng) on 01/04/2018.
 */

public class MessageFragment extends BaseFragment {

    private static final String TAG = "MessageFrag";

    private String mTitle;

    private void initialSetting(){
        //Set the Tool bar title
        setToolbarTitle(mTitle);

        //Set the bottom navigation visible
        setBottomNavFragment(true);

        //Use to set the search icon
        setHasOptionsMenu(false);

        //Set Login Icon Invisibility
        setLoginIconVisible(true);
        //Set the Login Icon
        setLoginIcon();
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment
        mTitle = getString(R.string.message_fragment_title);
        setPrevTitle(mTitle);

        initialSetting();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }
}
