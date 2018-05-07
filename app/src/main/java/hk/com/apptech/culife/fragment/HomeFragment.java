package hk.com.apptech.culife.fragment;

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
import hk.com.apptech.culife.Constant;
import hk.com.apptech.culife.R;
import hk.com.apptech.culife.http.HttpMethod;
import hk.com.apptech.culife.model.MapModel;
import hk.com.apptech.culife.model.MissionDetailModel;
import hk.com.apptech.culife.model.TakerInfoModel;
import hk.com.apptech.culife.model.UserModel;
import hk.com.apptech.culife.observer.ObserverOnNextListener;
import hk.com.apptech.culife.observer.ProgressObserver;
import hk.com.apptech.culife.utility.Utility;
import mehdi.sakout.fancybuttons.FancyButton;

public class HomeFragment extends BaseFragment  {

    private static final String TAG = "HomeFrag";
    //Title of this fragment
    private String mTitle;

    private void initialSetting(){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }
}
