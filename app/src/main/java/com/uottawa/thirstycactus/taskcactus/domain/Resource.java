package com.uottawa.thirstycactus.taskcactus.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Peter Nguyen on 11/20/17.
 *
 * This class represents resources used in a task
 */

public class Resource {

    // ATTRIBUTES

    private int id; // resource ID in the database (DB~)

    private String name;      // name of the resource
    private String desc;      // description of the resource

    // ASSOCIATIONS

    private List<Task> tasks; // tasks that allocate the current resource; [*] multiplicity


    // CONSTRUCTOR
    public Resource(int id, String name, String desc)
    {
        this.id = id;
        this.name = name;
        this.desc = desc;

        tasks = new LinkedList<>();
    }

    public Resource(String name, String desc)
    {
        this(0, name, desc);
    }


    // =============================================================================================

    // BIDIRECTIONAL LINKS

    // =============================================================================================

    /**
     * Adds a task to the tasks list
     *
     * UNIDIRECTIONAL:
     *  - set as protected to allow only the package access it
     *  - so the user doesn't accidentally use this method
     *
     * @param task task to be added
     */
    protected void useInTask(Task task)
    {
        tasks.add(task);
    }

    /**
     * Removes a task from the tasks list
     *
     * UNIDIRECTIONAL:
     *  - set as protected to allow only the package access it
     *  - so the user doesn't accidentally use this method
     *
     * @param task task to be removed
     */
    protected void removeFromTask(Task task)
    {
        tasks.remove(task);
    }


    /**
     * Unlink this class from all its tasks
     */
    public void prepareToDelete()
    {
        for (Task task : tasks)
            task.partiallyDeallocateResource(this);
    }

    // =============================================================================================

    // GETTERS/SETTERS (comments omitted due to self explanatory nature)

    // =============================================================================================

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public List<Task> getTasks()
    {
        return tasks;
    }


    public int getID()
    {
        return id;
    }

    public void setID(int id)
    {
        this.id = id;
    }
}
