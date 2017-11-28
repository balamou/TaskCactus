package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;
import com.uottawa.thirstycactus.taskcactus.domain.Person;

import org.w3c.dom.Text;

public class PINActivity extends AppCompatActivity
{
    // ATTRIBUTES

    private EditText PINpassEdit;
    private TextView loginAsText;


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        PINpassEdit = (EditText) findViewById(R.id.PINpassEdit);
        loginAsText = (TextView) findViewById(R.id.loginAsText);


        Intent intent = getIntent();
        int usr_id = intent.getIntExtra("USR_ID", 0);

        Person person = DataSingleton.getInstance().getUsers().get(usr_id);
        loginAsText.setText("(" + person.getFullName() + ")");
    }


    /**
     * Login: button that checks if the password was correct
     */
    public void onLogin(View view)
    {
        Intent intent = getIntent();
        int usr_id = intent.getIntExtra("USR_ID", 0);

        String PIN = PINpassEdit.getText().toString();
        DataSingleton dataSingleton = DataSingleton.getInstance();

        try
        {
            // ATTEMPT TO LOGIN AS PARENT
            Person person = dataSingleton.getUsers().get(usr_id);
            dataSingleton.login((Parent)person, PIN);
            Toast.makeText(getApplicationContext(), "Logged in as " + person.getFullName(), Toast.LENGTH_SHORT).show();
            this.finish();
        }
        catch(Exception e)
        {
            // NOT ABLE TO LOGIN AS PARENT
            Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Close the activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }
}
