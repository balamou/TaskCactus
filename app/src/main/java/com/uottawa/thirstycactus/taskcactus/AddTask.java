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

            if (task!=null)
            {
                nameEdit.setText(task.getName());
                descEdit.setText(task.getDesc());
                pointsEdit.setText(Integer.toString(task.getPoints()));
                res_task = task.getResources(); // get resources associated to THIS task
            }
        }


        // ADD RESOURCES WITH CHECKBOXES

        List<Resource> res = DataSingleton.getInstance().getResources(); // get ALL resources
        List<String> resourceName = new LinkedList<>();

        for (Resource r : res)
            resourceName.add(r.getName());

        checkBox = new CheckBox[resourceName.size()];
        initState = new boolean[resourceName.size()];

        for (int i = 0; i < resourceName.size(); i++)
        {
            TableRow row = new TableRow(this);
            row.setId(i);

            row.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            checkBox[i] = new CheckBox(this);
            //checkBox.setId(i);

            if (task_id>=0 && !res_task.isEmpty())
            {
                boolean isChecked = res_task.contains(res.get(i));
                checkBox[i].setChecked(isChecked);
                initState[i] = isChecked;
            }

            checkBox[i].setText(resourceName.get(i));
            row.addView(checkBox[i]);
            checkBoxLayout.addView(row);
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
        List<Resource> res = dataSingleton.getResources();

        String name = nameEdit.getText().toString();
        String desc = descEdit.getText().toString();
        String points = pointsEdit.getText().toString();

        int p;

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

            // ALLOCATE RESOURCES
            // that have the checkboxes on
            for (int i = 0; i<checkBox.length ; i++)
            {
                if (checkBox[i].isChecked())
                    task.allocateResource(res.get(i));
            }
            //--------------------

            dataSingleton.getTasks().add(task);
        }
        else if(task_id>=0) // flag>=0 is the ID of the task to be edited
        {
            Task task = dataSingleton.getTasks().get(task_id);

            task.setName(name);
            task.setDesc(desc);
            task.setPoints(p);


            // ALLOCATE RESOURCES/ possible deallocation of resources
            // that have the checkboxes on
            for (int i = 0; i<checkBox.length; i++)
            {
                if (initState[i] != checkBox[i].isChecked()) // check if the state is different
                {
                    if (checkBox[i].isChecked()) // Allocate a resource that wasn't allocated yet
                        task.allocateResource(res.get(i));
                    else // Deallocate a resource that was allocated
                        task.deallocateResource(res.get(i));
                }

            }
            //--------------------

            ViewSingleton.getInstance().updateTaskInfo();
        }




        ViewSingleton.getInstance().refreshTasks();

        this.finish();
    }

}
