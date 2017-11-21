package com.uottawa.thirstycactus.taskcactus.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Task {

    private String name;
    private String desc;  // Task description
    private int points;   // Points received for the task
    private boolean done; // Flag to check if task is completed
    private String notes; // Additional notes
    private Date deadline;

    //ASSOCIATIONS

    private Person assignedPerson; // person the task is assigned to; [0..1] multiplicity
    private List<Resource> resources; // allocated resources; [*] multiplicity


    //CONSTRUCTOR
    public Task(String name, String desc, int points, Date deadline, boolean done, String notes)
    {
        this.name = name;
        this.desc = desc;
        this.points = points;
        this.done = done;
        this.notes = notes;
        this.deadline = deadline;

        resources = new LinkedList<Resource>();
    }

    public Task(String name, String desc, int points, Date deadline)
    {
        // minimum arguments required for the Task class
        this(name, desc, points, deadline, false, "");
    }

    public void assignTask(Person person)
    {
        assignedPerson = person;
    }


    /**
     * Allocate a resource.
     * Adds the resource to the list of resources.
     *
     * @param r resource allocated
     */
    public void allocateResource(Resource r)
    {
        resources.add(resources.size(), r);
    }

    /**
     * Deallocate a resource.
     * Removes the resource to the list of resources.
     *
     * @param r resource deallocated
     */
    public void deallocateResource(Resource r)
    {
        resources.remove(r);
    }

    //EDIT TASK
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
