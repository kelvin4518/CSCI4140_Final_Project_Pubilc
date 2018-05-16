package hk.com.csci4140.culife.fragment;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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


import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.BaseActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.ProfileSettingAdapter;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.memberProfileModel;
import hk.com.csci4140.culife.model.HomeFragmentModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.Utility;
import io.reactivex.annotations.Nullable;
import jp.wasabeef.blurry.Blurry;

import com.bumptech.glide.Glide;


public class HabitDetailFragment extends BaseFragment{


    private static final String TAG = "HabitDetaiFrag";

    public JSONObject member_profiles;

    CatLoadingView mCatLoadingView;

    public int dummyHabitID;
    private String mHabitName;
    private String mHabitContent;

    public ArrayList<HabitModel> dummyHabitList;

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

    HabitModel mHabit;

    ArrayList<memberProfileModel> memberProfilelist = new ArrayList<memberProfileModel>();

    // habit detail confirm complete button
    Button mConfirmCompleteBtn;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(mConfirmCompleteBtn.getText().toString().equalsIgnoreCase("CONFIRM")){
                mConfirmCompleteBtn.setText("CANCEL");
                mConfirmCompleteBtn.setBackgroundColor(getResources().getColor(R.color.greyDim));
            }
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("habit is complete")
                    .setContentText("Congradulation on finishing a new task! Do you want to write a diary?")
                    .setConfirmText(getString(R.string.warning_confirm))
                    .setCancelText("Cancel")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Context mContext = null;
                            PostDiaryFragment diary = new PostDiaryFragment();
                            diary.habbitIDfordiary = dummyHabitID;
                            replaceFragment(diary,null);
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        mConfirmCompleteBtn.setText("确认完成");
                            sweetAlertDialog.dismissWithAnimation();
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
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    @OnClick(R.id.habit_detail_setting_icon)
    void navigateToSetting(){
        ShowBlogFragment showblog = new ShowBlogFragment();
        showblog.blog_habit_id = dummyHabitID;
        replaceFragment(showblog, null);
    }

    @OnClick(R.id.habit_detail_trophy_icon)
    void showRank(){
        memberProfilelist.clear();
        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", dummyHabitID);
            outerJsonParams.put("rank", jsonParams);
            Log.d(TAG,"habitid"+jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());
            callAPItoGetRank(entity);
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.habit_detail_share_icon)
    void showFriendList(){
        try{
            FriendListFragment destFragment = new FriendListFragment();
            destFragment.mNumberOfItems = 5;
            replaceFragment(destFragment, null);
        }catch (Exception e){
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

                final ArrayList<HabitModel> items = new ArrayList<HabitModel>();
                Log.d(TAG,"yo"+dummyHabitList);
                //Log.d(TAG,"yo"+member_profiles);
                for (HabitModel HM:dummyHabitList){
                    if (HM.ID == dummyHabitID){
                        HabitModel item = HM;
                        items.add(item);
                    }
                }
                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<HabitModel>(getContext(), R.layout.item_habit_detail, items) {
                            @Override
                            public void convert(ViewHolder holder, HabitModel s, int pos) {
                                //Log.d(TAG,"latestname"+s.name);
                                holder.setText(R.id.title, s.name);
                                //holder.setText(R.id.complete_number,"31" + " times");
                                holder.setText(R.id.content, s.description);
                                holder.setText(R.id.time_slot, s.startTime +" to "+s.endTime);
                                //holder.setText(R.id.GPS_contraint, "GPS not done here");
                                //holder.setText(R.id.GPS_auto_complete, "GPS allowed or not(not done here)");
                                //holder.setText(R.id.habit_parteners, "partner");
                                holder.setText(R.id.habit_supervisor, s.owner);

                                JSONObject jsonParams0 = new JSONObject();
                                JSONObject outerJsonParams0 = new JSONObject();
                                try {
                                    jsonParams0.put("habitid", dummyHabitID);
                                    outerJsonParams0.put("check", jsonParams0);
                                    StringEntity entity0 = new StringEntity(outerJsonParams0.toString());
                                    callAPItoCounttime(entity0,holder,s);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                ButterKnife.bind(this,itemView);
                                mConfirmCompleteBtn = itemView.findViewById(R.id.confirm_complete);
                                itemView.findViewById(R.id.confirm_complete).setOnClickListener(mOnClickListener);
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
                                    //checkifChecked(entity,mConfirmCompleteBtn);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
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

    public void callAPItoGetRank(StringEntity params) {
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);
        //mCatLoadingView = new CatLoadingView();

        //mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL + "habits/ranks", params, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                //mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : " + statusCode);
                Log.d("API_REPORT", "onSuccess: rank_members: " + response);


                member_profiles = response;
                Log.d(TAG, "Members: " + member_profiles);
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
                } else {
                    totalNumber = 0;
                }

                // TODO : need to add the userIDField
                mSourceData = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();

                final String name_key_1 = "column1_username";
                final String name_key_2 = "column2_username";
                final String name_key_3 = "column3_username";
                final String name_key_4 = "column4_username";
                final String icon_key_1 = "column1_icon_link";
                final String icon_key_2 = "column2_icon_link";
                final String icon_key_3 = "column3_icon_link";
                final String icon_key_4 = "column4_icon_link";


                for (int i = 0; i < totalNumber; i++) {
                    int column = (i + 1) % 4;

                    // TODO : the id is also needed
                    // TODO : use the result from API
                    String userName = memberProfilelist.get(i).username;
                    String iconLink = memberProfilelist.get(i).image;

                    if (column == 1) {
                        map.put(name_key_1, userName);
                        map.put(icon_key_1, iconLink);
                    } else if (column == 2) {
                        map.put(name_key_2, userName);
                        map.put(icon_key_2, iconLink);
                    } else if (column == 3) {
                        map.put(name_key_3, userName);
                        map.put(icon_key_3, iconLink);
                    } else {
                        map.put(name_key_4, userName);
                        map.put(icon_key_4, iconLink);
                        mSourceData.add(map);
                        map = new HashMap<String, String>();
                    }

                    if (column != 0 && i == totalNumber - 1) {
                        mSourceData.add(map);
                    }
                }

//                Log.d(TAG, "recyclerViewShowMemberList: Array! : "+mSourceData);


                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<Map<String, String>>(getContext(), R.layout.item_habit_detail_member_list_row, mSourceData) {
                            @Override
                            public void convert(ViewHolder holder, Map m, int pos) {
                                try {
                                    Glide.with(getContext()).load((String) m.get(icon_key_1)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, (String) m.get(name_key_1));
                                } catch (Exception e) {
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_1));
                                    holder.setText(R.id.item_habit_detail_member_list_name_1, "");
                                }

                                try {
                                    Glide.with(getContext()).load((String) m.get(icon_key_2)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, (String) m.get(name_key_2));
                                } catch (Exception e) {
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_2));
                                    holder.setText(R.id.item_habit_detail_member_list_name_2, "");
                                }

                                try {
                                    Glide.with(getContext()).load((String) m.get(icon_key_3)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, (String) m.get(name_key_3));
                                } catch (Exception e) {
                                    Glide.with(getContext()).load("").
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_3));
                                    holder.setText(R.id.item_habit_detail_member_list_name_3, "");
                                }

                                try {
                                    Glide.with(getContext()).load((String) m.get(icon_key_4)).
                                            into((ImageView) holder.itemView.findViewById(R.id.item_habit_detail_member_list_icon_4));
                                    holder.setText(R.id.item_habit_detail_member_list_name_4, (String) m.get(name_key_4));
                                } catch (Exception e) {
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
                Log.d("API_REPORT", "onFailure: status : " + statusCode);
                Log.d("API_REPORT", "onFailure: response : " + response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
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

        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/check_calendar",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: date_response: "+response);
                JSONArray date_list = new JSONArray();
                try {
                    date_list = response.getJSONArray("check_date");
                }
                catch (JSONException e) {
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
                Boolean flag = false;
                for (Integer i=1;i < date_list.length();i++) {
                    String date_string = new String();
                    try {
                        date_string = date_list.getString(i);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.d(TAG,"date_string"+date_string);
                    blockedDays.add(getCalendarObjectFromString(date_string));
                    flag = true;
                }
                if (flag) {
                    days = blockedDays.toArray(new Calendar[blockedDays.size()]);


                    Calendar[] today;
                    Calendar cal = Calendar.getInstance();
                    List<Calendar> selectedDays = new ArrayList<>();
                    selectedDays.add(cal);
                    today = selectedDays.toArray(new Calendar[selectedDays.size()]);
                    dpd.setSelectableDays(today);
                    dpd.setHighlightedDays(days);
                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                }
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
                Boolean flag = false;
                for (Integer i=1;i < date_list.length();i++) {
                    String date_string = new String();
                    try {
                        date_string = date_list.getString(i);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.d(TAG,"date_string"+date_string);
                    blockedDays.add(getCalendarObjectFromString(date_string));
                    flag = true;
                }
                if (flag) {
                    days = blockedDays.toArray(new Calendar[blockedDays.size()]);


                    Calendar[] today;
                    Calendar cal = Calendar.getInstance();
                    List<Calendar> selectedDays = new ArrayList<>();
                    selectedDays.add(cal);
                    today = selectedDays.toArray(new Calendar[selectedDays.size()]);
                    dpd.setSelectableDays(today);
                    dpd.setHighlightedDays(days);
                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                }
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

    public void callAPItoCounttime(StringEntity params,final ViewHolder holder, HabitModel s){
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


}
