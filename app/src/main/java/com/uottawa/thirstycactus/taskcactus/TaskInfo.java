package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.adapters.TaskInfoAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Task;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import org.w3c.dom.Text;

public class TaskInfo extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private int task_id;

    private TextView taskNameText;
    private TextView taskDescText;
    private TextView taskPointsText;

    private ListView usersListView;


    // CONSTRUCTOR

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        // INITIALIZE GRAPHICAL ELEMENTS
        taskNameText = (TextView) findViewById(R.id.taskNameText);
        taskDescText = (TextView) findViewById(R.id.taskDescText);
        taskPointsText = (TextView) findViewById(R.id.taskPointsText);

        // GET TASK ID
        Intent intent = getIntent();
        task_id = intent.getIntExtra("TASK_ID", -2);

        if (task_id>=0)
        {
            // FETCH TASK
            Task task = dataSingleton.getTasks().get(task_id);

            // INITIALIZE LISTVIEW
            usersListView = (ListView) findViewById(R.id.usersListView);
            TaskInfoAdapter taskInfoAdapter = new TaskInfoAdapter(this, task.getTaskDates());
            usersListView.setAdapter(taskInfoAdapter);


            // SET INFORMATION INTO TEXTVIEWs
            String description = task.getDesc();

            taskNameText.setText(task.getName());
            taskDescText.setText(description.isEmpty() ? "----" : description);
            taskPointsText.setText(Integer.toString(task.getPoints()));
        }
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
        dataSingleton.deleteTask(task_id);

        ViewSingleton.getInstance().refreshTasks();
        ViewSingleton.getInstance().refresh();

        this.finish();
    }


}
