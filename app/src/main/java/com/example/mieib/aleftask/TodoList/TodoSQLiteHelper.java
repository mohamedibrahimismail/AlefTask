package com.example.mieib.aleftask.TodoList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

    public TodoSQLiteHelper(Context context) {
        // Databse: todos_db, Version: 1
        super(context, "todos_db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute create table SQL
        db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, todo TEXT NOT NULL,tododate TEXT NOT NULL);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        // DROP table
        db.execSQL("DROP TABLE IF EXISTS todos");
        // Recreate table
        onCreate(db);
    }

}