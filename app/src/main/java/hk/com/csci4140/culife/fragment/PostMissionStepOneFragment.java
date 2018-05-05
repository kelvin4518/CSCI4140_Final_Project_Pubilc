package hk.com.csci4140.culife.fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.BaseActivity;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.CreateMissionOptionModel;
import hk.com.csci4140.culife.model.EditProfileOptionModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 22/04/2018.
 */

public class PostMissionStepOneFragment extends BaseFragment {

    private static final String TAG = "PostMissionStepOne";

    @BindView(R.id.post_mission_scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.post_mission_one_name)
    EditText postName;

    @BindView(R.id.post_mission_one_type_spinner)
    Spinner typeSpinner;

    @BindView(R.id.post_mission_one_date)
    TextView postDate;

    @BindView(R.id.post_mission_one_start_time)
    TextView startTime;

    @BindView(R.id.post_mission_one_end_time)
    TextView endTime;

    @BindView(R.id.post_mission_one_num)
    EditText postNum;

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

    @BindView(R.id.post_mission_one_interview_switch_button)
    SwitchButton needInterView;

    @BindView(R.id.post_mission_one_salary)
    EditText postSalary;

    @BindView(R.id.post_mission_bmapView)
    MapView mMapView;

    @BindView(R.id.post_mission_one_address)
    EditText postAddress;

    @BindView(R.id.post_mission_one_video_switch_button)
    SwitchButton needVideo;

    @BindView(R.id.post_mission_one_photo_switch_button)
    SwitchButton needPhoto;

    @BindView(R.id.post_mission_one_disclose_media_switch_button)
    SwitchButton discloseMedia;

    @BindView(R.id.post_mission_one_content)
    EditText postContent;

    @BindView(R.id.post_mission_one_requirement)
    EditText postRequirement;


    private String mTitle;
    private String mPrevTitle;

    //Parameter to post mission api
    HashMap<String, String> parameter;
    private String errorText;

    private CreateMissionOptionModel mOptionsModel;
    private int selectedType = -1;
    private int selectedMethod = -1;
    private int selectedPeriod = -1;

    //百度地图
    private BaiduMap mBaiduMap;
    private LatLng selectedPoint;


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
    public void onResume(){
        super.onResume();
        //Resume the map
        mMapView.onResume();
    }


    @Override
    public void onPause(){
        super.onPause();
        //Pause the map
        mMapView.onPause();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //关闭定位
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_post_mission_step_one, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart() {
        initialSetting();
        super.onStart();

        initialMethodSpinner();
        initialMap();
        if(mOptionsModel == null){
            callGetOptionsHttp();
        }
        else {
            initialMissionTypeSpinner();
            initialPeriodSpinner();
        }
        //If user is from step two
        if(parameter != null){
            //Mission Date
            if(parameter.get(Constant.MISSION_NO_SPEC_DATE).equals(Constant.FALSE)){
                postDate.setText(parameter.get(Constant.MISSION_DATE));
            }

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


    //Initial the Mission Type Spinner
    private void initialMissionTypeSpinner(){
        ArrayList<String> typeList = new ArrayList<>();
        //Initial the Type list
        for(CreateMissionOptionModel.Result.MissionType type : mOptionsModel.getResult().getMissionTypes()){
            typeList.add(type.getType());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, typeList);
        typeSpinner.setAdapter(spinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedType != -1){
            typeSpinner.setSelection(selectedType);
        }
    }


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
        for(CreateMissionOptionModel.Result.PostPeriod postPeriod : mOptionsModel.getResult().getPostPeriods()){
            periodList.add(postPeriod.getPeriod());
        }

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


    //Initial the Baidu Map
    private void initialMap(){
        //获取地图
        mBaiduMap = mMapView.getMap();
        //设置地图类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //Set Map Status to our location
        if(selectedPoint != null){
            // 构建MarkerOption，用于在地图上添加Marker
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
            MarkerOptions options = new MarkerOptions().position(selectedPoint)
                    .icon(bitmap);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(options);

            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(selectedPoint, 15);
            mBaiduMap.animateMapStatus(u);
        }
        else {
            LatLng point = new LatLng(getMyLatitude(), getMyLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, 15);
            mBaiduMap.animateMapStatus(u);
        }



        //解决与ScrollView冲突问题
        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        //Set Marker Bitmap
        final BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                //先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                selectedPoint = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(selectedPoint)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                //实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                //设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption().location(latLng);
                //发起反地理编码请求(经纬度->地址信息)
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            showBottomSnackBar(getString(R.string.post_mission_address_no_result));
                            Log.i(TAG, getString(R.string.post_mission_address_no_result));
                            return;
                        }

                        //获取反向地理编码结果
                        postAddress.setText(result.getAddress());
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
                geoCoder.reverseGeoCode(op);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }


    //Check if the user's input is valid
    private boolean checkInputValidation(){
        parameter = new HashMap<>();

        //Mission Title
        if(postName.getText() == null || postName.getText().toString().equals("")){
            errorText = getString(R.string.post_mission_input_name);
            return false;
        }
        parameter.put(Constant.MISSION_TITLE, postName.getText().toString());


        //Mission Type
        parameter.put(Constant.MISSION_TYPE_ID, Integer.toString(mOptionsModel.getResult().getMissionTypes().get(selectedType).getId()));
        parameter.put(Constant.MISSION_TYPE, typeSpinner.getSelectedItem().toString());


        //Mission Date
        if(postDate.getText().toString().equals(getString(R.string.post_mission_date_default))){
            parameter.put(Constant.MISSION_NO_SPEC_DATE, Constant.TRUE);
        }
        else {
            parameter.put(Constant.MISSION_NO_SPEC_DATE, Constant.FALSE);
            parameter.put(Constant.MISSION_DATE, postDate.getText().toString());
        }


        //Mission start time and end time
        if( (startTime.getText() == null || startTime.getText().toString().equals(""))
                ||
                (endTime.getText() == null || endTime.getText().toString().equals(""))){
            errorText = getString(R.string.post_mission_input_time);
            return false;
        }
        parameter.put(Constant.MISSION_START_TIME, startTime.getText().toString());
        parameter.put(Constant.MISSION_END_TIME, endTime.getText().toString());


        //Mission num of needed
        if(postNum.getText() == null || postNum.getText().toString().equals("")){
            errorText = getString(R.string.post_mission_input_num);
            return false;
        }
        parameter.put(Constant.MISSION_NUM_NEEDED, postNum.getText().toString());


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
            parameter.put(Constant.MISSION_POST_PERIOD_ID, Integer.toString(mOptionsModel.getResult().getPostPeriods().get(selectedPeriod).getId()));
            parameter.put(Constant.MISSION_POST_PERIOD, postPeriodSpinner.getSelectedItem().toString());
        }
        parameter.put(Constant.MISSION_POST_METHOD, Integer.toString(selectedMethod));


        //Mission need interview
        String needInterview = needInterView.isChecked() ? Constant.TRUE : Constant.FALSE;
        parameter.put(Constant.MISSION_NEED_INTERVIEW, needInterview);


        //Mission salary
        if(postSalary.getText() == null || postSalary.getText().toString().equals("")){
            errorText = getString(R.string.post_mission_input_salary);
            return false;
        }
        parameter.put(Constant.MISSION_SALARY, postSalary.getText().toString());


        //Mission Address
        if(postAddress.getText() != null && !postAddress.getText().toString().equals("")){
            parameter.put(Constant.MISSION_HAS_ADDRESS, Constant.TRUE);
            parameter.put(Constant.MISSION_ADDRESS, postAddress.getText().toString());
            parameter.put(Constant.MISSION_ADDRESS_LATITUDE, Double.toString(selectedPoint.latitude));
            parameter.put(Constant.MISSION_ADDRESS_LONGITUDE, Double.toString(selectedPoint.longitude));
        }
        else {
            parameter.put(Constant.MISSION_HAS_ADDRESS, Constant.FALSE);
        }


        //Mission need video
        String videoNeeded = needVideo.isChecked() ? Constant.TRUE : Constant.FALSE;
        parameter.put(Constant.MISSION_NEED_VIDEO, videoNeeded);


        //Mission need photo
        String photoNeeded = needPhoto.isChecked() ? Constant.TRUE : Constant.FALSE;
        parameter.put(Constant.MISSION_NEED_PHOTO, photoNeeded);


        //Mission disclose media
        String publicMedia = discloseMedia.isChecked() ? Constant.TRUE : Constant.FALSE;
        parameter.put(Constant.MISSION_DISCLOSE_MEDIA, publicMedia);

        //Mission Content
        if(postContent.getText() != null && !postContent.getText().toString().equals("")){
            parameter.put(Constant.MISSION_CONTENT, postContent.getText().toString());
        }


        //Mission Requirement
        if(postRequirement.getText() != null && !postRequirement.getText().toString().equals("")){
            parameter.put(Constant.MISSION_REQUIREMENT, postRequirement.getText().toString());
        }


        //Token and Region
        parameter.put(Constant.TOKEN, UserModel.token);
        parameter.put(Constant.MISSION_REGION, Integer.toString(UserModel.region));

        return true;
    }


    //Call the Http to get the options value
    private void callGetOptionsHttp(){
        ObserverOnNextListener<CreateMissionOptionModel> observer = new ObserverOnNextListener<CreateMissionOptionModel>() {
            @Override
            public void onNext(CreateMissionOptionModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){

                    mOptionsModel = model;

                    //Initial the type spinner
                    initialMissionTypeSpinner();

                    //Initial the post period spinner
                    initialPeriodSpinner();

                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getCreateMissionOptions(new ProgressObserver<CreateMissionOptionModel>(getContext(), observer), UserModel.token, Utility.getLanguageId(getContext()));
    }


    @OnClick(R.id.post_mission_one_date)
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
        if(checkInputValidation()){
            //Go to Step Two
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.MISSION_MAP_PARAMETER, parameter);
            setPrevTitle(mTitle);
            replaceFragment(new PostMissionStepTwoFragment(), bundle);
        }
        else {
            //Error dialog
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }

}
