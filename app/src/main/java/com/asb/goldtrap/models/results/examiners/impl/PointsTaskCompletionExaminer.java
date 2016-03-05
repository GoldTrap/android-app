package com.asb.goldtrap.models.results.examiners.impl;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;

/**
 * Created by arjun on 20/02/16.
 */
public class PointsTaskCompletionExaminer implements TaskCompletionExaminer {
    @Override
    public void examine(Score score, Task task) {
        if (score.basicScore() >= task.getPoints()) {
            score.getCompletedTasks().add(task);
        }
        else {
            score.getIncompleteTasks().add(task);
        }
    }
}
