package com.uottawa.thirstycactus.taskcactus.domain;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Person {

    // ATTRIBUTES

    private String firstName;     // first name
    private String lastName;      // last name
    private Date birthDate;       // date of birth (*optional [0..1])

    // ASSOCIATIONS

    //private List<Task> tasks;
    private List<TaskDate> taskDates; // association class liked to Task; [*] multiplicity
    private List<Parent> parents; // list of parents; [0..2] multiplicity


    // CONSTRUCTORS

    public Person(String firstName, String lastName, Date birthDate)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        this.parents = new LinkedList<>();
        this.taskDates = new LinkedList<>();
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
    public void assignTask(Task task, Date date, boolean completed, String notes)
    {
        new TaskDate(this, task, date, completed, notes);
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
     * Removes the taskDate associated to Task from the user
     *
     *  - set as protected to allow only the package access it
     *  - so the user doesn't accidentally use this method
     */
    protected void removeTask(Task task)
    {
        for (TaskDate t : taskDates)
        {
            if (t.getTask() == task)
            {
                task.unlinkTaskDate(t);
                taskDates.remove(t);
                break;
            }
        }

    }

    /**
     * Adds parent to the list
     * Creates a BIDIRECTIONAL link between Person and Parent.
     */
    public void addParent(Parent p)
    {
        parents.add(p);
        p.addChild(this);
    }

    /**
     * Removed parent from the list
     * Removes the BIDIRECTIONAL link between Person and Parent.
     */
    public void removeParent(Parent p)
    {
        parents.remove(p);
        p.removeChild(this);
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

}
