package com.asb.goldtrap.models.results.examiners.impl;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;

/**
 * Created by arjun on 28/11/15.
 */
public class LinesTaskCompletionExaminer implements TaskCompletionExaminer {
    @Override
    public void examine(Score score, Task task) {
        if (score.getHorizontalLines().size() + score.getVerticalLines().size() >=
                task.getCount()) {
            score.getCompletedTasks().add(task);
        }
        else {
            score.getIncompleteTasks().add(task);
        }
    }
}
