package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AssignTask extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private Spinner usersSpinner;
    private Spinner tasksSpinner;
    private EditText dateEdit;
    private EditText notesEdit;

    // ADD A NEW TASK

    private EditText nameEdit;
    private EditText descEdit;
    private EditText pointsEdit;

    private LinearLayout newTaskLayout;
    private Button createTaskBtn;

    private boolean newTask = false;

    private boolean fromUserInfo;
    private boolean fromTaskInfo;

    private int prevTask; // ID of previously selected task in the spinner

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);


        // LINK GRAPHICAL ELEMENTS
        usersSpinner = (Spinner) findViewById(R.id.usersSpinner);
        tasksSpinner = (Spinner) findViewById(R.id.tasksSpinner);

        dateEdit = (EditText) findViewById(R.id.dateEdit);
        notesEdit = (EditText) findViewById(R.id.notesEdit);


        nameEdit = (EditText) findViewById(R.id.nameEdit);
        descEdit = (EditText) findViewById(R.id.descEdit);
        pointsEdit = (EditText) findViewById(R.id.pointsEdit);

        newTaskLayout = (LinearLayout) findViewById(R.id.newTaskLayout);

        createTaskBtn = (Button) findViewById(R.id.createTaskBtn);

        // POPULATE INFORMATION
        populateSpinners(); // fill spinners with tasks and users

        Intent intent = getIntent();

        int user_id = intent.getIntExtra("USER_ID", -1);
        int task_id = intent.getIntExtra("TASK_ID", -1);

        fromUserInfo = user_id != -1;
        fromTaskInfo = task_id != -1;

        usersSpinner.setSelection(user_id + 1);
        tasksSpinner.setSelection(task_id + 1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        dateEdit.setText(simpleDateFormat.format(now));
    }

    /**
     * Populates task spinner with tasks
     * Populates user spinner with users
     */
    public void populateSpinners()
    {
        // USER SPINNER ++++++++++++++++++++++++++++

        // Extract full names from users into a list
        List<String> usersArray =  new ArrayList<>();
        usersArray.add("none");

        for (Person p : dataSingleton.getUsers())
            usersArray.add(p.getFullName());
        //------------------------------------------

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usersArray);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(adapter1);


        // TASK SPINNER ++++++++++++++++++++++++++++

        // Extract task names from tasks into a list
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("none");

        for (Task t : dataSingleton.getTasks())
            spinnerArray.add(t.getName());
        //------------------------------------------

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tasksSpinner.setAdapter(adapter);
    }

    // =============================================================================================

    // LISTENERS

    // =============================================================================================

    /**
     * Action when clicking on the plus button.
     * Shows the form to add a new task.
     */
    public void onAddTask(View view)
    {

        if (newTask == false)
        {
            // SHOW THE FORM
            prevTask = tasksSpinner.getSelectedItemPosition(); // save the previous task ID
            tasksSpinner.setSelection(0); // select 'none'

            newTaskLayout.setVisibility(View.VISIBLE);
            createTaskBtn.setText("-");
            newTask = true;
        }
        else
        {
            // HIDE THE FORM
            tasksSpinner.setSelection(prevTask);

            newTaskLayout.setVisibility(View.GONE);
            createTaskBtn.setText("+");
            newTask = false;
        }

    }


    /**
     * Exits activity without saving
     */
    public void onCancel(View view)
    {
        this.finish();
    }

    /**
     * Saves changes and exits the activity
     */
    public void onSave(View view)
    {
        int user_id = usersSpinner.getSelectedItemPosition() - 1;
        int task_id = tasksSpinner.getSelectedItemPosition() - 1;

        String date = dateEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String desc = descEdit.getText().toString();
        String points = pointsEdit.getText().toString();
        String note = notesEdit.getText().toString();


        int exit_code = dataSingleton.assignTask(user_id, task_id, newTask, date, name, desc, points, note);
        String[] warning = {"Task successfully assigned", "Please select user", "Please select task", "Invalid date format. Try MM/dd/yyyy", "Task name empty", "Points is not a valid number"};
        Toast.makeText(getApplicationContext(), warning[exit_code], Toast.LENGTH_SHORT).show();


        if (exit_code!=0) return ; //EXIT on failure

         // Check which activity opened the current activity; then update the corresponding fields
        if (fromUserInfo)
            ViewSingleton.getInstance().updateUserInfo();

        if (fromTaskInfo)
            ViewSingleton.getInstance().updateTaskInfo();


        this.finish();
    }

}
