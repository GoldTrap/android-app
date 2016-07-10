package com.asb.goldtrap.models.results.examiners.impl.goodies;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.eo.TaskType;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;
import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Created by arjun on 28/11/15.
 */
public class GoodieTaskCompletionExaminer implements TaskCompletionExaminer {
    @Override
    public void examine(Score score, Task task) {
        GoodiesState goodiesState = getGoodieStateForTaskType(task.getTaskType());
        if (null != score.getGoodies().get(goodiesState) &&
                score.getGoodies().get(goodiesState).size() >= task.getCount()) {
            score.getCompletedTasks().add(task);
        }
        else {
            score.getIncompleteTasks().add(task);
        }
    }

    private GoodiesState getGoodieStateForTaskType(TaskType taskType) {
        GoodiesState goodiesState = GoodiesState.NOTHING;
        switch (taskType) {
            case ONE_K:
                goodiesState = GoodiesState.ONE_K;
                break;
            case TWO_K:
                goodiesState = GoodiesState.TWO_K;
                break;
            case FIVE_K:
                goodiesState = GoodiesState.FIVE_K;
                break;
            case DIAMOND:
                goodiesState = GoodiesState.DIAMOND;
                break;
        }
        return goodiesState;
    }
}
