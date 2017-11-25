package com.uottawa.thirstycactus.taskcactus;

import com.uottawa.thirstycactus.taskcactus.adapters.TaskListview;
import com.uottawa.thirstycactus.taskcactus.adapters.UserListview;

/**
 * Created by Nate Adams on 11/21/17.
 */

public class ViewSingleton
{
    // ATTRIBUTES
    private static ViewSingleton instance;
    private UserListview usr;
    private TaskListview taskListview;

    private TaskInfo taskInfo;
    private UserInfo userInfo;

    // CONSTRUCTOR
    /**
     * Set to private to avoid multiple instantiations
     */
    private ViewSingleton() {}


    // =============================================================================================

    // METHODS

    // =============================================================================================

    /**
     * Creates the instance of DataSingleton once and returns it
     */
    public static ViewSingleton getInstance()
    {
        if (instance == null)
            instance = new ViewSingleton();

        return instance;
    }

    public void setAdapter(UserListview usr)
    {
        this.usr = usr;
    }

    public void setTaskListview(TaskListview taskListview)
    {
        this.taskListview = taskListview;
    }

    public void refreshTasks()
    {
        taskListview.notifyDataSetChanged();
    }

    public void refresh()
    {
        usr.notifyDataSetChanged();
    }

    public void setTaskInfo(TaskInfo taskInfo)
    {
        this.taskInfo = taskInfo;
    }

    public void update(int id)
    {
        if (taskInfo!=null)
            taskInfo.update(id);
    }


    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }

    public void updateUserInfo()
    {
        if (userInfo!=null)
            userInfo.update();
    }
}
