package com.example.nidhisingh.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.nidhisingh.todo.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nidhisingh on 7/24/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksManager";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DUE_DATE = "duedate";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRIORITY = "priority";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_PRIORITY + " TEXT" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);

    }

    // Adding new contact
    public void addTasks(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, task.getTaskName()); // task Name
            values.put(KEY_DUE_DATE, task.getDueDate()); // task due date
            values.put(KEY_NOTES, task.getTaskNotes()); // task notes
            values.put(KEY_STATUS, task.getStatus()); // task Name
            values.put(KEY_PRIORITY, task.getPriority()); // task Name

            // Inserting Row
            long rowid = db.insert(TABLE_TASKS, null, values);

            if (rowid != -1) {
                task.setId(rowid);
            }
        } finally {
            db.close(); // Closing database connection
        }
    }

    @Nullable
    Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID,
                        KEY_NAME, KEY_DUE_DATE, KEY_NOTES, KEY_STATUS, KEY_PRIORITY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Task task = new Task(
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return task
        return task;
    }


    public List<Task> getAllTasks() {
        // array of columns to fetch
        String[] columns = {
                KEY_ID,
                KEY_NAME,
                KEY_DUE_DATE,
                KEY_NOTES,
                KEY_STATUS,
                KEY_PRIORITY
        };

        List<Task>taskList = new ArrayList<Task>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_TASKS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getLong(0));
                task.setTaskName(cursor.getString(1));
                task.setDueDate(cursor.getString(2));
                task.setTaskNotes(cursor.getString(3));
                task.setStatus(cursor.getString(4));
                task.setPriority(cursor.getString(5));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return taskList;
    }

//    public void deleteRow(String taskname){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_TASKS, "KEY_NAME = ?", new String[]{taskname});
//        //db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COlUMN_NAME+"='"+value+"'");
//        db.close();
//
//    }

    public void deleteRow(long taskrow){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, "id = ?", new String[]{Long.toString(taskrow)});
        //db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COlUMN_NAME+"='"+value+"'");
        db.close();

    }

    public void updateRow(long row, Task t) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, t.getTaskName());
        cv.put(KEY_DUE_DATE, t.getDueDate());
        cv.put(KEY_NOTES, t.getTaskNotes());
        cv.put(KEY_STATUS, t.getStatus());
        cv.put(KEY_PRIORITY, t.getPriority());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_TASKS, cv, "id = ?", new String[]{Long.toString(row)});
        db.close();

    }

    // Getting All Contacts
    public List<Task> getAllTasks1() {
        List<Task> contactList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskName(cursor.getString(1));
                task.setDueDate(cursor.getString(2));
                task.setTaskNotes(cursor.getString(3));
                task.setStatus(cursor.getString(4));
                task.setPriority(cursor.getString(5));

                System.out.println(cursor.getString(1));
                System.out.println(cursor.getString(2));
                System.out.println(cursor.getString(3));
                System.out.println(cursor.getString(4));
                System.out.println(cursor.getString(5));


                // Adding contact to list
                contactList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }
}