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

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.ProfileSettingAdapter;
import hk.com.csci4140.culife.utility.Utility;
import jp.wasabeef.blurry.Blurry;

import com.bumptech.glide.Glide;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;



public class HabitDetailFragment extends BaseFragment{


    private static final String TAG = "HabitDetaiFrag";

    private String mHabitName;
    private String mHabitContent;


    private String mTitle;
    private String mPrevTitle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        // setGoBackIcon();
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the previous navigation bar selected item
                getBottomNav().setCurrentItem(((MainActivity) getActivity()).getPreviousItem());
            }
        });


        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(true);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }


    @BindView(R.id.habit_detail_logo_imageView)
    ImageView mLogoImageView;

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


    @BindView(R.id.habit_detail_scrollView)
    ScrollView mScrollView;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    ArrayList<Map<String, String>> mSourceData = new ArrayList<Map<String, String>>();






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
//        setToolbarTitle(mTitle);
//
//        setPrevTitle("习惯名称");

        //Set the toolbar title of this fragment
        mTitle = "习惯标题";
        setToolbarTitle(mTitle);

        initialSetting();

//        initialSetting();
    }





//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        //In case the duplicate menu item
//        try {
//            for(int i = 0; i < menu.size(); i ++){
//                menu.getItem(i).setVisible(false);
//            }
//        }catch (Exception e){
//            Log.e(TAG, "onCreateOptionsMenu: " + e.toString());
//        }
//
//        inflater.inflate(R.menu.user_profile_menu, menu);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_habit_detail, container, false);
        ButterKnife.bind(this, mView);



        try{
            String tempIconLink = "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg";
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







    // User interacting with the page

    @BindView(R.id.confirm_complete)
    Button confirmBtn;

    @OnClick(R.id.confirm_complete)
    void onClickConfirmComplete(){
        //Show the warning text
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("habit is complete")
                .setContentText("congradulation on finishing a new task")
                .setConfirmText(getString(R.string.warning_confirm))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(confirmBtn.getText().toString().equalsIgnoreCase("确认完成")){
                            confirmBtn.setText("取消");
                            confirmBtn.setBackgroundColor(getResources().getColor(R.color.greyDim));
                        } else if(confirmBtn.getText().toString().equalsIgnoreCase("取消")){
                            confirmBtn.setText("确认完成");
                            confirmBtn.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
                        }
//                        confirmBtn.setText("确认完成");
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    @OnClick(R.id.habit_detail_calendar_icon)
    void showCalendar(){

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

        blockedDays.add(getCalendarObjectFromString("05/23/2018"));
        blockedDays.add(getCalendarObjectFromString("05/24/2018"));
        blockedDays.add(getCalendarObjectFromString("05/25/2018"));
        blockedDays.add(getCalendarObjectFromString("05/21/2018"));
        blockedDays.add(getCalendarObjectFromString("05/26/2018"));
        blockedDays.add(getCalendarObjectFromString("05/27/2018"));
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

    Calendar getCalendarObjectFromString(String date_str){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
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


    @OnClick(R.id.habit_detail_share_icon)
    void showFriendList(){
        try{
            replaceFragment(new FriendListFragment(), null);
        }catch (Exception e){
            Log.d(TAG, "showFriendList: "+e);
        }
    }



    void recyclerViewCancelScroll(){
//        mBottomRecyclerView.setNestedScrollingEnabled(false);
    }

    void recyclerViewShowMemberList(int flag){
        try{
            if(flag==0){
                mBottomRecyclerView.setHasFixedSize(false);
                mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            mBottomRecyclerView.setLayoutManager(new CustomLayoutManager(getContext()){
//                @Override
//                public boolean canScrollVertically() {
//                    return false;
//                }
//            });
                mBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                ArrayList<String> items = new ArrayList<String>();
                for (int i = 1; i <= 20; i++) {
                    items.add("Detail " + i);
                }
                mBottomRecyclerView.setAdapter(
                        new CommonAdapter<String>(getContext(), R.layout.simple_list_item_1, items) {
                            @Override
                            public void convert(ViewHolder holder, String s, int pos) {
                                holder.setText(R.id.text_1, s);
//                            super.convert( holder,  s, pos);
                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                //AutoUtil.autoSize(itemView)
                            }
                        });
            }else{
                mBottomRecyclerView.setHasFixedSize(false);
                mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // TODO : get the total number from API result
                int totalNumber = 22;

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
                final String id_key_1 = "";
                final String id_key_2 = "";
                final String id_key_3 = "";
                final String id_key_4 = "";


                for (int i = 0; i < totalNumber; i++) {
                    int column = (i+1)%4;

                    // TODO : the id is also needed
                    // TODO : use the result from API
                    String userName = "Michael";
                    String iconLink = "https://i.ytimg.com/vi/SfLV8hD7zX4/maxresdefault.jpg";

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
            }
        }catch (Exception e){
            Log.d(TAG, "onCreateView: fail: "+e);
        }
        recyclerViewCancelScroll();
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

}
