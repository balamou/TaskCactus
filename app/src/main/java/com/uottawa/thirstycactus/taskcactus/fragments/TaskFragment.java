package com.uottawa.thirstycactus.taskcactus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.uottawa.thirstycactus.taskcactus.AddTask;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.TaskInfo;
import com.uottawa.thirstycactus.taskcactus.adapters.TaskListview;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
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
    private TaskListview taskListview;
    private Button addTaskBtn;

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
        addTaskBtn = view.findViewById(R.id.addTaskBtn);


        taskListview = new TaskListview(getActivity(), dataSingleton.getTasks());
        taskList.setAdapter(taskListview);
        ViewSingleton.getInstance().setTaskListview(taskListview);

        // ASSIGN ACTION ON ITEM CLICK
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(getActivity(), TaskInfo.class);
                intent.putExtra("TASK_ID", i); // ID of the task
                startActivity(intent);
            }
        });

        // ASSIGN ACTION TO ADD TASK BUTTON
        addTaskBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), AddTask.class);
                intent.putExtra("TASK_ID", -1); // flag -1 means new task
                startActivity(intent);
            }
        });


        return view;
    }

}
