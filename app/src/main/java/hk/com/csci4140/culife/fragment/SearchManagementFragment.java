package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.adapter.FragmentAdapter;

/**
 * Created by zhenghao(Kelvin Zheng) on 10/04/2018.
 */

public class SearchManagementFragment extends BaseFragment {

    private static final String TAG = "SearchManagementFrag";

    @BindView(R.id.management_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.management_view_pager)
    ViewPager mViewPager;


    private String mTitle;
    private String mPrevTitle;


    private void initialSetting(){
        //Set the Go back Icon
        setGoBackIcon();
        //Set Login Icon invisibility
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.search_management_title);
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
        View mView = inflater.inflate(R.layout.fragment_search_management, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Initial the view pager
        initView();
    }


    private void initView() {

        //Initial the tab item and title
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.search_management_tab_1_title));
        titles.add(getString(R.string.search_management_tab_2_title));
        titles.add(getString(R.string.search_management_tab_3_title));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        //Initial the view pager
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SearchNormalFragment());
        fragments.add(new SearchNearbyFragment());
        fragments.add(new SearchCollectionFragment());

        FragmentAdapter adapter = new FragmentAdapter(this.getChildFragmentManager(),fragments, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

}
