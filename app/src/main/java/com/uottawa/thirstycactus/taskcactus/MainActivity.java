package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.uottawa.thirstycactus.taskcactus.adapters.SectionsStatePagerAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.fragments.CalendarFragment;
import com.uottawa.thirstycactus.taskcactus.fragments.TaskFragment;
import com.uottawa.thirstycactus.taskcactus.fragments.UsersFragment;

public class MainActivity extends AppCompatActivity
{
    // ATTRIBUTES

    private static final String TAG = "MainActivity";
    private SectionsStatePagerAdapter adapter;
    private ViewPager mViewPager;


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d(TAG, "onCreate: Started");


        // Check if there are any users in the database
        DataSingleton.getInstance().setMainActivity(this);

        if (DataSingleton.getInstance().getUsers().isEmpty())
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //------


        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        getSupportActionBar().setElevation(0); // Remove shadow under action bar
    }

    /**
     * Initializes 3 main fragments (UserFragment, CalendarFragment and TaskFragment)
     * and appends them to the viewPager.
     *
     * @param viewPager
     */
    private void setUpViewPager(ViewPager viewPager)
    {
        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new UsersFragment(), "Users");
        adapter.addFragment(new CalendarFragment(), "Calendar");
        adapter.addFragment(new TaskFragment(), "Task");

        viewPager.setAdapter(adapter);
    }

}
