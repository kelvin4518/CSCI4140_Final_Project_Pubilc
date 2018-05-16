package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.model.ChatListItemModel;
import hk.com.csci4140.culife.model.InstantMessageModel;
import hk.com.csci4140.culife.model.UserModel;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class ChatListFragment extends BaseFragment {



    private CatLoadingView mCatLoadingView;


    private static final String TAG = "ChatListFragment";

    private DatabaseReference mDatabaseReference;

    private String mTitle;
    private String mPrevTitle;





    ArrayList<ChatListItemModel> mSourceData = new ArrayList<ChatListItemModel>();

    @BindView(R.id.chat_list_recyclerView)
    RecyclerView mRecyclerView;



















    @OnClick(R.id.chat_frag_fake_send_button)
    void sendMessage(){
        showBottomSnackBar("the text is sent");
//        InstantMessageModel message = new InstantMessageModel("my message","my username");
//        // child里面定义的string，是告诉database，这个东西存在database的哪个地方
//        mDatabaseReference.child("messages&michael_firebasechat_1&michael_firebasechat_2").push().setValue(message);
//        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private ArrayList<DataSnapshot> mSnapshotList = new ArrayList<DataSnapshot>();
    private ChildEventListener mChildEventListener = new ChildEventListener() {
        // 当有一个新的item出现时，东西都存在dataSnapshot（JSON）
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            // tell the recyclerview to the change of data
            Log.d(TAG, "onChildAdded: the new message"+mSnapshotList);
            showBottomSnackBar("new text arrived");
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public void setSourceData() {
        mSourceData = new ArrayList<ChatListItemModel>();
        // fake something for now


        for (int i = 0; i<= UserModel.myChatList.length(); i++){

            try{
                JSONObject object = UserModel.myChatList.getJSONObject(i);

                ChatListItemModel chatListItemModel = new ChatListItemModel();
                chatListItemModel.otherUserID = object.getString(Constant.USER_CHAT_LIST_OTHER_USER_ID);
                chatListItemModel.iconURL = object.getString(Constant.USER_CHAT_LIST_ICON_LINK);
                chatListItemModel.chattingToTitle = object.getString(Constant.USER_CHAT_LIST_NAME);
                chatListItemModel.lastChatTime = object.getString(Constant.USER_CHAT_LIST_LAST_DATE);
                chatListItemModel.lastChatMessage = object.getString(Constant.USER_CHAT_LIST_LAST_MESSAGE);
                chatListItemModel.correspondingChatDatabaseName = chatListItemModel.otherUserID;

                try {
                    chatListItemModel.isNotRead = object.getString(Constant.USER_CHAT_IS_NOT_READ);
                }catch (Exception e){
                    chatListItemModel.isNotRead = "false";
                }


                Log.d(TAG, "setSourceData: databaseName: "+chatListItemModel.correspondingChatDatabaseName);
                mSourceData.add(chatListItemModel);
            }catch (Exception e){

            }

        }
    }

    public void putDataToRecylerView(){

        // iterate the mSourceData
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG, "putDataToRecylerView: !!!RecyclerViewIsRecivingData!!! : "+mSourceData);

        mRecyclerView.setAdapter(
                new CommonAdapter<ChatListItemModel>(getContext(), R.layout.item_chat_list, mSourceData) {
                    @Override
                    public void convert(ViewHolder holder, ChatListItemModel eachSourceData, int pos) {
                        Glide.with(getContext()).load(eachSourceData.iconURL).
                                into((ImageView) holder.itemView.findViewById(R.id.item_chat_list_chat_image));
                        holder.setText(R.id.item_chat_list_chat_title, eachSourceData.chattingToTitle);
                        holder.setText(R.id.item_chat_list_last_message_time, eachSourceData.lastChatTime);
                        holder.setText(R.id.item_chat_list_last_chat_message, eachSourceData.lastChatMessage);
                        if(eachSourceData.isNotRead.equals("true")){
                            holder.setVisible(R.id.item_chat_list_is_not_read,true);
                        }else{
                            holder.setVisible(R.id.item_chat_list_is_not_read,false);
                        }
                        holder.itemView.setOnClickListener(new ChatListFragment.MyClickListener(pos));
                    }

                    @Override
                    public void onViewHolderCreated(ViewHolder holder, View itemView) {
                        super.onViewHolderCreated(holder, itemView);
                    }
                });
        
    }

    public class MyClickListener implements View.OnClickListener{
        int position;
        public MyClickListener(int my_position){
            this.position = my_position;
        }
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: chatlistfragment!!");
            try {
                ChatListItemModel chatListItemModel = mSourceData.get(position);
                UserModel.markReadForID(getContext(),chatListItemModel.otherUserID);
            }catch (Exception e){
            }



            ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
            ChatListItemModel model = mSourceData.get(position);
            chatDetailFragment.mOtherUserID = model.otherUserID;
            replaceFragment(chatDetailFragment,null);
        }
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

        inflater.inflate(R.menu.chat_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setPrevTitle(mTitle);
        switch (item.getItemId()){
            case R.id.show_friend_list_icon:
                callGetListAPIAndJumpToFriendList();
                break;
            default:
                break;
        }
        return true;
    }

    private void callGetListAPIAndJumpToFriendList(){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.get(getContext(),
                Constant.API_BASE_URL+"profiles/followees",
                null,
                ContentType.APPLICATION_JSON.getMimeType(),
                new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: getList");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);

                FriendListFragment friendListFragment = new FriendListFragment();
                friendListFragment.fragmentMode = Constant.FRIEND_LIST_FRAGMENT_START_CHAT_MODE;
                friendListFragment.initFriendListFragmentFromJSON(response);
                replaceFragment(friendListFragment,null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: getList");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
    }












    //Initial Setting of every fragment
    private void initialSetting() {
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

    private void initialDatabaseReferenceObject(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // before the page is shown to the user
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

        mTitle = "CULife";
        setToolbarTitle(mTitle);

        initialSetting();
        initialDatabaseReferenceObject();
        setSourceData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, mView);

        putDataToRecylerView();

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }

    @Override
    public void onResume(){
        // TODO : notify the change here
        // mRecyclerView.getAdapter().notifyDataSetChanged();
        try{
            setSourceData();
            putDataToRecylerView();
        }catch (Exception e){

        }
        super.onResume();
    }





















    // the page is dismissed
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
