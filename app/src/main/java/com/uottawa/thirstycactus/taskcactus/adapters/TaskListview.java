package com.uottawa.thirstycactus.taskcactus.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.domain.Task;

import java.util.List;


/**
 * Created by michelbalamou on 11/11/17.
 */

public class TaskListview extends ArrayAdapter
{
    // ATTRIBUTES

    private LayoutInflater mInflater;

    private List<Task> tasks;

    // CONSTRUCTOR

    public TaskListview(Activity context, List<Task> tasks)
    {
        super(context, R.layout.task_listview, tasks);
        mInflater = LayoutInflater.from(context);

        this.tasks = tasks;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.task_listview, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION +++
        viewHolder.tasknameText.setText(tasks.get(position).getName());

        if (tasks.get(position).getType() == "NEW") // hide icon if the task is not default
            viewHolder.taskImage.setVisibility(View.GONE);
        // SET UP OUTPUT INFORMATION ---

        return convertView;
    }


    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView tasknameText;
        ImageView taskImage;

        ViewHolder(View v)
        {
            tasknameText = v.findViewById(R.id.taskNameText);
            taskImage = v.findViewById(R.id.taskImage);
        }
    }
}
