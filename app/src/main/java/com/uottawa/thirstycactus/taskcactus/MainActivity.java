package com.uottawa.thirstycactus.taskcactus;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.uottawa.thirstycactus.taskcactus.fragments.CalendarFragment;
import com.uottawa.thirstycactus.taskcactus.fragments.TaskFragment;
import com.uottawa.thirstycactus.taskcactus.fragments.UsersFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SectionsStatePagerAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d(TAG, "onCreate: Started");


        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);

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

        // Initialize the fragments
        UsersFragment usr = new UsersFragment();
        CalendarFragment cal = new CalendarFragment();
        TaskFragment task = new TaskFragment();

        cal.setTaskFragment(task); // send an instance of the taskFragment to calendarFragment

        adapter.addFragment(usr, "Users");
        adapter.addFragment(cal, "Calendar");
        adapter.addFragment(task, "Task");


        viewPager.setAdapter(adapter);
    }


    /**
     * Switches to the fragment with the according title
     *
     * @param title the title of the fragment
     */
    public void setViewPager(String title)
    {
        int num=adapter.getFragmentNum(title);

        if (num!=-1)
            mViewPager.setCurrentItem(num);
    }


    /**
     * Switches view to Users
     */
    public void switchUser(View view)
    {
        setViewPager("Users");
    }

    /**
     * Switches view to Calendar
     */
    public void switchCalendar(View view)
    {
        setViewPager("Calendar");
    }

    /**
     * Switches view to Tasks
     */
    public void switchTask(View view)
    {
        setViewPager("Task");
    }


}
