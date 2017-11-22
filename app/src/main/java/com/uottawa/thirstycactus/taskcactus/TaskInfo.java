package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import org.w3c.dom.Text;

public class TaskInfo extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private int task_id;

    private TextView taskNameText;
    private TextView taskDescText;
    private TextView taskNotesText;
    private TextView taskPointsText;


    // CONSTRUCTOR

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        // INITIALIZE GRAPHICAL ELEMENTS
        taskNameText = (TextView) findViewById(R.id.taskNameText);
        taskDescText = (TextView) findViewById(R.id.taskDescText);
        taskNotesText = (TextView) findViewById(R.id.taskNotesText);
        taskPointsText = (TextView) findViewById(R.id.taskPointsText);

        // GET TASK ID
        Intent intent = getIntent();
        task_id = intent.getIntExtra("TASK_ID", -2);

        // SET INFORMATION INTO TEXTVIEWs
        if (task_id>=0)
        {
            Task task = dataSingleton.getTasks().get(task_id);
            taskNameText.setText(task.getName());
            taskDescText.setText(task.getDesc());
            taskNotesText.setText(task.getNotes());
            taskPointsText.setText(Integer.toString(task.getPoints()));
        }
    }


    /**
     * Closes the current activity and returns to main activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }

    /**
     * Opens activity to edit task
     */
    public void onEdit(View view)
    {
        Intent intent = new Intent(this, AddTask.class);

        intent.putExtra("TASK_ID", task_id); // ID of the task

        startActivity(intent);
    }

    /**
     * Deletes task
     */
    public void onDelete(View view)
    {
        dataSingleton.getTasks().remove(task_id);
        ViewSingleton.getInstance().refreshTasks();
        this.finish();
    }


}
