package com.asb.goldtrap.fragments.pregame;

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

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.TasksRecyclerAdapter;
import com.asb.goldtrap.models.eo.Level;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
    private Tracker tracker;

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
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
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
        NativeExpressAdView mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EAA51803F0D6A92C418E5D37FE508ACB")
                .build();
        mAdView.loadAd(adRequest);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.tasksShownAcknowledgement(levelResourceCode, levelCode);
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Fab")
                        .setAction("Tasks")
                        .setLabel(levelCode)
                        .build());
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

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        mListener.setTitleAndShowAppbar(R.string.tasks);
    }

    public interface OnFragmentInteractionListener {
        void tasksShownAcknowledgement(int levelResourceCode, String levelCode);

        void setTitleAndShowAppbar(int resId);
    }
}
