package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;

/**
 * Created by liujie(Jerry Liu) on 13/04/2018.
 */

public class SearchTakerResultFragment extends BaseFragment {

    private static final String TAG = "SearchTakerResultFrag";

    @BindView(R.id.taker_result_recycler_view)
    XRecyclerView mRecyclerView;

    private String mTitle;
    private String mPrevTitle;

    private void initialSetting(){
        //Set the Go Back
        setGoBackIcon();
        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.search_result);
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
        }
        setToolbarTitle(mTitle);

        initialSetting();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set previous Fragment's title
        setToolbarTitle(mPrevTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_search_taker_result, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //TODO: Get the parameter from previous fragment
        //TODO: Call API to get result
        //TODO: initialize the recycler view
        //TODO; Using the adapter: SearchTakerResultAdapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                //mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                // mRecyclerView.loadMoreComplete();
            }
        });

    }
}
