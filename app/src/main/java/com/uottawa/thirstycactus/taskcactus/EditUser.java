package com.uottawa.thirstycactus.taskcactus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditUser extends AppCompatActivity {

    private EditText fnameEdit;
    private EditText lnameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent();


        fnameEdit = (EditText)findViewById(R.id.fnameEdit);
        lnameEdit = (EditText)findViewById(R.id.lnameEdit);


        fnameEdit.setText(intent.getStringExtra("F_NAME"));
        lnameEdit.setText(intent.getStringExtra("L_NAME"));
    }

    /**
     * Exits the current activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }

}
