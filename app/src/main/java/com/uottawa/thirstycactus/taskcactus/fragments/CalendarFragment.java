package com.uottawa.thirstycactus.taskcactus.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.uottawa.thirstycactus.taskcactus.MainActivity;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;

/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the calendar view
 *
 */

public class CalendarFragment extends Fragment
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        return view;
    }
}
