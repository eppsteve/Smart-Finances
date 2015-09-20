package com.stevesoft.smartfinances.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stevesoft.smartfinances.ui.fragments.AccountsFragment;
import com.stevesoft.smartfinances.ui.fragments.DashboardFragment;
import com.stevesoft.smartfinances.ui.fragments.ReportsFragment;
import com.stevesoft.smartfinances.ui.fragments.TransactionsFragment;


/**
 * Created by steve on 8/16/15.
 * steve.alogaris@outlook.com
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            return new DashboardFragment();
        } else if (position == 1)
        {
            return new TransactionsFragment();
        } else if (position == 2)
        {
            return new AccountsFragment();
        } else
        {
            return new ReportsFragment();
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}