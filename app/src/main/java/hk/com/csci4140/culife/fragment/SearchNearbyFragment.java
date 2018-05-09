package hk.com.csci4140.culife.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.adapter.SearchNearbyAdapter;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.CreateMissionOptionModel;
import hk.com.csci4140.culife.model.NearbyListModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 10/04/2018.
 */

public class SearchNearbyFragment extends BaseFragment implements SearchNearbyAdapter.OnNearbyResultItemClickListener {

    private static final String TAG = "SearchNearbyFrag";

    @BindView(R.id.nearby_result_recycler_view)
    XRecyclerView mRecyclerView;

    private NearbyListModel listModel;
    private int totalPages;
    private int currentPage;

    SearchNearbyAdapter mAdapter;
    private boolean isRefresh = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_search_nearby, container, false);
        ButterKnife.bind(this, mView);


        return mView;
    }


    @Override
    public void onStart(){
        super.onStart();
        getBottomNav().setVisibility(View.GONE);
        setFragmentContentLayoutMarginBottom(0);

        if(listModel == null){
            currentPage = 0;
            callGetNearbyHttp(currentPage);
        }
        else {
            initialRecyclerView();
        }
    }


    //Initialize Recycler View
    private void initialRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new SearchNearbyAdapter(getContext(), listModel.getResult().getMissionLists());
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                currentPage = 0;
                isRefresh = true;
                callGetNearbyHttp(currentPage);
                //mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                currentPage ++;
                if(currentPage < totalPages){
                    callGetNearbyHttp(currentPage);
                }
                else {
                    mRecyclerView.loadMoreComplete();
                }
            }
        });
    }


    //Call API to get data
    private void callGetNearbyHttp(int page){
        //Put the parameter
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put(Constant.TOKEN, UserModel.token);
        parameter.put(Constant.NEARBY_LAT, Double.toString(getMyLatitude()));
        parameter.put(Constant.NEARBY_LNG, Double.toString(getMyLongitude()));
        int languageId = Utility.getLanguageId(getContext());
        parameter.put(Constant.NEARBY_LANGUAGE, Integer.toString(languageId));
        parameter.put(Constant.NEARBY_REGION, Integer.toString(UserModel.region));
        parameter.put(Constant.NEARBY_PAGE, Integer.toString(page));

        ObserverOnNextListener<NearbyListModel> observer = new ObserverOnNextListener<NearbyListModel>() {
            @Override
            public void onNext(NearbyListModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){


                    totalPages = model.getTotalPages();

                    if(currentPage == 0){
                        listModel = model;
                        if(isRefresh){
                            isRefresh = false;
                            mRecyclerView.refreshComplete();
                        }
                        initialRecyclerView();
                    }
                    else {
                        listModel.getResult().getMissionLists().addAll(model.getResult().getMissionLists());
                        mAdapter.notifyDataSetChanged();
                    }

                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getNearbyList(new ProgressObserver<NearbyListModel>(getContext(), observer), parameter);
    }


    @Override
    public void onLikeClick(final View v, final int position, final List<Boolean> likeItems){
        //Get the missionId
        int missionId = listModel.getResult().getMissionLists().get(position).getMissionDetail().getId();

        //Call api to add/remove fav
        ObserverOnNextListener<StandardModel> observer = new ObserverOnNextListener<StandardModel>() {
            @Override
            public void onNext(StandardModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //Set the icon status in recycler view
                    if(likeItems.get(position)){
                        ((ImageView)v).setImageResource(R.drawable.ic_star_unlike);
                        likeItems.set(position, false);
                        listModel.getResult().getMissionLists().get(position).getMissionDetail().setIsFav(Constant.FALSE);
                    }
                    else {
                        ((ImageView)v).setImageResource(R.drawable.ic_star_like);
                        likeItems.set(position, true);
                        listModel.getResult().getMissionLists().get(position).getMissionDetail().setIsFav(Constant.TRUE);
                    }
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().addMissionToFav(new ProgressObserver<StandardModel>(getContext(), observer), UserModel.token, missionId);
    }


    @Override
    public void onItemClick(View v, int position){
        //Go to Mission Detail Fragment
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.MISSION_DETAIL_MISSION_ID, listModel.getResult().getMissionLists().get(position).getMissionDetail().getId());
        setPrevTitle(getString(R.string.search_management_title));
        ((BaseFragment)getParentFragment()).replaceFragment(new MissionDetailFragment(), bundle);
    }

}
