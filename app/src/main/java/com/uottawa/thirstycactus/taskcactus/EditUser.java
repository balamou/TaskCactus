package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.util.Date;

/**
 * Created by michelbalamou on 11/2/17.
 *
 */

public class EditUser extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();
    private int user_id; // id of the user; the order the user appears in the global user list
    private Person user; // pointer to the user with the id


    // GRAPHICAL ATTRIBUTES

    private TextView nameText;
    private TextView numOfTasks;
    private TextView tasksDone;
    private TextView totalPoints;
    private TextView birthDayText;


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // RECEIVE DATA FROM PREVIOUS ACTIVITY
        Intent intent = getIntent();

        user_id = intent.getIntExtra("USER_ID", 0);
        user = dataSingleton.getUsers().get(user_id);

        // SET UP ALL GRAPHICAL OBJECTS
        nameText = (TextView)findViewById(R.id.nameText);
        numOfTasks = (TextView)findViewById(R.id.numOfTasks);
        tasksDone = (TextView)findViewById(R.id.tasksDone);
        totalPoints = (TextView)findViewById(R.id.totalPoints);
        birthDayText = (TextView)findViewById(R.id.birthDayText);

        // FILL INFORMATION
        nameText.setText(user.getFullName());

        numOfTasks.setText("Total number of tasks: " + user.tasksCompleted());
        tasksDone.setText("Tasks done: " + user.totalTasks());
        totalPoints.setText("Total points: " + user.getPoints());

        Date birthday =  user.getBirthDate();
        birthDayText.setText("Birthday: " + (birthday == null ? "Not set" : birthday));
    }

    /**
     * Exits the current activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }

    /**
     * Button to edit user
     */
    public void onEdit(View view)
    {
        Intent intent = new Intent(this, AddUser.class);
        intent.putExtra("USER_ID", user_id);
        startActivity(intent);
    }

}
