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

    @BindView(R.id.post_mission_two_salary)
    TextView missionSalary;

    @BindView(R.id.post_mission_two_location)
    TextView missionLocation;

    @BindView(R.id.post_mission_two_mission_date_with_icon)
    TextView missionDateWithIcon;

    @BindView(R.id.post_mission_two_switch_container)
    LinearLayout missionSwitchContainer;

    @BindView(R.id.post_mission_two_video_needed)
    TextView missionVideoNeeded;

    @BindView(R.id.post_mission_two_photo_needed)
    TextView missionPhotoNeeded;

    @BindView(R.id.post_mission_two_disclose_media)
    TextView missionDiscloseMedia;

    @BindView(R.id.post_mission_two_mission_date)
    TextView missionDate;

    @BindView(R.id.post_mission_two_time)
    TextView missionTime;

    @BindView(R.id.post_mission_two_content)
    TextView missionContent;

    @BindView(R.id.post_mission_two_requirement)
    TextView missionRequirement;

    @BindView(R.id.post_mission_two_others)
    TextView missionOthers;

    @BindView(R.id.post_mission_two_post_date_start)
    TextView missionPostStart;

    @BindView(R.id.post_mission_two_post_date_end)
    TextView missionPostEnd;

    @BindView(R.id.post_mission_two_post_period)
    TextView missionPostPeriod;

    @BindView(R.id.post_mission_two_charge_salary)
    TextView missionChargeSalary;

    @BindView(R.id.post_mission_two_num_needed)
    TextView missionNumNeeded;

    @BindView(R.id.post_mission_two_post_fee)
    TextView missionPostFee;

    @BindView(R.id.post_mission_two_service_fee)
    TextView missionServiceFee;

    @BindView(R.id.post_mission_two_total_fee)
    TextView missionTotalFee;


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
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
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
            float salary = Float.parseFloat(parameter.get(Constant.MISSION_SALARY));
            missionSalary.setText(String.format(getString(R.string.post_mission_two_salary_format), salary));

            //Location
            if(parameter.get(Constant.MISSION_HAS_ADDRESS).equals(Constant.FALSE)){
                missionLocation.setText(getString(R.string.post_mission_two_no_location));
            }
            else {
                missionLocation.setText(parameter.get(Constant.MISSION_ADDRESS));
            }

            //Date
            if(parameter.get(Constant.MISSION_NO_SPEC_DATE).equals(Constant.TRUE)){
                missionDateWithIcon.setText(getString(R.string.post_mission_two_no_date));
                missionDate.setText(getString(R.string.post_mission_two_no_date));
            }
            else {
                missionDateWithIcon.setText(Utility.convertToChineseDate(getContext(), parameter.get(Constant.MISSION_DATE)));
                missionDate.setText(Utility.convertToChineseDate(getContext(), parameter.get(Constant.MISSION_DATE)));
            }

            //Video, Photo and disclose media
            int goneNum = 0;
            if(parameter.get(Constant.MISSION_NEED_VIDEO).equals(Constant.FALSE)){
                missionVideoNeeded.setVisibility(View.GONE);
                goneNum ++;
            }
            if(parameter.get(Constant.MISSION_NEED_PHOTO).equals(Constant.FALSE)){
                missionPhotoNeeded.setVisibility(View.GONE);
                goneNum ++;
            }
            if(parameter.get(Constant.MISSION_DISCLOSE_MEDIA).equals(Constant.FALSE)){
                missionDiscloseMedia.setVisibility(View.GONE);
                goneNum ++;
            }
            if(goneNum == 3){
                missionSwitchContainer.setVisibility(View.INVISIBLE);
            }

            //Time
            missionTime.setText(String.format(getString(R.string.time_format_period),
                    parameter.get(Constant.MISSION_START_TIME),
                    parameter.get(Constant.MISSION_END_TIME)));

            //Content
            if(parameter.get(Constant.MISSION_CONTENT) != null){
                missionContent.setText(parameter.get(Constant.MISSION_CONTENT));
            }

            //Requirement
            if(parameter.get(Constant.MISSION_REQUIREMENT) != null){
                missionRequirement.setText(parameter.get(Constant.MISSION_REQUIREMENT));
            }

            //Others
            if(parameter.get(Constant.MISSION_NEED_INTERVIEW).equals(Constant.TRUE)){
                missionOthers.setText(getString(R.string.post_mission_two_need_interview));
            }

            //Post Period
            String endDate;
            if(parameter.get(Constant.MISSION_POST_METHOD).equals("1")){
                String startDate = Utility.getToday();
                // 0 == 3個月 ， 1 == 1個月
                int monthAfter = parameter.get(Constant.MISSION_POST_PERIOD_ID).equals("0") ? 3 : 1;
                endDate = Utility.getDateAfterMonth(startDate, monthAfter);
                missionPostStart.setText(Utility.convertToChineseDate(getContext(), startDate));
                missionPostEnd.setText(Utility.convertToChineseDate(getContext(), endDate));
                missionPostPeriod.setText(String.format(getString(R.string.post_mission_two_post_period_format),
                        parameter.get(Constant.MISSION_POST_PERIOD)));
            }
            else {
                String startDate = parameter.get(Constant.MISSION_POST_START);
                endDate = parameter.get(Constant.MISSION_POST_END);
                missionPostStart.setText(Utility.convertToChineseDate(getContext(), startDate));
                missionPostEnd.setText(Utility.convertToChineseDate(getContext(), endDate));
                int daysDiff = Utility.getDaysBetween(startDate, endDate);
                missionPostPeriod.setText(String.format(getString(R.string.post_mission_two_post_period_days_format),
                        daysDiff));
            }

            //Charge Salary
            missionChargeSalary.setText(String.format(getString(R.string.post_mission_two_fee_format), salary));

            //Num of needed
            int numNeeded = Integer.parseInt(parameter.get(Constant.MISSION_NUM_NEEDED));
            missionNumNeeded.setText(String.format(getString(R.string.post_mission_two_num_needed_format),
                    numNeeded));

            //Post Fee
            missionPostFee.setText(String.format(getString(R.string.post_mission_two_fee_format), salary));

            //Service Fee
            float serviceFee = (float) (salary * 0.03);
            missionServiceFee.setText(String.format(getString(R.string.post_mission_two_fee_format), serviceFee));

            //Total Fee
            float totalFee = salary + serviceFee;
            missionTotalFee.setText(String.format(getString(R.string.post_mission_two_fee_format), totalFee));


            //Put values into bundle that will be passed to finished fragment
            finishedBundle.putFloat(Constant.MISSION_POST_FINISHED_TOTAL_FEE, totalFee);
            finishedBundle.putString(Constant.MISSION_POST_FINISHED_END_DATE, endDate);
            finishedBundle.putInt(Constant.MISSION_POST_FINISHED_NUM_NEEDED, numNeeded);
            finishedBundle.putFloat(Constant.MISSION_POST_FINISHED_SERVICE_FEE, serviceFee);

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
