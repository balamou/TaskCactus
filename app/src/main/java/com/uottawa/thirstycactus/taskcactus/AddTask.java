package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

public class AddTask extends AppCompatActivity
{

    // GRAPHICAL ELEMENTS
    private EditText nameEdit;
    private EditText descEdit;
    private EditText pointsEdit;

    private int task_id;

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    // CONSTRUCTOR

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // LINK GRAPHICAL ELEMENTS
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        descEdit = (EditText) findViewById(R.id.descEdit);
        pointsEdit = (EditText) findViewById(R.id.pointsEdit);


        Intent intent = getIntent();
        task_id = intent.getIntExtra("TASK_ID", -2);

        if (task_id>=0)
        {
            Task task = dataSingleton.getTasks().get(task_id);

            if (task!=null)
            {
                nameEdit.setText(task.getName());
                descEdit.setText(task.getDesc());
                pointsEdit.setText(Integer.toString(task.getPoints()));
            }
        }
    }


    // =============================================================================================

    // METHODS

    // =============================================================================================


    /**
     * Exits the current activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }

    public void onSave(View view)
    {
        String name = nameEdit.getText().toString();
        String desc = descEdit.getText().toString();
        String points = pointsEdit.getText().toString();

        int p = 5;

        // Check if the points is a valid number
        try
        {
            p = Integer.parseInt(points);
        }
        catch (Exception e)
        {
            p = 5;
        }


        if (task_id == -1) // flag -1 means add new task
        {
            Task task = new Task(name, desc, p);

            dataSingleton.getTasks().add(task);
        }
        else if(task_id>=0) // flag>=0 is the ID of the task to be edited
        {
            Task task = dataSingleton.getTasks().get(task_id);

            task.setName(name);
            task.setDesc(desc);
            task.setPoints(p);

            ViewSingleton.getInstance().updateTaskInfo();
        }

        ViewSingleton.getInstance().refreshTasks();

        this.finish();
    }

}
