package com.uottawa.thirstycactus.taskcactus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class manages the fragments displayed on the main activity
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter
{
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Adds fragment to a list with the according title
     *
     * @param fragment
     * @param title the title of the fragment
     */
    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    /**
    * Returns a fragment with the title
    * @param title
     * @return returns the number of the fragment, returns -1 if the fragment with the title not found
    */
    public int getFragmentNum(String title)
    {
        for (int i=0; i<mFragmentList.size(); i++)
        {
            if (mFragmentTitleList.get(i).equals(title))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }
}
