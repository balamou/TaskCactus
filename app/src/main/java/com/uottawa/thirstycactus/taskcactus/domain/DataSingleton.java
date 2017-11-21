package com.uottawa.thirstycactus.taskcactus.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Peter Nguyen on 11/20/17.
 *
 * Singleton class to sync in all data throughout the app.
 */

public class DataSingleton
{
    private static DataSingleton instance;
    private boolean load = false; // flag that checks if the classes have been loaded from the database


    // ASSOCIATIONS
    private List<Task> default_tasks; // Default tasks, they cannot be removed nor added
    private List<Task> new_tasks;
    private List<Person> people;
    private List<Resource> resources;

    // CONSTRUCTOR
    /**
     * Set to private to avoid multiple instantiations
     */
    private DataSingleton()
    {
        default_tasks = new LinkedList<>();
        new_tasks = new LinkedList<>();
        people = new LinkedList<>();
        resources = new LinkedList<>();
    }

    // =============================================================================================

    // METHODS

    // =============================================================================================

    /**
     * Creates the instance of DataSingleton once and returns it
     */
    public static DataSingleton getInstance()
    {
        if (instance == null)
            instance = new DataSingleton();

        return instance;
    }

    /**
     * Loads data from the database.
     *
     *      ** Temporarily filled with Random data
     *
     */
    private void loadData()
    {
        if (!load)
        {
            // Placeholder data +++
            Parent michel = new Parent("Michel", "Balamou", null, 1, "1234");

            Person peter = new Person("Peter", "Nguyen", null);
            Person nate = new Person("Nate", "Adams", null);

            people.add(michel);
            people.add(peter);
            people.add(nate);


            default_tasks.add(new Task("Wash dishes", "", 2, null));
            default_tasks.add(new Task("Clean room", "", 4, null));
            default_tasks.add(new Task("Recycle", "", 5, null));


            new_tasks.add(new Task("Clean basement", "", 2, getTime(2017, 11, 10)));
            new_tasks.add(new Task("Finish app", "", 5, getTime(2017, 11, 10)));

            michel.assignTask(peter, new_tasks.get(1));
            michel.assignTask(nate, new_tasks.get(0));
            michel.assignTask(nate, default_tasks.get(1));

            nate.getTasks().get(1).setDone(true);
            // Placeholder data ---
        }

        load = true;
    }

    /**
     * Converts Year, Month and Day into Date.
     *
     * The reason for using this is because the Date constructor has been depreciated and
     * this is the new accepted way of making a date due to Internalization issues.
     */
    private Date getTime(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }


    // =============================================================================================

    // GETTERS: returns full lists

    // =============================================================================================

    /**
     * Returns the list of users from the database.
     */
    public List<Person> getUsers()
    {
        loadData();
        return people;
    }

    /**
     * Returns the list of default/premade tasks
     */
    public List<Task> getDefaultTasks()
    {
        loadData();
        return default_tasks;
    }

    /**
     * Returns the list of new tasks
     */
    public List<Task> getNewTasks()
    {
        loadData();
        return new_tasks;
    }

    // =============================================================================================

    // GETTERS: returns specific entries in each lists

    // =============================================================================================


    /**
     * Returns a list with all the user's last names
     *
     */
    public String[] getNameList()
    {
        loadData();

        String[] result = new String[people.size()];

        for (int i=0; i<people.size(); i++)
        {
            // Add the full name to each entry
            result[i] = people.get(i).getFullName();
        }

        return result;
    }


    /**
     * Returns the number of tasks left to do, each in the order the users were added.
     */
    public int[] getTasksToDo()
    {
        loadData();

        int[] result = new int[people.size()];

        for (int i=0; i<people.size(); i++)
        {
            // the result for each user is
            // TotalTasks - TasksCompleted
            result[i] = people.get(i).totalTasks() - people.get(i).tasksCompleted();
        }

        return result;
    }





}
