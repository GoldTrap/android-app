package com.asb.goldtrap.fragments.pregame;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.TasksRecyclerAdapter;
import com.asb.goldtrap.models.eo.Level;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Tasks To do
 */
public class TasksDisplayFragment extends Fragment {
    public static final String LEVEL_RESOURCE = "LEVEL_RESOURCE";
    public static final String LEVEL_CODE = "LEVEL_CODE";
    public static final String TAG = TasksDisplayFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private RecyclerView toDoRecyclerView;
    private FloatingActionButton play;
    private Level level;
    private int levelResourceCode;
    private String levelCode;
    /**
     * Get new Instance of TasksDisplayFragment
     *
     * @param resourceId The resource Id of the Level
     * @return A new instance of fragment TasksDisplayFragment.
     */
    public static TasksDisplayFragment newInstance(int resourceId, String levelCode) {
        TasksDisplayFragment fragment = new TasksDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL_RESOURCE, resourceId);
        bundle.putString(LEVEL_CODE, levelCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TasksDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        levelResourceCode = args.getInt(LEVEL_RESOURCE);
        levelCode = args.getString(LEVEL_CODE);
        doGSONStuff();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_display, container, false);
        toDoRecyclerView = (RecyclerView) view.findViewById(R.id.to_do_list);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter adapter = new TasksRecyclerAdapter(getContext(), level.getTasks());
        toDoRecyclerView.setAdapter(adapter);
        play = (FloatingActionButton) view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.tasksShownAcknowledgement(levelResourceCode, levelCode);
            }
        });
        return view;
    }

    private void doGSONStuff() {
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(levelResourceCode);
        level = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Level.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void tasksShownAcknowledgement(int levelResourceCode, String levelCode);
    }
}
