package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class EditUser extends AppCompatActivity {

    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent();


        nameText = (TextView)findViewById(R.id.nameText);

        String fullName = intent.getStringExtra("F_NAME") + " " + intent.getStringExtra("L_NAME");
        nameText.setText(fullName);
    }

    /**
     * Exits the current activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }

}
