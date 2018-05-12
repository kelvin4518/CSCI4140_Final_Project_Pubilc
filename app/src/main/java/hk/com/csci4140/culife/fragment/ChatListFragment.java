package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.model.ChatListItemModel;
import hk.com.csci4140.culife.model.InstantMessageModel;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class ChatListFragment extends BaseFragment {


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
        InstantMessageModel message = new InstantMessageModel("my message","my username");
        // child里面定义的string，是告诉database，这个东西存在database的哪个地方
        mDatabaseReference.child("messages&michael_firebasechat_1&michael_firebasechat_2").push().setValue(message);
        mDatabaseReference.addChildEventListener(mChildEventListener);
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


    public void setSourceData(ArrayList<ChatListItemModel> sourceData) {
        mSourceData = sourceData;
    }

    void putDataToRecylerView(){
        // fake something for now
        for (int i = 1;i<=5;i++){
            ChatListItemModel chatListItemModel = new ChatListItemModel();
            chatListItemModel.iconURL = "https://avatars0.githubusercontent.com/u/9919?s=280&v=4";
            chatListItemModel.chattingToTitle = "Faker";
            chatListItemModel.lastChatTime = "星期五";
            chatListItemModel.lastChatMessage = "dummy message";
            chatListItemModel.correspondingChatDatabaseName = "messages&michael_firebasechat_1&michael_firebasechat_2";
            mSourceData.add(chatListItemModel);
        }



        // iterate the mSourceData
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(
                new CommonAdapter<ChatListItemModel>(getContext(), R.layout.item_chat_list, mSourceData) {
                    @Override
                    public void convert(ViewHolder holder, ChatListItemModel eachSourceData, int pos) {
                        Glide.with(getContext()).load(eachSourceData.iconURL).
                                into((ImageView) holder.itemView.findViewById(R.id.item_chat_list_chat_image));
                        holder.setText(R.id.item_chat_list_chat_title, eachSourceData.chattingToTitle);
                        holder.setText(R.id.item_chat_list_last_message_time, eachSourceData.lastChatTime);
                        holder.setText(R.id.item_chat_list_last_chat_message, eachSourceData.lastChatMessage);
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
            ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
            ChatListItemModel model = mSourceData.get(position);
            chatDetailFragment.mDatabaseName = model.correspondingChatDatabaseName;
            replaceFragment(chatDetailFragment,null);
        }
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
            mTitle = getString(R.string.user_profile_fragment_title);
        }
        if (mPrevTitle == null) {
            mPrevTitle = getPrevTitle();
        }

        mTitle = "习惯标题";
        setToolbarTitle(mTitle);

        initialSetting();
        initialDatabaseReferenceObject();
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
        super.onResume();
    }


















    // the page is dismissed
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
