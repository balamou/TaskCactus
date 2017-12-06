package com.uottawa.thirstycactus.taskcactus.domain;
import android.widget.ListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Person
{
    // ATTRIBUTES

    private int id; // for database (DB~)

    private String firstName;     // first name
    private String lastName;      // last name
    private Date birthDate;       // date of birth (*optional [0..1])

    // ASSOCIATIONS

    private List<TaskDate> taskDates; // association class liked to Task; [*] multiplicity


    // CONSTRUCTORS


    /**
     * @param id ID of the person in the database
     */
    public Person(int id, String firstName, String lastName, Date birthDate)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        this.taskDates = new LinkedList<>();
    }


    public Person(String firstName, String lastName, Date birthDate)
    {
        this(0, firstName, lastName, birthDate);
    }

    public Person(String firstName, String lastName)
    {
        // Minimum required attributes for Person class
        this(firstName, lastName, null);
    }




    // =============================================================================================

    // BIDIRECTIONAL LINKS

    // =============================================================================================


    /**
     * Assigns the user a task; Makes a bidirectional link between Person and Task, through the
     * association class TaskDate
     */
    public TaskDate assignTask(Task task, Date date, boolean completed, String notes)
    {
        return new TaskDate(this, task, date, completed, notes);
    }

    /**
     *  UNIDIRECTIONAL link to TASKDATE
     */
    protected void linkTaskDate(TaskDate taskDate)
    {
        taskDates.add(taskDate);
    }

    protected void unlinkTaskDate(TaskDate taskDate)
    {
        taskDates.remove(taskDate);
    }


    /**
     * Removes all existing association classes;
     * This allows the User instance to be deleted without causing problems.
     */
    public void prepareToDelete()
    {
        for (TaskDate t : taskDates)
            t.partialRemovePerson();


        taskDates = null; // remove reference for Garbage Collector
    }

    // =============================================================================================

    // GETTERS/SETTERS (some comments omitted due to self explanatory nature)

    // =============================================================================================

    /**
     * Returns points gained by user.
     * Cheks all instances of tasks associated with said user and accumulated only completed tasks
     *
     * @return number of points
     */
    public int getPoints()
    {
        int totalPoints = 0;

        for (TaskDate t: taskDates)
        {
            if (t.getCompleted())
                totalPoints += t.getTask().getPoints();
        }

        return totalPoints;
    }

    /**
     * Returns the total number of tasks
     */
    public int totalTasks()
    {
        return taskDates.size();
    }

    /**
     * Returns the number of tasks marked as completed
     */
    public int tasksCompleted()
    {
        int total = 0;

        for (TaskDate t: taskDates)
        {
            if (t.getCompleted())
                total++; // increments only is task set as completed
        }

        return total;
    }

    /**
     * Returns the full name of the user.
     * First name, space then last name.
     */
    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    /**
     * Returns the list of all tasks assigned to the Person
     */
    public List<TaskDate> getTaskDates()
    {
        return taskDates;
    }


    /**
     * Returns the next task that is due (earliest by date), but not yet completed.
     * Returns null if there is no next task.
     *
     *  -- Better implementation would be using iterators
     *  -- AN EVEN BETTER implementation would be using HEAPS & PRIORITY QUEUES
     *
     *  -- ^ left out due to time concerns
     */
    public Task getNextTask()
    {
        if (taskDates.size() == 0) return null; // no next task


        TaskDate earliestTask = null;
        boolean found = false;

        // CIRCLES through every association instance of TaskDate to find which is the earliest
        // non completed task
        for (TaskDate t : taskDates)
        {
            // Checks if the current task is not completed yet
            if (!t.getCompleted())
            {
                if (!found)
                {
                    earliestTask = t; // this will point to the first task that is not yet completed in the list
                    found = true;
                }
                else
                {
                    Date currentTask = t.getDate();

                    // Checks if the earliest task so far is due later than the current task;
                    // if it is, then the current task becomes the earliest task
                    if (earliestTask.getDate().compareTo(currentTask) > 0)
                        earliestTask = t;
                }
            }
        }

        if (earliestTask!=null)
            return earliestTask.getTask();

        return null;
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


    /**
     * Returns date of birth in a human readable format
     * Ex: November 10, 2001
     */
    public String getReadableBirthday()
    {
        if (birthDate==null) return "Not set";

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(birthDate);
    }

    /**
     * Returns all taskDates associated to that date;
     * Only Year, Month and Day comparisons; The time is ommited
     */
    public List<TaskDate> getTaskDates(Date date)
    {
        List<TaskDate> result = new LinkedList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dates = sdf.format(date);

        for (TaskDate t: taskDates)
        {
            if (dates.equals(sdf.format(t.getDate())))
                result.add(t);
        }

        return result;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }
}
