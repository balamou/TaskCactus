package com.uottawa.thirstycactus.taskcactus.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.AddUser;
import com.uottawa.thirstycactus.taskcactus.AssignTask;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
import com.uottawa.thirstycactus.taskcactus.adapters.ExpandableListAdapter;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.TaskInfo;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Person;
import com.uottawa.thirstycactus.taskcactus.domain.TaskDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import android.widget.ExpandableListView.OnChildClickListener;

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
    private List<Button> colButtons;


    private TextView monthText;

    private Button nextWeekBtn;
    private Button prevWeekBtn;
    private Button todayBtn;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;


    private LinearLayout layout1;
    private LinearLayout layout2;

    private Button assignTaskBtn;
    private Button assignTaskBtn2;

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
        setIndicator(); //set indicator below button

        // show Today's date
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM, dd");
        monthText.setText(simpleDateformat.format(currentDate));

        // ASSIGN ACTIONS ==========================================================================

        nextWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeWeek(1);
            }
        });

        prevWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeWeek(-1);
            }
        });

        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onToday();
            }
        });

        assignTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAssignTask();
            }
        });
        assignTaskBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAssignTask();
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
        listAdapter = new ExpandableListAdapter(getActivity(), dataSingleton.getUsers(currentDate), currentDate);
        expandableListView.setAdapter(listAdapter);
        expandableListView.setOnChildClickListener(myListItemClicked);

        // Expands all views
        for (int j = 0; j < listAdapter.getGroupCount(); j++)
            expandableListView.expandGroup(j);


        // HIDE/SHOW THE EXPENDABLE LIST
        List<Person> users = dataSingleton.getUsers(currentDate); // users that have a task on that day

        if (users.isEmpty()) // if nobody has a task on that day
        {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
        else
        {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }


        //
        ViewSingleton.getInstance().setCalendarFragment(this);

        return view;
    }

    /**
     * Links all the graphical object to references
     */
    private void initGUI(View view)
    {
        buttons = new LinkedList<>();
        colButtons = new LinkedList<>();


        buttons.add((Button)view.findViewById(R.id.sundayBtn));
        buttons.add((Button)view.findViewById(R.id.mondayBtn));
        buttons.add((Button)view.findViewById(R.id.tuesdayBtn));
        buttons.add((Button)view.findViewById(R.id.wednesdayBtn));
        buttons.add((Button)view.findViewById(R.id.thursdayBtn));
        buttons.add((Button)view.findViewById(R.id.fridayBtn));
        buttons.add((Button)view.findViewById(R.id.saturdayBrn));


        colButtons.add((Button)view.findViewById(R.id.colBtn1));
        colButtons.add((Button)view.findViewById(R.id.colBtn2));
        colButtons.add((Button)view.findViewById(R.id.colBtn3));
        colButtons.add((Button)view.findViewById(R.id.colBtn4));
        colButtons.add((Button)view.findViewById(R.id.colBtn5));
        colButtons.add((Button)view.findViewById(R.id.colBtn6));
        colButtons.add((Button)view.findViewById(R.id.colBtn7));


        nextWeekBtn = view.findViewById(R.id.nextWeekBtn);
        prevWeekBtn = view.findViewById(R.id.prevWeekBtn);

        monthText = view.findViewById(R.id.monthText);
        todayBtn = view.findViewById(R.id.todayBtn);

        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);

        assignTaskBtn = view.findViewById(R.id.assignTaskBtn);
        assignTaskBtn2 = view.findViewById(R.id.assignTaskBtn2);
    }


    // =============================================================================================

    // ACTION METHODS

    // =============================================================================================

    //our child listener
    private OnChildClickListener myListItemClicked =  new OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


            Person person =  dataSingleton.getUsers(currentDate).get(groupPosition);
            TaskDate taskDate =  person.getTaskDates(currentDate).get(childPosition);

            int index = dataSingleton.getTasks().indexOf(taskDate.getTask());

            Intent intent = new Intent(getActivity(), TaskInfo.class);
            intent.putExtra("TASK_ID", index); // ID of the task
            startActivity(intent);

            return false;
        }
    };

    /**
     * Event that occurs after selecting a different date
     */
    private View.OnClickListener onChangeDate = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int pos = (int)view.getTag();
            currentDate = selectedWeek[pos]; // change date
            setButtonRED(pos); // change button Color
            refreshGUI(); // refresh GUI
        }
    };


    /**
     * Buttons that change the week;
     *
     * @param increment if +7 then moving to next week; if -7 then moving to previous week
     *                  if +1 then moving to next day; if -1 then moving to previous day
     */
    public void onChangeWeek(int increment)
    {
        currentDate = addDays(currentDate, increment); // Increment/decrement date
        setButtonText(currentDate); // change Button Text
        setButtonRED(dayOfWeek(currentDate)); // change button Color
        refreshGUI(); // refresh GUI
    }

    /**
     * Move back to today's date
     */
    public void onToday()
    {
        currentDate = new Date(); // change date
        setButtonText(currentDate); // change Button Text
        setButtonRED(dayOfWeek(currentDate)); // change button Color
        refreshGUI(); // refresh GUI
    }

    /**
     * Opens the assign task activity if logged in as a parent
     */
    public void openAssignTask()
    {
        // CHECK IF LOGGED IN AS PARENT
        if (!dataSingleton.isLoggedAsParent())
        {
            ViewSingleton.getInstance().showPopup(getContext(), "Please login as a Parent to assign a task");
            return ; // EXIT
        }

        Intent intent = new Intent(getActivity(), AssignTask.class);
        intent.putExtra("DATE", currentDate.getTime());
        startActivity(intent);
    }


    // =============================================================================================

    // HELPER METHODS

    // =============================================================================================

    /**
     * Indicator below the day
     */
    public void setIndicator()
    {
        // Change color
        for (int i = 0; i<selectedWeek.length; i++)
        {
            int col = Color.parseColor("#ffffff"); // WHITE

            if (dataSingleton.numTasks(selectedWeek[i])>0)
                col = Color.parseColor("#fe7373"); // BRIGHT RED

            colButtons.get(i).setBackgroundColor(col);
        }
    }

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
     * Changes the date displayed and refreshes the list view
     */
    public void refreshGUI()
    {
        // Show the date in the top
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, dd");
        monthText.setText(simpleDateFormat.format(currentDate));


        List<Person> users = dataSingleton.getUsers(currentDate); // users that have a task on that day

        setIndicator(); // Refresh indicator buttons

        if (users.isEmpty()) // if nobody has a task on that day
        {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
        else
        {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }

        listAdapter.setPeople(users);
        // Notify the list view that the date has been changed
        listAdapter.setDate(currentDate);
        listAdapter.notifyDataSetChanged();

        // Expands all views
        for (int j = 0; j < listAdapter.getGroupCount(); j++)
            expandableListView.expandGroup(j);
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
