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
    private List<Task> tasks;
    private List<Person> people;
    private List<Resource> resources;

    private Person loggedPerson;


    // CONSTRUCTOR
    /**
     * Set to private to avoid multiple instantiations
     */
    private DataSingleton()
    {
        tasks = new LinkedList<>();
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
            Date d = getDate(1997, 1, 6);
            Parent michel = new Parent("Michel", "Balamou", d, 1, "1234");

            Person peter = new Person("Peter", "Nguyen", null);
            Person nate = new Person("Nate", "Adams", null);

            peter.addParent(michel);
            nate.addParent(michel);

            loggedPerson = michel;

            people.add(michel);
            people.add(peter);
            people.add(nate);


            tasks.add(new Task("Wash dishes", "", 2, "DEFAULT"));
            tasks.add(new Task("Clean room", "", 4, "DEFAULT"));
            tasks.add(new Task("Recycle", "", 5, "DEFAULT"));


            Date d1 = getDate(2017, 10, 30); // 10 NOV 2017
            Date d2 = getDate(2017, 11, 2); // 15 NOV 2017

            tasks.add(new Task("Clean basement", "", 2));
            tasks.add(new Task("Finish app", "", 5));
            tasks.add(new Task("Wash car", "", 5));



            michel.assignTask(peter, tasks.get(0), d1, false, "");

            michel.assignTask(nate, tasks.get(3), d1, false, "");
            michel.assignTask(nate, tasks.get(4), d2, false, "");
            michel.assignTask(nate, tasks.get(5), d2, false, "");


            nate.getTaskDates().get(1).setCompleted(true);
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
    private Date getDate(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }


    // =============================================================================================

    // GETTERS: returns full lists; loads data before hand if it wasn't loaded

    // =============================================================================================

    /**
     * Returns the list of default/premade tasks
     */
    public List<Task> getTasks()
    {
        loadData();
        return tasks;
    }


    /**
     * Returns the list of users from the database.
     */
    public List<Person> getUsers()
    {
        loadData();
        return people;
    }


    /**
     * Returns the list of resources from the database.
     */
    public List<Resource> getResources()
    {
        loadData();
        return resources;
    }

    // =============================================================================================

    // METHODS

    // =============================================================================================

    /**
     * Returns the list of all TaskDates
     */
    public List<TaskDate> getAllAssociations()
    {
        List<TaskDate> taskDates = new LinkedList<>();
        for (Person p: people)
        {
            if(p.getTaskDates()!=null)
                taskDates.addAll(p.getTaskDates()); // appends all TaskDates from person to the end of the List
        }
        return taskDates;
    }

    /**
     * Returns the list of all TaskDates from that date
     */
    public List<TaskDate> getTasksFromDay(Date date)
    {
        List<TaskDate> taskDates = new LinkedList<>();
        for (Person p: people)
        {
            for(TaskDate t : p.getTaskDates())
            {
                if (t.getDate().equals(date))
                    taskDates.add(t);
            }
        }
        return taskDates;
    }

    /**
     * Delete task from the list at index
     */
    public void deleteTask(int index)
    {
        tasks.get(index).prepareToDelete();
        tasks.remove(index);
    }


    /**
     * Returns the currently logged in person
     */
    public Person getLoggedPerson()
    {
        loadData();
        return loggedPerson;
    }

    /**
     * Login as parent. A 4 digit PIN is required for authentication.
     *
     * @param parent to log on as
     * @param PIN the 4 digit password required for login
     * @throws IllegalArgumentException if the PIN is not valid
     */
    public void login(Parent parent, String PIN) throws IllegalArgumentException
    {
        loadData();
        if (parent.getHashedPIN().equals(PIN))
        {
            loggedPerson = parent;
        }
        else
        {
            throw new IllegalArgumentException("Invalid PIN.");
        }
    }

    /**
     * Login as person. No password required.
     *
     * @param person to log on as
     * @throws IllegalArgumentException if attempting to login as parent
     */
    public void login(Person person) throws IllegalArgumentException
    {
        loadData();
        if (!(person instanceof Parent))
        {
            loggedPerson = person;
        }
        else
        {
            throw new IllegalArgumentException("Attempt to login as parent without providing 4 digit PIN.");
        }
    }

}
