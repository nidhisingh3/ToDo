package com.example.nidhisingh.todo.model;

/**
 * Created by nidhisingh on 7/24/17.
 */

public class Task {
    //private variables
    long id;
    String taskName;
    String dueDate;
    String taskNotes;
    String status;
    String priority;


    public Task() {

    }
    public Task(String taskName, String dueDate, String taskNotes, String status, String priority) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.taskNotes = taskNotes;
        this.status = status;
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDueDate() {

        return this.dueDate;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
