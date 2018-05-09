package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.adapter.OtherHabbitAdapter;
import hk.com.csci4140.culife.model.UserModel;

/**
 * Created by liujie(Jerry Liu) on 01/04/2018.
 */

public class RangeFragment extends BaseFragment {

    @BindView(R.id.industry_recycler_view)
    RecyclerView mRecyclerView;

    private String mTitle;
    private String mPrevTitle;

    private void initialSetting(){

        setToolbarTitle(mTitle);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment, and set the prev title
        mTitle = getString(R.string.industry_list_title);
        mPrevTitle = getPrevTitle();

        initialSetting();
    }

    @Override
    public void onResume(){
        super.onResume();

        //Set the go back icon
        setGoBackIcon();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the previous fragment's title
        setToolbarTitle(mPrevTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_range, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Initial the recycler view
        Bundle bundle = new Bundle();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        bundle.putIntegerArrayList(Constant.EDIT_SELECTED_INDUSTRY_LIST, list);
        ArrayList<Integer> selectedIndustry = getArguments().getIntegerArrayList(Constant.EDIT_SELECTED_INDUSTRY_LIST);
        ArrayList<String> industryList = getArguments().getStringArrayList(Constant.EDIT_INDUSTRY_LIST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        OtherHabbitAdapter adapter = new OtherHabbitAdapter(getContext(), industryList, selectedIndustry);
        mRecyclerView.setAdapter(adapter);
    }

}
