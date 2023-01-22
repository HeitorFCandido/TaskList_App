package com.heitorcandido.tasklist.Model;

import java.io.Serializable;

public class Task implements Serializable {

    private Long id;

    private String taskName;
    private boolean statusTask;

    public boolean getStatusTask() {
        return statusTask;
    }

    public int getStatusTaskINT() {
        int statusI = 0;

        if (statusTask){
            statusI = 1;
        }

        return statusI;
    }

    public void setStatusTask(int statusTask) {

        this.statusTask = statusTask == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
