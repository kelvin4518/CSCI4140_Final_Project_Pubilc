package hk.com.csci4140.culife.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.MapModel;
import hk.com.csci4140.culife.model.MissionDetailModel;
import hk.com.csci4140.culife.model.TakerInfoModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by liujie(Jerry Liu) on 31/04/2018.
 */

public class HomeFragment extends BaseFragment  {

    private static final String TAG = "HomeFrag";

    @BindView(R.id.home_radio_group)
    RadioRealButtonGroup radioRealButtonGroup;

    @BindView(R.id.bmapView)
    MapView mMapView;

    @BindView(R.id.home_mission_card_container)
    CardView missionCardContainer;

    @BindView(R.id.home_mission_card_title)
    TextView missionCardTitle;

    @BindView(R.id.home_mission_card_salary)
    TextView missionCardSalary;

    @BindView(R.id.home_mission_card_icon)
    ImageView missionCardIcon;

    @BindView(R.id.home_mission_card_elapsed_time)
    TextView missionCardElaspedTime;

    @BindView(R.id.home_mission_card_date)
    TextView missionCardDate;

    @BindView(R.id.home_mission_card_location)
    TextView missionCardLocation;

    @BindView(R.id.home_taker_card_container)
    CardView takerCardContainer;

    @BindView(R.id.home_taker_card_icon)
    ImageView takerCardIcon;

    @BindView(R.id.home_taker_card_name)
    TextView takerCardName;

    @BindView(R.id.home_taker_card_evaluate_rating_bar)
    RatingBar takerCardRatingBar;

    @BindView(R.id.home_taker_card_commissioned_button)
    FancyButton takerCardCommissionedButton;

    @BindView(R.id.home_taker_card_take_times)
    TextView takerCardTimes;

    @BindView(R.id.home_taker_card_content)
    TextView takerCardContent;


    public enum SelectedTab{

        ALL(), MISSION(), TAKER()
    }

    //Title of this fragment
    private String mTitle;

    //Initialize the current selected tab
    private SelectedTab currentSelectedTab = SelectedTab.ALL;

    //Shift option map, for passing to Home Shift Fragment
    private HashMap<String, String> shiftOption;

    //Data from api
    private MapModel.Result mapData;

    //Marker Click listener
    BaiduMap.OnMarkerClickListener onMarkerClickListener;

    //Check if mission card view is shown
    private boolean isMissionCardShow;
    private int shownMissionCardId = -1;
    //Check if taker card view is shown
    private boolean isTakerCardShow;
    private int shownTakerCardId = -1;

    //百度地图
    private BaiduMap mBaiduMap;
    //If it is the first time to load the map, should jump to current position
    private boolean firstMove = true;
    //初始化LocationClient类
    public LocationClient mLocationClient = null;
    //定位请求回调接口
    public MyLocationListener myListener = new MyLocationListener();
    // 地图标注
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;


    private void initialSetting(){
        //Initialize the first move boolean
        firstMove = true;
        //Set the Tool bar title
        setToolbarTitle(mTitle);

        //Set the bottom navigation visible
        setBottomNavFragment(true);

        //Use to set the search icon
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the toolbar title of this fragment
        mTitle = getString(R.string.home_fragment_title);
        setPrevTitle(mTitle);

        initialSetting();
    }


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

        inflater.inflate(R.menu.main, menu);

        //Search Icon
        MenuItem item = menu.findItem(R.id.action_search);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //If user has login, go to search Fragment
                if(UserModel.isLogin){
                    setPrevTitle(mTitle);
                    replaceFragment(new SearchManagementFragment(), null);
                }
                else {
                    showBottomSnackBar(getString(R.string.should_login));
                }

                return false;
            }
        });

        //Set Login Icon Invisibility
        setLoginIconVisible(true);
        //Login Icon
        setLoginIcon();
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
        mLocationClient.stop();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onResume(){
        super.onResume();
        //Resume the map
        mMapView.onResume();
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Initialize the shift option
        if(shiftOption == null){
            shiftOption = new HashMap<>();
        }

        //获取地图
        mBaiduMap = mMapView.getMap();
        //设置地图类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //定位初始化
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        initLocation();
        //开启定位
        mLocationClient.start();


        //Initialize the tab click
        radioRealButtonGroup.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if(position == 0){
                    clickShowAll();
                }
                else if(position == 1){
                    clickShowMission();
                }
                else if(position == 2){
                    clickShowTaker();
                }
            }
        });
    }


    //Save the parameter to call api
    private void saveParameter(HashMap<String, String> parameter, String showType){
        //Save lat and lng
        parameter.put(Constant.HOME_LAT, Double.toString(getMyLatitude()));
        parameter.put(Constant.HOME_LNG, Double.toString(getMyLongitude()));
        //Save region, need to check if login
        if(UserModel.isLogin){
            parameter.put(Constant.HOME_REGION, Integer.toString(UserModel.region));
        }
        else {
            parameter.put(Constant.HOME_REGION, Integer.toString(Utility.getLanguageId(getContext())) );
        }
        //Save language
        if(!showType.equals(Constant.HOME_SHOW_TAKER)) {
            parameter.put(Constant.HOME_LANG, Integer.toString(Utility.getLanguageId(getContext())));
        }
        //Save the show type
        parameter.put(Constant.HOME_SHOW_TYPE, showType);
        //Save the shift - type
        if(shiftOption.get(Constant.HOME_SHIFT_TYPE) != null){
            parameter.put(Constant.HOME_TYPE_ID, shiftOption.get(Constant.HOME_SHIFT_TYPE));
        }
        //Save the shift - salary
        if(shiftOption.get(Constant.HOME_SHIFT_SALARY) != null){
            parameter.put(Constant.HOME_SALARY_ID, shiftOption.get(Constant.HOME_SHIFT_SALARY));
        }
        //Save the shift - start date and end date
        if(shiftOption.get(Constant.HOME_SHIFT_DATE_START) != null
                &&
                shiftOption.get(Constant.HOME_SHIFT_DATE_END) != null){
            parameter.put(Constant.HOME_START_DATE, shiftOption.get(Constant.HOME_SHIFT_DATE_START));
            parameter.put(Constant.HOME_END_DATE, shiftOption.get(Constant.HOME_SHIFT_DATE_END));
        }
    }


    //Click Show All, call show all api
    private void clickShowAll(){
        //Set the clicked tab
        currentSelectedTab = SelectedTab.ALL;

        //Save parameter
        HashMap<String, String> parameter = new HashMap<>();
        saveParameter(parameter, Constant.HOME_SHOW_ALL);

        //Call API
        ObserverOnNextListener<MapModel> observer = new ObserverOnNextListener<MapModel>() {
            @Override
            public void onNext(MapModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //When success, clear the marker click listener and marker
                    mBaiduMap.removeMarkerClickListener(onMarkerClickListener);
                    mBaiduMap.clear();

                    //Clear the card view of mission and taker
                    isMissionCardShow = false;
                    missionCardContainer.setVisibility(View.GONE);
                    isTakerCardShow = false;
                    takerCardContainer.setVisibility(View.GONE);

                    //Save the result data
                    mapData = model.getResult();

                    //If mission list is not empty
                    if(model.getTotalMissionNum() > 0){
                        //Call function to add markers to map
                        showMissionOnMap(model.getResult().getMissionLists());
                    }

                    //If taker list is not empty
                    if(model.getTotalTakerNum() > 0 ){
                        //Call function to add markers to map
                        showTakerOnMap(model.getResult().getTakerLists());
                    }

                    //Set the marker click listener
                    setOnMapMarkerClick();
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getMap(new ProgressObserver<MapModel>(getContext(), observer), parameter);
    }


    //Click Show Mission
    private void clickShowMission(){
        currentSelectedTab = SelectedTab.MISSION;

        //Save parameter
        HashMap<String, String> parameter = new HashMap<>();
        saveParameter(parameter, Constant.HOME_SHOW_MISSION);

        //Call API
        ObserverOnNextListener<MapModel> observer = new ObserverOnNextListener<MapModel>() {
            @Override
            public void onNext(MapModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //When success, clear the marker click listener and marker
                    mBaiduMap.removeMarkerClickListener(onMarkerClickListener);
                    mBaiduMap.clear();

                    //Clear the card view of mission and taker
                    isMissionCardShow = false;
                    missionCardContainer.setVisibility(View.GONE);
                    isTakerCardShow = false;
                    takerCardContainer.setVisibility(View.GONE);

                    //Save the result data
                    mapData = model.getResult();


                    //If mission list is not empty
                    if(model.getTotalMissionNum() > 0){
                        //Call function to add markers to map
                        showMissionOnMap(model.getResult().getMissionLists());
                        //Set the marker click listener
                        setOnMapMarkerClick();
                    }
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getMap(new ProgressObserver<MapModel>(getContext(), observer), parameter);
    }


    //Click Show Taker
    private void clickShowTaker(){
        currentSelectedTab = SelectedTab.TAKER;

        //Save parameter
        HashMap<String, String> parameter = new HashMap<>();
        saveParameter(parameter, Constant.HOME_SHOW_TAKER);

        //Call API
        ObserverOnNextListener<MapModel> observer = new ObserverOnNextListener<MapModel>() {
            @Override
            public void onNext(MapModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //When success, clear the marker click listener and marker
                    mBaiduMap.removeMarkerClickListener(onMarkerClickListener);
                    mBaiduMap.clear();

                    //Clear the card view of mission and taker
                    isMissionCardShow = false;
                    missionCardContainer.setVisibility(View.GONE);
                    isTakerCardShow = false;
                    takerCardContainer.setVisibility(View.GONE);

                    //Save the result data
                    mapData = model.getResult();
                    //If taker list is not empty
                    if(model.getTotalTakerNum() > 0 ){
                        //Call function to add markers to map
                        showTakerOnMap(model.getResult().getTakerLists());
                        //Set the marker click listener
                        setOnMapMarkerClick();
                    }
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getMap(new ProgressObserver<MapModel>(getContext(), observer), parameter);
    }


    //Show Mission On Map
    private void showMissionOnMap(List<MapModel.Result.MissionList> missionLists){
        //For every mission
        for(MapModel.Result.MissionList list : missionLists){
            //Inflate the bubble layout
            View view = getLayoutInflater().inflate(R.layout.item_mission_bubble, null);
            //Set the title
            TextView titleView = view.findViewById(R.id.bubble_mission_title);
            titleView.setText(list.getMissionDetail().getTitle());

            //Set the salary
            TextView salaryView = view.findViewById(R.id.bubble_mission_salary);
            float salary = Float.parseFloat(list.getMissionDetail().getSalary());
            salaryView.setText(String.format(getString(R.string.post_mission_two_salary_format), salary));

            //Convert view to bitmap and load to the overlay option
            Bitmap bm = Utility.loadBitmapFromBubbleView(view);
            BitmapDescriptor newBitmap = BitmapDescriptorFactory.fromBitmap(bm);
            LatLng point = new LatLng(list.getMissionDetail().getLat(), list.getMissionDetail().getLng());
            //Create overlay option
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(newBitmap);

            //Set the bundle info of Marker
            Bundle bundle = new Bundle();
            bundle.putString(Constant.HOME_MARKER_TYPE, Constant.HOME_MARKER_MISSION);
            bundle.putInt(Constant.HOME_MARKER_POSITION, missionLists.indexOf(list));

            //Add marker to map, add extra info
            mBaiduMap.addOverlay(option).setExtraInfo(bundle);
        }
    }


    //Show Taker On Map
    private void showTakerOnMap(List<MapModel.Result.TakerList> takerLists){
        //For every taker
        for(MapModel.Result.TakerList taker : takerLists){

            //Convert icon to bitmap and load to the overlay option
            BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_taker);
            LatLng point = new LatLng(taker.getTakerInfo().getLat(), taker.getTakerInfo().getLng());
            //Create overlay option
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bm);

            //Set the bundle info of Marker
            Bundle bundle = new Bundle();
            bundle.putString(Constant.HOME_MARKER_TYPE, Constant.HOME_MARKER_TAKER);
            bundle.putInt(Constant.HOME_MARKER_POSITION, takerLists.indexOf(taker));

            //Add marker to map, add extra info
            mBaiduMap.addOverlay(option).setExtraInfo(bundle);
        }
    }


    //Map on marker click listener
    private void setOnMapMarkerClick(){
        //Build the map marker on click listener
        onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Get the marker info
                Bundle bundle = marker.getExtraInfo();
                int markerPosition = bundle.getInt(Constant.HOME_MARKER_POSITION);
                String markerType = bundle.getString(Constant.HOME_MARKER_TYPE);

                //Check the type to see if it is mission or taker
                if(markerType.equals(Constant.HOME_MARKER_MISSION)){
                    //Get the mission detail according to the marker info
                    MissionDetailModel detail = mapData.getMissionLists().get(markerPosition).getMissionDetail();

                    //Invisible the taker card view
                    isTakerCardShow = false;
                    shownTakerCardId = -1;
                    takerCardContainer.setVisibility(View.GONE);

                    //If the card view is already show
                    if(isMissionCardShow){
                        if(shownMissionCardId == detail.getId()){
                            //If current card view is clicked marker's card view, then invisible the card view
                            missionCardContainer.setVisibility(View.GONE);
                            shownMissionCardId = -1;
                            isMissionCardShow = false;
                        }
                        else {
                            //Set the card view's information and update, set the shown card id
                            shownMissionCardId = detail.getId();
                            //Set title
                            missionCardTitle.setText(detail.getTitle());
                            //Set salary
                            float salary = Float.parseFloat(detail.getSalary());
                            missionCardSalary.setText(String.format(getString(R.string.post_mission_two_salary_format), salary));
                            //Set pass time
                            String passFormat = String.format(getString(R.string.home_card_mission_pass_date_format), detail.getPassedDate());
                            missionCardElaspedTime.setText(passFormat);
                            //Set icon
                            if(detail.getIcon() != null && !detail.getIcon().equals("")){
                                Glide.with(getContext()).load(detail.getIcon()).into(missionCardIcon);
                            }
                            //Set address
                            missionCardLocation.setText(detail.getAddress());
                            //TODO: date
                        }
                    }
                    else {
                        //If there has no card view is shown, show the card view and update information
                        isMissionCardShow = true;
                        //Set the shown card if for click
                        shownMissionCardId = detail.getId();
                        missionCardContainer.setVisibility(View.VISIBLE);
                        //Set title
                        missionCardTitle.setText(detail.getTitle());
                        //Set salary
                        float salary = Float.parseFloat(detail.getSalary());
                        missionCardSalary.setText(String.format(getString(R.string.post_mission_two_salary_format), salary));
                        //Set passed time
                        String passFormat = String.format(getString(R.string.home_card_mission_pass_date_format), detail.getPassedDate());
                        missionCardElaspedTime.setText(passFormat);
                        //set icon
                        if(detail.getIcon() != null && !detail.getIcon().equals("")){
                            Glide.with(getContext()).load(detail.getIcon()).into(missionCardIcon);
                        }
                        //Set address
                        missionCardLocation.setText(detail.getAddress());
                        //TODO: date
                    }
                }
                else if(markerType.equals(Constant.HOME_MARKER_TAKER)){
                    //Get the taker detail according to the marker info
                    TakerInfoModel detail = mapData.getTakerLists().get(markerPosition).getTakerInfo();

                    //Invisible the taker card view
                    isMissionCardShow = false;
                    shownMissionCardId = -1;
                    missionCardContainer.setVisibility(View.GONE);

                    //If the card view is already show
                    if(isTakerCardShow){
                        if(shownTakerCardId == detail.getUserId()){
                            //If current card view is clicked marker's card view, then invisible the card view
                            takerCardContainer.setVisibility(View.GONE);
                            shownTakerCardId = -1;
                            isTakerCardShow = false;
                        }
                        else {
                            //Set the card view's information and update, set the shown card id
                            shownTakerCardId = detail.getUserId();
                            //Set name
                            takerCardName.setText(detail.getName());
                            //Set rate
                            float rate = (float)detail.getRateNum();
                            takerCardRatingBar.setRating(rate);
                            //Set total mission
                            String totalMission = String.format(getString(R.string.home_card_taker_commission_times_format), detail.getTotalMissionNum());
                            takerCardTimes.setText(totalMission);
                            //Set icon
                            if(detail.getIconLink() != null && !detail.getIconLink().equals("")){
                                Glide.with(getContext()).load(detail.getIconLink()).into(takerCardIcon);
                            }
                            //Set content
                            takerCardContent.setText(detail.getIntroduction());
                        }
                    }
                    else {
                        //If there has no card view is shown, show the card view and update information
                        isTakerCardShow = true;
                        //Set the card view's information and update, set the shown card id
                        shownTakerCardId = detail.getUserId();
                        takerCardContainer.setVisibility(View.VISIBLE);
                        //Set name
                        takerCardName.setText(detail.getName());
                        //Set rate
                        float rate = (float)detail.getRateNum();
                        takerCardRatingBar.setRating(rate);
                        //Set total mission
                        String totalMission = String.format(getString(R.string.home_card_taker_commission_times_format), detail.getTotalMissionNum());
                        takerCardTimes.setText(totalMission);
                        //Set icon
                        if(detail.getIconLink() != null && !detail.getIconLink().equals("")){
                            Glide.with(getContext()).load(detail.getIconLink()).into(takerCardIcon);
                        }
                        //Set content
                        takerCardContent.setText(detail.getIntroduction());
                    }
                }
                return false;
            }
        };

        //Set the listener
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
    }


    //Click the mission card
    @OnClick(R.id.home_mission_card_container)
    void onClickMissionCard(){
        //go to mission detail fragment
        setPrevTitle(mTitle);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.MISSION_DETAIL_MISSION_ID, shownMissionCardId);
        replaceFragment(new MissionDetailFragment(), bundle);
    }


    //Click the commission button
    @OnClick(R.id.home_taker_card_commissioned_button)
    void onClickCommissionButton(){
        //TODO: Commission Part
        showBottomSnackBar("等待完成");
    }


    //Click the shift button
    @OnClick(R.id.home_shift_button)
    void onClickShiftButton(){
        //Go to HomeShiftFragment
        setPrevTitle(mTitle);
        //Put the shift options to bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.HOME_SHIFT_OPTION_VALUE, shiftOption);
        replaceFragment(new HomeShiftFragment(), bundle);
    }


    //My location button click and touch listener
    @OnClick(R.id.home_location_button)
    void onClickMyLocation(){
        LatLng point = new LatLng(getMyLatitude(), getMyLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, 15);
        mBaiduMap.animateMapStatus(u);
    }
    @OnTouch(R.id.home_location_button)
    boolean onTouchMyLocation(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.setBackgroundResource(R.drawable.circle_grey_background);
                break;
            case MotionEvent.ACTION_UP:
                view.setBackgroundResource(R.drawable.circle_white_background);
                break;
            default:
                break;
        }
        return false;
    }


    //Initialize the map's config
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    //Class to detect the lat and lng of user
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {

            //Get the user lat and long
            setMyLatitude(location.getLatitude());
            setMyLongitude(location.getLongitude());


            //=====核心代码=====
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            MyLocationConfiguration configuration = new MyLocationConfiguration(mCurrentMode, true, null);
            mBaiduMap.setMyLocationConfiguration(configuration);
            //=====核心代码=====

            //=====自定义标注及动态移动到当前位置到屏幕中间=====
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());

            //=====当前位置移动到屏幕中间代码
            // 21 => 5米
            // 20 => 10米
            // 15 => 500米
            // 10 => 20公里
            // 5  => 500公里
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, 15);
            if(firstMove) {
                firstMove = false;
                //mBaiduMap.animateMapStatus(u);
                mBaiduMap.setMapStatus(u);

                //Initialize the tab click
                switch (currentSelectedTab){
                    case ALL:
                        radioRealButtonGroup.setPosition(0);
                        clickShowAll();
                        break;
                    case MISSION:
                        radioRealButtonGroup.setPosition(1);
                        clickShowMission();
                        break;
                    case TAKER:
                        radioRealButtonGroup.setPosition(2);
                        clickShowTaker();
                        break;
                    default:
                        break;
                }
            }



            //=====当前位置移动到屏幕中间代码
            //=====自定义标注及动态移动到当前位置到屏幕中间=====

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            //Log.i("BaiduLocationApiDem", sb.toString());

        }
    }


}
