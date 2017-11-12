package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    /**
     * Exits the current activity
     */
    public void onCancel(View view)
    {
        this.finish();
    }
}
