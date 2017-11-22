package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.util.List;

/**
 * Created by Nathanael Adams on 11/21/17.
 *
 */

public class AddUser extends AppCompatActivity
{
    // ATTRIBUTES

    private EditText firstNameEdit;
    private EditText lastNameEdit;

    private DataSingleton dataSingleton = DataSingleton.getInstance();
    private int user_id;

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // INITIALIZE THE GRAPHICAL OBJECTS
        firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        lastNameEdit = (EditText) findViewById(R.id.lastNameEdit);

        // CAPTURE THE FLAG SENT FROM PREVIOUS ACTIVITY
        Intent intent = getIntent();
        user_id = intent.getIntExtra("USER_ID", -2);

        if (user_id>=0) // FLAG: EDIT CURRENT USER
        {
            Person user = dataSingleton.getUsers().get(user_id);

            firstNameEdit.setText(user.getFirstName());
            lastNameEdit.setText(user.getLastName());
        }
    }

    /**
     * Exits the current activity.
     */
    public void onCancel(View view)
    {
        this.finish();
    }


    /**
     * Add/save the user to the database.
     */
    public void onSave(View view)
    {
        List<Person> users = dataSingleton.getUsers();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();

        // CHECKS IF THE DATA IS VALID BEFORE SUBMITTING IT
        if (firstName!=null && !firstName.isEmpty() && lastName!=null && !lastName.isEmpty())
        {
            if (user_id == -1) // FLAG ADD NEW USER
            {
                Person newUser = new Person(firstName, lastName);

                users.add(newUser);
            }
            else if(user_id>=0)// MODIFY AN EXISTING USER
            {
                users.get(user_id).setFirstName(firstName);
                users.get(user_id).setLastName(lastName);
            }

            ViewSingleton.getInstance().refresh();

            this.finish();
        }
    }


}