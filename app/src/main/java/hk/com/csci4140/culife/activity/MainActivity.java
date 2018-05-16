package hk.com.csci4140.culife.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.app.DialogFragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
//import com.roger.catloadinglibrary.CatLoading;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.fragment.ChatListFragment;
import hk.com.csci4140.culife.fragment.FriendListFragment;
import hk.com.csci4140.culife.fragment.DatePickerFragment;
import hk.com.csci4140.culife.fragment.FriendMomentFragment;
import hk.com.csci4140.culife.fragment.HabitDetailFragment;
import hk.com.csci4140.culife.fragment.HomeFragment;
import hk.com.csci4140.culife.fragment.PolicyFragment;
import hk.com.csci4140.culife.fragment.PostMissionStepOneFragment;
import hk.com.csci4140.culife.fragment.UserProfileFragment;
import hk.com.csci4140.culife.model.ChatListItemModel;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.InstantMessageModel;
import hk.com.csci4140.culife.model.OtherUserModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;
import io.reactivex.annotations.NonNull;


import com.roger.catloadinglibrary.CatLoadingView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 31/04/2018.
 */


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private boolean hasInitNav = false;

    private int previousItem = 0;

    private CatLoadingView mCatLoadingView;










    private ChatListFragment mChatListFragment = new ChatListFragment();
    /**
     * 每次进行API请求，将无论follower还是followee都放在这个里面
     * */
    private ArrayList<OtherUserModel> mSourceOtherUserModel = new ArrayList<OtherUserModel>();
    private Integer mNeedToSearchID;
    private String mNewDialogNewMessage = "";


















    public void updateChatListFragment(){
        mChatListFragment.setSourceData();
        if(mChatListFragment.isVisible()){
            mChatListFragment.putDataToRecylerView();
        }
    }



    /**
     * when a new dialog is going to be displayed
     *
     * */

    private void searchThisUser(){
        /**
         * Target id : mNeedToSearchID
         * */

        for(int i=0;i<mSourceOtherUserModel.size();i++){
            OtherUserModel otherUserModel = mSourceOtherUserModel.get(i);
            if(otherUserModel.id.equals(String.valueOf(mNeedToSearchID))){
                try{
                    JSONObject object = new JSONObject();
                    object.put(Constant.USER_CHAT_LIST_OTHER_USER_ID,otherUserModel.id);
                    object.put(Constant.USER_CHAT_LIST_ICON_LINK,otherUserModel.iconLink);
                    object.put(Constant.USER_CHAT_LIST_NAME,otherUserModel.username);
                    object.put(Constant.USER_CHAT_LIST_LAST_MESSAGE,mNewDialogNewMessage);
                    object.put(Constant.USER_CHAT_LIST_LAST_DATE,otherUserModel);

                    String theIDOfUserThatIsChattingTo = SessionManager.getString(MainActivity.this,Constant.USER_CHATTING_DATABASE);
                    if(!theIDOfUserThatIsChattingTo.equals(mNeedToSearchID)){
                        object.put(Constant.USER_CHAT_IS_NOT_READ,"true");
                    }
                    UserModel.addNewChatToChatList(MainActivity.this,object);
                    updateChatListFragment();
                }catch (Exception e){

                }
            }
        }

    }

    private void addFollowerOrFolloweeToList(JSONObject response){
        try{
            JSONArray jsonArray = response.getJSONArray("profiles");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject eachUser = jsonArray.getJSONObject(i);
                OtherUserModel otherUserModel = new OtherUserModel();
                otherUserModel.id = String.valueOf(eachUser.getInt("id"));
                otherUserModel.username = eachUser.getString("username");
                otherUserModel.iconLink = eachUser.getString("image");
                otherUserModel.selfBio = eachUser.getString("bio");
                mSourceOtherUserModel.add(otherUserModel);
            }
        }catch (Exception e){

        }
    }

    private void callFollowerAPI(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);

        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);

        client.get(MainActivity.this,
                Constant.API_BASE_URL+"profiles/followers",
                null,
                ContentType.APPLICATION_JSON.getMimeType(),
                new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("API_REPORT", "onSuccess: follower getList");
                        Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                        Log.d("API_REPORT", "onSuccess: response: "+response);
                        addFollowerOrFolloweeToList(response);
                        searchThisUser();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        Log.d("API_REPORT", "onFailure: follower getList");
                        Log.d("API_REPORT", "onFailure: status : "+statusCode);
                        Log.d("API_REPORT", "onFailure: response : "+response);
                        searchThisUser();
                    }
                });
    }

    private void callFolloweeAPI(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);

        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);

        client.get(MainActivity.this,
                Constant.API_BASE_URL+"profiles/followees",
                null,
                ContentType.APPLICATION_JSON.getMimeType(),
                new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("API_REPORT", "onSuccess: followee getList");
                        Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                        Log.d("API_REPORT", "onSuccess: response: "+response);
                        addFollowerOrFolloweeToList(response);
                        callFollowerAPI();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        Log.d("API_REPORT", "onFailure: followee getList");
                        Log.d("API_REPORT", "onFailure: status : "+statusCode);
                        Log.d("API_REPORT", "onFailure: response : "+response);
                        callFollowerAPI();
                    }
                });
    }

    public void searchForUserAndCreateDialog(int otherUserID){
        mNeedToSearchID = otherUserID;
        callFolloweeAPI();
    }


    // 设置所有需要监听的database
    public ArrayList<DatabaseReference> DataBaseReferenceList = new ArrayList<DatabaseReference>();
    // 设置所有

    public class myChildEventListener implements ChildEventListener{

        int id;
        public myChildEventListener(int otherUserID){
            this.id = otherUserID;
        }

        // 当有一个新的item出现时，东西都存在dataSnapshot（JSON）
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: a new message is added by user : "+id);

            String theIDOfUserThatIsChattingTo = SessionManager.getString(MainActivity.this,Constant.USER_CHATTING_DATABASE);
//            if(theIDOfUserThatIsChattingTo)



            boolean isOldDialogChange = UserModel.isMovingExistingChatToTop(MainActivity.this,id,dataSnapshot.getValue(InstantMessageModel.class).getMessage());

            if(!isOldDialogChange){
                /**
                 * do API request and get user, then call the update
                 *
                 * 1. call get followee
                 * 2. call get follower
                 * 3. search for user information
                 * 4. add this object to the SessionManager
                 * 5. updateChatListFragment();
                 *
                 * */

                mNewDialogNewMessage = dataSnapshot.getValue(InstantMessageModel.class).getMessage();
                searchForUserAndCreateDialog(this.id);
            }else{
                updateChatListFragment();
            }
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
    }




    public void initializeDatabaseAndListener(){
        // set enough database reference
        for (int i=1;i<=40;i++){

            String databaseNameThatNeedToListen;

            int myID = Integer.valueOf(UserModel.myID);
            int otherID = Integer.valueOf(i);

            if(myID>otherID){
                databaseNameThatNeedToListen = "CHAT&"+String.valueOf(myID-otherID)+"&"+String.valueOf(myID+otherID);
                Log.d(TAG, "mDatabaseName: "+databaseNameThatNeedToListen);
            }else{
                databaseNameThatNeedToListen = "CHAT&"+String.valueOf(otherID-myID)+"&"+String.valueOf(myID+otherID);
                Log.d(TAG, "mDatabaseName: "+databaseNameThatNeedToListen);
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(databaseNameThatNeedToListen);
            databaseReference.addChildEventListener(new myChildEventListener(i));
            Log.d(TAG, "database reference for "+databaseNameThatNeedToListen+" is created");

//                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mDatabaseName);
//            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


















    // before user see the page, set which fragment is going to be displayed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Set the app language
        Utility.setLanguage(MainActivity.this);

        ButterKnife.bind(this);

        Log.d(TAG, "onCreate: user token : "+UserModel.token);

//        if(UserModel.isLogin){
//            if(UserModel.myID!=null && UserModel.myID!="" && UserModel.myID!="0"){
//                initializeDatabaseAndListener();
//            }
//        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationTracking();

    }

    @Override
    public void onStart() {
        super.onStart();
        //MainActivity needs tool bar
        setToolbarVisibility(true);

        //If the bottom navigation hasn't been initialized, we should initialize it
        if(!hasInitNav) {
            setBottomNav();

            //Check if the user has login, if not, show the policy fragment
            Fragment initFragment;
                    //UserModel.isLogin ? new HomeFragment() : new PolicyFragment();
            // michael added, to disable the terms and conditions page when starting the app


            initFragment = new HomeFragment();

            boolean hasBottomNav = UserModel.isLogin;
            //Set if the fragment has bottom navigation bar
            setBottomNavFragment(hasBottomNav);
            setFragment(initFragment, null);
            JSONObject jsonParams = new JSONObject();
//            callGetHabitListAPI();
            if(UserModel.myID == null || UserModel.myID == ""){
                callGetProfileAPI();
            }else{
                callGetHabitListAPI();
            }
        }
    }



    private void callGetProfileAPI(){

        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
//

        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        client.get(this,Constant.API_BASE_URL+"profiles",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("API_REPORT", "onSuccess: get profile");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);

                try {
                    JSONObject responseObject = response.getJSONObject("profile");
                    String id = responseObject.getString("id");
                    SessionManager.putString(MainActivity.this,Constant.USERID,id);
                    UserModel.initModel(MainActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callGetHabitListAPI();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("API_REPORT", "onFailure: get profile");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);

                callGetHabitListAPI();
            }
        });
    }



    private void callGetHabitListAPI(){

        /**
         *
         * I put it here to ensure that the listener is always created
         *
         * */
        if(UserModel.isLogin){
            if(UserModel.myID!=null && UserModel.myID!="" && UserModel.myID!="0"){
                initializeDatabaseAndListener();
            }
        }





        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
//

        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        client.get(this,Constant.API_BASE_URL+"habits/created",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("API_REPORT", "onSuccess: get habit list");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);
                showBottomSnackBar("Success get habit list");
                HomeFragment destFragment = new HomeFragment();
                //destFragment.justPassTheValue(response);
                destFragment.initHomePageDetail(response);
                setFragment(destFragment, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("API_REPORT", "onFailure: get habit list");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar("Fail get habit list");
                HomeFragment destFragment = new HomeFragment();
                setFragment(destFragment, null);
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        // newFragment.show(getSupportFragmentManager(), "datePicker");
        newFragment.show(this.getFragmentManager(), "datePicker");
    }
    // Bottom Navigation Bar

    //Set the bottom navigation bar
    private void setBottomNav(){
        hasInitNav = true;
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_home, R.drawable.ic_menu_camera, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_range, R.drawable.ic_menu_send, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_add, R.drawable.ic_action_add, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_message, R.drawable.ic_menu_gallery, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.nav_other_mission, R.drawable.ic_menu_manage, R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Disable thpre-initializede translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

        // Set listeners
        // TODO : rewrite the check login logic
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                setTabSelectFragment(position);
                return true;

            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

    }

    //Set the Tab selection
    private void setTabSelectFragment(int position){
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new HomeFragment();
                fragment = new FriendMomentFragment();
                // fragment = mChatListFragment;
                break;
            case 2:
                //fragment = mChatListFragment;
                //fragment = new ();
                fragment = new PostMissionStepOneFragment();
                break;
            case 3:
                // fragment = new PostMissionStepOneFragment();
                fragment = mChatListFragment;
                break;

            case 4:
                fragment = new UserProfileFragment();

                break;
            default:
                fragment = null;
                break;
        }
        setFragment(fragment, null);
    }












    //Get the previous selected item in bottom navigation bar
    public int getPreviousItem() {
        return previousItem;
    }




























    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mLocation;

    public void startLocationTracking(){
        createLocationRequest();
        try {
            checkLocationSetting();
        }catch (Exception e){
            Log.d("Location", "error: "+e);
        }
        startLocationUpdates();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000*60*10);
//        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setInterval(500000);
        mLocationRequest.setFastestInterval(500000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void checkLocationSetting(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d("Location Info", "LocationSettingsResponse is OK");
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            protected static final int REQUEST_CHECK_SETTINGS = 0x1;
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Location Info", "LocationSettingsResponse is Failure");
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                    }
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

        }else{
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        // Log.d("Location Info", "onLocationResult: "+location);
                        JSONObject object = new JSONObject();
                        try {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            Date today = Calendar.getInstance().getTime();
                            String reportDate = df.format(today);


                            if(!SessionManager.getString(MainActivity.this,Constant.GPS_RECORDING_DATE).equals(reportDate)){
                                SessionManager.putString(MainActivity.this,Constant.GPS_RECORDING_DATE,reportDate);

                                // 记录到了不同的天数了，需要处理信息
                                ArrayList<String> gpsList = new ArrayList<String>();
                                SessionManager.putArrayList(gpsList,Constant.USER_GPS_INFO);
                            }

                            object.put(Constant.GPS_LAT,String.valueOf(location.getLatitude()));
                            object.put(Constant.GPS_LNG,String.valueOf(location.getLongitude()));
                            ArrayList<String> gpsList = SessionManager.getArrayList(Constant.USER_GPS_INFO);
                            gpsList.add(object.toString());
                            SessionManager.putArrayList(gpsList,Constant.USER_GPS_INFO);

                            Log.d("Location Info", "onLocationResult: "+gpsList);
                        }catch (Exception e){

                        }
                    }
                }
            };

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }




    public void checkLastLocation(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "need explanation", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "no need explanation, go and ask", Toast.LENGTH_SHORT).show();
            }
        }else{
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener( MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLocation = location;
                            }else{
                            }
                        }
                    });
        }
    }

    public void checkUserActivity(){

        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);





        Intent intent = new Intent();
        intent.setAction("myaction");
        // Intent myIntent = new Intent(this,MainActivity.class);
        // myIntent.putExtra("Info","change");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification n = new Notification();
//        n.icon = R.drawable.ic_launcher;
//        n.when = System.currentTimeMillis();
//        n.setLatestEventInfo(this,"this is title", "this is a message", pi);
//        nm.notify(0, n);






        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
        Task<Void> task =
                ActivityRecognition.getClient(this).requestActivityTransitionUpdates(request,pi);

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                }
        );




//        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
//        Task<Void> deRegisterTask =
//                ActivityRecognition.getClient(context).removeActivityTransitionUpdates(myPendingIntent);
//
//        task.addOnSuccessListener(
//                new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void result) {
//                        myPendingIntent.cancel();
//                    }
//                });
//
//        task.addOnFailureListener(
//                new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        Log.e("MYCOMPONENT", e.getMessage());
//                    }
//                });

    }



}
