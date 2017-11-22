package com.uottawa.thirstycactus.taskcactus.domain;

import android.util.Log;

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
            Parent michel = new Parent("Michel", "Balamou", null, 1, "1234");

            Person peter = new Person("Peter", "Nguyen", null);
            Person nate = new Person("Nate", "Adams", null);

            loggedPerson = michel;

            people.add(michel);
            people.add(peter);
            people.add(nate);


            tasks.add(new Task("Wash dishes", "", 2, null, "DEFAULT"));
            tasks.add(new Task("Clean room", "", 4, null, "DEFAULT"));
            tasks.add(new Task("Recycle", "", 5, null, "DEFAULT"));


            Date d1 = getDate(2017, 11, 10); // 10 NOV 2017
            Date d2 = getDate(2017, 11, 15); // 15 NOV 2017

            tasks.add(new Task("Clean basement", "", 2, d1));
            tasks.add(new Task("Finish app", "", 5, d2));

            michel.assignTask(peter, tasks.get(0));
            michel.assignTask(nate, tasks.get(3));
            michel.assignTask(nate, tasks.get(4));


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