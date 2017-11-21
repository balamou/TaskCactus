package com.uottawa.thirstycactus.taskcactus.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

/**
 * Created by Peter Nguyen on 11/20/17.
 *
 * General purpose task class.
 */

public class Task {

    // ATTRIBUTES

    private String name;   // Name of the task
    private String desc;   // Task description
    private int points;    // Points received for the task
    private boolean done;  // Flag to check if task is completed
    private String notes;  // Additional notes
    private Date deadline; // Deadline of the task

    //ASSOCIATIONS

    private Person assignedPerson; // person the task is assigned to; [0..1] multiplicity
    private List<Resource> resources; // allocated resources; [*] multiplicity


    //CONSTRUCTORS

    public Task(String name, String desc, int points, Date deadline, boolean done, String notes)
    {
        this.name = name;
        this.desc = desc;
        this.points = points;
        this.done = done;
        this.notes = notes;
        this.deadline = deadline;

        resources = new LinkedList<>();
    }

    public Task(String name, String desc, int points, Date deadline)
    {
        // minimum arguments required for the Task class
        this(name, desc, points, deadline, false, "");
    }



    // =============================================================================================

    // BIDIRECTIONAL LINKS

    // =============================================================================================

    /**
     * Assigns a task to a person
     * Creates a BIDIRECTIONAL link between Task and Person.
     *
     * @param person person to complete the task
     */
    public void assignTask(Person person)
    {
        if (assignedPerson!=null)
        {
            // checks if the task is currently assigned
            // if it is it removes the link from that person
            removePerson();
        }
        else
        {
            // makes a unidirectional link with person
            assignedPerson = person;

            // makes a unidirectional link back to task
            if (person!=null)
                person.assignTask(this);
        }
    }

    /**
     * Removes task from person currently responsible for the task.
     * Removes the BIDIRECTIONAL link between Task and Person.
     */
    public void removePerson()
    {
        if (assignedPerson != null)
        {
            assignedPerson.removeTask(this);
            assignedPerson = null;
        }
    }


    /**
     * Allocate a resource.
     * Adds the resource to the list of resources.
     * Creates a BIDIRECTIONAL link between Task and Resource.
     *
     * @param r resource allocated
     */
    public void allocateResource(Resource r)
    {
        resources.add(r);
        r.useInTask(this);
    }

    /**
     * Deallocate a resource.
     * Removes the resource to the list of resources.
     * Removes the BIDIRECTIONAL link between Task and Resource.
     *
     * @param r resource deallocated
     */
    public void deallocateResource(Resource r)
    {
        resources.remove(r);
        r.removeFromTask(this);
    }


    // =============================================================================================

    // GETTERS/SETTERS (comments omitted due to self explanatory nature)

    // =============================================================================================


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public int getPoints()
    {
        return points;
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }

    public boolean getDone()
    {
        return done;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
    }

    public Date getDeadline()
    {
        return deadline;
    }

}
