package com.uottawa.thirstycactus.taskcactus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uottawa.thirstycactus.taskcactus.adapters.TaskListview;
import com.uottawa.thirstycactus.taskcactus.adapters.UserListview;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;
import com.uottawa.thirstycactus.taskcactus.domain.Resource;
import com.uottawa.thirstycactus.taskcactus.fragments.CalendarFragment;

import static android.R.attr.id;

/**
 * Created by Nate Adams on 11/21/17.
 *
 * This class has a reference to adapters that it needs to refresh after modifying the data.
 *
 * It is more convenient to store all those objects that need to be refreshed in one place
 * instead of passing the reference from activity to activity.
 */

public class ViewSingleton
{
    // ATTRIBUTES
    private static ViewSingleton instance;

    // ALL CLASSES THAT NEED TO BE REFERENCED & REFRESHED FROM ANOTHER CLASS
    private UserListview usr;
    private TaskListview taskListview;

    private TaskInfo taskInfo;
    private UserInfo userInfo;

    private CalendarFragment calendarFragment;

    // CONSTRUCTOR
    /**
     * Set to private to avoid multiple instantiations
     */
    private ViewSingleton() {}

    /**
     * Creates the instance of DataSingleton once and returns it
     */
    public static ViewSingleton getInstance()
    {
        if (instance == null)
            instance = new ViewSingleton();

        return instance;
    }

    // =============================================================================================

    // SETTERS

    // =============================================================================================

    public void setAdapter(UserListview usr)
    {
        this.usr = usr;
    }

    public void setTaskListview(TaskListview taskListview)
    {
        this.taskListview = taskListview;
    }

    public void setTaskInfo(TaskInfo taskInfo)
    {
        this.taskInfo = taskInfo;
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }

    public void setCalendarFragment(CalendarFragment calendarFragment)
    {
        this.calendarFragment = calendarFragment;
    }

    // =============================================================================================

    // REFRESH METHODS

    // =============================================================================================

    /**
     * Refreshes the main list of tasks in the main activity: Task Fragment
     */
    public void refreshTasks()
    {
        if (taskListview!=null) taskListview.notifyDataSetChanged();
    }

    /**
     * Refreshes the main list of users in the main activity: Users Fragment
     */
    public void refresh()
    {
        if (usr!=null) usr.notifyDataSetChanged();
    }


    /**
     * Refreshes the information displayed for one individual task
     */
    public void updateTaskInfo()
    {
        if (taskInfo!=null)
            taskInfo.update();
    }


    /**
     * Refreshes the information displayed for one individual user
     */
    public void updateUserInfo()
    {
        if (userInfo!=null)
            userInfo.update();
    }

    /**
     * Refreshes the calendar information
     */
    public void updateCalendar()
    {
        if (calendarFragment!=null)
            calendarFragment.refreshGUI();
    }

    // =============================================================================================

    // DIALOG METHODS

    // =============================================================================================

    /**
     * Shows a dialog with a particular message; The okay button just closes the dialog
     *
     * Example: when clicking delete user and the currently logged person is not a Parent
     *          the dialog shows a message that you are not able to perform the action
     *
     * @param context the application context
     * @param msg message to be displayed
     */
    public void showPopup(Context context, String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle("Note"); // the title

        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * This method returns a dialog; the positive (OK) is set later one outside of the scope of the
     * method with its particular implementation.
     *
     * Example: When deleting a user this method returns a dialog box and the listener for the
     *          OK button is implemented on top of the returned result
     *
     *
     *
     *         AlertDialog.Builder box = confirmationBox(getApplicationContext(), "Are you sure you want to delete this user?");
     *
     *         box.setPositiveButton("YES", new DialogInterface.OnClickListener()
     *         {
     *              public void onClick(DialogInterface dialog, int whichButton)
     *              {
     *                  // DELETE USER HERE
     *              }
     *         }).show();
     *
     *
     *
     * @param context the application context
     * @param msg message to be displayed
     */
    public AlertDialog.Builder confirmationBox(Context context, String msg)
    {
        return new AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null);
    }
}
