package com.asb.goldtrap.fragments.postgame;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.TasksRecyclerAdapter;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.result.ScoreComputer;
import com.asb.goldtrap.models.results.computers.result.impl.ScoreComputerImpl;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.sound.SoundModel;
import com.asb.goldtrap.models.sound.impl.SoundModelImpl;
import com.asb.goldtrap.models.sound.strategy.NoteType;
import com.asb.goldtrap.models.sound.strategy.SoundHelper;
import com.asb.goldtrap.models.sound.strategy.factory.SoundFactory;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import java.util.List;

/**
 * Score Fragment
 * Created on 29/11/2015
 */
public class ScoreFragment extends Fragment {

    public static final String TAG = ScoreFragment.class.getSimpleName();
    public static final String SNAPSHOT = "snapshot";
    public static final String LEVEL_CODE = "levelCode";
    private Score score;
    private TextView result;
    private TextView points;
    private TextView tasks;
    private RecyclerView taskList;
    private FloatingActionButton action;
    private OnFragmentInteractionListener mListener;
    private ScoreComputer scoreComputer;
    private String levelCode;
    private SoundHelper soundHelper;
    private Tracker tracker;

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Create new instance of Score Fragment
     *
     * @param snapshot  snapshot
     * @param levelCode levelCode
     * @return A new instance of fragment ScoreFragment.
     */
    public static ScoreFragment newInstance(String snapshot, String levelCode) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putString(SNAPSHOT, snapshot);
        args.putString(LEVEL_CODE, levelCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        if (null != getArguments()) {
            SoundModel soundModel = new SoundModelImpl(getContext());
            SoundFactory soundFactory = new SoundFactory();
            soundHelper = soundFactory.getSoundHelper(soundModel.getSoundType(), getContext());
            Gson gson = new Gson();
            DotsGameSnapshot snapshot =
                    gson.fromJson(getArguments().getString(SNAPSHOT), DotsGameSnapshot.class);
            scoreComputer = new ScoreComputerImpl(snapshot);
            scoreComputer.computeScoreWithResults();
            score = snapshot.getScore();
            levelCode = getArguments().getString(LEVEL_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        result = (TextView) view.findViewById(R.id.result);
        points = (TextView) view.findViewById(R.id.points);
        tasks = (TextView) view.findViewById(R.id.tasks);
        taskList = (RecyclerView) view.findViewById(R.id.task_list);
        List<Task> tasksList = null;
        switch (score.getResult()) {
            case WON:
                soundHelper.playSound(NoteType.WIN);
                result.setText(R.string.won);
                tasks.setText(R.string.completed_tasks);
                tasksList = score.getCompletedTasks();
                break;
            case LOST:
                soundHelper.playSound(NoteType.LOSE);
                result.setText(R.string.lost);
                tasks.setText(R.string.incomplete_tasks);
                tasksList = score.getIncompleteTasks();
                break;
            case DRAW:
                soundHelper.playSound(NoteType.DRAW);
                result.setText(R.string.draw);
                tasks.setText(R.string.completed_tasks);
                tasksList = score.getCompletedTasks();
                break;
        }
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Result")
                .setAction(levelCode)
                .setLabel(score.getResult().name())
                .setValue(score.basicScore())
                .build());
        RecyclerView.Adapter adapter = new TasksRecyclerAdapter(getContext(), tasksList);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        action = (FloatingActionButton) view.findViewById(R.id.action);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onScoreViewed(score, levelCode);
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Fab")
                        .setAction("Score")
                        .setLabel(levelCode)
                        .build());
            }
        });
        points.setText(getString(R.string.points, score.basicScore()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public interface OnFragmentInteractionListener {
        void onScoreViewed(Score score, String levelCode);
    }
}
