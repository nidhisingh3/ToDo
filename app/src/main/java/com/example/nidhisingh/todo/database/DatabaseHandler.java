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
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DUETIME = "duetime";


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
                + KEY_PRIORITY + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DUETIME +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
        System.out.println("New table created");

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
            values.put(KEY_CATEGORY, task.getCategory());
            values.put(KEY_DUETIME, task.getDueTime());

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
                        KEY_NAME, KEY_DUE_DATE, KEY_NOTES, KEY_STATUS, KEY_PRIORITY, KEY_CATEGORY, KEY_DUETIME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Task task = new Task(
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return task
        return task;
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
        cv.put(KEY_CATEGORY, t.getCategory());
        cv.put(KEY_DUETIME, t.getDueTime());
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.update(TABLE_TASKS, cv, "id = ?", new String[]{Long.toString(row)});

        }
        finally {
            db.close();
        }


    }
    public List<Task> getAllTasks() {
        // array of columns to fetch
        String[] columns = {
                KEY_ID,
                KEY_NAME,
                KEY_DUE_DATE,
                KEY_NOTES,
                KEY_STATUS,
                KEY_PRIORITY,
                KEY_CATEGORY,
                KEY_DUETIME

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
                task.setCategory(cursor.getString(6));
                task.setDueTime(cursor.getString(7));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return taskList;
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