package com.uottawa.thirstycactus.taskcactus.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    private Date selectedDate;
    private Calendar calendar = Calendar.getInstance();

    // GRAPHICAL ITEMS
    private List<Button> buttons;

    private Button nextWeekBtn;
    private Button prevWeekBtn;
    private TextView monthText;

    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);


        // POPULATE BUTTONS
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


        // CALCULATE DATE & WEEK ===================================================================
        currentDate = new Date();
        selectedDate = currentDate;
        String[] days = getDaysOfWeek(currentDate);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM");


        for (int i=0; i<7; i++)
            buttons.get(i).setText(days[i]); // set days displayed on buttons

        calendar.setTime(currentDate);
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        buttons.get(index-1).setTextColor(Color.parseColor("#ff0000")); // set current date as RED


        monthText.setText(simpleDateformat.format(currentDate)); // set month

        // ASSIGN ACTIONS ==========================================================================

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextWeek();
            }
        });

        prevWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrevWeek();
            }
        });

        // CREATE A LISTENER
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onChangeDate((int)view.getTag());
            }
        };

        // SET THE SAME LISTENER TO ALL BUTTONS
        int i = 0;
        for (Button b : buttons)
        {
            b.setTag(i);
            b.setOnClickListener(listener);
            i++;
        }

        return view;
    }

    /**
     * When going to next week
     */
    public void onNextWeek()
    {
        currentDate = addDays(currentDate, 7);
        String[] days = getDaysOfWeek(currentDate);

        for (int i=0; i<7; i++)
        {
            buttons.get(i).setText(days[i]);
        }
    }


    /**
     * When going to last week
     */
    public void onPrevWeek()
    {
        currentDate = addDays(currentDate, -7);
        String[] days = getDaysOfWeek(currentDate);

        for (int i=0; i<7; i++)
        {
            buttons.get(i).setText(days[i]);
        }
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

    /**
     * Event that occurs after selecting a different date
     */
    public void onChangeDate(int pos)
    {
        // Change color
        for (Button b : buttons)
            b.setTextColor(Color.parseColor("#000000")); // set all buttons as Black

        buttons.get(pos).setTextColor(Color.parseColor("#ff0000")); // set selected button as RED


        selectedDate = selectedWeek[pos];

        //monthText.setText(selectedDate.toString());

    }
}
