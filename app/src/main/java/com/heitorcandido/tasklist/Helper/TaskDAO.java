package com.heitorcandido.tasklist.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.heitorcandido.tasklist.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements ITaskDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public TaskDAO(Context context) {
        DbHelper dataBase = new DbHelper(context);
        write = dataBase.getWritableDatabase();
        read = dataBase.getReadableDatabase();
    }

    @Override
    public boolean save(Task task) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", task.getTaskName());
        contentValues.put("finish", task.getStatusTask());

        try {
            write.insert(DbHelper.TABLE_TASK, null, contentValues);
            Log.i("INFO DB", "Success to save task");
        } catch (Exception e) {
            Log.i("INFO DB", "Fail to save task, error: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean update(Task task) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", task.getTaskName());
        contentValues.put("finish", task.getStatusTask());

        try {
            String[] args = {task.getId().toString()};
            write.update(DbHelper.TABLE_TASK, contentValues, "id=?", args);
            Log.i("INFO DB", "Success to update the task");
        } catch (Exception e) {
            Log.i("INFO DB", "Fail to update the task, error: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Task task) {

        try {

            String[] args = {task.getId().toString()};
            write.delete(DbHelper.TABLE_TASK, "id=?", args);

            Log.i("INFO DB", "Success to delete the task");
        } catch (Exception e) {
            Log.i("INFO DB", "Fail to delete the task, error: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Task> list() {
        List<Task> taskList = new ArrayList<>();

        String sqlRecover = "SELECT * FROM " + DbHelper.TABLE_TASK + " ;"; // ORDER BY finish
        Cursor cursor = read.rawQuery(sqlRecover, null);

        while (cursor.moveToNext()) {
            Task task = new Task();

            int idColumn = cursor.getColumnIndex("id");
            int taskNameColumn = cursor.getColumnIndex("name");
            int taskStatusColumn = cursor.getColumnIndex("finish");

            Long id = cursor.getLong(idColumn);
            String taskName = cursor.getString(taskNameColumn);
            int taskStatus = cursor.getInt(taskStatusColumn);

            task.setId(id);
            task.setTaskName(taskName);
            task.setStatusTask(taskStatus);

            taskList.add(task);
        }

        return taskList;
    }
}
