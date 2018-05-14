package hk.com.csci4140.culife.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.HomeFragmentAdapter;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.HomeFragmentModel;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.MapModel;
import hk.com.csci4140.culife.model.MissionDetailModel;
import hk.com.csci4140.culife.model.TakerInfoModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;
import mehdi.sakout.fancybuttons.FancyButton;

public class HomeFragment extends BaseFragment {

    //@BindView(R.id.habbit_tablayout)
    //
    // TabLayout mTabLayout;

    @BindView(R.id.habbit_list)
    RecyclerView mRecyclerView;

    ArrayList<Map<String, String>> mSourceData = new ArrayList<Map<String, String>>();

    CatLoadingView mCatLoadingView;

    ArrayList<HomeFragmentModel> hList;

    private static final String TAG = "HomeFrag";
    //Title of this fragment
    private String mTitle;

    ArrayList<HomeFragmentModel> mList;
    ArrayList<HomeFragmentModel> mList_tmp;

    public static int habitID = 0;

    ArrayList<HomeFragmentModel> LList;

    Button mConfirmCompleteBtn;

    private ArrayList<HabitModel> mHabitList; // 总的list， this is a list of habit to be shown

    private ArrayList<HabitModel> mDisplayHabitList; // 每个tab展示的habit的list， this is a list of habit that actually displayed on each tab

    private ArrayList<HabitModel> getHabitListDetialFromJson(JSONObject response) throws JSONException {
        ArrayList<HabitModel> habitList = new ArrayList<HabitModel>();

        JSONArray habits_list = response.getJSONArray("habits");
        for (int i = 0; i < habits_list.length(); i++) {
            JSONObject value = habits_list.getJSONObject(i);
            HabitModel habit  = new HabitModel();
            habit.initHabitWithJSON(value);
            habitList.add(habit);
        }

        return habitList;
    }

    public void initHomePageDetail(JSONObject response) {
        try {
            mHabitList = getHabitListDetialFromJson(response);
            //HabitDetailFragment habitDetailFragment = new HabitDetailFragment();
            //Log.d(TAG,"mhabitLIst"+mHabitList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<HomeFragmentModel> getHabitListFromJson(JSONObject response) throws JSONException {
        ArrayList<HomeFragmentModel> HomeList = new ArrayList<HomeFragmentModel>();

        JSONArray habits_list = response.getJSONArray("habits");
        for (int i = 0; i < habits_list.length(); i++) {
            JSONObject value = habits_list.getJSONObject(i);
            HomeFragmentModel habit  = new HomeFragmentModel();
            habit.initHomePageWithJSON(value);
            HomeList.add(habit);
        }

        return HomeList;
    }

    public void initHomePageFragment(JSONObject response){
        try {
            mList = getHabitListFromJson(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void justPassTheValue(ArrayList<HomeFragmentModel> hList){
        LList = hList;
        Log.d(TAG,"pass"+LList);
    }

    public class MyClickListener implements View.OnClickListener{
        int position;

        public MyClickListener(int my_position){
            this.position = my_position;
        }
        @Override
        public void onClick(View v) {
//            showBottomSnackBar(mList_tmp.get(position).getHabitId() + "");
            JSONObject jsonParams = new JSONObject();
            JSONObject outerJsonParams = new JSONObject();
            try {
                HomeFragmentModel bean = mList_tmp.get(position);
                habitID = bean.getHabitId();
                jsonParams.put("habitid", habitID);
                outerJsonParams.put("habit",jsonParams);
                StringEntity entity = new StringEntity(outerJsonParams.toString());
                callShowSingleHabitDetailAPI(entity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialSetting() {
        setToolbarTitle(mTitle);

        //Set the bottom navigation visible
        setBottomNavFragment(true);

        //Use to set the search icon
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the toolbar title of this fragment
        mTitle = getString(R.string.home_fragment_title);
        setPrevTitle(mTitle);

        initialSetting();

        callGetProfileAPIToGetID();

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", habitID);
            outerJsonParams.put("habit", jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());
            //Log.d(TAG,"bodyentity"+jsonParams);
            callAPItoGetMember(entity);
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);
        mList = new ArrayList<HomeFragmentModel>();
        mList_tmp = new ArrayList<HomeFragmentModel>();
        initData(); // TODO: add a json object here and initial the data with it

        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    /*public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mList = new ArrayList<HomeFragmentModel>();
        mList_tmp = new ArrayList<HomeFragmentModel>();
        if (mList.isEmpty()){
            initData();
            Log.d(TAG,"resume"+hList);
        }
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //In case the duplicate menu item
        try {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreateOptionsMenu: " + e.toString());
        }

        inflater.inflate(R.menu.main, menu);

        //Search Icon
        MenuItem item = menu.findItem(R.id.action_search);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //If user has login, go to search Fragment
                if (UserModel.isLogin) {
                    setPrevTitle(mTitle);
                    replaceFragment(new SearchManagementFragment(), null);
                } else {
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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("habit is complete")
                    .setContentText("congradulation on finishing a new task")
                    .setConfirmText(getString(R.string.warning_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if(mConfirmCompleteBtn.getText().toString().equalsIgnoreCase("CHECK")){
                                Log.d(TAG,"CHECK HERE");
                                mConfirmCompleteBtn.setText("CANSLE");
                                mConfirmCompleteBtn.setBackgroundColor(getResources().getColor(R.color.greyDim));
                            } else { }
                        //mConfirmCompleteBtn.setText("确认完成");
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    };


    public void initData() {
        //mList = new ArrayList<HomeFragmentModel>();
        callShowHabitDetailListAPI();

        //Local data
        /*String longContent = "Get up early";
        String shortContent = "Work";
        for (int i = 0; i < 24; i++) {
            HomeFragmentModel bean = new HomeFragmentModel();
            if (i % 2 == 0) {
                bean.setTitle(shortContent);
                String time = i + ":00";
                bean.setTime(time);
                bean.setIdentity("Owner");
                bean.setId(i);
            } else {
                bean.setTitle(longContent);
                String time = i + ":00";
                bean.setTime(time);
                bean.setIdentity("Participate");
                bean.setId(i);
            }
            mList.add(bean);
        }*/
    }
    // API : call API and handle result
    public void callShowSingleHabitDetailAPI(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(),Constant.API_BASE_URL+"habits/show",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);

                HabitModel habit  = new HabitModel();
                habit.initHabitWithJSON(response);
                HabitDetailFragment habitDetailFragment = new HabitDetailFragment();
                habitDetailFragment.dummyHabitID = habitID;
                //Log.d(TAG,"mhabitList"+mHabitList);
                ArrayList<HabitModel> tem_HabitList = new ArrayList<>();
                tem_HabitList.add(habit);
//                habitDetailFragment.dummyHabitList = mHabitList;
                habitDetailFragment.dummyHabitList = tem_HabitList;
                JSONObject jsonParams = new JSONObject();
                JSONObject outerJsonParams = new JSONObject();
                try {
                    jsonParams.put("habitid", habitID);
                    outerJsonParams.put("habit", jsonParams);
                    StringEntity entity = new StringEntity(outerJsonParams.toString());
                    //Log.d(TAG,"bodyentity"+jsonParams);
                    callAPItoGetMember(entity);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                replaceFragment(habitDetailFragment,null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
    }

    public void callShowHabitDetailListAPI(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
        String AuthorizationToken = "Token "+UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.get(getContext(),Constant.API_BASE_URL+"habits/created",null, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);

                initHomePageFragment(response);
                //hList = mList;
                for(HomeFragmentModel bean: mList) {
                    String time = bean.getTime();
                    if (time.compareTo("null")!=0) {
                        String[] time_list = time.split(":");
                        Integer Hour = Integer.parseInt(time_list[0]);
                        Integer minute = Integer.parseInt(time_list[1]);
                        if (Hour >= 0 && Hour < 12) {
                            mList_tmp.add(bean);
                        }
                    }
                    else {
                        mList_tmp.add(bean);
                    }
                }
                //mList=mList_tmp;
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(
                        new CommonAdapter<HomeFragmentModel>(getContext(), R.layout.find_habbit_list, mList_tmp) {
                            @Override
                            public void convert(ViewHolder holder, HomeFragmentModel s,final int pos) {
                                holder.setText(R.id.other_habbit_title, s.getTitle());
                                holder.setText(R.id.time, s.getTime());
                                holder.setText(R.id.owner, s.getIdentity());
//                                holder.itemView.setOnClickListener(new MyClickListener(pos));
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        JSONObject jsonParams = new JSONObject();
                                        JSONObject outerJsonParams = new JSONObject();
                                        try {
                                            HomeFragmentModel bean = mList_tmp.get(pos);
                                            habitID = bean.getHabitId();

                                            jsonParams.put("habitid", habitID + "");
                                            outerJsonParams.put("habit",jsonParams);
                                            StringEntity entity = new StringEntity(outerJsonParams.toString());
                                            callShowSingleHabitDetailAPI(entity);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                ButterKnife.bind(this,itemView);
                                mConfirmCompleteBtn = itemView.findViewById(R.id.checkoutHabit);
                                itemView.findViewById(R.id.checkoutHabit).setOnClickListener(mOnClickListener);
                            }
                        });

                //Log.d(TAG,"initHomePage"+hList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });

        client.get(getContext(),Constant.API_BASE_URL+"habits/registered",null, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: response: "+response);

                initHomePageFragment(response);
                //hList = mList;
                for(HomeFragmentModel bean: mList) {
                    String time = bean.getTime();
                    if (time.compareTo("null")!=0) {
                        String[] time_list = time.split(":");
                        Integer Hour = Integer.parseInt(time_list[0]);
                        Integer minute = Integer.parseInt(time_list[1]);
                        if (Hour >= 0 && Hour < 12) {
                            mList_tmp.add(bean);
                        }
                    }
                    else {
                        mList_tmp.add(bean);
                    }
                }
                //mList=mList_tmp;
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setAdapter(
                        new CommonAdapter<HomeFragmentModel>(getContext(), R.layout.find_habbit_list, mList_tmp) {
                            @Override
                            public void convert(ViewHolder holder, HomeFragmentModel s,final int pos) {
                                holder.setText(R.id.other_habbit_title, s.getTitle());
                                holder.setText(R.id.time, s.getTime());
                                holder.setText(R.id.owner, s.getIdentity());
//                                holder.itemView.setOnClickListener(new MyClickListener(pos));
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        JSONObject jsonParams = new JSONObject();
                                        JSONObject outerJsonParams = new JSONObject();
                                        try {
                                            HomeFragmentModel bean = mList_tmp.get(pos);
                                            habitID = bean.getHabitId();

                                            jsonParams.put("habitid", habitID + "");
                                            outerJsonParams.put("habit",jsonParams);
                                            StringEntity entity = new StringEntity(outerJsonParams.toString());
                                            callShowSingleHabitDetailAPI(entity);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                ButterKnife.bind(this,itemView);
                                mConfirmCompleteBtn = itemView.findViewById(R.id.checkoutHabit);
                                itemView.findViewById(R.id.checkoutHabit).setOnClickListener(mOnClickListener);
                            }
                        });

                //Log.d(TAG,"initHomePage"+hList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }

    public void callGetProfileAPIToGetID(){
        if(UserModel.isLogin){
            if(UserModel.myID!=null && UserModel.myID!="" && UserModel.myID!="0"){
                return;
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setMaxRetriesAndTimeout(0,AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT);
            String AuthorizationToken = "Token "+UserModel.token;
            client.addHeader("Authorization","Token "+UserModel.token);
//            mCatLoadingView = new CatLoadingView();

//            mCatLoadingView.show(getFragmentManager(), "");

            client.get(getContext(),Constant.API_BASE_URL+"profiles/"+UserModel.myUserName,null, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "onSuccess: profiles");
                    Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                    Log.d("API_REPORT", "onSuccess: response: "+response);

//                    SessionManager.putString();
                    try {
                        JSONObject responseObject = response.getJSONObject("profile");
                        String id = String.valueOf(responseObject.getInt("id"));
                        SessionManager.putString(getContext(), Constant.USERID, id);
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "onFailure: profiles");
                    Log.d("API_REPORT", "onFailure: status : "+statusCode);
                    Log.d("API_REPORT", "onFailure: response : "+response);
                }
            });
        }else{
            return;
        }
    }

    public void callAPItoGetMember(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        //mCatLoadingView = new CatLoadingView();

        //mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/members",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                //mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: status : "+statusCode);
                Log.d("API_REPORT", "onSuccess: member_response: "+response);

                HabitDetailFragment habitDetailFragment = new HabitDetailFragment();
                habitDetailFragment.member_profiles = response;
                //replaceFragment(habitDetailFragment,null);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
        //Log.d(TAG,"outsideclient"+mList);
    }
}