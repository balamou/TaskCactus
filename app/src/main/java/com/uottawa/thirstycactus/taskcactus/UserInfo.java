package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.adapters.UserInfoAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;
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
    private ViewSingleton viewSingleton = ViewSingleton.getInstance();

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

    private Button loginBtn;
    private Button deleteBtn;


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
        loginBtn = (Button) findViewById(R.id.loginBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);

        // FILL INFORMATION
        ViewSingleton.getInstance().setUserInfo(this);
        update();

        // If current user is logged in as self
        if (dataSingleton.getLoggedPerson() == user)
        {
            loginBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

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

        userInfo.notifyDataSetChanged();
    }

    /**
     * Button to edit user
     */
    public void onEdit(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (dataSingleton.isLoggedAsParent())
        {
            Intent intent = new Intent(this, AddUser.class);
            intent.putExtra("USER_ID", user_id);
            startActivity(intent);
        }
        else
        {
            // SHOW DIALOG
            viewSingleton.showPopup(this, "Please login as a Parent to edit user");
        }
    }


    /**
     * Button that leads to assignement of tasks view
     */
    public void onAssignTask(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (dataSingleton.isLoggedAsParent())
        {
            Intent intent = new Intent(this, AssignTask.class);
            intent.putExtra("USER_ID", user_id);
            startActivity(intent);
        }
        else
        {
            // SHOW DIALOG
            viewSingleton.showPopup(this, "Please login as a Parent to assign a task");
        }
    }



    /**
     * Delete user
     */
    public void onDelete(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (dataSingleton.isLoggedAsParent())
        {
            // DELETE USER
        }
        else
        {
            // SHOW DIALOG
            viewSingleton.showPopup(this, "Please login as a Parent to delete a user");
        }
    }


    /**
     * Login
     */
    public void onLogin(View view)
    {
        Person loginTo = dataSingleton.getUsers().get(user_id);

        if (loginTo instanceof Parent)
        {
            // SHOW PASSWORD ACTIVITY
            Intent intent = new Intent(this, PINActivity.class);
            intent.getIntExtra("USR_ID", user_id);
            startActivity(intent);
        }
        else
        {
            dataSingleton.login(loginTo);
            Toast.makeText(getApplicationContext(), "Logged in as " + loginTo.getFullName(), Toast.LENGTH_SHORT).show();
        }
    }
}
