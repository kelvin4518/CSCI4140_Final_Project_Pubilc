package hk.com.csci4140.culife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by maoyuxuan(Michael Mao) on 10/04/2018.
 */

/**
 * This Adapter is for ViewPager
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "FragmentAdapter";

    private List<Fragment> mFragments ;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> mTitles) {
        super(fm);
        mFragments = fragments;
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
