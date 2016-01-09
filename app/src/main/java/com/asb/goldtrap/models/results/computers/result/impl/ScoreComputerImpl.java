package com.asb.goldtrap.models.results.computers.result.impl;

import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.components.ScoreComponentsComputer;
import com.asb.goldtrap.models.results.computers.components.impl.CellScoreComputer;
import com.asb.goldtrap.models.results.computers.components.impl.DynamicGoodieScoreComputer;
import com.asb.goldtrap.models.results.computers.components.impl.GoodieScoreComputer;
import com.asb.goldtrap.models.results.computers.components.impl.HorizontalLinesScoreComputer;
import com.asb.goldtrap.models.results.computers.components.impl.ResultComputer;
import com.asb.goldtrap.models.results.computers.components.impl.VerticalLinesScoreComputer;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.GoodiesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.HorizontalLinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.LinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.VerticalLinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.goodies.DynamicGoodieTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.goodies.GoodieTaskCompletionExaminer;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

import java.util.Arrays;
import java.util.List;

/**
 * Created by arjun on 09/01/16.
 */
public class ScoreComputerImpl implements ScoreComputer {

    private List<ScoreComponentsComputer> scoreComputers;
    private ScoreComponentsComputer resultComputer;
    private DotsGameSnapshot dotsGameSnapshot;
    private TaskCompletionExaminer linesTaskCompletionExaminer =
            new LinesTaskCompletionExaminer();
    private TaskCompletionExaminer horizontalLinesTaskCompletionExaminer =
            new HorizontalLinesTaskCompletionExaminer();
    private TaskCompletionExaminer verticalLinesTaskCompletionExaminer =
            new VerticalLinesTaskCompletionExaminer();
    private TaskCompletionExaminer goodiesTaskCompletionExaminer =
            new GoodiesTaskCompletionExaminer();
    private TaskCompletionExaminer goodieTaskCompletionExaminer =
            new GoodieTaskCompletionExaminer();
    private TaskCompletionExaminer dynamicGoodieTaskCompletionExaminer =
            new DynamicGoodieTaskCompletionExaminer();

    public ScoreComputerImpl(DotsGameSnapshot dotsGameSnapshot) {
        this.dotsGameSnapshot = dotsGameSnapshot;
        resultComputer = new ResultComputer(dotsGameSnapshot.getCells());
        scoreComputers =
                Arrays.asList(new CellScoreComputer(dotsGameSnapshot.getCells()),
                        new HorizontalLinesScoreComputer(dotsGameSnapshot.getCells()),
                        new VerticalLinesScoreComputer(dotsGameSnapshot.getCells()),
                        new GoodieScoreComputer(dotsGameSnapshot.getGoodies(),
                                dotsGameSnapshot.getCells()),
                        new DynamicGoodieScoreComputer(dotsGameSnapshot.getDynamicGoodies(),
                                dotsGameSnapshot.getCells()));
    }

    @Override
    public void computeScore() {
        dotsGameSnapshot.getScore().clearScore();
        for (ScoreComponentsComputer scoreComputer : scoreComputers) {
            scoreComputer.computeScore(dotsGameSnapshot);
        }
    }

    @Override
    public void computeScoreWithResults() {
        computeScore();
        resultComputer.computeScore(dotsGameSnapshot);
        computeResult(dotsGameSnapshot.getTasks(), dotsGameSnapshot.getScore());
    }

    private void computeResult(List<Task> tasks, Score score) {
        for (Task task : tasks) {
            switch (task.getTaskType()) {
                case LINES:
                    linesTaskCompletionExaminer.examine(score, task);
                    break;
                case HORIZONTAL_LINE:
                    horizontalLinesTaskCompletionExaminer.examine(score, task);
                    break;
                case VERTICAL_LINE:
                    verticalLinesTaskCompletionExaminer.examine(score, task);
                    break;
                case GOODIES:
                    goodiesTaskCompletionExaminer.examine(score, task);
                    break;
                case ONE_K:
                case TWO_K:
                case FIVE_K:
                case DIAMOND:
                    goodieTaskCompletionExaminer.examine(score, task);
                    break;
                case DYNAMIC_GOODIE:
                    dynamicGoodieTaskCompletionExaminer.examine(score, task);
                    break;
            }
        }
        resultComputer.computeScore(dotsGameSnapshot);
    }
}
