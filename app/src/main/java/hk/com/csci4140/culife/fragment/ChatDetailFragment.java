package hk.com.csci4140.culife.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import hk.com.csci4140.culife.model.UserModel;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class ChatDetailFragment extends BaseFragment {


    public String mUserName = UserModel.myChatName;
    public String mDatabaseName;
    public String mOtherUserID;
    public DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList = new ArrayList<DataSnapshot>();



    private static final String TAG = "ChatDetailFragment";


    private String mTitle;
    private String mPrevTitle;

    @BindView(R.id.chat_detail_message_reclyerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.chat_detail_message_input)
    EditText mEditText;

    @OnClick(R.id.chat_detail_sendButton)
    void sendMessage(){
        // get the user input and send
        String input = mEditText.getText().toString();
        if (!input.equals("")) {
            InstantMessageModel chat = new InstantMessageModel(input, mUserName);
            mDatabaseReference.push().setValue(chat);
            mEditText.setText("");
        }
    }


    private ArrayList<InstantMessageModel> mSourceData = new ArrayList<InstantMessageModel>();






















    private ChildEventListener mChildEventListener = new ChildEventListener() {
        // 当有一个新的item出现时，东西都存在dataSnapshot（JSON）
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            try{
                mSourceData.add(dataSnapshot.getValue(InstantMessageModel.class));
            }catch (Exception e){
                showBottomSnackBar("here is the problem");
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mSourceData.size()-1);
            //mRecyclerView.smoothScrollToPosition(mSourceData.size()-1);
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




    void putDataToRecylerView(){

        // iterate the mSourceData
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        int myID = Integer.valueOf(UserModel.myID);
        int otherID = Integer.valueOf(mOtherUserID);

        if(myID>otherID){
            mDatabaseName = "CHAT&"+String.valueOf(myID-otherID)+"&"+String.valueOf(myID+otherID);
            Log.d(TAG, "mDatabaseName: "+mDatabaseName);
        }else{
            mDatabaseName = "CHAT&"+String.valueOf(otherID-myID)+"&"+String.valueOf(myID+otherID);
            Log.d(TAG, "mDatabaseName: "+mDatabaseName);
        }


        Log.d(TAG, "putDataToRecylerView: the chatroom child database name is : "+mDatabaseName);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mDatabaseName);
        mDatabaseReference.addChildEventListener(mChildEventListener);

        mRecyclerView.setAdapter(
                new CommonAdapter<InstantMessageModel>(getContext(), R.layout.chat_message_row, mSourceData) {
                    @Override
                    public void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, InstantMessageModel eachSourceData, int pos) {
                        holder.setText(R.id.chat_message_author,eachSourceData.getAuthor());
                        holder.setText(R.id.chat_message,eachSourceData.getMessage());
                        if(eachSourceData.getAuthor().equals(mUserName)){
                            // TODO : beautify the layout
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.END;
                            holder.itemView.findViewById(R.id.chat_message_author).setLayoutParams(params);
                            holder.itemView.findViewById(R.id.chat_message).setLayoutParams(params);
                        }else{
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.START;
                            holder.itemView.findViewById(R.id.chat_message_author).setLayoutParams(params);
                            holder.itemView.findViewById(R.id.chat_message).setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onViewHolderCreated(com.zhy.adapter.recyclerview.base.ViewHolder holder, View itemView) {
                        super.onViewHolderCreated(holder, itemView);
                    }
                });
    }





























    //Initial Setting of every fragment
    private void initialSetting() {
        setGoBackIcon();

        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_chat_detail, container, false);
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

    @Override
    public void onPause(){
        try {
            UserModel.markReadForID(getContext(),mOtherUserID);
        }catch (Exception e){
        }

        super.onPause();
    }
















    // the page is dismissed
    @Override
    public void onDestroy(){
        mEditText.setText("");
        super.onDestroy();
    }
}
