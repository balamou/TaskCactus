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
    private String name;      // name of the resource
    private String desc;      // description of the resource

    // ASSOCIATIONS

    private List<Task> tasks; // tasks that allocate the current resource; [*] multiplicity
    private List<Parent> parents; // tasks that allocate the current resource; [*] multiplicity


    // CONSTRUCTOR
    public Resource(String name, String desc)
    {
        this.name = name;
        this.desc = desc;

        tasks = new LinkedList<>();
        parents = new LinkedList<>();
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
     * @param task
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
     * @param task
     */
    protected void removeFromTask(Task task)
    {
        tasks.remove(task);
    }


    /**
     * Adds a parent to the list of parents.
     * Creates a BIDIRECTIONAL link between Resource and Parent.
     *
     * @param parent
     */
    public void assignParent(Parent parent)
    {
        parents.add(parent);
        parent.linkResource(this);
    }

    /**
     * Removes a parent to the list of parents.
     * Removes the BIDIRECTIONAL link between Resource and Parent.
     *
     * @param parent
     */
    public void removeParent(Parent parent)
    {
        parents.remove(parent);
        parent.unlinkResource(this);
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
}
