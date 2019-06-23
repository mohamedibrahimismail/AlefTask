package com.example.mieib.aleftask.TodoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    private SQLiteDatabase db;
    private TodoSQLiteHelper dbHelper;

    public TodoDAO(Context context) {
        dbHelper = new TodoSQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Close the db
    public void close() {
        db.close();
    }

    /**
     * Create new TODO object
     * @param todoText
     */
    public void createTodo(String todoText,String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo", todoText);
        contentValues.put("tododate", date);
        // Insert into DB
        db.insert("todos", null, contentValues);
    }

    /**
     * Delete TODO object
     * @param todoId
     */
    public void deleteTodo(int todoId) {
        // Delete from DB where id match
        db.delete("todos", "_id = " + todoId, null);
    }

    /**
     * Get all TODOs.
     * @return
     */
    public List getTodos() {
        List todoList = new ArrayList();

        // Name of the columns we want to select
        String[] tableColumns = new String[] {"_id","todo","tododate"};

        // Query the database
        Cursor cursor = db.query("todos", tableColumns, null, null, null, null, null);
        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {
            Todo todo = new Todo();
            // Take values from the DB
            todo.setId(cursor.getInt(0));
            todo.setText(cursor.getString(1));
            todo.setDate(cursor.getString(2));

            // Add to the DB
            todoList.add(todo);

            // Move to the next result
            cursor.moveToNext();
        }

        return todoList;
    }

}
