package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.adapter.FriendMomentAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class
FriendMomentFragment extends BaseFragment {
    //    private RecyclerView mRecyclerView;
    List<FriendMomentModel> mList = new ArrayList<>();

    @BindView(R.id.friend_moment_recyclerview)
    RecyclerView mRecyclerView;

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

        initData();
        FriendMomentAdapter adapter = new FriendMomentAdapter(mList, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

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
}
