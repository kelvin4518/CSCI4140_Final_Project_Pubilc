package hk.com.csci4140.culife.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.LoginActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.tasks.Task;

public class LocationServiceFragment extends BaseFragment {

    private static final String TAG = "LocationServiceTestFrag";

    private String mTitle;
    private String mPrevTitle;

    private FusedLocationProviderClient mFusedLocationClient;


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
        mTitle = "Test Location";
        setToolbarTitle(mTitle);

        initialSetting();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
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

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }






    // User interacting with the page

    @BindView(R.id.confirm_complete)
    Button confirmBtn;

    @OnClick(R.id.confirm_complete)
    void onClickConfirmComplete(){
        checkLastLocation();
//        checkUserActivity();
    }

    public void checkLastLocation(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getContext(), "need explanation", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "no need explanation, go and ask", Toast.LENGTH_SHORT).show();
            }
        }else{
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener( getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Toast.makeText(getContext(), "get the location back", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "no location back", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void checkUserActivity(){

        List<ActivityTransition> transitions = new ArrayList<>();
//        transitions.add(
//                new ActivityTransition.Builder()
//                        .setActivityType(DetectedActivity.IN_VEHICLE)
//                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
//                        .build());
//        transitions.add(
//                new ActivityTransition.Builder()
//                        .setActivityType(DetectedActivity.IN_VEHICLE)
//                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
//                        .build());

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



//        Toast.makeText(getContext(), "Into check user activity", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setAction("myaction");
        // Intent myIntent = new Intent(getContext(),MainActivity.class);
        // myIntent.putExtra("Info","change");
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification n = new Notification();
//        n.icon = R.drawable.ic_launcher;
//        n.when = System.currentTimeMillis();
//        n.setLatestEventInfo(this,"this is title", "this is a message", pi);
//        nm.notify(0, n);






        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
        Task<Void> task =
                ActivityRecognition.getClient(getContext()).requestActivityTransitionUpdates(request,pi);

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(getContext(),"task on success !!!!!!!!!!!!!",Toast.LENGTH_SHORT);
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(),"task on error ???????????????",Toast.LENGTH_SHORT);
                    }
                }
        );



//        Toast.makeText(getContext(), "end check user activity", Toast.LENGTH_SHORT).show();


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

    // the page is dismissed
    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the title of previous fragment
        setToolbarTitle(mPrevTitle);
    }

}
