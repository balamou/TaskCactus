package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        // POPULATE INFORMATION
        populateSpinners(); // fill spinners with tasks and users

        Intent intent = getIntent();

        int user_id = intent.getIntExtra("USER_ID", -1);
        int task_id = intent.getIntExtra("TASK_ID", -1);

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try
        {
            int user_id = usersSpinner.getSelectedItemPosition() - 1;
            int task_id = tasksSpinner.getSelectedItemPosition() - 1;

            // STOP IF USER NOT SELECTED
            if (user_id == -1)
            {
                Toast.makeText(getApplicationContext(), "Please select user", Toast.LENGTH_SHORT).show();
                return ;
            }

            // STOP IF TASK NOT SELECTED
            if (task_id == -1)
            {
                Toast.makeText(getApplicationContext(), "Please select task", Toast.LENGTH_SHORT).show();
                return ;
            }

            // FIND USER
            Person p = dataSingleton.getUsers().get(user_id);
            Task t = dataSingleton.getTasks().get(task_id);

            Date date = simpleDateFormat.parse(dateEdit.getText().toString());
            String note = notesEdit.getText().toString();

            p.assignTask(t, date, false, note);

            this.finish();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Invalid date format. Try MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
        }

    }

}
