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
import com.uottawa.thirstycactus.taskcactus.domain.Resource;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import java.util.List;

/**
 * Created by Peter on 11/27/17.
 */

public class ResAdapter extends ArrayAdapter
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
        viewHolder.descText.setText(res.getDesc());

        viewHolder.removeBtn.setTag(position);
        viewHolder.removeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int pos = (int)view.getTag();
                removeItem(pos);
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
        List<Task> tasks = res.getTasks(); // every task that allocated this resource

        for (Task t : tasks)
            t.deallocateResource(res);

        //remove(pos); // built in remove method

        taskResources.remove(pos);

        Toast.makeText(getContext(),  "Removing " + res.getName(), Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView resourceNameText;
        TextView descText;
        Button removeBtn;

        ViewHolder(View v)
        {
            resourceNameText = v.findViewById(R.id.resourceNameText);
            descText = v.findViewById(R.id.descText);
            removeBtn =  v.findViewById(R.id.removeBtn);
        }
    }
}
