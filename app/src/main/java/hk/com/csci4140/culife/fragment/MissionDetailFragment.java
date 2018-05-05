package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.CreateMissionModel;
import hk.com.csci4140.culife.model.GetMissionDetailModel;
import hk.com.csci4140.culife.model.MissionDetailModel;
import hk.com.csci4140.culife.model.PublisherModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by zhenghao(Kelvin Zheng) on 04/04/2018.
 */

public class MissionDetailFragment extends BaseFragment {

    private static final String TAG = "MissionDetailFrag";

    @BindView(R.id.card_mission_icon)
    ImageView mMissionIcon;

    @BindView(R.id.card_mission_title)
    TextView mMissionTitle;

    @BindView(R.id.card_mission_salary)
    TextView mMissionSalary;

    @BindView(R.id.card_mission_like_icon)
    ImageView mSmallLikeIcon;

    @BindView(R.id.card_mission_elapsed_time)
    TextView mMissionElapsedTime;

    @BindView(R.id.card_mission_location)
    TextView mMissionLocation;

    @BindView(R.id.card_mission_date)
    TextView mMissionDate;

    @BindView(R.id.card_mission_video_photo_container)
    LinearLayout mMissionPhotoVideoContainer;

    @BindView(R.id.card_mission_support_video)
    TextView mMissionVideo;

    @BindView(R.id.card_mission_support_photo)
    TextView mMissionPhoto;

    @BindView(R.id.card_mission_disclose_media)
    TextView mMissionDiscloseMedia;

    @BindView(R.id.mission_detail_mission_date)
    TextView mMissionDateLarge;

    @BindView(R.id.mission_detail_two_time)
    TextView mMissionTimePeriod;

    @BindView(R.id.mission_detail_content)
    TextView mMissionContent;

    @BindView(R.id.mission_detail_requirement)
    TextView mMissionRequirement;

    @BindView(R.id.mission_detail_others)
    TextView mMissionOthers;

    @BindView(R.id.mission_detail_publisher_icon)
    ImageView mGiverIcon;

    @BindView(R.id.mission_detail_publisher_name)
    TextView mGiverName;

    @BindView(R.id.mission_detail_publisher_evaluate_rating_bar)
    RatingBar mGiverRatingBar;

    @BindView(R.id.mission_detail_publisher_joined_time)
    TextView mGiverJoinedTime;

    @BindView(R.id.mission_detail_publisher_recruit_num)
    TextView mGiverTotalMissionNum;

    @BindView(R.id.mission_detail_publisher_like_icon)
    ImageView mGiverLikeIcon;

    @BindView(R.id.mission_detail_message_button)
    FancyButton messageButton;

    @BindView(R.id.mission_detail_apply_button)
    FancyButton applyButton;


    private String mTitle;
    private String mPrevTitle;

    private int missionId = -1;

    private Menu fragmentMenu;
    private boolean isLike;
    private boolean discloseMedia;

    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = getString(R.string.mission_detail_title);
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

        inflater.inflate(R.menu.mission_detail_menu, menu);

        fragmentMenu = menu;
    }


    //Menu Item On Click
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_like:
                //If user click like
                //If user has not login
                if(UserModel.token.equals("")){
                    showBottomSnackBar(getString(R.string.should_login));
                }
                else {
                    //Call api to add/remove fav
                    ObserverOnNextListener<StandardModel> observer = new ObserverOnNextListener<StandardModel>() {
                        @Override
                        public void onNext(StandardModel model) {
                            if (model.getStatus().equals(Constant.CONNECT_SUCCESS)) {
                                //Set the icon status
                                if (isLike) {
                                    isLike = false;
                                    item.setIcon(R.drawable.ic_star_unlike);
                                } else {
                                    isLike = true;
                                    item.setIcon(R.drawable.ic_star_like);
                                }
                            } else if (model.getStatus().equals(Constant.CONNECT_FAILED)) {
                                showBottomSnackBar(getString(R.string.network_connect_errors));
                                Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                            }
                        }
                    };
                    HttpMethod.getInstance().addMissionToFav(new ProgressObserver<StandardModel>(getContext(), observer), UserModel.token, missionId);
                }
                break;
            case R.id.action_settings:
                //if user click setting
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title(R.string.mission_detail_setting_title)
                        .customView(R.layout.item_mission_detail_setting, true)
                        .positiveText(R.string.success_confirm)
                        .build();
                SwitchButton switchButton = (SwitchButton) dialog.findViewById(R.id.mission_detail_setting_switch_button);
                if(discloseMedia){
                    switchButton.setCheckedNoEvent(true);
                }
                else {
                    switchButton.setCheckedNoEvent(false);
                }
                switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //TODO: I don't know what I can do
                        showBottomSnackBar("Do something here");
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_mission_detail, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();


        try{
            missionId = getArguments().getInt(Constant.MISSION_DETAIL_MISSION_ID);
        }catch (Exception e){
            showBottomSnackBar(getString(R.string.network_connect_errors));
            Log.e(TAG, e.toString());
        }

        if(missionId != -1) {
            getMissionDetailApi(missionId);
        }
    }


    //Call Api to get mission detail
    private void getMissionDetailApi(int missionId){
        ObserverOnNextListener<GetMissionDetailModel> observer = new ObserverOnNextListener<GetMissionDetailModel>() {
            @Override
            public void onNext(GetMissionDetailModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){

                    updateUI(model.getResult());
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        if(UserModel.token.equals("")){
            HttpMethod.getInstance().getMissionDetail(new ProgressObserver<GetMissionDetailModel>(getContext(), observer), missionId, UserModel.region, Utility.getLanguageId(getContext()));
        }
        else {
            HttpMethod.getInstance().getMissionDetail(new ProgressObserver<GetMissionDetailModel>(getContext(), observer), UserModel.token, missionId, UserModel.region, Utility.getLanguageId(getContext()));
        }

    }


    //Update UI
    private void updateUI(GetMissionDetailModel.Result result){
        MissionDetailModel missionDetail = result.getMissionDetail();
        PublisherModel giverInfo = result.getPublisherInfo();

        //Check if is fav
        if(missionDetail.getIsFav().equals(Constant.TRUE)){
            isLike = true;
            fragmentMenu.findItem(R.id.action_like).setIcon(R.drawable.ic_star_like);
        }
        else {
            isLike = false;
            fragmentMenu.findItem(R.id.action_like).setIcon(R.drawable.ic_star_unlike);
        }

        //Mission Icon
        if(missionDetail.getIcon() != null && !missionDetail.getIcon().equals("")){
            Glide.with(getContext()).load(missionDetail.getId()).into(mMissionIcon);
        }

        //Mission Title
        if(missionDetail.getTitle() != null){
            mMissionTitle.setText(missionDetail.getTitle());
        }

        //Salary
        if(missionDetail.getSalary() != null){
            float salary = Float.parseFloat(missionDetail.getSalary());
            String salaryFormat = String.format(getString(R.string.post_mission_two_fee_format), salary);
            mMissionSalary.setText(salaryFormat);
        }

        //Pass Date and like icon
        String passFormat = String.format(getString(R.string.mission_detail_pass_date_format), missionDetail.getPassedDate());
        mMissionElapsedTime.setText(passFormat);
        mSmallLikeIcon.setVisibility(View.GONE);

        //Address
        if(missionDetail.getAddress() != null && !missionDetail.getAddress().equals("")){
            mMissionLocation.setText(missionDetail.getAddress());
        }
        else {
            mMissionLocation.setText(getString(R.string.mission_detail_no_address));
        }

        //Date
        if(missionDetail.getNoSpecificDate() == 0){
            mMissionDate.setText(missionDetail.getDate());
            mMissionDateLarge.setText(missionDetail.getDate());
        }
        else {
            mMissionDate.setText(getString(R.string.mission_detail_no_date));
            mMissionDateLarge.setText(getString(R.string.mission_detail_no_date));
        }


        //Video, photo, media
        int numDismiss = 0;
        if(missionDetail.getNeedVideo() == 0){
            mMissionVideo.setVisibility(View.GONE);
            numDismiss ++;
        }
        if(missionDetail.getNeedPhoto() == 0){
            mMissionPhoto.setVisibility(View.GONE);
            numDismiss ++;
        }
        if(missionDetail.getPublicMedia() == 0){
            discloseMedia = false;
            mMissionDiscloseMedia.setVisibility(View.GONE);
            numDismiss ++;
        }
        else {
            discloseMedia = true;
        }

        if(numDismiss == 3){
            mMissionPhotoVideoContainer.setVisibility(View.GONE);
        }

        //Time Period
        if( (missionDetail.getStartTime() != null && !missionDetail.getStartTime().equals(""))
            &&
                (missionDetail.getEndTime() != null && !missionDetail.getEndTime().equals(""))){
            String startTime = missionDetail.getStartTime().substring(0,5);
            String endTime = missionDetail.getEndTime().substring(0, 5);
            String timeFormat = String.format(getString(R.string.mission_detail_time_format),
                    startTime, endTime);
            mMissionTimePeriod.setText(timeFormat);
        }
        else {
            mMissionTimePeriod.setText(getString(R.string.default_value));
        }

        //Content
        if(missionDetail.getContent() != null && !missionDetail.getContent().equals("")){
            mMissionContent.setText(missionDetail.getContent());
        }
        else {
            mMissionContent.setText(getString(R.string.mission_detail_no_content));
        }

        //Requirement
        if(missionDetail.getRequirement() != null && !missionDetail.getRequirement().equals("")){
            mMissionRequirement.setText(missionDetail.getRequirement());
        }
        else {
            mMissionRequirement.setText(getString(R.string.mission_detail_no_requirement));
        }

        //Others
        if(missionDetail.getNeedFace() == 1){
            mMissionOthers.setText(getString(R.string.mission_detail_need_interview));
        }
        else {
            mMissionOthers.setText(getString(R.string.mission_detail_no_others));
        }

        //Giver Icon
        if(giverInfo.getIconLink() != null && !giverInfo.getIconLink().equals("")){
            Glide.with(getContext()).load(giverInfo.getIconLink()).into(mGiverIcon);
        }

        //Name
        if(giverInfo.getName() != null && !giverInfo.getName().equals("")){
            mGiverName.setText(giverInfo.getName());
        }

        //Rating
        if(giverInfo.getRate() >= 0){
            float rate = (float)giverInfo.getRate();
            mGiverRatingBar.setRating(rate);
        }

        //Joined Time
        if(giverInfo.getJoinedDays() != null && !giverInfo.getJoinedDays().equals("")){
            String joinedFormat = String.format(getString(R.string.mission_detail_publisher_joined_time_format), giverInfo.getJoinedDays());
            mGiverJoinedTime.setText(joinedFormat);
        }

        //Total Mission
        if(giverInfo.getTotalMission() >= 0){
            String totalFormat = String.format(getString(R.string.mission_detail_publisher_recruit_num_format), giverInfo.getTotalMission());
            mGiverTotalMissionNum.setText(totalFormat);
        }

        //Message and Apply button visibility
        if(result.getIsOwner() == 1){
            messageButton.setVisibility(View.GONE);
            applyButton.setVisibility(View.GONE);
        }

    }


    @OnClick(R.id.mission_detail_message_button)
    void onClickMessage(){
        //TODO: message function
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText("Still need to implemented")
                .setConfirmText(getString(R.string.warning_confirm))
                .show();
    }


    @OnClick(R.id.mission_detail_apply_button)
    void onClickApply(){
        //TODO: apply function
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText("Still need to implemented")
                .setConfirmText(getString(R.string.warning_confirm))
                .show();
    }


    @OnClick(R.id.mission_detail_publisher_like_icon)
    void onClickGiverLike(){
        //TODO: I don't know what is this used for?
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText("Still need to implemented")
                .setConfirmText(getString(R.string.warning_confirm))
                .show();
    }

}
