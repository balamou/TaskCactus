package com.uottawa.thirstycactus.taskcactus;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Log.d(TAG, "onCreate: Started");

        mSectionsStatePagerAdapter=new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);

    }

    private void setUpViewPager(ViewPager viewPager)
    {
        SectionsStatePagerAdapter adapter=new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClalendarFragment(), "Calendar");
        adapter.addFragment(new TaskFragment(), "Task");


        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNum)
    {

        mViewPager.setCurrentItem(fragmentNum);
    }

}
