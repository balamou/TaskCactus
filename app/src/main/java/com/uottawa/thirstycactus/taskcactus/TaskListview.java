package com.uottawa.thirstycactus.taskcactus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by michelbalamou on 11/11/17.
 */

public class TaskListview extends ArrayAdapter<String> {

    private String[] taskname;
    private LayoutInflater mInflater;

    public TaskListview(Activity context, String[] taskname)
    {
        super(context, R.layout.user_listview, taskname);

        this.taskname = taskname;

        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView==null)
        {
            convertView=mInflater.inflate(R.layout.task_listview, null);
            viewHolder= new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tasknameText.setText(taskname[position]);

        return convertView;
    }

    class ViewHolder
    {
        TextView tasknameText;

        ViewHolder(View v)
        {
            tasknameText = v.findViewById(R.id.taskNameText);
        }
    }
}
