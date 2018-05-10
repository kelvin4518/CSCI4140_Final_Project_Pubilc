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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.nanchen.wavesidebar.SearchEditText;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.ProfileSettingAdapter;
import hk.com.csci4140.culife.model.UserContactModel;
import hk.com.csci4140.culife.utility.Utility;
import jp.wasabeef.blurry.Blurry;

import com.bumptech.glide.Glide;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;




public class FriendListFragment extends BaseFragment {


    private static final String TAG = "FriendListFrag";

    private String mHabitName;
    private String mHabitContent;


    private String mTitle;
    private String mPrevTitle;




    @BindView(R.id.main_side_bar)
    WaveSideBarView mWaveSideBarView;

    @BindView(R.id.main_search)
    SearchEditText mSearchEditText;

    @BindView(R.id.friend_list_recyclerView)
    RecyclerView mRecyclerView;
    

    private List<UserContactModel> mUserContactModels;











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







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, mView);

        preparationWork();
        putDataIntoRecylerView();

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






    
    
    void putDataIntoRecylerView(){

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= 20; i++) {
            items.add("Member " + i);
        }
        mRecyclerView.setAdapter(
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
        
    }

    void preparationWork(){
        // 使用右侧的首字母栏进行检索
        // when using the side bar to navigate
        mWaveSideBarView = getActivity().findViewById(R.id.main_side_bar);
        mWaveSideBarView.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i=0; i<mUserContactModels.size(); i++) {
                    if (mUserContactModels.get(i).getIndex().equals(letter)) {
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        // 使用搜索按钮进行搜索
        // search by top bar
        mSearchEditText = getActivity().findViewById(R.id.main_search);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //You can do something in there
//                mShowModels.clear();
//                for (UserContactModel model : mUserContactModels) {
//                    String str = Trans2PinYinUtil.trans2PinYin(model.getName());
//                    if (str.contains(s.toString())|| model.getName().contains(s.toString())) {
//                        mShowModels.add(model);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
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



}
