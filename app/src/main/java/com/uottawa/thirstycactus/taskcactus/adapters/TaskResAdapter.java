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
 *
 * Shows all resources allocated by a particular task
 */

public class TaskResAdapter extends ArrayAdapter
{
    // ATTRIBUTES

    private List<Resource> taskResources;
    private int task_id;

    
    // CONSTRUCTOR

    public TaskResAdapter(Activity context, List<Resource> taskResources, int task_id)
    {
        super(context, R.layout.resources_listview, taskResources);

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.resources_listview, null);
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
        String desc = res.getDesc();
        viewHolder.descText.setText(desc.isEmpty() ? "Desc: ----" : "Desc: " + desc);

        viewHolder.deallocateBtn.setTag(position);
        viewHolder.deallocateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                removeItem((int)view.getTag());
            }
        });

        // SET UP OUTPUT INFORMATION ------------------

        return convertView;
    }

    public int getCount()
    {
        return taskResources.size();
    }


    /**
     * Removed a resource from a task (only if logged as parent)
     *
     * @param res_id position of the item clicked in this list (Resource list)
     */
    private void removeItem(int res_id)
    {
        if (!DataSingleton.getInstance().isLoggedAsParent()) // CHECK IF LOGGED IN AS PARENT
        {
            ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to deallocate a resource");
            return ; // EXIT
        }

        Resource res = taskResources.get(res_id); // get resource
        Task task = DataSingleton.getInstance().getTasks().get(task_id); // get task


        DataSingleton.getInstance().deallocateResource(res, task); // CLEAN REMOVAL

        Toast.makeText(getContext(), "Resource deallocated", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged(); // REFRESH
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
