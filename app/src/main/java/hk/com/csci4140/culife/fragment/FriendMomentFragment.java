package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.FriendMomentAdapter;
import hk.com.csci4140.culife.model.FriendHabitDetailModel;
import hk.com.csci4140.culife.model.FriendMomentModel;
import hk.com.csci4140.culife.model.HomeFragmentModel;
import hk.com.csci4140.culife.model.UserModel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class
FriendMomentFragment extends BaseFragment {
    //    private RecyclerView mRecyclerView;
    ArrayList<FriendMomentModel> mList;
    ArrayList<FriendMomentModel> mList_tmp;

    ArrayList<Boolean> registerPosition;

    @BindView(R.id.friend_moment_recyclerview)
    RecyclerView mRecyclerView;

    CommonAdapter<FriendMomentModel> adapter;

    CatLoadingView mCatLoadingView;

    private ArrayList<FriendMomentModel> getHabitListFromJson(JSONObject response) throws JSONException {
        ArrayList<FriendMomentModel> FriendMomentList = new ArrayList<FriendMomentModel>();
        JSONArray habits_list = response.getJSONArray("habits");
        for (int i = 0; i < habits_list.length(); i++) {
            JSONObject value = habits_list.getJSONObject(i);
            FriendMomentModel habit  = new FriendMomentModel();
            habit.initFriendMoment(value);
            FriendMomentList.add(habit);
        }

        return FriendMomentList;
    }

    public void initHomePageFragment(JSONObject response){
        try {
            mList = getHabitListFromJson(response);
            for(int i = 0; i < mList.size(); i ++){
                if(mList.get(i).getFavorited()){
                    registerPosition.add(Boolean.TRUE);
                }
                else {
                    registerPosition.add(Boolean.FALSE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_friend_moment, container, false);
        ButterKnife.bind(this, mView);

        mList = new ArrayList<FriendMomentModel>();
        mList_tmp = new ArrayList<FriendMomentModel>();
        registerPosition = new ArrayList<>();

        initData();
        //FriendMomentAdapter adapter = new FriendMomentAdapter(mList, getContext());



        return mView;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        callShowHabitDetailListAPI();
        /*String longContent = "-->游泳、快走、慢跑、骑自行车，及一切有氧运动都能锻炼心脏。有氧运动好处多：能锻炼心肺、增强循环系统功能、燃烧脂肪、加大肺活量、降低血压，甚至能预防糖尿病，减少心脏病的发生。美国运动医学院建议，想知道有氧运动强度是否合适，可在运动后测试心率，以达到最高心率的60%—90%为宜。如果想通过有氧运动来减肥，可以选择低度到中度的运动强度，同时延长运动时间，这种方法消耗的热量更多。运动频率每周3—5次，每次20—60分钟。想要锻炼肌肉，可以练举重、做体操以及其他重复伸、屈肌肉的运动。肌肉锻炼可以燃烧热量、增强骨密度、减少受伤，尤其是关节受伤的几率，还能预防骨质疏松。 在做举重运动前，先测一下，如果连续举8次你最多能举多重的东西，就从这个重量开始练习。当你可以连续12次举起这个重量时，试试增加5%的重量。注意每次练习时，要连续举8—12次，这样可以达到肌肉最大耐力的70%—80%，锻炼效果较好。每周2—3次，但要避免连续两天锻炼同一组肌肉群， 以便让肌肉有充分的恢复时间。";
        String shortContent = "-->健身是一种体育项目，如各种徒手健美操、韵律操、形体操以及各种自抗力动作。";
        for (int i = 0; i < 20; i++) {
            FriendMomentModel bean = new FriendMomentModel();
            if (i % 2 == 0) {
                bean.setContent(i + shortContent);
                bean.setId(i);
            } else {
                bean.setContent(i + longContent);
                bean.setId(i);
            }
            mList.add(bean);
        }*/
    }


    private void callShowHabitDetailListAPI() {
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.get(getContext(), Constant.API_BASE_URL + "habits/feed", null, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "FriendonSuccess: login");
                Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                Log.d("API_REPORT", "FriendonSuccess: response: " + response);

                initHomePageFragment(response);

                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                updateRecyclerView();

                //Log.d(TAG,"CheckThismList"+mList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "FriendonFailure: login");
                Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                Log.d("API_REPORT", "FriendonFailure: response : " + response);
                showBottomSnackBar(getString(R.string.habbit_pull_fail));
            }
        });
    }

    private void updateRecyclerView(){
        adapter = new CommonAdapter<FriendMomentModel>(getContext(), R.layout.find_habbit_list, mList) {
            @Override
            public void convert(final ViewHolder holder, FriendMomentModel s, final int pos) {
                holder.setText(R.id.other_habbit_title, mList.get(pos).getTitle());
                holder.setText(R.id.time, mList.get(pos).getStart_time());
                holder.setText(R.id.owner, mList.get(pos).getAuthor_username());

                if(registerPosition.get(pos)){
                    holder.setText(R.id.checkoutHabit, "Cancel");
                    holder.getView(R.id.checkoutHabit).setBackgroundColor(getResources().getColor(R.color.greyDim));
                    ((Button)holder.getView(R.id.checkoutHabit)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            taskDeRegisterAPI(mList.get(pos).getId(), pos);
                        }
                    });
                }
                else {
                    holder.setText(R.id.checkoutHabit, "Register");
                    holder.getView(R.id.checkoutHabit).setBackgroundColor(getResources().getColor(R.color.gray_btn_bg_color));
                    ((Button)holder.getView(R.id.checkoutHabit)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            taskRegisterAPI(mList.get(pos).getId(), pos);
                        }
                    });
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FriendHabitDetailModel habit  = new FriendHabitDetailModel();
                        habit.initHabitWithModel(mList.get(pos));
                        FriendHabitDetailFragment habitDetailFragment = new FriendHabitDetailFragment();
                        habitDetailFragment.dummyHabitID = mList.get(pos).getId();
                        //Log.d(TAG,"mhabitList"+mHabitList);
                        ArrayList<FriendHabitDetailModel> mHabitList = new ArrayList<>();
                        mHabitList.add(habit);
                        habitDetailFragment.dummyHabitList = mHabitList;
                        JSONObject jsonParams = new JSONObject();
                        JSONObject outerJsonParams = new JSONObject();
                        try {
                            jsonParams.put("habitid", mList.get(pos).getId());
                            outerJsonParams.put("habit", jsonParams);
                            StringEntity entity = new StringEntity(outerJsonParams.toString());
                            //Log.d(TAG,"bodyentity"+jsonParams);
                            callAPItoGetMember(entity, habitDetailFragment);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });



//                holder.setText(R.id.time, s.getTime());
//                holder.setText(R.id.owner, s.getIdentity());
//                holder.itemView.setOnClickListener(new FriendMomentModel.MyClickListener(pos));

            }
            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                super.onViewHolderCreated(holder, itemView);
                ButterKnife.bind(this,itemView);
//                Button mConfirmCompleteBtn = itemView.findViewById(R.id.checkoutHabit);

            }
        };
        mRecyclerView.setAdapter(adapter);
    }


    private void taskRegisterAPI(final int id, final int pos){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            outerJsonParams.put("habit",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.post(getContext(), Constant.API_BASE_URL + "habits/favorite", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);

                    createCalendarAPI(id, pos);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void createCalendarAPI(int id, final int pos){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            jsonParams.put("body", "");
            jsonParams.put("score", 0);
            outerJsonParams.put("check",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.post(getContext(), Constant.API_BASE_URL + "habits/check_create", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);

                    mList.get(pos).setFavorited(Boolean.TRUE);
                    try {
                        registerPosition.set(pos, Boolean.TRUE);
                    }catch (Exception e){

                    }
                    updateRecyclerView();


                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Register success!")
                            .setContentText("congratulation on registering a new task")
                            .setConfirmText(getString(R.string.warning_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void taskDeRegisterAPI(int id, final int pos){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token " + UserModel.token;
        client.addHeader("Authorization", "Token " + UserModel.token);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", id + "");
            outerJsonParams.put("habit",jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());

            mCatLoadingView = new CatLoadingView();

            mCatLoadingView.show(getFragmentManager(), "");
            client.delete(getContext(), Constant.API_BASE_URL + "habits/favorite", entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonSuccess: login");
                    Log.d("API_REPORT", "FriendonSuccess: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonSuccess: response: " + response);

                    mList.get(pos).setFavorited(Boolean.FALSE);
                    try {
                        registerPosition.set(pos, Boolean.FALSE);
                    }catch (Exception e){

                    }
                    updateRecyclerView();


                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Register success!")
                            .setContentText("congratulation on registering a new task")
                            .setConfirmText(getString(R.string.warning_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    mCatLoadingView.dismiss();
                    Log.d("API_REPORT", "FriendonFailure: login");
                    Log.d("API_REPORT", "FriendonFailure: status : " + statusCode);
                    Log.d("API_REPORT", "FriendonFailure: response : " + response);
                    showBottomSnackBar(getString(R.string.habbit_pull_fail));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void callAPItoGetMember(StringEntity params, final FriendHabitDetailFragment habitDetailFragment){
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

                habitDetailFragment.member_profiles = response;

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
        //Log.d(TAG,"outsideclient"+mList);
    }
}
