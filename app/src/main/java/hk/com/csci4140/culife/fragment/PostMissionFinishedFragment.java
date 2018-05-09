package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by liujie(Jerry Liu) on 30/04/2018.
 */


public class PostMissionFinishedFragment extends BaseFragment {

    private static final String TAG = "PostMissionFinished";

    @BindView(R.id.post_mission_finished_total_fee)
    TextView mTotalFee;

    @BindView(R.id.post_mission_finished_end_date)
    TextView mEndDate;

    //@BindView(R.id.post_mission_finished_num_and_service_fee)
    //TextView mNumNeededAndServiceFee;

    private String mTitle;

    private Bundle bundle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon;
        //Go to Home Fragment (with no back)
        //TODO: WARNING!!! EVERY TIME YOU WANT TO OVERRIDE THE GO BACK ICON, YOU SHOULD ALSO OVERRIDIE IT IN BASEACTIVITY'S ONBACKPRESS()!!!
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the home page fragment, item num = 0;
                getBottomNav().setCurrentItem(0);
            }
        });

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = getString(R.string.post_mission_finished_title);
        }
        setToolbarTitle(mTitle);

        //Get the bundle from previous fragment
        if(bundle == null){
            bundle = getArguments();
        }

        initialSetting();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_post_mission_finished, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Total Fee
        float totalFee = bundle.getFloat(Constant.MISSION_POST_FINISHED_TOTAL_FEE);
        mTotalFee.setText(String.format(getString(R.string.post_mission_finished_total_fee_format),
                totalFee));

        //End Date
        String endDate = bundle.getString(Constant.MISSION_POST_FINISHED_END_DATE);
        String endDateChinese = Utility.convertToChineseDate(getContext(), endDate);
        mEndDate.setText(String.format(getString(R.string.post_mission_finished_end_date_format),
                endDateChinese));

        //Num needed and service fee
        int numNeeded = bundle.getInt(Constant.MISSION_POST_FINISHED_NUM_NEEDED);
        float serviceFee = bundle.getFloat(Constant.MISSION_POST_FINISHED_SERVICE_FEE);
        //mNumNeededAndServiceFee.setText(String.format(getString(R.string.post_mission_finished_num_and_service_fee_format),
        //        numNeeded, serviceFee));
    }


    @OnClick(R.id.post_mission_finished_continue_button)
    void onClickContinue(){
        int missionId = bundle.getInt(Constant.MISSION_POST_FINISHED_MISSION_ID);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.MISSION_DETAIL_MISSION_ID, missionId);
        setPrevTitle(mTitle);
        replaceFragment(new MissionDetailFragment(), bundle);
    }
}
