package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Resource;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import java.util.LinkedList;
import java.util.List;

import static android.R.id.list;

public class AddTask extends AppCompatActivity
{

    // GRAPHICAL ELEMENTS
    private EditText nameEdit;
    private EditText descEdit;
    private EditText pointsEdit;

    private LinearLayout checkBoxLayout;

    private int task_id;

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private CheckBox[] checkBox;
    private boolean[] initState;


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
        checkBoxLayout = (LinearLayout) findViewById(R.id.checkBoxLayout);


        // POPULATE FIELDS

        Intent intent = getIntent();
        task_id = intent.getIntExtra("TASK_ID", -2);
        List<Resource> res_task = new LinkedList<>();


        if (task_id>=0)
        {
            Task task = dataSingleton.getTasks().get(task_id);

            nameEdit.setText(task.getName());
            descEdit.setText(task.getDesc());
            pointsEdit.setText(Integer.toString(task.getPoints()));
            res_task = task.getResources(); // get resources associated to THIS task
        }

        populateCheckboxes(res_task);
    }


    // =============================================================================================

    // METHODS

    // =============================================================================================

    public void populateCheckboxes(List<Resource> res_task)
    {
        // ADD RESOURCES WITH CHECKBOXES

        List<Resource> res = DataSingleton.getInstance().getResources(); // get ALL resources
        List<String> resourceName = new LinkedList<>();

        for (Resource r : res)
            resourceName.add(r.getName()); // get name of all resources

        checkBox = new CheckBox[resourceName.size()];
        initState = new boolean[resourceName.size()];


        // CREATE CHECKBOXES
        for (int i = 0; i < resourceName.size(); i++)
        {
            TableRow row = new TableRow(this);
            row.setId(i);

            row.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)); // set layout
            checkBox[i] = new CheckBox(this); // create checkbox

            // See which checkboxes to set as checked
            if (task_id>=0 && !res_task.isEmpty())
            {
                boolean isChecked = res_task.contains(res.get(i));

                checkBox[i].setChecked(isChecked);
                initState[i] = isChecked; // save initial state
            }

            checkBox[i].setText(resourceName.get(i));
            row.addView(checkBox[i]);
            checkBoxLayout.addView(row);
        }
    }


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


        boolean[] res = new boolean[checkBox.length];
        for (int i = 0; i<checkBox.length; i++)
        {
            res[i] = checkBox[i].isChecked();
        }

        String msg;
        int exit_code;

        if (task_id == -1) // FLAG -1 means add new task
        {
            String[] warning = {"Task added", "Please enter a task name", "Points has to be a number"};
            exit_code = dataSingleton.addTask(name, desc, points, res);
            msg = warning[exit_code];
        }
        else // ANY OTHER NUMBER IS EDIT THE TASK
        {
            String[] warning = {"Task edited", "Please enter a task name", "Points has to be a number"};
            exit_code = dataSingleton.updateTask(task_id, name, desc, points, initState, res);
            msg = warning[exit_code];

            ViewSingleton.getInstance().updateTaskInfo();
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        if (exit_code == 0)
        {
            ViewSingleton.getInstance().refreshTasks();
            this.finish();
        }
    }

}
