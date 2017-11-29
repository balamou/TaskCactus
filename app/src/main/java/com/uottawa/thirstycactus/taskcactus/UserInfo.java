package com.uottawa.thirstycactus.taskcactus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        Person person = dataSingleton.getUsers().get(user_id);

        // CHECK IF LOGGED IN AS PARENT
        if (!dataSingleton.isLoggedAsParent())
        {
            // SHOW DIALOG
            viewSingleton.showPopup(this, "Please login as a Parent to delete a user");
            return ; // EXIT
        }

        if (dataSingleton.getLoggedPerson() == person) // You cannot delete yourself; Login as another parent to do so
        {
            viewSingleton.showPopup(this, "You cannot delete yourself. Login as another parent to do so.");
            return ; // EXIT
        }


        // DELETE USER
        dataSingleton.deleteUser(user_id);

        ViewSingleton.getInstance().refresh();
        this.finish();
    }


    /**
     * Login
     */
    public void onLogin(View view)
    {
        final Person loginTo = dataSingleton.getUsers().get(user_id);

        if (loginTo instanceof Parent) // ATTEMPT TO LOGIN AS PARENT
        {
            // SHOW PASSWORD ACTIVITY
            Intent intent = new Intent(this, PINActivity.class);
            intent.putExtra("USR_ID", user_id); // send user id
            startActivity(intent);
        }
        else // ATTEMPT TO LOGIN AS CHILD; NO PASSWORD REQUIRED
        {
            if (dataSingleton.isLoggedAsParent())
            {
                // Alert parent that once logging in as child;
                // He/she might need a password to log back as parent.
                showDialog(loginTo);
            }
            else
            {
                loginAsChild(loginTo);
            }
        }
    }

    /**
     * Logs in as a child
     */
    private void loginAsChild(Person loginTo)
    {
        dataSingleton.login(loginTo);

        Toast.makeText(getApplicationContext(), "Logged in as " + loginTo.getFullName(), Toast.LENGTH_SHORT).show();

        if (loginTo instanceof Parent) // HIDE LOGIN and DELETE buttons
        {
            deleteBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
        }
    }

    /**
     * Alert parent that once logging in as child;
     * He/she might need a password to log back as parent
     */
    private void showDialog(final Person loginTo)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Note");
        alertDialog.setMessage("When logging in as a child a password will be required to log back on as a parent");


        // OK
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                loginAsChild(loginTo);
                dialog.dismiss();
            }
        });

        // CANCEL
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
