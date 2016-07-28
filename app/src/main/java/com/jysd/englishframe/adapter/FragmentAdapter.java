package com.jysd.englishframe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jysd.englishframe.fragment.ProjectFragment;

import java.util.List;

/**
 * Created by 陈渝金 on 2016/7/22.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitles;
    /**
     *
     * @param fm
     * @param titles  标题
     */
    public FragmentAdapter(FragmentManager fm , List<String> titles) {
        super(fm);
        this.mTitles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        ProjectFragment fragment=new ProjectFragment();
        return fragment;
    }

    @Override
    public int getCount() {
       return mTitles.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
