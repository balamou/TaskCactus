package com.uottawa.thirstycactus.taskcactus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.uottawa.thirstycactus.taskcactus.R.id.textDate;


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
        textDate = (TextView)view.findViewById(R.id.textDate);


        return view;
    }

    public void setDate(int i, int i1, int i2)
    {
        textDate.setText(getMonth(i1) + " " +i2 + ", " + i);
    }


    /**
     * Convert int month into a string representation.
     *
     * @param m
     * @return
     */
    private String getMonth(int m)
    {
        String[] months={"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[m];
    }

}
