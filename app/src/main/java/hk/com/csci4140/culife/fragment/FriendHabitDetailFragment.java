package hk.com.csci4140.culife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.FriendHabitDetailModel;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.model.memberProfileModel;
import io.reactivex.annotations.Nullable;

public class FriendHabitDetailFragment extends BaseFragment {



    private static final String TAG = "FriHabitDetail";

    public JSONObject member_profiles;

    CatLoadingView mCatLoadingView;

    public int dummyHabitID;
    private String mHabitName;
    private String mHabitContent;

    public ArrayList<FriendHabitDetailModel> dummyHabitList;

    private String mTitle;
    private String mPrevTitle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        //setGoBackIcon();
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the previous navigation bar selected item
                //getBottomNav().setCurrentItem(((MainActivity) getActivity()).getPreviousItem());
                //super.onBackPressed();
                getFragmentManager().popBackStack();
            }
        });

        //Log.d(TAG,"dummydata"+dummyHabitID);
        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(true);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }

    // 顶上的banner ; the top banner
    @BindView(R.id.habit_detail_logo_imageView)
    ImageView mLogoImageView;

    @Nullable
    @BindView(R.id.supervisor_logo)
    ImageView mSupervisorView;
    @Nullable
    @BindView(R.id.parteners_logo)
    ImageView mPartenersView;

    @BindView(R.id.habit_detail_background_banner)
    ImageView mBackgroundBannerImageView;

    @BindView(R.id.habit_detail_recyclerView)
    RecyclerView mBottomRecyclerView;


    // 四个圆形的图标 ; the 4 circle icon
    @BindView(R.id.habit_detail_calendar_icon)
    ImageView mCalendarIcon;

    @BindView(R.id.habit_detail_trophy_icon)
    ImageView mTrophyIcon;

    @BindView(R.id.habit_detail_setting_icon)
    ImageView mSettingIcon;

    @BindView(R.id.habit_detail_share_icon)
    ImageView mShareIcon;


    // 2个tab : the 2 tabs
    @BindView(R.id.habit_detail_selection_tab)
    TabLayout mTabLayout;


    // the scrollview
    @BindView(R.id.habit_detail_scrollView)
    ScrollView mScrollView;

    // the floating button
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ArrayList<Map<String, String>> mSourceData = new ArrayList<Map<String, String>>();

    FriendHabitDetailModel mHabit;

    ArrayList<memberProfileModel> memberProfilelist = new ArrayList<memberProfileModel>();

    // habit detail confirm complete button
    Button mConfirmCompleteBtn;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("habit is complete")
                    .setContentText("congradulation on finishing a new task")
                    .setConfirmText(getString(R.string.warning_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if(mConfirmCompleteBtn.getText().toString().equalsIgnoreCase("CONFIRM")){
                                mConfirmCompleteBtn.setText("CANSEL");
                                mConfirmCompleteBtn.setBackgroundColor(getResources().getColor(R.color.greyDim));
                                JSONObject jsonParams = new JSONObject();
                                JSONObject outerJsonParams = new JSONObject();
                                try {
                                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                    String date = sDateFormat.format(new java.util.Date());
                                    jsonParams.put("habitid", dummyHabitID);
                                    jsonParams.put("body",date);

                                    outerJsonParams.put("check", jsonParams);
                                    StringEntity entity = new StringEntity(outerJsonParams.toString());
                                    //Log.d(TAG,"bodyentity"+jsonParams);
                                    updateCheck(entity);
                                }
                                catch (JSONException e) {
                                    Log.i("august", e.toString());
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    Log.i("august", e.toString());
                                    e.printStackTrace();
                                }
                            }
//                        mConfirmCompleteBtn.setText("确认完成");
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    };



    // before the page is shown to the suer
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


        //Set the toolbar title of this fragment
        mTitle = "CULife";
        setToolbarTitle(mTitle);

        initialSetting();

//        initialSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_habit_detail, container, false);
        ButterKnife.bind(this, mView);



        try{
            String tempIconLink = "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg";//TODO: Change to data
            fab.attachToRecyclerView(mBottomRecyclerView);
//            fab.attachToScrollView((ObservableScrollView) mScrollView);
            Glide.with(getContext()).load(tempIconLink).into(mLogoImageView);
            // Blurry.with(getContext()).capture(mView).into(mBackgroundBannerImageView);
        }catch (Exception e){
            Log.i("august", e.toString());

        }


        recyclerViewShowMemberList(0);


        try{
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition()==0){
                        recyclerViewShowMemberList(0);
                    }else{
                        recyclerViewShowMemberList(1);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }catch (Exception e){
            Log.i("august", e.toString());

        }


        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    // Calendar
    @OnClick(R.id.habit_detail_calendar_icon)
    void showCalendar(){
        if(dummyHabitList.get(0).isFavorited){
            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String date = sDateFormat.format(new java.util.Date());
                jsonParams.put("habitid", dummyHabitID);
                outerJsonParams.put("check", jsonParams);
                Log.d(TAG,"habitid"+jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                callAPItoGetDate(entity);
            }
            catch (JSONException e) {
                Log.i("august", e.toString());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                Log.i("august", e.toString());
                e.printStackTrace();
            }
        }
        else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Error")
                    .setContentText("You should first register this task")
                    .setConfirmText(getString(R.string.warning_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }

    }

    Calendar getCalendarObjectFromString(String date_str){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        try {
            date = formatter.parse(date_str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            Log.i("august", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    // TODO : give the real habitModel to the fragment
    @OnClick(R.id.habit_detail_setting_icon)
    void navigateToSetting(){
        PostMissionStepOneFragment editFragment = new PostMissionStepOneFragment();
        mHabit = new FriendHabitDetailModel();
        mHabit.ID = 28;
        mHabit.name = "我给你一个假的";
        mHabit.description = "假的description";
//        editFragment.initEditMode(mHabit);
        replaceFragment(editFragment, null);
    }

    @OnClick(R.id.habit_detail_trophy_icon)
    void showRank(){
        Log.d(TAG,"still need to do");//TODO: need to do rank
    }

    @OnClick(R.id.habit_detail_share_icon)
    void showFriendList(){
        try{
            FriendListFragment destFragment = new FriendListFragment();
            destFragment.mNumberOfItems = 5;
            replaceFragment(destFragment, null);
        }catch (Exception e){
            Log.i("august", e.toString());
            Log.d(TAG, "showFriendList: "+e);
        }
    }

    void recyclerViewCancelScroll(){
//        mBottomRecyclerView.setNestedScrollingEnabled(false);
    }

    void recyclerViewShowMemberList(int flag){
        //String identity;
        //Bundle message_bundle = getIntent().getExtras();
        //identity = message_bundle.getString("message");
        //Log.d(TAG, "Testmessage: "+ identity);
        try{
            if(flag==0){
                mBottomRecyclerView.setHasFixedSize(false);
                mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                final ArrayList<FriendHabitDetailModel> items = new ArrayList<FriendHabitDetailModel>();
                Log.d(TAG,"yo"+dummyHabitList);
                for (FriendHabitDetailModel HM:dummyHabitList){
                    if (HM.ID == dummyHabitID){
                        FriendHabitDetailModel item = HM;
                        items.add(item);
                    }
                }
                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<FriendHabitDetailModel>(getContext(), R.layout.item_habit_detail, items) {
                            @Override
                            public void convert(ViewHolder holder, FriendHabitDetailModel s, int pos) {
                                //Log.d(TAG,"latestname"+s.name);
                                holder.setText(R.id.title, s.name);
                                holder.setText(R.id.complete_number,"31" + " times");
                                holder.setText(R.id.content, s.description);
                                holder.setText(R.id.time_slot, s.startTime +" to "+s.endTime);

                                //holder.setText(R.id.GPS_contraint, "GPS not done here");
                                //holder.setText(R.id.GPS_auto_complete, "GPS allowed or not(not done here)");
                                //holder.setText(R.id.habit_parteners, "partner");
                                holder.setText(R.id.habit_supervisor, s.owner);
                                //Glide.with(getContext()).load(s.userImage).into(mSupervisorView);

                                JSONArray member_list = new JSONArray();
                                if (member_profiles != null) {
                                    try {
                                        member_list = member_profiles.getJSONArray("profiles");
                                    } catch (JSONException e) {
                                        Log.i("august", e.toString());
                                        e.printStackTrace();
                                    }

                                    for (Integer i = 0; i < member_list.length(); i++) {
                                        memberProfileModel memberProfile = new memberProfileModel();
                                        JSONObject jso = new JSONObject();
                                        try {
                                            jso = member_list.getJSONObject(i);
                                        } catch (JSONException e) {
                                            Log.i("august", e.toString());
                                            e.printStackTrace();
                                        }
                                        memberProfile.initMemberProfileWithJSON(jso);
                                        memberProfilelist.add(memberProfile);
                                    }
                                }

                                if (!memberProfilelist.isEmpty()) {
                                    memberProfileModel element_member = memberProfilelist.get(0);
                                    //Glide.with(getContext()).load(element_member.image).into(mPartenersView);
                                    holder.setText(R.id.partner_complete_info, element_member.username + " and other partners");
                                }
                                else{
                                    holder.setText(R.id.partner_complete_info, "No one has joined!");
                                }

                                if(s.isFavorited){
                                    holder.setText(R.id.confirm_complete, "Cancel");
                                    holder.getView(R.id.confirm_complete).setBackgroundColor(getResources().getColor(R.color.greyDim));
                                    JSONObject jsonParams0 = new JSONObject();
                                    JSONObject outerJsonParams0 = new JSONObject();
                                    try {
                                        jsonParams0.put("habitid", dummyHabitID);
                                        outerJsonParams0.put("check", jsonParams0);
                                        StringEntity entity0 = new StringEntity(outerJsonParams0.toString());
                                        callAPItoCounttime(entity0,holder,s);
                                    }
                                    catch (JSONException e) {
                                        Log.i("august", e.toString());
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                        Log.i("august", e.toString());
                                        e.printStackTrace();
                                    }

                                    ((Button)holder.getView(R.id.confirm_complete)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            taskDeRegisterAPI(dummyHabitID);
                                        }
                                    });
                                }
                                else {
                                    holder.setText(R.id.confirm_complete, "Register");
                                    holder.getView(R.id.confirm_complete).setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
                                    holder.getView(R.id.complete_number_title).setVisibility(View.GONE);
                                    holder.getView(R.id.complete_number).setVisibility(View.GONE);

                                    ((Button)holder.getView(R.id.confirm_complete)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            taskRegisterAPI(dummyHabitID);
                                        }
                                    });
                                }


                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                ButterKnife.bind(this,itemView);
                                mConfirmCompleteBtn = itemView.findViewById(R.id.confirm_complete);
                                itemView.findViewById(R.id.confirm_complete).setOnClickListener(mOnClickListener);
                            }
                        });
            }else{
                memberProfilelist.clear();
                JSONObject jsonParams = new JSONObject();
                JSONObject outerJsonParams = new JSONObject();
                try {
                    jsonParams.put("habitid", dummyHabitID);
                    outerJsonParams.put("habit", jsonParams);
                    StringEntity entity = new StringEntity(outerJsonParams.toString());
                    //Log.d(TAG,"bodyentity"+jsonParams);
                    callAPItoGetMember(entity);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){

            Log.d(TAG, "onCreateView: fail: "+e);
        }
        recyclerViewCancelScroll();
    }


    public void callAPItoGetMember(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        //mCatLoadingView = new CatLoadingView();

        //mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/author",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                //mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: member_response: "+response);


                member_profiles = response;
                Log.d(TAG,"Members: "+member_profiles);
                JSONArray member_list = new JSONArray();
                if (member_profiles != null) {
                    try {
                        member_list = member_profiles.getJSONArray("profiles");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (Integer i = 0; i < member_list.length(); i++) {
                        memberProfileModel memberProfile = new memberProfileModel();
                        JSONObject jso = new JSONObject();
                        try {
                            jso = member_list.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        memberProfile.initMemberProfileWithJSON(jso);
                        memberProfilelist.add(memberProfile);
                    }
                }
                mBottomRecyclerView.setHasFixedSize(false);
                mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // TODO : get the total number from API result
                int totalNumber;
                if (memberProfilelist != null) {

                    totalNumber = memberProfilelist.size();
                }
                else{
                    totalNumber = 0;
                }

                // TODO : need to add the userIDField
                mSourceData = new ArrayList<Map<String, String>>();
                Map <String,String> map =  new HashMap<String,String>();

                final String name_key_1 = "column1_username";
                final String name_key_2 = "column2_username";
                final String name_key_3 = "column3_username";
                final String name_key_4 = "column4_username";
                final String icon_key_1 = "column1_icon_link";
                final String icon_key_2 = "column2_icon_link";
                final String icon_key_3 = "column3_icon_link";
                final String icon_key_4 = "column4_icon_link";


                for (int i = 0; i < totalNumber; i++) {
                    int column = (i+1)%4;

                    // TODO : the id is also needed
                    // TODO : use the result from API
                    String userName = memberProfilelist.get(i).username;
                    String iconLink = memberProfilelist.get(i).image;

                    if(column == 1){
                        map.put(name_key_1,userName);
                        map.put(icon_key_1,iconLink);
                    }else if(column == 2){
                        map.put(name_key_2,userName);
                        map.put(icon_key_2,iconLink);
                    }else if(column == 3){
                        map.put(name_key_3,userName);
                        map.put(icon_key_3,iconLink);
                    }else{
                        map.put(name_key_4,userName);
                        map.put(icon_key_4,iconLink);
                        mSourceData.add(map);
                        map = new HashMap<String,String>();
                    }

                    if(column!=0 && i==totalNumber-1){
                        mSourceData.add(map);
                    }
                }

//                Log.d(TAG, "recyclerViewShowMemberList: Array! : "+mSourceData);


                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<Map<String, String>>(getContext(), R.layout.item_habit_detail_member_list_row, mSourceData) {
                            @Override
                            public void convert(ViewHolder holder, Map m, int pos) {
                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_1)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, (String)m.get(name_key_1));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_2)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, (String)m.get(name_key_2));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_3)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, (String)m.get(name_key_3));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_4)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_4));
                                    holder.setText(R.id.item_habit_detail_member_list_name_4, (String)m.get(name_key_4));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_4));
                                    holder.setText(R.id.item_habit_detail_member_list_name_4, "");
                                }

                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                //AutoUtil.autoSize(itemView)
                            }
                        });
                //replaceFragment(habitDetailFragment,null);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        client.post(getContext(), Constant.API_BASE_URL+"habits/members",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                //mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: member_response: "+response);


                member_profiles = response;
                Log.d(TAG,"Members: "+member_profiles);
                JSONArray member_list = new JSONArray();
                if (member_profiles != null) {
                    try {
                        member_list = member_profiles.getJSONArray("profiles");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (Integer i = 0; i < member_list.length(); i++) {
                        memberProfileModel memberProfile = new memberProfileModel();
                        JSONObject jso = new JSONObject();
                        try {
                            jso = member_list.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        memberProfile.initMemberProfileWithJSON(jso);
                        memberProfilelist.add(memberProfile);
                    }
                }

                mBottomRecyclerView.setHasFixedSize(false);
                mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // TODO : get the total number from API result
                int totalNumber;
                if (member_profiles != null) {
                    //totalNumber = member_profiles.length();
                    totalNumber = memberProfilelist.size();
                }
                else{
                    totalNumber = 0;
                }

                // TODO : need to add the userIDField
                mSourceData = new ArrayList<Map<String, String>>();
                Map <String,String> map =  new HashMap<String,String>();

                final String name_key_1 = "column1_username";
                final String name_key_2 = "column2_username";
                final String name_key_3 = "column3_username";
                final String name_key_4 = "column4_username";
                final String icon_key_1 = "column1_icon_link";
                final String icon_key_2 = "column2_icon_link";
                final String icon_key_3 = "column3_icon_link";
                final String icon_key_4 = "column4_icon_link";


                for (int i = 0; i < totalNumber; i++) {
                    int column = (i+1)%4;

                    // TODO : the id is also needed
                    // TODO : use the result from API
                    String userName = memberProfilelist.get(i).username;
                    String iconLink = memberProfilelist.get(i).image;

                    if(column == 1){
                        map.put(name_key_1,userName);
                        map.put(icon_key_1,iconLink);
                    }else if(column == 2){
                        map.put(name_key_2,userName);
                        map.put(icon_key_2,iconLink);
                    }else if(column == 3){
                        map.put(name_key_3,userName);
                        map.put(icon_key_3,iconLink);
                    }else{
                        map.put(name_key_4,userName);
                        map.put(icon_key_4,iconLink);
                        mSourceData.add(map);
                        map = new HashMap<String,String>();
                    }

                    if(column!=0 && i==totalNumber-1){
                        mSourceData.add(map);
                    }
                }
              

//                Log.d(TAG, "recyclerViewShowMemberList: Array! : "+mSourceData);


                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<Map<String, String>>(getContext(), R.layout.item_habit_detail_member_list_row, mSourceData) {
                            @Override
                            public void convert(ViewHolder holder, Map m, int pos) {
                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_1)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, (String)m.get(name_key_1));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_2)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, (String)m.get(name_key_2));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_3)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, (String)m.get(name_key_3));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, "");
                                }

                                try{
                                    Glide.with(getContext()).load((String)m.get(icon_key_4)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_4));
                                    holder.setText(R.id.item_habit_detail_member_list_name_4, (String)m.get(name_key_4));
                                }catch (Exception e){
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_4));
                                    holder.setText(R.id.item_habit_detail_member_list_name_4, "");
                                }

                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                //AutoUtil.autoSize(itemView)
                            }
                        });

                //replaceFragment(habitDetailFragment,null);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }

    // the page is dismissed
    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the title of previous fragment
//        setToolbarTitle(mPrevTitle);
    }

    public class CustomLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    public void updateCheck(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.put(getContext(), Constant.API_BASE_URL+"habits/check_update",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: json_response: "+response);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }

    public void callAPItoGetDate(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/check_calendar",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                JSONArray date_list = new JSONArray();
                try {
                    date_list = response.getJSONArray("check_date");
                }
                catch (JSONException e) {
                    Log.i("august", e.toString());
                    e.printStackTrace();
                }
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2);

                Calendar[] days;
                List<Calendar> blockedDays = new ArrayList<>();

                for (Integer i=0;i < date_list.length();i++) {
                    String date_string = new String();
                    try {
                        date_string = date_list.getString(i);
                    }
                    catch (JSONException e) {
                        Log.i("august", e.toString());
                        e.printStackTrace();
                    }

                    //Log.d(TAG,"date_string"+date_string);
                    blockedDays.add(getCalendarObjectFromString(date_string));
                }
                days = blockedDays.toArray(new Calendar[blockedDays.size()]);


                Calendar[] today;
                Calendar cal = Calendar.getInstance();
                List<Calendar> selectedDays = new ArrayList<>();
                selectedDays.add(cal);
                today = selectedDays.toArray(new Calendar[selectedDays.size()]);
                dpd.setSelectableDays(today);
                dpd.setHighlightedDays(days);
                dpd.show(getActivity().getFragmentManager(),"Datepickerdialog");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }

    public void callAPItoCounttime(StringEntity params,final ViewHolder holder, FriendHabitDetailModel s){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/check_calendar",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                JSONArray date_list = new JSONArray();
                try {
                    date_list = response.getJSONArray("check_date");
                }
                catch (JSONException e) {
                    Log.i("august", e.toString());
                    e.printStackTrace();
                }
                holder.setText(R.id.complete_number,(date_list.length()-1)+" times" );

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }

    private void taskRegisterAPI(final int id){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            outerJsonParams.put("habit",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.post(getContext(), Constant.API_BASE_URL + "habits/favorite", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);


                    createCalendarAPI(id);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void taskDeRegisterAPI(final int id){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            outerJsonParams.put("habit",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.delete(getContext(), Constant.API_BASE_URL + "habits/favorite", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);


                    dummyHabitList.get(0).isFavorited = Boolean.FALSE;


                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("DeRegister success!")
                            .setContentText("congratulation on registering a new task")
                            .setConfirmText(getString(R.string.warning_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    recyclerViewShowMemberList(0);
                                }
                            })
                            .show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void createCalendarAPI(int id){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            jsonParams.put("body", "");
            jsonParams.put("score", 0);
            outerJsonParams.put("check",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.post(getContext(), Constant.API_BASE_URL + "habits/check_create", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);

                    dummyHabitList.get(0).isFavorited = Boolean.TRUE;


                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Register success!")
                            .setContentText("congratulation on registering a new task")
                            .setConfirmText(getString(R.string.warning_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    recyclerViewShowMemberList(0);
                                }
                            })
                            .show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
