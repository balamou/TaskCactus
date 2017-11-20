package com.uottawa.thirstycactus.taskcactus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uottawa.thirstycactus.taskcactus.R;



/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the view with tasks.
 * Each date has a different number of tasks associated with them.
 */

public class TaskFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        return view;
    }

}
