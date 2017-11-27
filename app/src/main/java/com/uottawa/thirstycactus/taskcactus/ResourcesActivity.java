package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.uottawa.thirstycactus.taskcactus.adapters.ResAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;

public class ResourcesActivity extends AppCompatActivity
{

    // ATTRIBUTES

    private ListView resourcesListView;

    private LinearLayout newResLayout;
    private Button addBtn;


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);


        newResLayout = (LinearLayout) findViewById(R.id.newResLayout);
        addBtn = (Button) findViewById(R.id.addBtn);

        resourcesListView = (ListView) findViewById(R.id.resourcesListView);
        ResAdapter resAdapter = new ResAdapter(this, DataSingleton.getInstance().getResources());
        resourcesListView.setAdapter(resAdapter);
    }

    public void showResFields(View view)
    {
        if (newResLayout.getVisibility() == View.GONE)
        {
            newResLayout.setVisibility(View.VISIBLE);
            addBtn.setText("-");
        }
        else
        {
            newResLayout.setVisibility(View.GONE);
            addBtn.setText("+");
        }
    }

    public void onCancel(View view)
    {
        newResLayout.setVisibility(View.GONE);
        addBtn.setText("+");
    }
}
