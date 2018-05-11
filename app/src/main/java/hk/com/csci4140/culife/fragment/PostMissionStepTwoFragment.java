package hk.com.csci4140.culife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.CreateMissionModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by liujie(Jerry Liu) on 28/04/2018.
 */

public class PostMissionStepTwoFragment extends BaseFragment {

    private static final String TAG = "PostMissionStepTwo";

    @BindView(R.id.post_mission_two_title)
    TextView missionTitle;

    @BindView(R.id.post_mission_two_mission_date)
    TextView missionDate;

    @BindView(R.id.post_mission_two_time)
    TextView missionTime;

    @BindView(R.id.post_mission_two_content)
    TextView missionContent;

    private String mTitle;
    private String mPrevTitle;

    private HashMap<String, String> parameter;

    //Bundle that will pass to finished fragment
    Bundle finishedBundle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(parameter == null){
            Bundle bundle = getArguments();
            try {
                parameter = (HashMap<String, String>) bundle.getSerializable(Constant.MISSION_MAP_PARAMETER);
            }catch (Exception e){
                showBottomSnackBar(getString(R.string.network_connect_errors));
                parameter = null;
                Log.e(TAG, e.toString());
            }
        }
        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = parameter.get(Constant.MISSION_TITLE);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_post_mission_step_two, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        finishedBundle = new Bundle();

        //Check if get the parameter correctly
        if(parameter != null){
            //Title
            missionTitle.setText(parameter.get(Constant.MISSION_TITLE));

            //Salary
            //float salary = Float.parseFloat(parameter.get(Constant.MISSION_SALARY));
            //missionSalary.setText(String.format(getString(R.string.post_mission_two_salary_format), salary));

            //Time
            missionTime.setText(String.format(getString(R.string.time_format_period),
                    parameter.get(Constant.MISSION_START_TIME),
                    parameter.get(Constant.MISSION_END_TIME)));

            missionDate.setText(String.format(getString(R.string.date_format_period),
                    parameter.get(Constant.MISSION_POST_START),
                    parameter.get(Constant.MISSION_POST_END)));

            //Content
            if(parameter.get(Constant.MISSION_CONTENT) != null){
                missionContent.setText(parameter.get(Constant.MISSION_CONTENT));
            }
        }
    }


    //Call the Http to post the mission
    private void callCreateMissionHttp(){
        ObserverOnNextListener<CreateMissionModel> observer = new ObserverOnNextListener<CreateMissionModel>() {
            @Override
            public void onNext(CreateMissionModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //TODO: Should go to payment page, waiting api to complete, now just go to finished page
                    finishedBundle.putInt(Constant.MISSION_POST_FINISHED_MISSION_ID, model.getResult().getMissionId());
                    replaceFragment(new PostMissionFinishedFragment(), finishedBundle);
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().createMission(new ProgressObserver<CreateMissionModel>(getContext(), observer), parameter);
    }


    @OnClick(R.id.post_mission_two_continue_button)
    void onClickContinueButton(){
        //Call the create mission api
        callCreateMissionHttp();
    }
}
