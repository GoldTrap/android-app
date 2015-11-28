package com.asb.goldtrap.models.results.examiners;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;

/**
 * Created by arjun on 28/11/15.
 */
public interface TaskCompletionExaminer {
    void examine(Score score, Task task);
}
