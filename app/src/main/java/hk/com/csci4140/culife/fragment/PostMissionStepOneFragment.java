package hk.com.csci4140.culife.fragment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.BaseActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.CreateMissionOptionModel;
import hk.com.csci4140.culife.model.EditProfileOptionModel;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;
import retrofit2.http.HEAD;

/**
 * Created by zhenghao(Kelvin Zheng) on 22/04/2018.
 *
 * Structure:
 * 1. Bind the views and create variables
 * 2. Bind the onClick and set the selectos
 * 3. Do works before the user see the page
 * 4. Do works after the user see the page
 */

public class PostMissionStepOneFragment extends BaseFragment {



    public boolean isEditMode;
    public HabitModel mHabit;
    public void initEditMode(HabitModel habit){
        mHabit = habit;
        isEditMode = true;
    }









    // Part 1 : Bind the view and create variables
    private static final String TAG = "PostMissionStepOne";

    @BindView(R.id.post_mission_scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.create_habit_name)
    EditText mHabitName;

    @BindView(R.id.post_mission_one_start_time)
    TextView startTime;

    @BindView(R.id.post_mission_one_end_time)
    TextView endTime;


    @BindView(R.id.post_mission_one_method_spinner)
    Spinner postMethodSpinner;

    @BindView(R.id.post_mission_one_period_spinner)
    Spinner postPeriodSpinner;

    @BindView(R.id.post_mission_one_period_start_time)
    TextView postPeriodStart;

    @BindView(R.id.post_mission_one_period_from)
    TextView postPeriodFrom;

    @BindView(R.id.post_mission_one_period_end_time)
    TextView postPeriodEnd;

    @BindView(R.id.create_habit_content)
    EditText mHabitContent;

    @BindView(R.id.post_mission_one_continue_button)
    Button mConfirmButton;

    @BindView(R.id.create_habit_set_public_switch)
    Switch mIsPublic;

    @BindView(R.id.create_habit_send_notification_switch)
    Switch mSendNotification;

    private String mTitle;
    private String mPrevTitle;

    //Paramete
    // r to post mission api
    HashMap<String, String> parameter;
    private String errorText;

    //
    //private CreateMissionOptionModel mOptionsModel;
    private int selectedType = -1;
    private int selectedMethod = -1;
    private int selectedPeriod = -1;

    CatLoadingView mView;













    // Part 2: Bind the onclick and set the selectors
    //Initialize the post method spinner
    private void initialMethodSpinner(){
        postMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMethod = i;
                if(i == 0){
                    postPeriodSpinner.setVisibility(View.GONE);
                    setPeriodDateVisibility(true);
                }
                else if(i == 1){
                    postPeriodSpinner.setVisibility(View.VISIBLE);
                    setPeriodDateVisibility(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(selectedMethod != -1){
            postMethodSpinner.setSelection(selectedMethod);
        }
    }

    //Visibility of the Date period
    private void setPeriodDateVisibility(boolean visible){
        if(visible){
            postPeriodStart.setVisibility(View.VISIBLE);
            postPeriodFrom.setVisibility(View.VISIBLE);
            postPeriodEnd.setVisibility(View.VISIBLE);
        }
        else {
            postPeriodStart.setVisibility(View.GONE);
            postPeriodFrom.setVisibility(View.GONE);
            postPeriodEnd.setVisibility(View.GONE);
        }
    }

    //Initial the post period spinner
    private void initialPeriodSpinner(){
        ArrayList<String> periodList = new ArrayList<>();
        //Initial the Period list

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, periodList);
        postPeriodSpinner.setAdapter(spinnerAdapter);
        postPeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPeriod = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedPeriod != -1){
            postPeriodSpinner.setSelection(selectedPeriod);
        }
    }

    //Check if the user's input is valid
    private boolean checkInputValidation(){
        parameter = new HashMap<>();

        //Mission Title
        if(mHabitName.getText() == null || mHabitName.getText().toString().equals("")){
            errorText = getString(R.string.post_mission_input_name);
            return false;
        }
        parameter.put(Constant.MISSION_TITLE, mHabitName.getText().toString());

        if(mHabitContent.getText() == null || mHabitContent.getText().toString().equals("")){
            errorText = getString(R.string.post_habbit_content);
            return false;
        }

        parameter.put(Constant.MISSION_POST_HABBIT_CONTENT, mHabitContent.getText().toString());

        //Mission start time and end time
        if( (startTime.getText() == null || startTime.getText().toString().equals(""))
                ||
                (endTime.getText() == null || endTime.getText().toString().equals(""))){
            errorText = getString(R.string.post_mission_input_time);
            return false;
        }
        parameter.put(Constant.MISSION_START_TIME, startTime.getText().toString());
        parameter.put(Constant.MISSION_END_TIME, endTime.getText().toString());


        //Mission post method and post period
        if(selectedMethod == 0){
            if( (postPeriodStart.getText() == null || postPeriodStart.getText().toString().equals(""))
                    ||
                    (postPeriodEnd.getText() == null || postPeriodEnd.getText().toString().equals(""))){
                errorText = getString(R.string.post_mission_input_period_date);
                return false;
            }
            parameter.put(Constant.MISSION_POST_START, postPeriodStart.getText().toString());
            parameter.put(Constant.MISSION_POST_END, postPeriodEnd.getText().toString());
        }
        else {
            //parameter.put(Constant.MISSION_POST_PERIOD_ID, Integer.toString(mOptionsModel.getResult().getPostPeriods().get(selectedPeriod).getId()));
            parameter.put(Constant.MISSION_POST_PERIOD, postPeriodSpinner.getSelectedItem().toString());
        }
        parameter.put(Constant.MISSION_POST_METHOD, Integer.toString(selectedMethod));


        //Token and Region
        parameter.put(Constant.TOKEN, UserModel.token);
        parameter.put(Constant.MISSION_REGION, Integer.toString(UserModel.region));

        return true;
    }

    @OnClick({R.id.post_mission_one_start_time, R.id.post_mission_one_end_time})
    void onClickMissionTime(final TextView textView){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(textView.getId() == R.id.post_mission_one_start_time){
                    String start = Utility.getTimeString(hourOfDay, minute);
                    if(endTime.getText() != null && !endTime.getText().toString().equals("") ){
                        String end = endTime.getText().toString();
                        if(Utility.compareTime(start, end)){
                            //设置TextView显示最终选择的时间
                            textView.setText(String.format(getString(R.string.time_format), hourOfDay, minute));
                        }
                        else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getString(R.string.warning_title))
                                    .setContentText(getString(R.string.post_mission_start_after_end))
                                    .setConfirmText(getString(R.string.warning_confirm))
                                    .show();
                        }
                    }
                    else {
                        //设置TextView显示最终选择的时间
                        textView.setText(start);
                    }
                }
                else if(textView.getId() == R.id.post_mission_one_end_time){
                    String end = Utility.getTimeString(hourOfDay, minute);
                    if(startTime.getText() != null && !startTime.getText().toString().equals("") ){
                        String start = startTime.getText().toString();
                        if(Utility.compareTime(start, end)){
                            //设置TextView显示最终选择的时间
                            textView.setText(end);
                        }
                        else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getString(R.string.warning_title))
                                    .setContentText(getString(R.string.post_mission_start_after_end))
                                    .setConfirmText(getString(R.string.warning_confirm))
                                    .show();
                        }
                    }
                    else {
                        //设置TextView显示最终选择的时间
                        textView.setText(end);
                    }
                }

            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    @OnClick({R.id.post_mission_one_period_start_time, R.id.post_mission_one_period_end_time})
    void onClickPeriodDate(final TextView textView){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //If picked date is early than today
                String pickedDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                String today = Utility.getToday();
                if(Utility.compareDate(pickedDate, today)){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.warning_title))
                            .setContentText(getString(R.string.post_mission_date_before_today_warning))
                            .setConfirmText(getString(R.string.warning_confirm))
                            .show();
                    return;
                }
                //If click the start Date
                if(textView.getId() == R.id.post_mission_one_period_start_time){
                    String startDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                    if(postPeriodEnd.getText() != null && !postPeriodEnd.getText().toString().equals("") ){
                        String endDate = postPeriodEnd.getText().toString();
                        if(Utility.compareDate(startDate, endDate)){
                            //设置TextView显示最终选择的时间
                            textView.setText(startDate);
                        }
                        else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getString(R.string.warning_title))
                                    .setContentText(getString(R.string.post_mission_period_start_after_end))
                                    .setConfirmText(getString(R.string.warning_confirm))
                                    .show();
                        }
                    }
                    else {
                        //设置TextView显示最终选择的时间
                        textView.setText(startDate);
                    }
                }//If click the end Date
                else if(textView.getId() == R.id.post_mission_one_period_end_time){
                    String endDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                    if(postPeriodStart.getText() != null && !postPeriodStart.getText().toString().equals("") ){
                        String startDate = postPeriodStart.getText().toString();
                        if(Utility.compareDate(startDate, endDate)){
                            //设置TextView显示最终选择的时间
                            textView.setText(endDate);
                        }
                        else {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getString(R.string.warning_title))
                                    .setContentText(getString(R.string.post_mission_period_start_after_end))
                                    .setConfirmText(getString(R.string.warning_confirm))
                                    .show();
                        }
                    }
                    else {
                        //设置TextView显示最终选择的时间
                        textView.setText(endDate);
                    }
                }

            }
        }, mYear, mMonth, mDay);
        //弹出选择日期对话框
        datePickerDialog.show();
    }

    @OnClick(R.id.post_mission_one_continue_button)
    void onClickPostMission(){
        // check the input and decide
        if(checkHabitInputIsValid()){
            saveParameter();
        }else{
            //Error dialog
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        saveParameter();
//        if(checkInputValidation()){
//            //Go to Step Two
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constant.MISSION_MAP_PARAMETER, parameter);
//            setPrevTitle(mTitle);
//            replaceFragment(new PostMissionStepTwoFragment(), bundle);
//        }
//        else {
//            //Error dialog
//            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getString(R.string.warning_title))
//                    .setContentText(errorText)
//                    .setConfirmText(getString(R.string.warning_confirm))
//                    .show();
//        }
        }
    }





//    @OnClick(R.id.post_mission_one_date)
    void onClickMissionDate(final TextView textView){
        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.selection_title))
                .titleColor(getResources().getColor(R.color.colorAccent))
                .itemsColor(getResources().getColor(R.color.textColor))
                .items(R.array.post_mission_date_array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which == 0){
                            textView.setText(text.toString());
                        }
                        else {
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                //选择完日期后会调用该回调函数
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    //If picked date is early than today
                                    String pickedDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                                    String today = Utility.getToday();
                                    if(Utility.compareDate(pickedDate, today)){
                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning_title))
                                                .setContentText(getString(R.string.post_mission_date_before_today_warning))
                                                .setConfirmText(getString(R.string.warning_confirm))
                                                .show();
                                        return;
                                    }

                                    //因为monthOfYear会比实际月份少一月所以这边要加1
                                    textView.setText(Utility.getDateString(year, (monthOfYear + 1), dayOfMonth));

                                }
                            }, mYear, mMonth, mDay);
                            //弹出选择日期对话框
                            datePickerDialog.show();
                        }
                    }
                })
                .show();
    }





    private boolean checkHabitInputIsValid(){
        //Mission Title
        if(mHabitName.getText() == null || mHabitName.getText().toString().equals("")){
            errorText = getString(R.string.post_mission_input_name);
            return false;
        }

        if(mHabitContent.getText() == null || mHabitContent.getText().toString().equals("")){
            errorText = getString(R.string.post_habbit_content);
            return false;
        }

        return true;
    }










    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
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
        setBottomNavFragment(false);
        setPrevBottomNavFragment(true);
    }

    @Override
    public void onStart() {
        initialSetting();
        super.onStart();

        initialMethodSpinner();
        initialPeriodSpinner();

        //If user is from step two
        if(parameter != null){
            //Mission Date

            //Start Time and End Time
            startTime.setText(parameter.get(Constant.MISSION_START_TIME));
            endTime.setText(parameter.get(Constant.MISSION_END_TIME));

            //Post Period
            if(parameter.get(Constant.MISSION_POST_METHOD).equals(Constant.TRUE)){
                postPeriodStart.setText(parameter.get(Constant.MISSION_POST_START));
                postPeriodEnd.setText(parameter.get(Constant.MISSION_POST_END));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = getString(R.string.post_mission_step_one_title);
        }
        if (mPrevTitle == null) {
            mPrevTitle = getPrevTitle();
        }
        setToolbarTitle(mTitle);

        initialSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_post_mission_step_one, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isEditMode){
            mHabitName.setText(mHabit.name);
            mHabitContent.setText(mHabit.description);
            mConfirmButton.setText("修改");
        }
    }










    // after user exit the page
    @Override
    public void onPause(){
        super.onPause();
    }




    private void saveParameter(){
        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("body", mHabitContent.getText().toString());
            jsonParams.put("title", mHabitName.getText().toString());
            jsonParams.put("description", mHabitContent.getText().toString());
            jsonParams.put("start_date", postPeriodStart.getText().toString());
            jsonParams.put("end_date", postPeriodEnd.getText().toString());
            if(!("".equals(startTime.getText().toString()))) {
                jsonParams.put("start_time", startTime.getText().toString());
            }
            if(!("".equals(endTime.getText().toString()))) {
                jsonParams.put("end_time", endTime.getText().toString());
            }
            jsonParams.put("is_public", (mIsPublic.isChecked() ? 1 : 0));
            jsonParams.put("send_notification", (mSendNotification.isChecked() ? 1 : 0));
            if(isEditMode){
                jsonParams.put("habitid",mHabit.ID);
            }

            Log.d(Constant.API_REPORT_TAG, "midnight: "+postPeriodStart.getText().toString());
            Log.d(Constant.API_REPORT_TAG, "midnight: "+startTime.getText().toString());
            Log.d(Constant.API_REPORT_TAG, "midnight: public"+(mIsPublic.isChecked() ? 1 : 0));

            outerJsonParams.put("habit",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());


            if(isEditMode){
                callUpdateHabitAPI(entity);
            }else{
                callCreateHabitAPI(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    // Handle Create Habit Request
    private void callCreateHabitAPI(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);

        mView = new CatLoadingView();

        mView.show(getFragmentManager(), "");

        client.post(getContext(),Constant.API_BASE_URL+"habits/create",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onSuccess: create habit");
                Log.d(Constant.API_REPORT_TAG, "onSuccess: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onSuccess: response: "+response);
                //showBottomSnackBar("Welcome to CULife !");
                JSONObject jsonHabit = new JSONObject();
                try {
                    jsonHabit = response.getJSONObject("habit");
                }catch (JSONException e){
                    e.printStackTrace();
                }

                Integer newCreateHabitID = 0;
                try {
                    newCreateHabitID = jsonHabit.getInt("id");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                JSONObject jsonParams1 = new JSONObject();
                JSONObject outerJsonParams1 = new JSONObject();
                try{
                    jsonParams1.put("habitid",newCreateHabitID);
                    jsonParams1.put("body","");
                    jsonParams1.put("score",0);
                    outerJsonParams1.put("check",jsonParams1);
                    StringEntity entity1 = new StringEntity(outerJsonParams1.toString());
                    //Log.d(TAG,"checkcreate"+outerJsonParams1);
                    callCreateCheckAPI(entity1);}
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                replaceActivity(MainActivity.class, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onFailure: create habit");
                Log.d(Constant.API_REPORT_TAG, "onFailure: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onFailure: response : "+response);
            }
        });
    }


    // Handle Update Habit Request
    private void callUpdateHabitAPI(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);

        mView = new CatLoadingView();

        mView.show(getFragmentManager(), "");

        client.post(getContext(),Constant.API_BASE_URL+"habits/update",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onSuccess: update habit");
                Log.d(Constant.API_REPORT_TAG, "onSuccess: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onSuccess: response: "+response);
                showBottomSnackBar("Update Habit Success");
                replaceActivity(MainActivity.class, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onFailure: update habit");
                Log.d(Constant.API_REPORT_TAG, "onFailure: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onFailure: response : "+response);
            }
        });
    }

    private void callCreateCheckAPI(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);

        mView = new CatLoadingView();

        mView.show(getFragmentManager(), "");

        client.post(getContext(),Constant.API_BASE_URL+"habits/check_create",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onSuccess: create check");
                Log.d(Constant.API_REPORT_TAG, "onSuccess: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onSuccess: response: "+response);
                //showBottomSnackBar("Welcome to CULife !");
                replaceActivity(MainActivity.class, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mView.dismiss();
                Log.d(Constant.API_REPORT_TAG, "onFailure: create check");
                Log.d(Constant.API_REPORT_TAG, "onFailure: status : "+statusCode);
                Log.d(Constant.API_REPORT_TAG, "onFailure: response : "+response);
            }
        });
    }

}
