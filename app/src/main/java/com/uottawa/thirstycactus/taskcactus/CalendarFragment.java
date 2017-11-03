package com.uottawa.thirstycactus.taskcactus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the calendar view
 *
 */

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";
    private TaskFragment taskFragment;

    /**
     *  Sets a TaskFragment to associate with a CalendarFragment
     *
     * @param taskFragment
     */
    public void setTaskFragment(TaskFragment taskFragment)
    {
        this.taskFragment=taskFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_fragment, container, false);


        CalendarView calendarView=(CalendarView)view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //Toast.makeText(getActivity(), "Going to task", Toast.LENGTH_LONG).show();

                MainActivity act=(MainActivity)getActivity();
                taskFragment.setDate(i, i1, i2);
                act.setViewPager("Task");
            }
        });

        return view;
    }

}
