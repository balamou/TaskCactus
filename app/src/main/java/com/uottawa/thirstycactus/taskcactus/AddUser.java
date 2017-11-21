package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Nathanael Adams on 11/21/17.
 *
 */

public class AddUser extends AppCompatActivity
{
    // ATTRIBUTES

    private EditText firstNameEdit;
    private EditText lastNameEdit;

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

        //
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

    }
}
