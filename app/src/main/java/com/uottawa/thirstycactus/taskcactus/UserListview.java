package com.uottawa.thirstycactus.taskcactus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.domain.Person;

import java.util.List;


/**
 * Created by michelbalamou on 11/11/17.
 */

public class UserListview extends ArrayAdapter
{
    // ATTRIBUTES

    private LayoutInflater mInflater;

    private List<Person> users;


    // CONSTRUCTOR

    public UserListview(Activity context, List<Person> users)
    {
        super(context, R.layout.user_listview, users);
        mInflater = LayoutInflater.from(context);

        this.users = users;
    }


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

        // SET UP OUTPUT INFORMATION +++
        viewHolder.name.setText(users.get(position).getFullName());
        int choresLeft = users.get(position).totalTasks() - users.get(position).tasksCompleted();
        viewHolder.num.setText("Chores to do: " + choresLeft);
        // SET UP OUTPUT INFORMATION +++

        return convertView;
    }

    // =============================================================================================

    // VIEW HOLDER: this class stores all the graphical elements that are displayed in one row

    // =============================================================================================

    class ViewHolder
    {
        TextView name;
        TextView num;

        ViewHolder(View v)
        {
            name = v.findViewById(R.id.nameText);
            num =  v.findViewById(R.id.numchoresText);
        }
    }
}
