package com.asb.goldtrap.fragments.postgame;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.TasksRecyclerAdapter;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;

import java.util.List;

/**
 * Score Fragment
 * Created on 29/11/2015
 */
public class ScoreFragment extends Fragment {

    public static final String TAG = ScoreFragment.class.getSimpleName();
    private Score score;
    private TextView result;
    private TextView points;
    private TextView tasks;
    private RecyclerView taskList;
    private FloatingActionButton action;
    private OnFragmentInteractionListener mListener;

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Create new instance of Score Fragment
     *
     * @return A new instance of fragment ScoreFragment.
     */
    public static ScoreFragment newInstance() {
        return new ScoreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        score = ((GoldTrapApplication) getActivity().getApplication()).getDotsGameSnapshot()
                .getScoreWithResult();
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
                result.setText(R.string.won);
                tasks.setText(R.string.completed_tasks);
                tasksList = score.getCompletedTasks();
                break;
            case LOST:
                result.setText(R.string.lost);
                tasks.setText(R.string.incomplete_tasks);
                tasksList = score.getIncompleteTasks();
                break;
            case DRAW:
                result.setText(R.string.draw);
                tasks.setText(R.string.completed_tasks);
                tasksList = score.getCompletedTasks();
                break;
        }
        RecyclerView.Adapter adapter = new TasksRecyclerAdapter(getContext(), tasksList);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        action = (FloatingActionButton) view.findViewById(R.id.action);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onScoreViewed();
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

    public interface OnFragmentInteractionListener {
        void onScoreViewed();
    }
}
