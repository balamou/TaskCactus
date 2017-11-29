package com.uottawa.thirstycactus.taskcactus.domain;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by michelbalamou on 10/31/17.
 */

public class MyDBHandler extends SQLiteOpenHelper
{
    // ATTRIBUTES
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TaskCactus.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_PASSWORD = "password";


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
        String CREATE_USERS_TABLE =
                "CREATE TABLE " +  TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRSTNAME  + " TEXT,"
                + COLUMN_LASTNAME + " TEXT,"
                + COLUMN_BIRTHDAY + " DATE,"
                + COLUMN_PASSWORD + " TEXT"  +")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

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
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String birthDate = cursor.getString(3);
                String password = cursor.getString(4);


                SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = null;
                try
                {
                    date = iso8601Format.parse(birthDate);
                }
                catch (Exception e)
                {

                }

                // Load user
                Person user;
                if (password.isEmpty())
                    user = new Person(firstName, lastName, date); // add user
                else
                    user = new Parent(firstName, lastName, date, password); // add as parent if password not empty

                result.add(user);
            } while (cursor.moveToNext());
        }

        db.close();
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
        if (person instanceof Parent) values.put(COLUMN_PASSWORD, ((Parent)person).getHashedPIN());

        // Insert final query
        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    /**
     * Updates a record of a person in the database
     *
     * @param person
     */
    public int updatePerson(Person person, int user_id)
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
            values.put(COLUMN_PASSWORD, ((Parent)person).getHashedPIN());

        // updating row
        return db.update(TABLE_USERS, values, "id=" + Integer.toString(user_id), null );
    }


    /**
     * @param user_id
     */
    public int deletePerson(int user_id)
    {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_USERS, COLUMN_ID + " = " + user_id, null);
    }

    public void clean()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

}