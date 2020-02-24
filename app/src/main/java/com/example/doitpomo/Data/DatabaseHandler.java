package com.example.doitpomo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.doitpomo.Model.Subtask;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.Utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TODO_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + Constants.KEY_TODO_ITEM + " TEXT NOT NULL,"
                + Constants.KEY_TIME_SPENT + " INTEGER,"
                + Constants.KEY_DONE + " INTEGER,"
                + Constants.KEY_PRIORITY + " TEXT NOT NULL,"
                + Constants.KEY_DESCRIPTION + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG,"
                + Constants.KEY_COMPLETION_DATE + " LONG,"
                + Constants.KEY_TODO_DATE_FINISH + " LONG);";

        db.execSQL(CREATE_TODO_TABLE);

        String CREATE_SUBTASK_TABLE = "CREATE TABLE " + Constants.SUBTASK_TABLE_NAME + "("
                + Constants.KEY_SUBTASK_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + Constants.KEY_SUBTASK_ITEM + " TEXT NOT NULL,"
                + Constants.KEY_SUBTASK_DONE + " INTEGER,"
                + Constants.KEY_TODO_ID + " INTEGER);";

        db.execSQL(CREATE_SUBTASK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SUBTASK_TABLE_NAME);
        onCreate(db);

    }

    /**
     * CRUD Operations: Create, Read, Update, Delete Methods
     */

    //Add TodoItem
    public void addTodoItem(TodoItem todoItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TODO_ITEM, todoItem.getName());
        values.put(Constants.KEY_TIME_SPENT, todoItem.getTimeSpent());
        values.put(Constants.KEY_PRIORITY, todoItem.getPriority());
        values.put(Constants.KEY_DESCRIPTION, todoItem.getDescription());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());
        values.put(Constants.KEY_TODO_DATE_FINISH, todoItem.getFinishDate());
        values.put(Constants.KEY_COMPLETION_DATE, todoItem.getCompletionDate());
        values.put(Constants.KEY_DONE, todoItem.getDone());

        //insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");
    }

    //Add SubTask

    public void addSubtask(Subtask subtask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_SUBTASK_ITEM, subtask.getName());
        values.put(Constants.KEY_SUBTASK_DONE, subtask.getDone());
        values.put(Constants.KEY_TODO_ID, subtask.getTaskId());
    }

    //Get a TodoItem
    public TodoItem getTodoItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]
                        {       Constants.KEY_ID,
                                Constants.KEY_TODO_ITEM,
                                Constants.KEY_TIME_SPENT,
                                Constants.KEY_DONE,
                                Constants.KEY_PRIORITY,
                                Constants.KEY_DESCRIPTION,
                                Constants.KEY_DATE_NAME,
                                Constants.KEY_COMPLETION_DATE,
                                Constants.KEY_TODO_DATE_FINISH},
                Constants.KEY_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        TodoItem todoItem = new TodoItem();
        todoItem.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        todoItem.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_ITEM)));
        todoItem.setTimeSpent(cursor.getInt(cursor.getColumnIndex(Constants.KEY_TIME_SPENT)));
        todoItem.setDone(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DONE))));
        todoItem.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
        todoItem.setPriority(cursor.getString(cursor.getColumnIndex(Constants.KEY_PRIORITY)));
        todoItem.setCompletionDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_COMPLETION_DATE)));
        todoItem.setFinishDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_DATE_FINISH)));

        //convert timestamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

        todoItem.setDateItemAdded(formatedDate);


        return todoItem;
    }

    public Subtask getSubtask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.SUBTASK_TABLE_NAME, new String[]
                        {
                                Constants.KEY_SUBTASK_ID,
                                Constants.KEY_SUBTASK_ITEM,
                                Constants.KEY_SUBTASK_DONE,
                                Constants.KEY_TODO_ID},
                Constants.KEY_SUBTASK_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Subtask subtask = new Subtask();
        subtask.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_ID))));
        subtask.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_ITEM)));
        subtask.setDone(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_DONE))));
        subtask.setTaskId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_ID))));

        return subtask;
    }

    //Get all TodoItems
    public List<TodoItem> getAllTodoItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<TodoItem> todoItemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID, Constants.KEY_TODO_ITEM, Constants.KEY_TIME_SPENT, Constants.KEY_DONE,
                        Constants.KEY_PRIORITY, Constants.KEY_DESCRIPTION,
                        Constants.KEY_DATE_NAME, Constants.KEY_COMPLETION_DATE, Constants.KEY_TODO_DATE_FINISH},
                null, null, null, null, Constants.KEY_TODO_DATE_FINISH + " ASC");

        if (cursor.moveToFirst()) {
            do {
                TodoItem todoItem = new TodoItem();
                todoItem.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                todoItem.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_ITEM)));
                todoItem.setPriority(cursor.getString(cursor.getColumnIndex(Constants.KEY_PRIORITY)));
                todoItem.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                todoItem.setTimeSpent(cursor.getInt(cursor.getColumnIndex(Constants.KEY_TIME_SPENT)));
                todoItem.setFinishDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_DATE_FINISH)));
                todoItem.setCompletionDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_COMPLETION_DATE)));
                todoItem.setDone(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DONE))));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                todoItem.setDateItemAdded(formatedDate);

                //Add to th groceryList
                todoItemList.add(todoItem);

            } while (cursor.moveToNext());
        }

        return todoItemList;
    }

    public List<Subtask> getAllSubtaks() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Subtask> subtasksList = new ArrayList <>();

        Cursor cursor = db.query(Constants.SUBTASK_TABLE_NAME, new String[] {
                Constants.KEY_SUBTASK_ID,
                Constants.KEY_SUBTASK_ITEM,
                Constants.KEY_SUBTASK_DONE,
                Constants.KEY_TODO_ITEM
        }, null, null, null, null, Constants.KEY_SUBTASK_DONE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_ID))));
                subtask.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_ITEM)));
                subtask.setDone(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_SUBTASK_DONE))));
                subtask.setTaskId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_ID))));
            } while (cursor.moveToNext());
        }

        return subtasksList;
    }

    //Update TodoITemList
    public int updateTodoItem(TodoItem todoItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TODO_ITEM, todoItem.getName());
        values.put(Constants.KEY_PRIORITY, todoItem.getPriority());
        values.put(Constants.KEY_DESCRIPTION, todoItem.getDescription());
        values.put(Constants.KEY_TIME_SPENT, todoItem.getTimeSpent());
        values.put(Constants.KEY_TODO_DATE_FINISH, todoItem.getFinishDate());
        values.put(Constants.KEY_COMPLETION_DATE, todoItem.getCompletionDate());
        values.put(Constants.KEY_DONE, todoItem.getDone());
//        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis()); //get system time

        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {String.valueOf(todoItem.getId())});
    }

    public int updateSubtask(Subtask subtask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_SUBTASK_ITEM, subtask.getName());
        values.put(Constants.KEY_SUBTASK_DONE, subtask.getDone());
        values.put(Constants.KEY_TODO_ID, subtask.getTaskId());

        return db.update(Constants.SUBTASK_TABLE_NAME, values, Constants.KEY_SUBTASK_ID +"=?", new String[] {String.valueOf(subtask.getId())});

    }

    //Delete TodoItem
    public void deleteTodoItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[] {String.valueOf(id)});

        db.close();
    }

    //Get count
    public int getTodoItemCount() {

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
