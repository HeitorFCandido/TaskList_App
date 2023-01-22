package com.heitorcandido.tasklist.Helper;

import com.heitorcandido.tasklist.Model.Task;

import java.util.List;

public interface ITaskDAO {
    public boolean save(Task task);
    public boolean update(Task task);
    public boolean delete(Task task);
    public List<Task> list();
}
