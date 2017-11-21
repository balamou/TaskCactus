package com.uottawa.thirstycactus.taskcactus.domain;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Person {

    // ATTRIBUTES

    private String firstName;
    private String lastName;
    private Date birthDate;

    // ASSOCIATIONS

    private List<Task> tasks; // all tasks of the current person; [*] multiplicity

    // CONSTRUCTORS

    Person(String firstName, String lastName, Date birthDate)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        this.tasks = new LinkedList<Task>();
    }

    Person(String firstName, String lastName)
    {
        // Minimum required attributes for Person class
        this(firstName, lastName, null);
    }


    /**
     * Returns points gained by user.
     * Cheks all instances of tasks associated with said user and accumulated only completed tasks
     *
     * @return number of points
     */
    public int getPoints()
    {
        int totalPoints = 0;

        for (Task t: tasks)
        {
            if (t.getDone())
                totalPoints += t.getPoints();
        }

        return totalPoints;
    }


    /**
     * Assigns the user a task
     */
    public void assignTask(Task t)
    {
        tasks.add(t);
    }

    /**
     * Removes task t from the user;
     */
    public void removeTask(Task t)
    {
        tasks.remove(t);
    }


    // GETTERS/SETTERS

    /**
     * Returns the full name of the user.
     * First name, space then last name.
     */
    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

}
