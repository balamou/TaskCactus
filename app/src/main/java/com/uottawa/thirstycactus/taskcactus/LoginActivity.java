package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;


/**
 * Created by Nathanael Adams on 11/21/17.
 */

public class LoginActivity extends AppCompatActivity
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();

    // GUI ATTRIBUTES

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText birthDayEdit;
    private EditText passwordText;

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // INITIALIZE THE GRAPHICAL OBJECTS
        firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        lastNameEdit = (EditText) findViewById(R.id.lastNameEdit);
        passwordText = (EditText) findViewById(R.id.passwordText);
        birthDayEdit = (EditText) findViewById(R.id.birthDayEdit);

    }


    // =============================================================================================

    // LISTENERS

    // =============================================================================================


    /**
     * Add/save the user to the database.
     */
    public void onSave(View view)
    {
        dataSingleton.setDefaults(); // SET DEFAULT TASKS & RESOURCES

        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String birthDay = birthDayEdit.getText().toString();
        String password = passwordText.getText().toString();

        int exit_code = dataSingleton.addUser("Parent", firstName, lastName, birthDay, password);

        String[] warning = {"New user " +  firstName + " " + lastName + " added", "Please enter the first name", "Please enter the last name", "Please enter a 4 digit password", "Invalid date format"};
        String msg = warning[exit_code];
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        if (exit_code == 0)
        {
            // Login as the user just created
            Parent p = (Parent)dataSingleton.getUsers().get(0);
            dataSingleton.login(p, p.getPIN());
            ViewSingleton.getInstance().refresh();
            this.finish();
        }
    }




}
