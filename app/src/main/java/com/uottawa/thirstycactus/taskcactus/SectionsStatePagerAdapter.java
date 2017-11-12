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
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitle = new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Adds fragment to a list with the according title
     *
     * @param fragment the fragment to add
     * @param title the title of the fragment
     */
    public void addFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        fragmentListTitle.add(title);
    }

    /**
     * Returns a fragment with the title
     *
     * @param title the title of the fragment
     * @return returns the number of the fragment, returns -1 if the fragment with the title not found
    */
    public int getFragmentNum(String title)
    {
        for (int i = 0; i< fragmentList.size(); i++)
            if (fragmentListTitle.get(i).equals(title))
                return i;

        return -1;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }
}
