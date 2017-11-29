package com.uottawa.thirstycactus.taskcactus.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Resource;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import java.util.List;

/**
 * Created by Julie on 11/27/17.
 */

public class ResourcesAdapter extends ArrayAdapter
{
    // ATTRIBUTES

    private List<Resource> taskResources;
    private int task_id;

    private LayoutInflater mInflater;


    // CONSTRUCTOR

    public ResourcesAdapter(Activity context, List<Resource> taskResources, int task_id)
    {
        super(context, R.layout.resources_listview, taskResources);
        mInflater = LayoutInflater.from(context);

        this.taskResources = taskResources;
        this.task_id = task_id;
    }

    // CONSTRUCTOR

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView==null)
        {
            convertView=mInflater.inflate(R.layout.resources_listview, null);
            viewHolder= new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION ++++++++++++++++++
        Resource res = taskResources.get(position);

        viewHolder.resourceNameText.setText(res.getName());
        viewHolder.descText.setText(res.getDesc());

        viewHolder.deallocateBtn.setTag(position);
        viewHolder.deallocateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // CHECK IF LOGGED IN AS PARENT
                if (DataSingleton.getInstance().isLoggedAsParent())
                {
                    int pos = (int)view.getTag();
                    removeItem(pos);
                }
                else
                {
                    // SHOW DIALOG 
                    ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to deallocate a resource");
                }
            }
        });

        // SET UP OUTPUT INFORMATION ------------------

        return convertView;
    }

    public int getCount()
    {
        return taskResources.size();
    }

    private void removeItem(int pos)
    {
        Resource res = taskResources.get(pos);
        Task task = DataSingleton.getInstance().getTasks().get(task_id);
        task.deallocateResource(res);

        remove(pos); // built in remove method

        notifyDataSetChanged();
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView resourceNameText;
        TextView descText;
        Button deallocateBtn;

        ViewHolder(View v)
        {
            resourceNameText = v.findViewById(R.id.resourceNameText);
            descText = v.findViewById(R.id.descText);
            deallocateBtn =  v.findViewById(R.id.deallocateBtn);
        }
    }
}
