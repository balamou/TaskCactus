package com.uottawa.thirstycactus.taskcactus.domain;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Person {

    // ATTRIBUTES

    private String firstName;     // first name
    private String lastName;      // last name
    private Date birthDate;       // date of birth (*optional [0..1])

    // ASSOCIATIONS

    private List<Task> tasks;     // all tasks of the current person; [*] multiplicity
    private List<Parent> parents; // list of parents; [0..2] multiplicity


    // CONSTRUCTORS

    public Person(String firstName, String lastName, Date birthDate)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

        this.tasks = new LinkedList<>();
        this.parents = new LinkedList<>();
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
     * Assigns the user a task
     *
     * UNIDIRECTIONAL:
     *  - set as protected to allow only the package access it
     *  - so the user doesn't accidentally use this method
     */
    protected void assignTask(Task t)
    {
        tasks.add(t);
    }

    /**
     * Removes task t from the user
     *
     * UNIDIRECTIONAL:
     *  - set as protected to allow only the package access it
     *  - so the user doesn't accidentally use this method
     */
    protected void removeTask(Task t)
    {
        tasks.remove(t);
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

        for (Task t: tasks)
        {
            if (t.getDone())
                totalPoints += t.getPoints();
        }

        return totalPoints;
    }

    /**
     * Returns the total number of tasks
     */
    public int totalTasks()
    {
        return tasks.size();
    }

    /**
     * Returns the number of tasks marked as completed
     */
    public int tasksCompleted()
    {
        int total = 0;

        for (Task t: tasks)
        {
            if (t.getDone())
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
    public List<Task> getTasks()
    {
        return tasks;
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
