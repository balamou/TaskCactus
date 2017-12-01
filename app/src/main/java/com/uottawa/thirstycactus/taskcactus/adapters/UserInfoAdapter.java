package com.uottawa.thirstycactus.taskcactus.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import java.util.List;

/**
 * Created by michelbalamou on 11/22/17.
 */

public class UserInfoAdapter extends ArrayAdapter
{
    // ATTRIBUTES

    private List<TaskDate> taskDates;

    private LayoutInflater mInflater;

    private int user_id;


    // CONSTRUCTOR

    public UserInfoAdapter(Activity context, List<TaskDate> taskDates, int user_id)
    {
        super(context, R.layout.userinfo_listview, taskDates);
        mInflater = LayoutInflater.from(context);

        this.taskDates = taskDates;
        this.user_id = user_id;
    }

    // CONSTRUCTOR

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
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
        TaskDate taskDate = taskDates.get(position);

        viewHolder.taskDateText.setText(taskDate.getReadableDate());
        viewHolder.taskCheckBox.setText(taskDate.getTask().getName());
        viewHolder.taskCheckBox.setChecked(taskDate.getCompleted());

        // SET LISTENERS
        viewHolder.taskCheckBox.setTag(position);
        viewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                boolean isChecked = ((CheckBox) view).isChecked();

                DataSingleton dataSingleton = DataSingleton.getInstance();

                // Only the person himself can set it as checked
                Person person = dataSingleton.getUsers().get(user_id);
                if (dataSingleton.getLoggedPerson() != person)
                {
                    ((CheckBox) view).setChecked(!isChecked); //reserve the checkbox
                    ViewSingleton.getInstance().showPopup(getContext(), "Please login as a " + person.getFullName() + " to change the status of the task"); // SHOW DIALOG 
                    return ; // EXIT
                }


                setChecked((int)view.getTag(), isChecked);
            }
        });

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


    @Override
    public int getCount()
    {
        return taskDates.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return taskDates.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return 0;
    }


    private void setChecked(int pos, boolean b)
    {
        taskDates.get(pos).setCompleted(b); // set completed in the association class
        DataSingleton.getInstance().setCompleted(taskDates.get(pos)); // SAVE COMPLETED IN THE DATABASE DB~

        // GUI
        ViewSingleton.getInstance().updateUserInfo(); // UPDATE THE VIEW
    }

    private void removeItem(int pos)
    {
        if (!DataSingleton.getInstance().isLoggedAsParent()) // CHECK IF LOGGED IN AS PARENT
        {
            ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to remove task"); // SHOW DIALOG 
            return ; // EXIT
        }

        TaskDate taskDate = taskDates.get(pos);
        String taskName = taskDate.getTask().getName();

        DataSingleton.getInstance().unassignTask(taskDate); // CLEAN REMOVAL

        Toast.makeText(getContext(), "Task '" + taskName + "' unassigned", Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        CheckBox taskCheckBox;
        TextView taskDateText;
        Button removeButton;

        ViewHolder(View v)
        {
            taskCheckBox = v.findViewById(R.id.taskCheckBox);
            taskDateText = v.findViewById(R.id.taskDateText);
            removeButton = v.findViewById(R.id.removeBtn);
        }
    }
}
