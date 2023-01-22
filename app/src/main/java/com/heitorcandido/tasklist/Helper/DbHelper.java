package com.heitorcandido.tasklist.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String DB_NAME = "DATABASE_TASKS";
    public static String TABLE_TASK = "tasks";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, finish BOOLEAN DEFAULT 0);";

        try {
            dataBase.execSQL(createTable);
            Log.i("INFO DB", "Success to create the table");
        } catch (Exception e) {
            Log.i("INFO DB", "Fail to create the table, error: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int i, int i1) {

    }
}
