package com.uottawa.thirstycactus.taskcactus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.R;

import static android.R.attr.y;


/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the view with tasks.
 * Each date has a different number of tasks associated with them.
 */

public class TaskFragment extends Fragment {

    TextView textDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);
        textDate = view.findViewById(R.id.textDate);


        return view;
    }

    /**
     * Sets the date.
     * The date is used to show the tasks on that particular day.
     *
     * @param year
     * @param month
     * @param day
     * */
    public void setDate(int year, int month, int day)
    {
        textDate.setText(getMonth(month) + " " + day + ", " + year);
    }


    /**
     * Convert int month into a string representation.
     *
     * @param m integer value of the month
     * @return name of the month
     */
    private String getMonth(int m)
    {
        String[] months={"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[m];
    }

}
