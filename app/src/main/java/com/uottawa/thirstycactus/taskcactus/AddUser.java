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


/**
 * Created by Nathanael Adams on 11/21/17.
 */

public class AddUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();
    private int user_id; // id of the user

    // GUI ATTRIBUTES

    private Spinner accountSpinner;

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText birthDayEdit;
    private EditText passwordText;

    private LinearLayout parentLayout;
    private LinearLayout accountLayout;

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
        passwordText = (EditText) findViewById(R.id.passwordText);
        birthDayEdit = (EditText) findViewById(R.id.birthDayEdit);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        accountLayout = (LinearLayout) findViewById(R.id.accountLayout);

        // SET UP SPINNER
        accountSpinner = (Spinner) findViewById(R.id.accountSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.account_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(adapter);
        accountSpinner.setOnItemSelectedListener(this);


        // CAPTURE THE FLAG SENT FROM PREVIOUS ACTIVITY
        loadUserInfo();
    }

    /**
     * Loads user information if a user was selected in the previous activity
     */
    public void loadUserInfo()
    {
        // CAPTURE THE FLAG SENT FROM PREVIOUS ACTIVITY
        Intent intent = getIntent();
        user_id = intent.getIntExtra("USER_ID", -2);

        if (user_id>=0) // FLAG: EDIT CURRENT USER
        {
            accountLayout.setVisibility(View.GONE); // HIDE THE ACCOUNT TYPE CHANGE
            Person user = dataSingleton.getUsers().get(user_id);

            firstNameEdit.setText(user.getFirstName());
            lastNameEdit.setText(user.getLastName());

            // DISPLAY BIRTH DAY
            Date birth = user.getBirthDate();

            if (birth!=null)
            {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                birthDayEdit.setText(format.format(birth));
            }

            // SHOW/HIDE password box
            if (user instanceof Parent) // show the password box only if the type of user is parent
            {
                accountSpinner.setSelection(1);
                parentLayout.setVisibility(View.VISIBLE);
                passwordText.setText(((Parent) user).getHashedPIN());
            }
        }
    }

    // =============================================================================================

    // LISTENERS

    // =============================================================================================

    /**
     * When changing the selection in the spinner;
     * Show/hide the password box
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        if (parent.getItemAtPosition(pos).equals("Child"))
        {
            parentLayout.setVisibility(View.GONE);
        }
        else if (parent.getItemAtPosition(pos).equals("Parent"))
        {
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        // Nothing happens
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
        String birthDay = birthDayEdit.getText().toString();
        String password = passwordText.getText().toString();

        // CONVERT STRING TO DATE
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
            }
        }


        // CHECKS IF THE DATA IS VALID BEFORE SUBMITTING IT
        if (firstName!=null && !firstName.isEmpty() && lastName!=null && !lastName.isEmpty())
        {
            if (user_id == -1) // FLAG: ADD NEW USER
            {
                Person newUser;

                if (accountSpinner.getSelectedItem().equals("Parent")) // ADD A PARENT
                {
                    newUser = new Parent(firstName, lastName, birth, 0, password);
                }
                else
                {
                    newUser = new Person(firstName, lastName); // ADD A CHILD

                    // CHANGE BIRTHDAY IF NOT EMPTY
                    if (birth!=null) newUser.setBirthDate(birth);
                }

                users.add(newUser);
                Toast.makeText(getApplicationContext(), "User " +  newUser.getFullName() + " added", Toast.LENGTH_SHORT).show();
            }
            else if(user_id>=0)// MODIFY AN EXISTING USER
            {
                users.get(user_id).setFirstName(firstName);
                users.get(user_id).setLastName(lastName);

                if (birth!=null) // CHANGE BIRTHDAY IF NOT EMPTY
                    users.get(user_id).setBirthDate(birth);

                ViewSingleton.getInstance().updateUserInfo();
                Toast.makeText(getApplicationContext(), "Changes to " +  users.get(user_id).getFullName() + " saved", Toast.LENGTH_SHORT).show();
            }

            ViewSingleton.getInstance().refresh();

            this.finish();
        }
    }


}
