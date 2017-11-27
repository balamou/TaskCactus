package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ResourcesActivity extends AppCompatActivity
{

    // ATTRIBUTES

    private ListView resourcesListView;



    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);


        resourcesListView = (ListView) findViewById(R.id.resourcesListView);

    }
}
