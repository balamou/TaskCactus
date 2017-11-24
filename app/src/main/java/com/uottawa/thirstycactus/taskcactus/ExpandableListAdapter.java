package com.uottawa.thirstycactus.taskcactus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.domain.Person;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import java.util.Date;
import java.util.List;


/**
 * Created by Julie on 11/24/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    // ATTRIBUTES

    private Context context;
    private List<Person> people;

    private Date date;


    // CONSTRUCTOR
    public ExpandableListAdapter(Context context, List<Person> people, Date date)
    {
        this.context = context;
        this.people = people;
        this.date = date;
    }


    public void setPeople(List<Person> people)
    {
        this.people = people;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        List<TaskDate> taskDates = people.get(groupPosition).getTaskDates(date);

        return taskDates.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent)
    {
        TaskDate child = (TaskDate)getChild(groupPosition, childPosition);

        if (view == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView textChild = view.findViewById(R.id.textChild);
        textChild.setText(child.getTask().getName());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        List<TaskDate> taskDates = people.get(groupPosition).getTaskDates(date);
        return taskDates.size();
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        return people.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return people.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent)
    {
        Person person = (Person) getGroup(groupPosition);

        if (view == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView textHeader = view.findViewById(R.id.textHeader);
        textHeader.setText(person.getFullName());

        return view;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}