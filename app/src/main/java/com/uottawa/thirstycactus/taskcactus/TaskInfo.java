package com.uottawa.thirstycactus.taskcactus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.adapters.ResourcesAdapter;
import com.uottawa.thirstycactus.taskcactus.adapters.TaskInfoAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Task;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import org.w3c.dom.Text;

import static android.R.attr.id;

public class TaskInfo extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private int task_id;

    private TextView taskNameText;
    private TextView taskDescText;
    private TextView taskPointsText;

    private ListView usersListView; // list of users the task has been assigned to
    private ListView resourcesListView; // list of allocated resources


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

        usersListView = (ListView) findViewById(R.id.usersListView);
        resourcesListView = (ListView) findViewById(R.id.resourcesListView);

        ViewSingleton.getInstance().setTaskInfo(this);

        // GET TASK ID
        Intent intent = getIntent();
        task_id = intent.getIntExtra("TASK_ID", -2);

        update();
    }


    /**
     * Updates the fields with information about the Task
     */
    public void update()
    {
        if (task_id>=0)
        {
            // FETCH TASK
            Task task = dataSingleton.getTasks().get(task_id);

            // INITIALIZE USER LISTVIEW
            TaskInfoAdapter taskInfoAdapter = new TaskInfoAdapter(this, task.getTaskDates());
            usersListView.setAdapter(taskInfoAdapter);

            // INITIALIZE RESOURCES LISTVIEW
            ResourcesAdapter resAdapter = new ResourcesAdapter(this, task.getResources(), task_id);
            resourcesListView.setAdapter(resAdapter);


            // SET INFORMATION INTO TEXTVIEWs
            String description = task.getDesc();

            taskNameText.setText(task.getName());
            taskDescText.setText(description.isEmpty() ? "----" : description);
            taskPointsText.setText(Integer.toString(task.getPoints()));

            taskInfoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Opens activity to edit task
     */
    public void onEdit(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (dataSingleton.isLoggedAsParent())
        {
            Intent intent = new Intent(this, AddTask.class);
            intent.putExtra("TASK_ID", task_id); // ID of the task
            startActivity(intent);
        }
        else
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(this, "Please login as a Parent to edit a task");
        }
    }

    /**
     * Deletes task
     */
    public void onDelete(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (!dataSingleton.isLoggedAsParent())
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(this, "Please login as a Parent to delete a task");
            return;
        }

        // CONFIRMATION BOX ++++
        AlertDialog.Builder b = ViewSingleton.getInstance().confirmationBox(this, "Are you sure you want to delete the task? ");

        final TaskInfo taskInfo = this;

        b.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                dataSingleton.deleteTask(task_id);

                ViewSingleton.getInstance().refreshTasks();
                ViewSingleton.getInstance().refresh();
                taskInfo.finish();
            }
        });

        b.show();
    }


    /**
     * Button that leads to assignment of tasks view
     */
    public void onAssignTask(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (dataSingleton.isLoggedAsParent())
        {
            Intent intent = new Intent(this, AssignTask.class);
            intent.putExtra("TASK_ID", task_id);
            startActivity(intent);
        }
        else
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(this, "Please login as a Parent to assign a task");
        }
    }


}
