package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.adapters.UserInfoAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.util.Date;

/**
 * Created by michelbalamou on 11/2/17.
 *
 */

public class UserInfo extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    private int user_id; // id of the user; the order the user appears in the global user list
    private Person user; // pointer to the user with the id


    // GRAPHICAL ATTRIBUTES

    private TextView nameText;

    private TextView allocatedTasks;
    private TextView tasksCompleted;
    private TextView totalPoints;
    private TextView birthDayText;

    private ListView tasksListView;
    private UserInfoAdapter userInfo;

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        // RECEIVE DATA FROM PREVIOUS ACTIVITY
        Intent intent = getIntent();

        user_id = intent.getIntExtra("USER_ID", 0);
        user = dataSingleton.getUsers().get(user_id);

        // SETUP LIST VIEW
        tasksListView = (ListView)findViewById(R.id.tasksListView);
        userInfo = new UserInfoAdapter(this, user.getTaskDates());
        tasksListView.setAdapter(userInfo);

        // SET UP ALL GRAPHICAL OBJECTS
        nameText = (TextView) findViewById(R.id.nameText);
        allocatedTasks = (TextView) findViewById(R.id.allocatedTasksText);
        tasksCompleted = (TextView) findViewById(R.id.tasksCompleted);
        totalPoints = (TextView) findViewById(R.id.totalPoints);
        birthDayText = (TextView) findViewById(R.id.birthDayText);

        // FILL INFORMATION
        ViewSingleton.getInstance().setUserInfo(this);
        update();
    }

    /**
     * Updates the information displayed about the user
     */
    public void update()
    {
        nameText.setText(user.getFullName());

        allocatedTasks.setText(Integer.toString(user.totalTasks()));
        tasksCompleted.setText(Integer.toString(user.tasksCompleted()));
        totalPoints.setText(Integer.toString(user.getPoints()));

        birthDayText.setText(user.getReadableBirthday());
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


    /**
     * Button that leads to assignement of tasks view
     */
    public void onAssignTask(View view)
    {
        Intent intent = new Intent(this, AssignTask.class);
        intent.putExtra("USER_ID", user_id);
        startActivity(intent);
    }
}
