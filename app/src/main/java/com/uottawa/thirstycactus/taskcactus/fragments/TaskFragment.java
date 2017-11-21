package com.uottawa.thirstycactus.taskcactus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.TaskListview;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;


/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the view with tasks.
 * Each date has a different number of tasks associated with them.
 */

public class TaskFragment extends Fragment
{
    //ATTRIBUTES

    private ListView taskList;
    private DataSingleton dataSingleton = DataSingleton.getInstance();


    // =============================================================================================

    // METHODS

    // =============================================================================================


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        taskList = view.findViewById(R.id.tasksList);
        TaskListview task = new TaskListview(getActivity(), dataSingleton.getDefaultTasks());
        taskList.setAdapter(task);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // OPEN NEW ACTIVITY WITH TASK DESCRIPTION

                //Intent intent = new Intent(getActivity(), EditUser.class);

                //intent.putExtra("USER_ID", i);

                //startActivity(intent);
            }
        });

        return view;
    }

}
