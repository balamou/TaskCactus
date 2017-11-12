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

public class UserListview extends ArrayAdapter<String> {

    private String[] username;
    private int[] chores;
    private Activity context;
    private LayoutInflater mInflater;

    public UserListview(Activity context, String[] username, int[] chores)
    {
        super(context, R.layout.user_listview, username);

        this.context = context;
        this.username = username;
        this.chores = chores;

        mInflater = LayoutInflater.from(context);
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


        viewHolder.name.setText(username[position]);
        viewHolder.num.setText("Chores to do: " + Integer.toString(chores[position]));

        return convertView;
    }

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
