package com.uottawa.thirstycactus.taskcactus.domain;

import java.util.Date;

/**
 * Created by Peter Nguyen on 11/22/17.
 *
 * Association class between Task and Person
 */

public class TaskDate
{
    // ATTRIBUTES
    private Date date;
    private boolean completed;
    private String notes;

    // ASSOCIATIONS
    private Person person;
    private Task task;


    // CONSTRUCTOR
    public TaskDate(Person person, Task task, Date date, boolean completed, String notes) throws IllegalArgumentException
    {
        this.person = person;
        this.task = task;
        this.date = date;
        this.completed = completed;
        this.notes = notes;

        if (task==null || person==null)
            throw new IllegalArgumentException("Attempting to make a link with null element");

        task.linkTaskDate(this); // makes a link back to the task
        person.linkTaskDate(this); // makes a link back to the person
    }

    public TaskDate(Person person, Task task, Date date) throws IllegalArgumentException
    {
        this(person, task, date, false, "");
    }


    // =============================================================================================

    // BIDIRECTIONAL LINKS

    // =============================================================================================

    public void removeLink()
    {
        person.unlinkTaskDate(this);
        task.unlinkTaskDate(this);

        person = null;
        task = null;
    }

    // =============================================================================================

    // GETTERS/SETTERS

    // =============================================================================================


    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }


    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public boolean getCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
