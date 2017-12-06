package com.uottawa.thirstycactus.taskcactus.domain;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.parseInt;


/**
 * Created by michelbalamou on 10/31/17.
 */

public class MyDBHandler extends SQLiteOpenHelper
{
    // ATTRIBUTES
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TaskCactus.db";

    // TABLE NAMES
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_RESOURCES = "resources";

    private static final String TABLE_TASK_DATES = "task_dates"; // ASSOCIATION BETWEEN TASK & PERSON
    private static final String TABLE_RES_TASK = "res_task"; // ASSOCIATION BETWEEN TASK & RESOURCE


    // COMMON
    private static final String COLUMN_ID = "_id";

    // USERS TABLE
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LOGGED = "logged";

    // TASKS TABLE
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_POINTS = "points";
    private static final String COLUMN_TYPE = "type"; // DEFAULT/NEW


    // RESOURCES TABLE
    private static final String COLUMN_RESOURCE_ID = "res_id";


    // TASKDATE TABLE
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_DATE = "date";


    // TABLE CREATE STATEMENTS
    private static final String CREATE_USERS_TABLE = "CREATE TABLE "
            + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_FIRSTNAME  + " TEXT,"
            + COLUMN_LASTNAME + " TEXT,"
            + COLUMN_BIRTHDAY + " DATE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_LOGGED + " INTEGER  DEFAULT 0" + ")";


    private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
            + TABLE_TASKS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME  + " TEXT,"
            + COLUMN_DESC + " TEXT,"
            + COLUMN_POINTS + " INTEGER,"
            + COLUMN_TYPE + " TEXT" + ")";

    private static final String CREATE_RESOURCES_TABLE = "CREATE TABLE "
            + TABLE_RESOURCES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME  + " TEXT,"
            + COLUMN_DESC + " TEXT" + ")";




    private static final String CREATE_TASKDATES_TABLE = "CREATE TABLE "
            + TABLE_TASK_DATES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TASK_ID + " INTEGER,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_NOTE + " TEXT,"
            + COLUMN_COMPLETED + " INTEGER," // 0 - false, 1 - true
            + COLUMN_DATE + " DATE" + ")";


    private static final String CREATE_RES_TASK_TABLE = "CREATE TABLE "
            + TABLE_RES_TASK + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_RESOURCE_ID + " INTEGER,"
            + COLUMN_TASK_ID + " INTEGER" + ")";


    // CONSTRUCTOR
    public MyDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_RESOURCES_TABLE);

        db.execSQL(CREATE_TASKDATES_TABLE);
        db.execSQL(CREATE_RES_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_DATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RES_TASK);

        // Create new tables
        onCreate(db);
    }


    // =============================================================================================

    // PERSON/USER

    // =============================================================================================

    /***
     * Returns a list of all users from the database
     */
    public List<Person> getAllUsers()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);

        List<Person> result = new LinkedList<>();

        if (cursor.moveToFirst())
        {
            do {
                int id = parseInt(cursor.getString(0));
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String birthDate = cursor.getString(3);
                String password = cursor.getString(4);

                Date date = toDate(birthDate);

                // Load user
                Person user;
                if (password==null || password.isEmpty())
                    user = new Person(id, firstName, lastName, date); // add user
                else
                    user = new Parent(id, firstName, lastName, date, password); // add as parent if password not empty

                result.add(user);
            } while (cursor.moveToNext());
        }

        db.close();
        return result;
    }


    /**
     * Converts String date to Date
     * Format: yyyy-MM-dd HH:mm:ss
     */
    public Date toDate(String date)
    {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date result = null;
        try
        {
            result = iso8601Format.parse(date);
        }
        catch (Exception e)
        {
            // nothing
        }

        return result;
    }


    /**
     * Adds a new person to the database
     *
     * @param person information about the person
     */
    public void addPerson(Person person)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add values to query
        values.put(COLUMN_FIRSTNAME, person.getFirstName());
        values.put(COLUMN_LASTNAME, person.getLastName());

        // Convert date to String
        if (person.getBirthDate()!=null)
        {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = iso8601Format.format(person.getBirthDate());
            values.put(COLUMN_BIRTHDAY, date);
        }

        // Add password
        if (person instanceof Parent) values.put(COLUMN_PASSWORD, ((Parent)person).getPIN());

        // Insert final query
        long id = db.insert(TABLE_USERS, null, values);
        person.setID((int)id); // set person id
        db.close();
    }


    /**
     * Updates a record of a person in the database
     *
     * @param person
     */
    public int updatePerson(Person person)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // INSERT NEW DATA
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, person.getFirstName());
        values.put(COLUMN_LASTNAME, person.getLastName());

        if (person.getBirthDate()!=null)
        {
            // Convert date to String
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = iso8601Format.format(person.getBirthDate());
            values.put(COLUMN_BIRTHDAY, date);
        }

        if (person instanceof Parent)
            values.put(COLUMN_PASSWORD, ((Parent)person).getPIN());

        // updating row
        return db.update(TABLE_USERS, values, COLUMN_ID + " = " + person.getID(), null);
    }


    /**
     * @param user_id
     */
    public int deletePerson(int user_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        removeUserAssoc(user_id);

        return db.delete(TABLE_USERS, COLUMN_ID + " = " + user_id, null);
    }


    // =============================================================================================

    // OTHER

    // =============================================================================================

    /**
     * UPDATE ALL TABLES
     */
    public void clean()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        onUpgrade(db, 0, 0);
    }

    public void setLogged(int user_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGGED, 1);

        ContentValues valuesZero = new ContentValues();
        valuesZero.put(COLUMN_LOGGED, 0);

        db.update(TABLE_USERS, valuesZero, null, null);
        db.update(TABLE_USERS, values, COLUMN_ID + " = " + user_id, null);
    }


    /**
     * Returns ID of the user logged in
     * Returns -1 if nobody is logged
     */
    public int getLogged()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_USERS + " WHERE " + COLUMN_LOGGED + " = 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                return cursor.getInt(0); // return first occurance
            }
            while (cursor.moveToNext());
        }

        return -1;
    }

    // =============================================================================================

    // TASKS

    // =============================================================================================

    /**
     * Get the number of tasks on that particular date
     */
    public int getNumTasks(Date date)
    {
        // Convert date to String
        if (date==null) return 0;

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
        String deadline = iso8601Format.format(date);

        // compare only date not time
        String countQuery = "SELECT  * FROM " + TABLE_TASK_DATES + " WHERE " + COLUMN_DATE +" >= date('"+ deadline +"') AND " + COLUMN_DATE + " <  date('" + deadline + "', '+1 day')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /***
     * Returns a list of all tasks from the database
     */
    public List<Task> getAllTasks()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_TASKS;
        Cursor cursor = db.rawQuery(query, null);

        List<Task> result = new LinkedList<>();

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                int points = cursor.getInt(3);
                String type = cursor.getString(4);

                Task task = new Task(id, name, desc, points, type);

                result.add(task);
            } while (cursor.moveToNext());
        }

        db.close();
        return result;
    }

    /**
     * Adds a new task to the database
     *
     * @param task information about the person
     */
    public void addTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add values to query
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_DESC, task.getDesc());
        values.put(COLUMN_POINTS, task.getPoints());
        values.put(COLUMN_TYPE, task.getType());

        // Insert final query
        long id = db.insert(TABLE_TASKS, null, values);
        task.setID((int)id); // Set task id
        db.close();
    }


    /**
     * Updates a record of a task in the database
     *
     * @param task
     */
    public int updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // INSERT NEW DATA
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_DESC, task.getDesc());
        values.put(COLUMN_POINTS, task.getPoints());
        values.put(COLUMN_TYPE, task.getType());


        // updating row
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = " + task.getID(), null);
    }


    /**
     * @param task_id
     */
    public int deleteTask(int task_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        removeTaskAssoc(task_id);
        removeLinksToTask(task_id);

        return db.delete(TABLE_TASKS, COLUMN_ID + " = " + task_id, null);
    }

    // =============================================================================================

    // TASK DATE

    // =============================================================================================

    /**
     * @param taskDate
     */
    public void assignTask(TaskDate taskDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_ID, taskDate.getTask().getID());
        values.put(COLUMN_USER_ID, taskDate.getPerson().getID());
        values.put(COLUMN_NOTE, taskDate.getNotes());
        values.put(COLUMN_COMPLETED, taskDate.getCompleted() ? 1 : 0);

        if (taskDate.getDate()!=null)
        {
            // Convert date to String
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = iso8601Format.format(taskDate.getDate());
            values.put(COLUMN_DATE, date);
        }

        long id = db.insert(TABLE_TASK_DATES, null, values);
        taskDate.setID((int)id);
    }


    public void loadAssociations(List<Person> people, List<Task> tasks)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_TASK_DATES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                int task_id = cursor.getInt(1);
                int user_id = cursor.getInt(2);
                String note = cursor.getString(3);
                int completed = cursor.getInt(4);
                String date = cursor.getString(5);


                Person p = null;
                Task t = null;

                // find person by id
                for (Person person : people)
                {
                    if (person.getID() == user_id)
                    {
                        p = person;
                        break;
                    }
                }

                // find task by id
                for (Task task : tasks)
                {
                    if (task.getID() == task_id)
                    {
                        t = task;
                        break;
                    }
                }

                // Create association between Person and Task
                if (p!=null && t!=null)
                    p.assignTask(t, toDate(date), completed == 1 ? true : false, note).setID(id);
                else
                    Log.wtf("DATABASE", "Both task and person are null? line 477 in database handler.");


            } while (cursor.moveToNext());
        }

        db.close();
    }


    /**
     * Removes all the tasks associates with a user at user_id
     */
    public int removeUserAssoc(int user_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TASK_DATES, COLUMN_USER_ID + " = " + user_id, null);
    }


    /**
     * Removes all the tasks associates with a task at task_id
     */
    public int removeTaskAssoc(int task_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TASK_DATES, COLUMN_TASK_ID + " = " + task_id, null);
    }

    /**
     * @param taskDate
     */
    public int unassignTask(TaskDate taskDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TASK_DATES, COLUMN_ID + " = " + taskDate.getID(), null);
    }

    /**
     * @param taskDate
     */
    public int setCompleted(TaskDate taskDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // INSERT NEW DATA
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, taskDate.getCompleted() ? 1 : 0);

        // updating row
        return db.update(TABLE_TASK_DATES, values, COLUMN_ID + " = " + taskDate.getID(), null);
    }

    // =============================================================================================

    // RESOURCES

    // =============================================================================================
    /***
     * Returns a list of all resources from the database
     */
    public List<Resource> getAllResources()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_RESOURCES;
        Cursor cursor = db.rawQuery(query, null);

        List<Resource> result = new LinkedList<>();

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);

                Resource res = new Resource(id, name, desc);

                result.add(res);
            } while (cursor.moveToNext());
        }

        db.close();
        return result;
    }

    /**
     * Adds a new resource to the database
     *
     * @param resource information about the resource
     */
    public void addResource(Resource resource)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add values to query
        values.put(COLUMN_NAME, resource.getName());
        values.put(COLUMN_DESC, resource.getDesc());

        // Insert final query
        long id = db.insert(TABLE_RESOURCES, null, values);
        resource.setID((int)id); // Set task id
        db.close();
    }


    /**
     * Updates a record of a resource in the database
     *
     * @param resource
     */
    public int updateResource(Resource resource)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // INSERT NEW DATA
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, resource.getName());
        values.put(COLUMN_DESC, resource.getDesc());


        // updating row
        return db.update(TABLE_RESOURCES, values, COLUMN_ID + " = " + resource.getID(), null);
    }


    /**
     * @param res_id
     */
    public int deleteResource(int res_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        removeTaskAssoc(res_id);
        removeLinksToRes(res_id);

        return db.delete(TABLE_RESOURCES, COLUMN_ID + " = " + res_id, null);
    }

    // =============================================================================================

    // RESOURCES - TASK ASSOCIATION

    // =============================================================================================

    /**
     * @param res
     * @param task
     */
    public void allocateResource(Resource res, Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RESOURCE_ID, res.getID());
        values.put(COLUMN_TASK_ID, task.getID());

        db.insert(TABLE_RES_TASK, null, values);
        db.close();
    }

    public void deallocateResource(Resource res, Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = COLUMN_RESOURCE_ID + " = ? AND " + COLUMN_TASK_ID + " = ? ";

        String[] args = new String[] {
                Integer.toString(res.getID()),
                Integer.toString(task.getID())};

        db.delete(TABLE_RES_TASK, query, args);
        db.close();
    }

    public void loadResAssoc(List<Resource> resources, List<Task> tasks)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_RES_TASK;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                int res_id = cursor.getInt(1);
                int task_id = cursor.getInt(2);

                Resource r = null;
                Task t = null;

                // find resource by id
                for (Resource res : resources)
                {
                    if (res.getID() == res_id)
                    {
                        r = res;
                        break;
                    }
                }

                // find task by id
                for (Task task : tasks)
                {
                    if (task.getID() == task_id)
                    {
                        t = task;
                        break;
                    }
                }

                // Create association between Person and Task
                if (r!=null && t!=null)
                    t.allocateResource(r);
                else
                    Log.wtf("DATABASE", "Both task and resource are null? line 685 in database handler.");


            } while (cursor.moveToNext());
        }

        db.close();
    }



    /**
     * Removes all the resources associates with a task at task_id
     *
     * About to delete a Task
     */
    public int removeLinksToTask(int task_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_RES_TASK, COLUMN_TASK_ID + " = " + task_id, null);
    }


    /**
     * Removes all the resources associates with a resource at res_id
     *
     * About to delete a Resource
     */
    public int removeLinksToRes(int res_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_RES_TASK, COLUMN_RESOURCE_ID + " = " + res_id, null);
    }

    // =============================================================================================

    // METHODS FOR DEBUGGING THE DATABASE

    // =============================================================================================

    /**
     * Displays data from the database in a table
     */
    public void display()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);


        int w = 15;

        Log.wtf("ROW", fill("-", 71));
        Log.wtf("ROW", center("USERS", 71));
        Log.wtf("ROW", fill("-", 71));

        Log.wtf("ROW", format("ID", 5) + format("Firstname", w) + format("Lastname", w) + format("BirthDate", w) + format("Password", w) + format("Logged", w));
        Log.wtf("ROW", fill("-", 71));

        if (cursor.moveToFirst())
        {
            do {
                String output = "";
                for (int i=0; i<6; i++)
                {
                    String s = cursor.getString(i);
                    output += format(s == null ? "---" : s, i==0 ? 5 : w);
                }
                Log.wtf("ROW", output);
                //Log.wtf("ROW", format(id, 5) + format(firstName, w) + format(lastName, w) + format(birthDate==null ? "---" : birthDate, w) + format(password==null ? "---" : password, w) + format(log, w));
            } while (cursor.moveToNext());
        }

        Log.wtf("ROW", fill("-", 71));
        Log.wtf("ROW", center("TASKS", 71));
        Log.wtf("ROW", fill("-", 71));
        Log.wtf("ROW", format("ID", 5) + format("Name", 20) + format("Desc", 20) + format("Points", 10) + format("Type", w));
        Log.wtf("ROW", fill("-", 71));

        query = "Select * FROM " + TABLE_TASKS;
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                String points = cursor.getString(3);
                String type = cursor.getString(4);

                w = 10;

                Log.wtf("ROW", format(id, 5) + format(name, 20) + format(desc.isEmpty() ? "---" : desc, 20) + format(points, 10) + format(type, w));
            } while (cursor.moveToNext());
        }

        Log.wtf("ROW", fill("-", 71));
        Log.wtf("ROW", center("RESOURCES", 71));
        Log.wtf("ROW", fill("-", 71));
        Log.wtf("ROW", format("ID", 5) + format("Name", 20) + format("Desc", w));
        Log.wtf("ROW", fill("-", 71));



        query = "Select * FROM " + TABLE_RESOURCES;
        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);

                Log.wtf("ROW", format(id, 5) + format(name, 20) + format(desc.isEmpty() ? "---" : desc, w));
            } while (cursor.moveToNext());
        }


        db.close();
    }


    /**
     * Contains a string within a finite amount of space to display in a table cell.
     * Adds spaces to the string if its less than 'width'.
     * Removes any character of a string whose index is over 'width'.
     * THIS METHOD IS PURELY FOR VISUAL ENHANCEMENT
     *
     *  Example: format("45.6345690", 8)  returns "45.6..  "
     *  Example: format("Hello", 8)  returns "Hello   "
     *  Example: format("My name is Bond", 14)  returns "My name is..  "
     *
     * @param s string to be resized
     * @param width width of the table column
     * @return a resized string
     */
    public String format(String s, int width)
    {
        int len = (s!=null) ? s.length() : 4;

        if (len<width)
        {
            for (int i=0; i<(width-len); i++)
                s+=" ";
        }
        else
        {
            s=s.substring(0, width-4);
            s+="..  ";
        }

        return s;
    }

    /**
     * Creates a string of size 'size' and fills it with the character
     * in string 's'
     *
     * Example: fill("-", 10) returns "----------"
     * Example: fill("~", 5) returns "~~~~~"
     *
     * @param s character to be filled
     * @param size size of the resutling string
     * @return a string with a repeating character
     */
    public String fill(String s, int size)
    {
        String filler="";
        for (int i=0; i<size; i++)
            filler+=s;

        return filler;
    }


    /**
     * Centers the string 's'.
     * This method adds enough spaces to the beginning of the string to
     * make it appear centred over 'size' number of spaces.
     * THIS METHOD IS PURELY FOR VISUAL ENHANCMENT
     *
     *  Example: center("Pasta", 20)  returns "       Pasta        "
     *  Example: center("Home", 30)  returns "             Home             "
     *
     * Precondition: size.length()<s
     *
     * @param s string to center
     * @param size length of the final string
     * @return a centered string
     */
    public String center(String s, int size){
        String spaces=fill(" ", size/2-s.length()/2);
        return spaces + s + spaces;
    }
}