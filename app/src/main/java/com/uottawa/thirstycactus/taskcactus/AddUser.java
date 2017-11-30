package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    private ImageView userImage;

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


        userImage = (ImageView) findViewById(R.id.userImage);

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

                userImage.setImageResource(R.drawable.pic1); // parent logo
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

            userImage.setImageResource(R.drawable.pic2); // child logo
        }
        else if (parent.getItemAtPosition(pos).equals("Parent"))
        {
            parentLayout.setVisibility(View.VISIBLE);
            userImage.setImageResource(R.drawable.pic1); // parent logo
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
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String birthDay = birthDayEdit.getText().toString();
        String password = passwordText.getText().toString();

        String msg;
        int exit_code;

        if (user_id == -1) // FLAG TO ADD NEW USER
        {
            exit_code = dataSingleton.addUser((String) accountSpinner.getSelectedItem(), firstName, lastName, birthDay, password);

            String[] warning = {"User " +  firstName + " " + lastName + " added", "Please enter the first name", "Please enter the last name", "Please enter a 4 digit password", "Invalid date format"};
            msg = warning[exit_code];
        }
        else // FLAG TO EDIT EXISTING USER
        {
            exit_code = dataSingleton.updateUser(user_id, firstName, lastName, birthDay, password);

            String[] warning = {"Changes to " +  firstName + " " + lastName + " saved", "Please enter the first name", "Please enter the last name", "Please enter a 4 digit password", "Invalid date format"};
            msg = warning[exit_code];

            ViewSingleton.getInstance().updateUserInfo();
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        // SUCCESS: terminate activity & refresh list view
        if (exit_code == 0)
        {
            ViewSingleton.getInstance().refresh();
            this.finish();
        }
    }


}
