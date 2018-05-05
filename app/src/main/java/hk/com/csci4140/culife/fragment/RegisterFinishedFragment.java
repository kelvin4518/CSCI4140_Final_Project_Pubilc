package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.model.UserModel;

/**
 * Created by liujie(Jerry Liu) on 08/04/2018.
 */

public class RegisterFinishedFragment extends BaseFragment {

    private static final String TAG = "RegisterFinishedFrag";

    private String mTitle;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment
        mTitle = getString(R.string.register_finished_title);
        setToolbarTitle(mTitle);
    }

    @Override
    public void onResume(){
        super.onResume();

        //Set the Go back Icon, can not simply go back, should goto main activity
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity(MainActivity.class, null);
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_register_finished, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();

        //get the bundle from previous fragment to use data to login
        Bundle bundle = getArguments();
        if(token == null) {
            token = bundle.getString(Constant.TOKEN);
            int region = bundle.getInt(Constant.REGISTER_REGION);
            UserModel.login(getContext(), false, token, null, region, 1);
        }
    }

    @OnClick(R.id.register_finished_continue_button)
    void onClickContinue(){
        //Goto Create profile fragment
        setPrevTitle(mTitle);
        replaceFragment(new EditProfileFragment(), null);
    }
}
