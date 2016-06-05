package com.asb.goldtrap.fragments.play;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.GoldTrapApplication;
import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.LevelRecyclerAdapter;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.levels.LevelModel;
import com.asb.goldtrap.models.levels.impl.CursorLevelModel;
import com.asb.goldtrap.spansize.LevelSpanSizeLoopkup;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Browse Lessons Fragment
 */
public class BrowseLevelsFragment extends Fragment implements LevelModel.Listener {
    private static final String EPISODE_CODE = "episodeCode";
    public static final String NAME = "NAME";
    private String episodeCode;
    private String name;
    public static String TAG = BrowseLevelsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private LevelModel levelModel;
    private RecyclerView.Adapter adapter;
    private Tracker tracker;

    public BrowseLevelsFragment() {
    }

    /**
     * Browse Lessons Fragment Factory Method
     *
     * @param episodeCode Episode Code.
     * @param name        Name of the Level.
     * @return A new instance of fragment BrowseLevelsFragment.
     */
    public static BrowseLevelsFragment newInstance(String episodeCode, String name) {
        BrowseLevelsFragment fragment = new BrowseLevelsFragment();
        Bundle args = new Bundle();
        args.putString(EPISODE_CODE, episodeCode);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = GoldTrapApplication.getInstance().getDefaultTracker();
        if (getArguments() != null) {
            episodeCode = getArguments().getString(EPISODE_CODE);
            name = getArguments().getString(NAME);
        }
        levelModel =
                new CursorLevelModel(getContext(), getActivity().getSupportLoaderManager(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_levels, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 9);
        gridLayoutManager.setSpanSizeLookup(new LevelSpanSizeLoopkup());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.levels);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new LevelRecyclerAdapter(getContext(), levelModel,
                new LevelRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(int position) {
                        handleLevelClick(position);
                    }
                });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void handleLevelClick(int position) {
        Level level = levelModel.getLevel(position);
        mListener.onLevelClicked(level);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Level Click")
                .setAction(level.getCode())
                .setLabel(level.getName())
                .setValue(position)
                .build());
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
        levelModel.loadLevels(episodeCode);
        Log.i(TAG, "Setting screen name: " + TAG);
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onLevelClicked(Level level);
    }
}
