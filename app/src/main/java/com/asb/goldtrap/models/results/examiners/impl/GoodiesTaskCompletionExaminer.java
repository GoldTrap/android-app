package com.asb.goldtrap.models.results.examiners.impl;

import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;

import java.util.List;

/**
 * Created by arjun on 28/11/15.
 */
public class GoodiesTaskCompletionExaminer implements TaskCompletionExaminer {
    @Override
    public void examine(Score score, Task task) {
        int goodieCount = 0;
        for (List<Goodie> goodies : score.getGoodies().values()) {
            goodieCount += goodies.size();
        }
        if (goodieCount >= task.getCount()) {
            score.getCompletedTasks().add(task);
        }
        else {
            score.getIncompleteTasks().add(task);
        }
    }
}
