package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.R;

/**
 * Created by zhenghao(Kelvin Zheng) on 08/04/2018.
 */

public class ResetPasswordFinishedFragment extends BaseFragment {

    private static final String TAG = "ResetPasswordFinishedFrag";

    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment
        mTitle = getString(R.string.reset_password_finished_title);
        setToolbarTitle(mTitle);

    }

    @Override
    public void onResume(){
        super.onResume();

        //Set the Go back Icon, can not simply go back, should back to login fragment
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFragmentManagerStack();
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_reset_password_finished, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @OnClick(R.id.register_finished_continue_button)
    void onClickContinue(){
        clearFragmentManagerStack();
    }
}
