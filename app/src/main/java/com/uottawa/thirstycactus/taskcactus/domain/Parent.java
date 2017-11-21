package com.uottawa.thirstycactus.taskcactus.domain;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Peter Nguyen on 11/20/17.
 */

public class Parent extends Person {

    // ATTRIBUTES

    private int priorityLevel; // priority level of the parent
    private String hashedPIN; // 4 digit PIN password


    // ASSOCIATIONS
    private List<Person> children; // children the parent has; [*] multiplicity

    // CONSTRUCTOR

    public Parent(String firstName, String lastName, Date birthDate, int priorityLevel, String hashedPIN)
    {
        super(firstName, lastName, birthDate);

        this.priorityLevel = priorityLevel;
        this.hashedPIN = hashedPIN;

        this.children = new LinkedList<Person>();
    }


    // =============================================================================================

    // BIDIRECTIONAL LINKS

    // =============================================================================================


    /**
     * Adds a child to the list of children
     *
     *  Set as protected to allow only the package access it,
     *  so the user doesn't accidentally use this method.
     *
     * @param child
     */
    protected void addChild(Person child)
    {
        children.add(child);
    }


    /**
     * Removes a child to the list of children
     *
     *  Set as protected to allow only the package access it,
     *  so the user doesn't accidentally use this method.
     *
     * @param child
     */
    protected void removeChild(Person child)
    {
        children.remove(child);
    }


    /**
     * Assigns a Task to a person
     *
     * @param person pointer to person
     * @param task task to be assigned
     */
    public void assignTask(Person person, Task task)
    {
        task.assignTask(person); // creates a bidirectional link from Task to person
    }

    /**
     * Assigns a Task to a person
     *
     * @param fullName name of the person to be assigned
     * @param task task to be assigned
     */
    public void assignTask(String fullName, Task task)
    {
        for (Person p : children)
        {
            if (p.getFirstName().equals(fullName))
            {
                task.assignTask(p); // creates a bidirectional link from Task to person
                return ;
            }
        }
    }

    // =============================================================================================

    // GETTERS/SETTERS

    // =============================================================================================


    public void setPriorityLevel(int priorityLevel)
    {
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel()
    {
        return priorityLevel;
    }

    public void setHashedPIN(String hashedPIN)
    {
        this.hashedPIN = hashedPIN;
    }

    public String getHashedPIN()
    {
        return hashedPIN;
    }
}
