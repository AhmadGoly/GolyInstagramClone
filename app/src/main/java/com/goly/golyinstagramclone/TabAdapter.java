package com.goly.golyinstagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    //superClass Function: nothing edited in this func
    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //superClass Function: totally edited this func. first it was returning null and now its returning fragments.
        switch (position) {
            case 0:
                return new ProfileTab();
            case 1:
                return new UsersTab();
            case 2:
                return new SharePictureTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //superClass Function: edited from 0 to 3
        return 3;
    }
    //added this function to set title.
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Profile";
            case 1:
                return "Users";
            case 2:
                return "Share Picture";
            default:
                return "Social Media";
        }
    }
}
