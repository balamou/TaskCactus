package com.uottawa.thirstycactus.taskcactus.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.ExpandableListAdapter;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by michelbalamou on 10/22/17.
 *
 * This class represents the calendar view
 *
 */

public class CalendarFragment extends Fragment
{
    // ATTRIBUTES

    private DataSingleton dataSingleton = DataSingleton.getInstance();
    private Date currentDate;

    private Date[] selectedWeek = new Date[7];
    private Calendar calendar = Calendar.getInstance();

    // GRAPHICAL ITEMS
    private List<Button> buttons;

    private TextView monthText;

    private Button nextWeekBtn;
    private Button prevWeekBtn;
    private Button todayBtn;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        initGUI(view);

        // CALCULATE DATE & WEEK ===================================================================
        currentDate = new Date();
        int index = dayOfWeek(currentDate);

        setButtonText(currentDate);
        buttons.get(index).setTextColor(Color.parseColor("#ff0000")); // set current date as RED

        // show Today's date
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM, dd");
        monthText.setText(simpleDateformat.format(currentDate));

        // ASSIGN ACTIONS ==========================================================================

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeWeek(7);
            }
        });

        prevWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeWeek(-7);
            }
        });

        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onToday();
            }
        });

        // SET THE SAME LISTENER TO ALL BUTTONS
        int i = 0;
        for (Button b : buttons)
        {
            b.setTag(i);
            b.setOnClickListener(onChangeDate);
            i++;
        }

        // EXPENDABLE LIST VIEW
        expandableListView = view.findViewById(R.id.expandableListView);
        listAdapter = new ExpandableListAdapter(getActivity(), dataSingleton.getUsers(), currentDate);
        expandableListView.setAdapter(listAdapter);

        // Expands all views
        for (int j = 0; j < listAdapter.getGroupCount(); j++)
            expandableListView.expandGroup(j);


        return view;
    }

    /**
     * Links all the graphical object to references
     */
    private void initGUI(View view)
    {
        buttons = new LinkedList<>();

        buttons.add((Button)view.findViewById(R.id.sundayBtn));
        buttons.add((Button)view.findViewById(R.id.mondayBtn));
        buttons.add((Button)view.findViewById(R.id.tuesdayBtn));
        buttons.add((Button)view.findViewById(R.id.wednesdayBtn));
        buttons.add((Button)view.findViewById(R.id.thursdayBtn));
        buttons.add((Button)view.findViewById(R.id.fridayBtn));
        buttons.add((Button)view.findViewById(R.id.saturdayBrn));

        nextWeekBtn = view.findViewById(R.id.nextWeekBtn);
        prevWeekBtn = view.findViewById(R.id.prevWeekBtn);

        monthText = view.findViewById(R.id.monthText);
        todayBtn = view.findViewById(R.id.todayBtn);
    }


    // =============================================================================================

    // ACTION METHODS

    // =============================================================================================

    /**
     * Event that occurs after selecting a different date
     */
    private  View.OnClickListener onChangeDate = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int pos = (int)view.getTag();
            currentDate = selectedWeek[pos]; // change date
            setButtonRED(pos); // change button Color
            refreshGUI(currentDate); // refresh GUI
        }
    };


    /**
     * Buttons that change the week;
     *
     * @param increment if +7 then moving to next week; if -7 then moving to previous week
     */
    public void onChangeWeek(int increment)
    {
        currentDate = addDays(currentDate, increment); // Increment/decrement date
        setButtonText(currentDate); // change Button Text
        refreshGUI(currentDate); // refresh GUI
    }

    /**
     * Move back to today's date
     */
    public void onToday()
    {
        currentDate = new Date(); // change date
        setButtonText(currentDate); // change Button Text
        setButtonRED(dayOfWeek(currentDate)); // change button Color
        refreshGUI(currentDate); // refresh GUI
    }


    // =============================================================================================

    // HELPER METHODS

    // =============================================================================================

    /**
     * Sets all buttons as black except one
     *
     * @param pos position of the button in the list/ Day of the week
     */
    private void setButtonRED(int pos)
    {
        // Change color
        for (Button b : buttons)
            b.setTextColor(Color.parseColor("#000000")); // set all buttons as Black

        buttons.get(pos).setTextColor(Color.parseColor("#ff0000")); // set selected button as RED
    }

    /**
     * Updates the text of each button to the current week
     *
     * @param date date that points at the week
     */
    private void setButtonText(Date date)
    {
        String[] days = getDaysOfWeek(date);

        // set days displayed on buttons
        for (int i=0; i<7; i++)
            buttons.get(i).setText(days[i]);
    }

    /**
     * Changes the date displayed and refeshes the list view
     */
    public void refreshGUI(Date date)
    {
        // Show the date in the top
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, dd");
        monthText.setText(simpleDateFormat.format(date));

        // Notify the list view that the date has been changed
        listAdapter.setDate(date);
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Checks what day of the week is 'date'
     */
    public int dayOfWeek(Date date)
    {
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * Returns an array with every day of this week
     */
    public String[] getDaysOfWeek(Date refDate)
    {
        calendar.setTime(refDate);

        SimpleDateFormat format = new SimpleDateFormat("dd");

        String[] days = new String[7];
        int delta =- calendar.get(GregorianCalendar.DAY_OF_WEEK) + 1; //add 1 if your week start on sunday
        calendar.add(Calendar.DAY_OF_MONTH, delta);

        for (int i = 0; i<7; i++)
        {
            days[i] = format.format(calendar.getTime());
            selectedWeek[i] = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }


    /**
     * Add days to a date
     */
    public Date addDays(Date refDate, int days)
    {
        calendar.setTime(refDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

}
