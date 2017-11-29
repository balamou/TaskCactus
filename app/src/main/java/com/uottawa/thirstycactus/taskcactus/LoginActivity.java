package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.password;
import static com.uottawa.thirstycactus.taskcactus.R.id.accountLayout;
import static com.uottawa.thirstycactus.taskcactus.R.id.accountSpinner;
import static com.uottawa.thirstycactus.taskcactus.R.id.parentLayout;


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
        List<Person> users = dataSingleton.getUsers();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String birthDay = birthDayEdit.getText().toString();
        String password = passwordText.getText().toString();

        // CHECKS IF THE DATA IS VALID BEFORE SUBMITTING IT +++++++++
        Date birth = null;
        if (!birthDay.isEmpty())
        {
            try
            {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                birth = format.parse(birthDay);
            }
            catch (Exception e)
            {
                // INVALID DATE FORMAT
                Toast.makeText(getApplicationContext(), "Please enter valid date", Toast.LENGTH_SHORT).show();
            }
        }

        if (!isValid(firstName, lastName, password)) return; // EXIT

        // ADD A PARENT +++++++
        Person newUser = new Parent(firstName, lastName, birth, password);
        users.add(newUser);
        Toast.makeText(getApplicationContext(), "New user " +  newUser.getFullName() + " created", Toast.LENGTH_SHORT).show();

        this.finish();
    }


    /**
     * Checks if the information is valid and sends a toast otherwise.
     */
    public boolean isValid(String firstName, String lastName, String password)
    {

        if (firstName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter the first name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lastName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter the last name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length()<4)
        {
            Toast.makeText(getApplicationContext(), "Please enter a 4 digit password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
