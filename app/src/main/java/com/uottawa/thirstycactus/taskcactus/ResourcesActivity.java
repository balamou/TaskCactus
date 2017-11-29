package com.uottawa.thirstycactus.taskcactus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.adapters.ResAdapter;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Resource;

public class ResourcesActivity extends AppCompatActivity
{

    // ATTRIBUTES

    private ListView resourcesListView;
    private ResAdapter resAdapter;

    private LinearLayout newResLayout;
    private Button addBtn;

    private EditText nameEdit;
    private EditText descEdit;


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
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        descEdit = (EditText) findViewById(R.id.descEdit);


        resourcesListView = (ListView) findViewById(R.id.resourcesListView);
        resAdapter = new ResAdapter(this, DataSingleton.getInstance().getResources());
        resourcesListView.setAdapter(resAdapter);
    }

    /**
     * SHOWS/HIDES the from 'add a resource';
     * It is located right above the listview.
     */
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

    /**
     * Hides the form 'add a resource'
     */
    public void onCancel(View view)
    {
        newResLayout.setVisibility(View.GONE);
        addBtn.setText("+");
    }

    /**
     * Saves a new resource
     */
    public void onSave(View view)
    {
        // CHECK IF LOGGED IN AS PARENT
        if (!DataSingleton.getInstance().isLoggedAsParent())
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(this, "Please login as a Parent to add a new resource");
            return ; // EXIT
        }

        String name = nameEdit.getText().toString();
        String desc = descEdit.getText().toString();

        // MAKES SURE THE RESOURCE NAME IS NOT EMPTY
        if (name.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a resource name", Toast.LENGTH_SHORT).show();
            return ;
        }

        Resource res = new Resource(name, desc);

        DataSingleton.getInstance().getResources().add(res);

        Toast.makeText(getApplicationContext(), "Resource '" + name + "' added", Toast.LENGTH_SHORT).show();

        // REFRESH LISTVIEW
        resAdapter.notifyDataSetChanged();

        // RESET FORMS
        newResLayout.setVisibility(View.GONE);
        addBtn.setText("+");
        nameEdit.setText("");
        descEdit.setText("");
    }
}
