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
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by michelbalamou on 11/23/17.
 *
 * Displays the list of users that have been assigned to a Particular task.
 */

public class TaskInfoAdapter extends ArrayAdapter<TaskDate>
{
    // ATTRIBUTES

    private List<TaskDate> taskDates;

    // CONSTRUCTOR

    public TaskInfoAdapter(Activity context, List<TaskDate> taskDates)
    {
        super(context, R.layout.taskinfo_listview, taskDates);

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.taskinfo_listview, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION ++++++++++++++++++
        TaskDate task = taskDates.get(position);

        viewHolder.personNameText.setText(task.getPerson().getFullName());
        viewHolder.taskDateText.setText(task.getReadableDate());

        viewHolder.removeButton.setTag(position);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener()
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
        return taskDates.size();
    }


    /**
     * Unassigns a user from a task
     *
     * @param pos position of the item that has been clicked
     */
    private void removeItem(int pos)
    {
        if (!DataSingleton.getInstance().isLoggedAsParent()) // CHECK IF LOGGED IN AS PARENT
        {
            // SHOW DIALOG 
            ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to unassign a task");
            return ; // EXIT
        }

        TaskDate taskDate = taskDates.get(pos);
        String taskName = taskDate.getTask().getName(); // get name of the task

        DataSingleton.getInstance().unassignTask(taskDate); // CLEAN REMOVAL

        Toast.makeText(getContext(), "Task '" + taskName + "' unasigned", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged(); // REFRESH
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView personNameText;
        TextView taskDateText;
        Button removeButton;

        ViewHolder(View v)
        {
            personNameText = v.findViewById(R.id.personNameText);
            taskDateText =  v.findViewById(R.id.taskDateText);
            removeButton =  v.findViewById(R.id.removeBtn);
        }
    }
}
