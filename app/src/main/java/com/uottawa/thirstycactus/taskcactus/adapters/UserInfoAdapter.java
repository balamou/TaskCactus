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

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by michelbalamou on 11/22/17.
 */

public class UserInfoAdapter extends ArrayAdapter
{
    // ATTRIBUTES

    private List<TaskDate> taskDates;

    private LayoutInflater mInflater;


    // CONSTRUCTOR

    public UserInfoAdapter(Activity context, List<TaskDate> taskDates)
    {
        super(context, R.layout.user_listview, taskDates);
        mInflater = LayoutInflater.from(context);

        this.taskDates = taskDates;
    }

    // CONSTRUCTOR

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView==null)
        {
            convertView=mInflater.inflate(R.layout.userinfo_listview, null);
            viewHolder= new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION ++++++++++++++++++
        TaskDate task = taskDates.get(position);

        viewHolder.taskNameText.setText(task.getTask().getName());
        viewHolder.taskDateText.setText(task.getReadableDate());

        viewHolder.removeButton.setTag(position);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int pos= (int)view.getTag();
                removeItem(pos);
            }
        });

        // SET UP OUTPUT INFORMATION ------------------

        return convertView;
    }

    public int getCount()
    {
        return taskDates.size();
    }

    private void removeItem(int pos)
    {
        taskDates.get(pos).removeLink();
        remove(pos); // built in remove method

        notifyDataSetChanged();
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView taskNameText;
        TextView taskDateText;
        Button removeButton;

        ViewHolder(View v)
        {
            taskNameText = v.findViewById(R.id.taskNameText);
            taskDateText =  v.findViewById(R.id.taskDateText);
            removeButton =  v.findViewById(R.id.removeBtn);
        }
    }
}
