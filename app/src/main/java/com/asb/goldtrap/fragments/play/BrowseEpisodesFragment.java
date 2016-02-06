package com.asb.goldtrap.fragments.play;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.R;
import com.asb.goldtrap.adapters.CursorRecyclerAdapter;
import com.asb.goldtrap.models.episodes.EpisodeModel;
import com.asb.goldtrap.models.episodes.impl.EpisodeModelImpl;

/**
 * Browse Episodes Fragment
 */
public class BrowseEpisodesFragment extends Fragment {
    public static String TAG = BrowseEpisodesFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private EpisodeModel episodeModel;
    private RecyclerView recyclerView;
    private Cursor cursor;

    public BrowseEpisodesFragment() {
        // Required empty public constructor
    }

    /**
     * Factory Method
     */
    public static BrowseEpisodesFragment newInstance() {
        return new BrowseEpisodesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_episodes, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.episodes);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new CursorRecyclerAdapter(getContext(), cursor,
                new CursorRecyclerAdapter.ViewHolder.ViewHolderClicks() {
                    @Override
                    public void onClick(int position) {
                        handleEpisodeClick(position);
                    }
                }));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        episodeModel = new EpisodeModelImpl(getContext());
        this.cursor = episodeModel.getAllEpisodes();
    }

    private void handleEpisodeClick(int position) {

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
    public void onDestroy() {
        super.onDestroy();
        if (null != cursor) {
            cursor.close();
        }
    }

    public interface OnFragmentInteractionListener {

    }
}
