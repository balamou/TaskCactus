package com.uottawa.thirstycactus.taskcactus.domain;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Peter Nguyen on 11/20/17.
 *
 * Singleton class to sync in all data throughout the app.
 * Communicates with the database.
 */

public class DataSingleton
{
    // ATTRIBUTES
    private static DataSingleton instance;
    private boolean load = false; // flag that checks if the classes have been loaded from the database
    private MyDBHandler dbHandler; // instance of the database handler


    // ASSOCIATIONS
    private List<Task> tasks;
    private List<Person> people;
    private List<Resource> resources;

    private Person loggedPerson;


    // CONSTRUCTOR
    /**
     * Set to private to avoid multiple instantiations
     */
    private DataSingleton()
    {
        tasks = new LinkedList<>();
        people = new LinkedList<>();
        resources = new LinkedList<>();
    }

    // =============================================================================================

    // METHODS

    // =============================================================================================

    /**
     * Creates the instance of DataSingleton once and returns it
     */
    public static DataSingleton getInstance()
    {
        if (instance == null)
            instance = new DataSingleton();

        return instance;
    }


    /**
     * Loads data from the database.
     *
     *      ** Temporarily filled with Random data
     *
     */
    private void loadData()
    {
        boolean load_database = true;

        if (!load)
        {
            if (!load_database)
            {
                temporaryData(); // in case the database malfunctions
            }
            else
            {
                people = dbHandler.getAllUsers();
                tasks = dbHandler.getAllTasks();
                resources = dbHandler.getAllResources();

                dbHandler.loadAssociations(people, tasks);
                dbHandler.loadResAssoc(resources, tasks);

                dbHandler.display(); // Display database content for debugging

                // FIND PERSON LOGGED IN
                int log_id = dbHandler.getLogged();
                for (Person person : people)
                {
                    if (person.getID() == log_id)
                    {
                        loggedPerson = person;
                        break;
                    }
                }
            }
        }

        load = true;
    }

    /**
     * Fills the lists with placeholder data for demonstrative purposes.
     * Also shows how every class interacts with each other.
     */
    public void temporaryData()
    {
        // Placeholder data
        Date d = getDate(1997, 1, 6);
        Parent michel = new Parent("Michel", "Balamou", d, "1234");

        Person peter = new Person("Peter", "Nguyen", null);
        Person nate = new Person("Nate", "Adams", null);


        loggedPerson = michel;

        people.add(michel);
        people.add(peter);
        people.add(nate);


        tasks.add(new Task("Wash dishes", "", 2, "DEFAULT"));
        tasks.add(new Task("Clean room", "", 4, "DEFAULT"));
        tasks.add(new Task("Recycle", "", 5, "DEFAULT"));



        tasks.add(new Task("Clean basement", "", 2));
        tasks.add(new Task("Finish app", "", 5));

        tasks.add(new Task("Wash car", "", 5));


        //RESOURCES +++

        resources.add(new Resource("Sponge", "For washing dishes"));
        resources.add(new Resource("Car", "To clean the car"));
        resources.add(new Resource("Fork", "For forking stuff"));

        tasks.get(0).allocateResource(resources.get(0));
        tasks.get(5).allocateResource(resources.get(1));

        //RESOURCES ---
        Date d1 = getDate(2017, 10, 24); // NOV 24, 2017
        Date d2 = getDate(2017, 11, 2); // DEC 2, 2017


        michel.assignTask(peter, tasks.get(0), d1, false, "");

        michel.assignTask(nate, tasks.get(3), d1, false, "");
        michel.assignTask(nate, tasks.get(4), d2, false, "");
        michel.assignTask(nate, tasks.get(5), d2, false, "");


        nate.getTaskDates().get(1).setCompleted(true);
    }

    /**
     * Initialize the database;
     */
    public void initDatabase(Context mainActivity)
    {
        dbHandler = new MyDBHandler(mainActivity);
    }

    /**
     * Converts Year, Month and Day into Date.
     *
     * The reason for using this is because the Date constructor has been depreciated and
     * this is the new accepted way of making a date due to Internalization issues.
     */
    private Date getDate(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }


    // =============================================================================================

    // GETTERS: returns full lists; loads data before hand if it wasn't loaded

    // =============================================================================================

    /**
     * Returns the list of default/premade tasks
     */
    public List<Task> getTasks()
    {
        loadData();
        return tasks;
    }


    /**
     * Returns the list of users from the database.
     */
    public List<Person> getUsers()
    {
        loadData();
        return people;
    }


    /**
     * Returns the list of resources from the database.
     */
    public List<Resource> getResources()
    {
        loadData();
        return resources;
    }


    /**
     * Returns a list of people who have a task on that day
     */
    public List<Person> getUsers(Date date)
    {
        loadData();
        List<Person> result = new LinkedList<>();

        for (Person p : people)
        {
            if (!p.getTaskDates(date).isEmpty())
                result.add(p);
        }

        return result;
    }


    /**
     * Number of tasks on a particular date
     */
    public int numTasks(Date date)
    {
        return dbHandler.getNumTasks(date); // DB~
    }
    // =============================================================================================

    // LOGIN

    // =============================================================================================

    /**
     * Returns the currently logged in person
     */
    public Person getLoggedPerson()
    {
        loadData();
        return loggedPerson;
    }

    /**
     * Login as parent. A 4 digit PIN is required for authentication.
     *
     * @param parent to log on as
     * @param PIN the 4 digit password required for login
     * @throws IllegalArgumentException if the PIN is not valid
     */
    public void login(Parent parent, String PIN) throws IllegalArgumentException
    {
        loadData();
        if (parent.getPIN().equals(PIN))
        {
            loggedPerson = parent;
            dbHandler.setLogged(parent.getID()); // DB~
        }
        else
        {
            throw new IllegalArgumentException("Invalid PIN.");
        }
    }

    /**
     * Login as person. No password required.
     *
     * @param person to log on as
     * @throws IllegalArgumentException if attempting to login as parent
     */
    public void login(Person person) throws IllegalArgumentException
    {
        loadData();
        if (!(person instanceof Parent))
        {
            loggedPerson = person;
            dbHandler.setLogged(person.getID()); // DB~
        }
        else
        {
            throw new IllegalArgumentException("Attempt to login as parent without providing 4 digit PIN.");
        }
    }


    /**
     * Checked if the currently logged in person is a parent
     */
    public boolean isLoggedAsParent()
    {
        return (loggedPerson instanceof Parent);
    }


    // =============================================================================================

    // DATABASE RELATED METHODS: PERSON

    // =============================================================================================

    public void resetDatabase()
    {
        dbHandler.clean();
    }

    public void setDefaults()
    {
        List<Task> tasks = new LinkedList<>();

        // Add default tasks to the database
        tasks.add(new Task("Wash dishes", "All the dishes", 5, "DEFAULT"));
        tasks.add(new Task("Clean room", "Get all your clothes off the chair", 5, "DEFAULT"));
        tasks.add(new Task("Recycle", "Good for environment", 5, "DEFAULT"));
        tasks.add(new Task("Clean basement", "", 5, "DEFAULT"));


        for (Task task : tasks)
            dbHandler.addTask(task);

        load = false;
        loadData();
    }

    /**
     * Adds a new person to the database
     *
     * @param userType type of account - Parent/Child
     * @param firstName
     * @param lastName
     * @param birthDay birth date (optional)
     * @param password 4 digit PIN (not required if child account)
     *
     * @return Exit Codes 0, 1, 2, 3, 4
     * 0 - success
     * 1 - first name empty
     * 2 - last name empty
     * 3 - password less than 4 digits
     * 4 - invalid date format (birth date)
     */
    public int addUser(String userType, String firstName, String lastName, String birthDay, String password)
    {
        // CHECKS IF THE DATA IS VALID BEFORE SUBMITTING IT
        if (firstName.isEmpty()) return 1; // Please enter the first name
        if (lastName.isEmpty()) return 2; // Please enter the last name
        if (userType.equals("Parent") && password.length()<4) return 3; // Please enter a 4 digit password


        Date birth = null;
        if (!birthDay.isEmpty())
        {
            try
            {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                birth = format.parse(birthDay);
            }
            catch (Exception e)
            {
                return 4; // Invalid date format
            }
        }

        // ADD A NEW USER
        Person newUser;

        if (userType.equals("Parent")) // ADD A PARENT
        {
            newUser = new Parent(firstName, lastName, birth, password);
        }
        else
        {
            newUser = new Person(firstName, lastName); // ADD A CHILD

            if (birth!=null) newUser.setBirthDate(birth); // CHANGE BIRTHDAY IF NOT EMPTY
        }

        people.add(newUser);
        dbHandler.addPerson(newUser); // DB~
        return 0; // Successfully added new user
    }

    /**
     * Updates the person in the database
     *
     * @param firstName
     * @param lastName
     * @param birthDay birth date (optional)
     * @param password 4 digit PIN (not required if child account)
     *
     * @return Exit Codes 0, 1, 2, 3, 4
     * 0 - success
     * 1 - first name empty
     * 2 - last name empty
     * 3 - password less than 4 digits
     * 4 - invalid date format (birth date)
     */
    public int updateUser(int user_id, String firstName, String lastName, String birthDay, String password)
    {
        Person person = people.get(user_id);

        // CHECKS IF THE DATA IS VALID BEFORE SUBMITTING IT
        if (firstName.isEmpty()) return 1; // Please enter the first name
        if (lastName.isEmpty()) return 2; // Please enter the last name
        if (person instanceof Parent && password.length()<4) return 3; // Please enter a 4 digit password

        Date birth = null;
        if (!birthDay.isEmpty())
        {
            try
            {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                birth = format.parse(birthDay);
            }
            catch (Exception e)
            {
                return 4; // Invalid date format
            }
        }

        // MODIFY AN EXISTING USER
        person.setFirstName(firstName);
        person.setLastName(lastName);

        if (birth!=null) person.setBirthDate(birth); // Change birthday if not empty

        if (person instanceof Parent) ((Parent)person).setPIN(password); // Change password if account is Parent

        dbHandler.updatePerson(person); // DB~
        return 0; // Successfully updated
    }


    /**
     * Delete a user from the list at index
     *
     * @param index index of the user in the people list
     */
    public void deleteUser(int index)
    {
        Person person = people.get(index);

        dbHandler.deletePerson(person.getID()); // DB~

        people.get(index).prepareToDelete();
        people.remove(index);
    }


    // =============================================================================================

    // DATABASE RELATED METHODS: TASK

    // =============================================================================================


    /***
     * @param name
     * @param desc
     * @param points
     * @param res boolean array of resources that have been added
     *
     * @return Exit Codes 0, 1, 2
     * 0 - success
     * 1 - task name empty
     * 2 - points is not a valid number
     */
    public int addTask(String name, String desc, String points, boolean[] res)
    {
        if (name.isEmpty()) return 1; // Please enter a task name

        int p;

        // Check if the points is a valid number
        try
        {
            p = Integer.parseInt(points);
        }
        catch (Exception e)
        {
            return 2; // Points has to be a number
        }


        Task task = new Task(name, desc, p);



        tasks.add(task);
        dbHandler.addTask(task); // DB~

        // ALLOCATE RESOURCES: that have the checkboxes on
        for (int i = 0; i<res.length ; i++)
        {
            if (res[i] && i<resources.size())
            {
                task.allocateResource(resources.get(i));

                dbHandler.allocateResource(resources.get(i), task); // DB~
            }
        }

        return 0; // Success
    }

    /***
     * @param task_id the position of the task in the
     * @param name
     * @param desc
     * @param points
     * @param initState intial state of resources
     * @param res boolean array of resources that have been added
     *
     * Example:
     *   resources = [res1,  res2,  res3]       <- All resources (used an array in this example, but its a LinkedList)
     *   initState = [false, true,  false]      <- Resources allocated before
     *   res       = [true,  false, false]      <- Resources allocated now
     *
     *   ^ this means task with ID 'task_id' has to allocate res1 and deallocate tes2
     *
     * @return Exit Codes 0, 1, 2
     * 0 - success
     * 1 - task name empty
     * 2 - points is not a valid number
     */
    public int updateTask(int task_id, String name, String desc, String points, boolean[] initState, boolean[] res)
    {
        if (name.isEmpty()) return 1; // Please enter a task name

        int p;

        // Check if the points is a valid number
        try
        {
            p = Integer.parseInt(points);
        }
        catch (Exception e)
        {
            return 2; // Points has to be a number
        }


        Task task = tasks.get(task_id);

        task.setName(name);
        task.setDesc(desc);
        task.setPoints(p);


        // ALLOCATE RESOURCES:
        // possible deallocation of resources that have the checkboxes off
        for (int i = 0; i<res.length; i++)
        {
            if (initState[i] != res[i]) // check if the state is different
            {
                if (res[i]) // Allocate a resource that wasn't allocated yet
                {
                    task.allocateResource(resources.get(i));
                    dbHandler.allocateResource(resources.get(i), task); // DB~
                }
                else // Deallocate a resource that was allocated
                {
                    task.deallocateResource(resources.get(i));
                    dbHandler.deallocateResource(resources.get(i), task); // DB~
                }
            }
        }

        dbHandler.updateTask(task); // DB~
        return 0; // Success
    }

    /**
     * Delete a task from the list at index
     */
    public void deleteTask(int index)
    {
        Task task = tasks.get(index);

        dbHandler.deleteTask(task.getID()); // DB~

        task.prepareToDelete();
        tasks.remove(index);
    }

    // =============================================================================================

    // DATABASE RELATED METHODS: TASK-PERSON ASSOCIATION

    // =============================================================================================

    /**
     *
     * @param user_id ID of the user in 'users' list
     * @param task_id ID of the task in 'tasks' list
     * @param newTask true means create a new task, false means task_id is the assigned task
     *
     * @param deadline date in the format of MM/dd/yyyy
     *
     * @param name cannot be an empty string
     * @param desc (optional) can be empty
     * @param points has to be a strict integer in a string format
     * @param note (optional) can be empty
     *
     *
     * @return Exit Codes 0, 1, 2, 3, 4
     * 0 - success
     * 1 - user not selected
     * 2 - task not selected
     * 3 - Invalid date format; try MM/dd/yyyy
     *
     * 4 - (1) task name empty (exit code 1 from addTask)
     * 5 - (2) points is not a valid number (exit code 2 from addTask)
     */
    public int assignTask(int user_id, int task_id, boolean newTask, String deadline, String name, String desc, String points, String note)
    {
        // CHECK VALIDITY OF DATA: returns exit code other than 0 on failure
        if (user_id == -1) return 1; // Please select user
        if (task_id == -1 && !newTask) return 2; // Please select task

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Date date;
        try
        {
            date = simpleDateFormat.parse(deadline);
        }
        catch(Exception e)
        {
            return 3; // Invalid date format. Try MM/dd/yyyy.
        }


        // ASSIGN TASK TO USER
        Person person = people.get(user_id);
        Task task;

        if (newTask) // CREATE NEW TASK
        {
            int exit_code = addTask(name, desc, points, new boolean[0]);

            if (exit_code!=0)
                return 3 + exit_code; // EXIT on failure; Shift by 3 (largest exit code in assignTask)
            else
                task = tasks.get(tasks.size()-1); // get task that was just added

        }
        else
        {
            task = tasks.get(task_id); // USE SELECTED TASK
        }

        TaskDate taskDate = person.assignTask(task, date, false, note);
        dbHandler.assignTask(taskDate); // DB~
        return 0; // SUCCESS
    }

    /**
     * Remove association between Person and Task
     */
    public void unassignTask(TaskDate taskDate)
    {
        taskDate.removeLink();

        dbHandler.unassignTask(taskDate);
    }

    /**
     * Set taskDate as completed
     */
    public void setCompleted(TaskDate taskDate)
    {
        dbHandler.setCompleted(taskDate);
    }

    // =============================================================================================

    // DATABASE RELATED METHODS: RESOURCES (comments omitted due to self explanatory nature)

    // =============================================================================================

    public void addResource(Resource res)
    {
        dbHandler.addResource(res); // DB~
        resources.add(res);
    }

    public void editResource(Resource res)
    {
        dbHandler.updateResource(res); // DB~
    }

    public void removeResource(Resource res)
    {
        dbHandler.deleteResource(res.getID()); // DB~

        res.prepareToDelete();
        resources.remove(res);
    }

    public void deallocateResource(Resource res, Task task)
    {
        dbHandler.deallocateResource(res, task); // DB~
        task.deallocateResource(res);
    }
}
