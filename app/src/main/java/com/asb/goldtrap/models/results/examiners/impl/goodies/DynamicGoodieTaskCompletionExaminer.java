package com.asb.goldtrap.models.results.examiners.impl.goodies;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;

/**
 * Created by arjun on 28/11/15.
 */
public class DynamicGoodieTaskCompletionExaminer implements TaskCompletionExaminer {
    @Override
    public void examine(Score score, Task task) {
        if (score.getDynamicGoodies().size() >= task.getCount()) {
            score.getCompletedTasks().add(task);
        }
        else {
            score.getIncompleteTasks().add(task);
        }
    }
}
