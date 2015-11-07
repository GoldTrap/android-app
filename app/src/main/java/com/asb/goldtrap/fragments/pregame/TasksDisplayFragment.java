package com.asb.goldtrap.fragments.pregame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
public class TasksDisplayFragment extends DialogFragment {
    public static final String LEVEL_RESOURCE = "LEVEL_RESOURCE";
    public static final String TAG = TasksDisplayFragment.class.getSimpleName();
    private RecyclerView toDoRecyclerView;
    private FloatingActionButton play;
    private Level level;

    /**
     * Get new Instance of TasksDisplayFragment
     *
     * @param resourceId The resource Id of the Level
     * @return A new instance of fragment TasksDisplayFragment.
     */
    public static TasksDisplayFragment newInstance(int resourceId) {
        TasksDisplayFragment fragment = new TasksDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL_RESOURCE, resourceId);
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
        int resourceId = args.getInt(LEVEL_RESOURCE);
        doGSONStuff(resourceId);
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
                TasksDisplayFragment.this.dismiss();
            }
        });
        return view;
    }

    private void doGSONStuff(int resourceId) {
        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(resourceId);
        level = gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), Level.class);
    }
}
