package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 07/11/15.
 */
public class Task {
    private TaskType taskType;
    private int count;
    private int points;

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
