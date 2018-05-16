package hk.com.csci4140.culife.fragment;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.goyourfly.multiple.adapter.MultipleAdapter;
import com.goyourfly.multiple.adapter.MultipleSelect;
import com.goyourfly.multiple.adapter.menu.MenuBar;
import com.goyourfly.multiple.adapter.viewholder.DecorateFactory;
import com.goyourfly.multiple.adapter.viewholder.view.CustomViewFactory;
import com.goyourfly.multiple.adapter.viewholder.view.RadioBtnFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nanchen.wavesidebar.SearchEditText;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;
import com.nanchen.wavesidebar.WaveSideBarView.OnSelectIndexItemListener;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.ProfileSettingAdapter;
import hk.com.csci4140.culife.model.ChatListItemModel;
import hk.com.csci4140.culife.model.OtherUserModel;
import hk.com.csci4140.culife.model.UserContactModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;
import jp.wasabeef.blurry.Blurry;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;




public class FriendListFragment extends BaseFragment {


    private CatLoadingView mCatLoadingView;

    public String fragmentMode;
    /**
     * Explain for fragmentMode: see the constant page for detail
     * */
    private boolean hasLoadedOnce;
    private boolean hasLoadedOnce_myFollower;
    private boolean hasLoadedOnce_iAmFollowing;
    /**
     * Explain for hasLoadedOnce:
     *
     * if hasn't load friend list from API, then load it
     * if already loaded friend list, then don't load
     *
     * */

    private ArrayList<OtherUserModel> mSourceData = new ArrayList<OtherUserModel>();
    private ArrayList<OtherUserModel> mSourceData_myFollower = new ArrayList<OtherUserModel>();
    private ArrayList<OtherUserModel> mSourceData_iAmFollowing = new ArrayList<OtherUserModel>();

    public int mNumberOfItems;
    /**
     *
     * */

    public void initFriendListFragmentFromJSON(JSONObject response){
        if(fragmentMode == Constant.FRIEND_LIST_FRAGMENT_START_CHAT_MODE){
            try{
                JSONArray jsonArray = response.getJSONArray("profiles");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject eachUser = jsonArray.getJSONObject(i);
                    OtherUserModel otherUserModel = new OtherUserModel();
                    otherUserModel.id = String.valueOf(eachUser.getInt("id"));
                    otherUserModel.username = eachUser.getString("username");
                    otherUserModel.iconLink = eachUser.getString("image");
                    otherUserModel.selfBio = eachUser.getString("bio");
                    mSourceData.add(otherUserModel);
                }
            }catch (Exception e){

            }
        }
    }
























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
//        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
//        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Go to the previous navigation bar selected item
//                getBottomNav().setCurrentItem(((MainActivity) getActivity()).getPreviousItem());
//            }
//        });
//
//
//        //Set Login Icon Invisible
//        setLoginIconVisible(false);
//
//        //Set the bottom navigation visibility
//        setBottomNavFragment(true);
//        setPrevBottomNavFragment(true);
//
//        //Use to set the menu icon
//        setHasOptionsMenu(true);



        //Set the Go Back
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
    }





    // before the page is shown to the suer
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = "CULife";
        }
        if (mPrevTitle == null) {
            mPrevTitle = getPrevTitle();
        }
//        setToolbarTitle(mTitle);
//
//        setPrevTitle("习惯名称");

        //Set the toolbar title of this fragment
        mTitle = "CULife";
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


        mRecyclerView.setAdapter(
                new CommonAdapter<OtherUserModel>(getContext(), R.layout.item_friend_list_with_selection, mSourceData) {
                    @Override
                    public void convert(ViewHolder holder, OtherUserModel s, int pos) {
                        String iconLink = s.iconLink;
//                        Glide.with(getContext()).
                        Glide.with(getContext()).
                                load(iconLink).
                                into((ImageView) holder.itemView.findViewById(R.id.item_friend_select_logo));
                        holder.setText(R.id.item_friend_select_name, s.username);
                        holder.itemView.setOnClickListener(new FriendListFragment.MyClickListener(pos));
                    }

                    @Override
                    public void onViewHolderCreated(ViewHolder holder, View itemView) {
                        super.onViewHolderCreated(holder, itemView);
                        //AutoUtil.autoSize(itemView)
                    }
                });

        RadioBtnFactory btnFactory = new RadioBtnFactory(R.color.red_btn_bg_color,3, Gravity.LEFT);

        MultipleAdapter adapter = MultipleSelect
                .with(getActivity())
                .adapter(mRecyclerView.getAdapter())
                .decorateFactory(btnFactory)
                .build();

        mRecyclerView.setAdapter(adapter);
    }

    public class MyClickListener implements View.OnClickListener{
        int position;
        public MyClickListener(int my_position){
            this.position = my_position;
        }
        @Override
        public void onClick(View v) {
            if(fragmentMode==Constant.FRIEND_LIST_FRAGMENT_START_CHAT_MODE){
                OtherUserModel userSelected = mSourceData.get(position);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);
                try {
                    JSONObject object = new JSONObject();
                    object.put(Constant.USER_CHAT_LIST_OTHER_USER_ID,userSelected.id);
                    object.put(Constant.USER_CHAT_LIST_ICON_LINK,userSelected.iconLink);
                    object.put(Constant.USER_CHAT_LIST_NAME,userSelected.username);
                    object.put(Constant.USER_CHAT_LIST_LAST_MESSAGE,"");
                    object.put(Constant.USER_CHAT_LIST_LAST_DATE,reportDate);
                    UserModel.addNewChatToChatList(getContext(),object);


                    // TODO : Should let ChatListFragment to call the replace fragment, so that when exit the chat interface, it go back to ChatListFragmet
                    ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
                    // chatDetailFragment.mDatabaseName = userSelected.id;
                    chatDetailFragment.mOtherUserID = userSelected.id;
                    replaceFragment(chatDetailFragment,null);
                }catch (Exception e){

                }

            }else if(fragmentMode==Constant.FRIEND_LIST_FRAGMENT_INVITE_MODE){

            }else{

            }
        }
    }



    void preparationWork(){
        // 使用右侧的首字母栏进行检索
        // when using the side bar to navigate
        mWaveSideBarView.setOnSelectIndexItemListener(new OnSelectIndexItemListener() {
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
