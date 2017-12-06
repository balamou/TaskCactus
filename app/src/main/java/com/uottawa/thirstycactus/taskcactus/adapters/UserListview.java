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
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Parent;
import com.uottawa.thirstycactus.taskcactus.domain.Person;
import com.uottawa.thirstycactus.taskcactus.domain.Task;


import java.util.List;


/**
 * Created by michelbalamou on 11/11/17.
 *
 * Main list of all tasks displayed in the Users fragment
 */

public class UserListview extends ArrayAdapter<Person>
{
    // ATTRIBUTES

    private LayoutInflater mInflater;

    private List<Person> users;
    private DataSingleton dataSingleton = DataSingleton.getInstance();


    // CONSTRUCTOR

    public UserListview(Activity context, List<Person> users)
    {
        super(context, R.layout.user_listview, users);
        mInflater = LayoutInflater.from(context);

        this.users = users;
    }

    // METHODS

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView==null)
        {
            convertView=mInflater.inflate(R.layout.user_listview, null);
            viewHolder= new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // SET UP OUTPUT INFORMATION ++++++++++++++++++
        Person person = users.get(position);
        int tasksLeft = person.totalTasks() - person.tasksCompleted();

        // checks if the user completed all tasks and the name of the next task, if such exists
        Task nextTask = person.getNextTask();
        String status = nextTask == null ? "All tasks completed" : "Next task: " + person.getNextTask().getName();

        // Checks if person is currently logged in
        boolean loggedIn = dataSingleton.getLoggedPerson() == person;

        viewHolder.fullnameText.setText(users.get(position).getFullName() + (loggedIn ? " (me)" : ""));
        viewHolder.tasksLeftText.setText("Tasks to do: " + tasksLeft);
        viewHolder.nextTaskText.setText(status);

        if (person instanceof Parent)
            viewHolder.userImage.setImageResource(R.drawable.pic1); // parent logo
        else
            viewHolder.userImage.setImageResource(R.drawable.pic2); // child logo

        // SET UP OUTPUT INFORMATION ------------------

        return convertView;
    }


    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView fullnameText;
        TextView tasksLeftText;
        TextView nextTaskText;
        ImageView userImage;

        ViewHolder(View v)
        {
            fullnameText = v.findViewById(R.id.fullnameText);
            tasksLeftText =  v.findViewById(R.id.tasksLeftText);
            nextTaskText = v.findViewById(R.id.nextTaskText);
            userImage = v.findViewById(R.id.userImage);
        }
    }
}
