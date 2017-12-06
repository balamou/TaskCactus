package com.uottawa.thirstycactus.taskcactus.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Resource;

import java.util.List;

/**
 * Created by Peter on 11/27/17.
 *
 * This adapter is responsible for displaying every item in the resource list.
 */

public class ResAdapter extends ArrayAdapter<Resource>
{
    // ATTRIBUTES

    private List<Resource> taskResources;
    private LayoutInflater mInflater;


    // CONSTRUCTOR

    public ResAdapter(Activity context, List<Resource> taskResources)
    {
        super(context, R.layout.res_listview, taskResources);
        mInflater = LayoutInflater.from(context);

        this.taskResources = taskResources;
    }

    // CONSTRUCTOR

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView==null)
        {
            convertView = mInflater.inflate(R.layout.res_listview, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION ++++++++++++++++++
        Resource res = taskResources.get(position);

        viewHolder.resourceNameText.setText(res.getName());

        String desc = res.getDesc();
        viewHolder.descText.setText(desc.isEmpty() ? "Desc: ----" : "Desc: " + desc);

        viewHolder.removeBtn.setTag(position);
        viewHolder.removeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                removeItem((int)view.getTag());
            }
        });


        viewHolder.editBtn.setTag(position);
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // CHECK IF LOGGED IN AS PARENT
                if (!DataSingleton.getInstance().isLoggedAsParent())
                {
                    // SHOW DIALOG 
                    ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to remove a resource");
                    return ; // EXIT
                }

                int pos = (int)view.getTag();
                showPopup(pos);

            }
        });
        // SET UP OUTPUT INFORMATION ------------------

        return convertView;
    }


    /**
     * Shows a dialog with two EditTexts to edit the name and the description of the resource
     *
     * @param pos position of the resource in the List in DataSingleton
     */
    @SuppressLint("InflateParams")
    public void showPopup(final int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View myView = mInflater.inflate(R.layout.resource_popup, null);
        builder.setView(myView);

        final EditText nameEdit = myView.findViewById(R.id.nameEdit);
        final EditText descEdit = myView.findViewById(R.id.descEdit);

        Resource res = DataSingleton.getInstance().getResources().get(pos);
        nameEdit.setText(res.getName());
        descEdit.setText(res.getDesc());

        // SETUP ACTIONS
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                editResource(pos, nameEdit.getText().toString(), descEdit.getText().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create(); // create alert dialog
        alertDialog.show(); // show it
    }


    /**
     * Edit the resource (only if logged as Parent)
     *
     * @param pos position of the resource in the List in the DataSingleton
     * @param name the new name of the Resource
     * @param desc the new description of the resource
     */
    private void editResource(int pos, String name, String desc)
    {
        if (name.isEmpty())
        {
            Toast.makeText(getContext(), "Please enter resource name", Toast.LENGTH_SHORT).show();
            return ; // EXIT;
        }

        Resource res = DataSingleton.getInstance().getResources().get(pos);
        res.setName(name);
        res.setDesc(desc);

        DataSingleton.getInstance().editResource(res); // DB~
        notifyDataSetChanged();

        Toast.makeText(getContext(), "Resource edited", Toast.LENGTH_SHORT).show();
    }


    /**
     * Removes a resource (only if logged as Parent)
     *
     * @param pos position of the resource in the ResourceList of the DataSingleton
     */
    private void removeItem(int pos)
    {
        if (!DataSingleton.getInstance().isLoggedAsParent()) // CHECK IF LOGGED IN AS PARENT
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to remove a resource");
            return ; // EXIT
        }

        Resource res = taskResources.get(pos);

        DataSingleton.getInstance().removeResource(res);

        Toast.makeText(getContext(),  "Removed resource " + res.getName(), Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }

    public int getCount()
    {
        return taskResources.size();
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView resourceNameText;
        TextView descText;
        Button removeBtn;
        Button editBtn;

        ViewHolder(View v)
        {
            resourceNameText = v.findViewById(R.id.resourceNameText);
            descText = v.findViewById(R.id.descText);
            removeBtn = v.findViewById(R.id.removeBtn);
            editBtn = v.findViewById(R.id.editBtn);
        }
    }
}
