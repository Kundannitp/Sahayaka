package com.pranks.doctalk.BlogsAndHome.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pranks.doctalk.BlogsAndHome.AllBlogs;
import com.pranks.doctalk.BlogsAndHome.MyBlogs;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int nofTabs;

    public PagerAdapter(FragmentManager fm,int NumberOfTabs){
        super(fm);
        this.nofTabs=NumberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                AllBlogs al=new AllBlogs();
                return al;
            case 1:
                MyBlogs mb=new MyBlogs();
                return mb;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nofTabs;
    }
}
